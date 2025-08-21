import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient zur Überprüfung von IBANs.
 * Sie enthält Methoden, um Länge, Struktur und Prüfsumme einer IBAN zu validieren.
 */
public class IBANChecker {

    /** Enthält die erwarteten IBAN-Längen pro Land (Schlüssel = Ländercode). */
    private static final Map<String, Integer> chars = new HashMap<>();
    static {
        chars.put("AT", 20);
        chars.put("BE", 16);
        chars.put("CZ", 24);
        chars.put("DE", 22);
        chars.put("DK", 18);
        chars.put("FR", 27);
    }

    /**
     * Startmethode des Programms.
     *
     * @param args Kommandozeilenargumente (werden hier nicht benutzt)
     */
    public static void main(String[] args) {
        String iban = "DE227902007600279131";
        System.out.println("Welcome to the IBAN Checker!");
        System.out.println("IBAN " + iban + " is " + validate(iban));
    }

    /**
     * Validiert eine gegebene IBAN anhand von Länge, Ländercode und Prüfziffer.
     *
     * @param iban IBAN, die überprüft werden soll
     * @return true, wenn die IBAN gültig ist, sonst false
     */
    public static boolean validate(String iban) {
        if (!checkLength(iban)) {
            return false;
        }
        String rearrangedIban = rearrangeIban(iban);
        String convertedIban = convertToInteger(rearrangedIban);
        List<String> segments = createSegments(convertedIban);
        return calculate(segments) == 1;
    }

    /**
     * Führt die Modulo-97-Prüfung auf den Segmenten der IBAN aus.
     *
     * @param segments Liste von Zahlen-Strings
     * @return Ergebnis der Berechnung (sollte 1 sein bei gültiger IBAN)
     */
    private static int calculate(List<String> segments) {
        long n = 0;
        for (String segment : segments) {
            if (segment.length() == 9) {
                n = Long.parseLong(segment) % 97;
            } else {
                segment = n + segment;
                n = Long.parseLong(segment) % 97;
            }
        }
        return (int) n;
    }

    /**
     * Überprüft, ob die Länge der IBAN zum angegebenen Land passt.
     *
     * @param iban IBAN-String
     * @return true, wenn Länge korrekt ist, sonst false
     */
    private static boolean checkLength(String iban) {
        String countryCode = iban.substring(0, 2);
        return chars.containsKey(countryCode) && chars.get(countryCode) ==
                iban.length();
    }

    /**
     * Konvertiert eine IBAN in eine reine Zahlendarstellung (Buchstaben → Zahlen).
     *
     * @param iban IBAN-String
     * @return konvertierte IBAN als Zahl-String
     */
    private static String convertToInteger(String iban) {
        StringBuilder convertedIban = new StringBuilder();
        String upperIban = iban.toUpperCase();
        for (char c : upperIban.toCharArray()) {
            if (Character.isDigit(c)) {
                convertedIban.append(c);
            }
            if (Character.isLetter(c)) {
                convertedIban.append(c - 55);
            }
        }
        return convertedIban.toString();
    }

    /**
     * Zerlegt die IBAN in Segmente für die Berechnung.
     *
     * @param iban konvertierte IBAN
     * @return Liste von Segmenten
     */
    private static List<String> createSegments(String iban) {
        List<String> segments = new ArrayList<>();
        String remainingIban = iban;
        segments.add(remainingIban.substring(0, 9));
        remainingIban = remainingIban.substring(9);
        while (remainingIban.length() >= 9) {
            segments.add(remainingIban.substring(0, 7));
            remainingIban = remainingIban.substring(7);
        }
        segments.add(remainingIban);
        return segments;
    }

    /**
     * Verschiebt die ersten vier Zeichen (Ländercode + Prüfziffer) ans Ende der IBAN.
     *
     * @param iban IBAN-String
     * @return umgestellte IBAN
     */
    private static String rearrangeIban(String iban) {
        return iban.substring(4) + iban.substring(0, 4);
    }
}

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IBANCheckerTest {
    @Test
    public void testValidIBAN() {
        String iban = "DE22790200760027913168";
        assertTrue(IBANChecker.validate(iban), "Invalid IBAN");
    }

    @Test
    public void testInvalidIBAN() {
        String iban = "DE21790200760027913173";
        assertFalse(IBANChecker.validate(iban), "Invalid IBAN cannot be as valid IBAN");
    }

    @Test
    public void testKurzIBAN() {
        String iban = "DE227902007600279131";
        assertFalse(IBANChecker.validate(iban), "Mistake in IBAN lange");
    }

    @Test
    void testUnknownCountryCode() {
        String iban = "XX22790200760027913168";
        assertFalse(IBANChecker.validate(iban),"Unbekannter Ländercode darf nicht gültig sein");
    }

}

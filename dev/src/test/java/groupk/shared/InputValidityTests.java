package groupk.shared;

import static org.junit.jupiter.api.Assertions.*;

import groupk.shared.inputValidity.InputValidity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import javax.swing.*;

import static groupk.CustomAssertions.*;
public class InputValidityTests {
    @Test
    public void testPhoneValidator() {
        String[] correct = {"050-1234567",
                "057-1234566", "052-7654321"};
        String[] incorrect = {"0555-243231", "050-123456", "150-1234567", "-1234567", "0501234567"};
        testPattern(InputValidity.phone, correct, incorrect);
    }
    @Test
    public void testAddressValidator() {
        String[] correct = {
                "White Tower 19, Ramla",
                "Yossi Banai 12, Beer Sheva",
                "Flowers 35, Shoham"
        };
        String[] incorrect = {
                "White Tower, Ramla", // no number
                "19, Ramla", // no street name
                "White Tower 19 Ramla", // no comma
                "White Tower Ramla",
                "Ramla"
        };
        testPattern(InputValidity.address, correct, incorrect);
    }

    private void testPattern(InputValidity.StringInputValidator validator,
                             String[] correct, String[] incorrect) {
        for(String s: correct) {
            assertTrue(validator.test(s), s + " shouldv'e matches but didn't");
        }

        for(String s: incorrect) {
            assertFalse(validator.test(s), s + " shouldn't have matches but did");
        }

    }
}

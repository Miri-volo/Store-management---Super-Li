package groupk.shared.inputValidity;

import groupk.shared.business.Suppliers.BusinessLogicException;

public class InputValidity {

    private InputValidity() {}

    public static final StringInputValidator address = new StringInputValidator(
            "[Street (letters, numbers, whitespaces)] [number], [city (letters, numbers, whitespaces)]",
            "^[ \\w]+ \\d+\\, [ \\w]+$"
    );

    public static final StringInputValidator phone = new StringInputValidator(
            "05X-XXXXXXX",
            "^05\\d\\-\\d{7}$");


    public static class StringInputValidator {
        public final String format;
        public final String regex;

        public StringInputValidator(String format, String regex) {
            this.format = format;
            this.regex = regex;
        }

        public boolean test(String input) {
            return input.matches(regex);
        }

        public void throwIfNotMatch(String input) {
            if(!test(input)) {
                throw new BusinessLogicException("Format error: " + input + "\ndoesn't match pattern: " + format);
            }
        }
    }
}

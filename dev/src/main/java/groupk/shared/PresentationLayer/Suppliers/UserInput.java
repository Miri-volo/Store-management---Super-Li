package groupk.shared.PresentationLayer.Suppliers;


import groupk.shared.inputValidity.InputValidity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.MissingFormatArgumentException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserInput {

    private Scanner scanner = new Scanner(System.in);

    private static UserInput instance = null;

    private UserInput() {

    }

    public static UserInput getInstance() {
        if (instance == null)
            instance = new UserInput();
        return instance;
    }

    public int nextInt(String message) {
        //goes in a loop to get int and prints the message we provided each time
        boolean retry = true;
        int nextInt = 0;
        while (retry) {
            try {
                UserOutput.getInstance().print(message);
                nextInt = Integer.parseInt(scanner.nextLine());
                if (nextInt < 0)
                    throw new InputMismatchException("Number was negative");
                retry = false;
            } catch (Exception e) {
                UserOutput.getInstance().println("Please try again.");
            }
        }
        return nextInt;
    }


    public boolean nextBoolean(String message) {
        //goes in a loop to get boolean and prints the message we provided each time
        boolean retry = true;
        boolean nextBool = false;
        while (retry) {
            try {
                UserOutput.getInstance().print(message + " (true/false) ");
                String ans = scanner.nextLine();
                if (!ans.equalsIgnoreCase("true") && !ans.equalsIgnoreCase("false")) {
                    throw new InputMismatchException("String is not true or false");
                }
                nextBool = Boolean.parseBoolean(ans.toLowerCase());
                retry = false;
            } catch (Exception e) {
                UserOutput.getInstance().println("Please try again.");
            }
        }
        return nextBool;
    }

    public String nextString(String message) {
        //goes in a loop to get String and prints the message we provided each time
        boolean retry = true;
        String nextString = "";
        while (retry) {
            try {
                UserOutput.getInstance().print(message);
                nextString = scanner.nextLine();
                retry = false;
            } catch (Exception e) {
                if (!scanner.hasNext()) {
                    scanner.nextLine();
                }
                UserOutput.getInstance().println("Please try again.");
            }
        }
        return nextString;
    }

    public float nextFloat(String message) {
        //goes in a loop to get int and prints the message we provided each time
        boolean retry = true;
        float nextFloat = 0;
        while (retry) {
            try {
                UserOutput.getInstance().print(message);
                nextFloat = Float.parseFloat(scanner.nextLine());
                if (nextFloat < 0)
                    throw new InputMismatchException("Number was negative");
                retry = false;
            } catch (Exception e) {
                UserOutput.getInstance().println("Please try again.");
            }
        }
        return nextFloat;
    }

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public LocalDate nextDate(String message) {
        //goes in a loop to get int and prints the message we provided each time
        boolean retry = true;
        LocalDate nextDate = null;
        while (retry) {
            try {
                UserOutput.getInstance().print(message + "(format: YYYY/MM/DD " +
                        "; or TODAY (all uppercase) for today) ");
                String inputLine = scanner.nextLine();
                if ("TODAY".equals(inputLine)) {
                    return LocalDate.now();
                }
                nextDate = LocalDate.parse(inputLine, dateTimeFormatter);
                retry = false;
            } catch (Exception e) {
                UserOutput.getInstance().println("Please try again.");
            }
        }
        return nextDate;
    }

    public <TEnum extends Enum<TEnum>> TEnum nextEnum(
            String prompt, Class<TEnum> enumType) {
        TEnum selected = null;
        TEnum[] values = enumType.getEnumConstants();
        while (true) {
            try {
                UserOutput.getInstance().println(prompt);
                for (int i = 0; i < values.length; i++) {
                    UserOutput.getInstance().println((i + 1) + ": " + values[i]);
                }
                return values[Integer.parseInt(scanner.nextLine().trim()) - 1];
            } catch (Exception e) {
                UserOutput.getInstance().println("please try again");
            }
        }
    }

    public String nextPhone(String s) {
        boolean retry = true;
        String inputLine = "";
        while (retry) {
            try {
                UserOutput.getInstance().print(s + "(format: " + InputValidity.phone.format + ") ");
                inputLine = scanner.nextLine();
                if (!InputValidity.phone.test(inputLine)) {
                    throw new MissingFormatArgumentException("Please use format:  "+ InputValidity.phone.format);
                }
                retry = false;
            } catch (Exception e) {
                UserOutput.getInstance().println(e.getMessage());
            }
        }
        return inputLine;
    }

    public String nextAddress(String s) {
        boolean retry = true;
        String inputLine = "";
        while (retry) {
            try {
                UserOutput.getInstance().print(s + "(format: " + InputValidity.address.format + ") ");
                inputLine = scanner.nextLine();
                if (!InputValidity.address.test(inputLine)) {
                    throw new MissingFormatArgumentException(" Please use format: " + InputValidity.address.format );
                }
                retry = false;
            } catch (Exception e) {
                UserOutput.getInstance().println(e.getMessage());
            }
        }
        return inputLine;
    }


}


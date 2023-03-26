package groupk.shared.PresentationLayer.EmployeesLogistics;

import groupk.logistics.DataLayer.TruckingMapper;
import groupk.shared.PresentationLayer.EmployeesLogistics.command.Command;
import groupk.shared.service.Response;
import groupk.shared.service.Service;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Shift;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CommandRunner {
    private final Command[] commands;
    private final Runnable onStop;
    private Employee subject;
    private Service service;

    public CommandRunner(Command[] commands, Service service, Runnable onStop, Connection conn) {
        this.commands = commands;
        this.onStop = onStop;
        this.service = service;
    }

    public void invoke(String line) {
        for (Command command: commands) {
            if (command.isVisible(subject != null ? subject.role : null)) {
                if (command.isMatching(line)) {
                    command.execute(line.split(" "), this);
                    return;
                }
            }
        }
        help();
    }

    public void introduce() {
        System.out.println("For usage information enter 'help'.");
    }

    public void stop() {
        onStop.run();
    }

    public void setSubject(Employee subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject.id;
    }

    public Service getService() {
        return service;
    }

    private void help() {
        System.out.println("Supported commands:");
        int indent = Arrays.stream(commands)
                .map(command -> command.name().length())
                .max(Integer::compareTo)
                .orElse(0) + 2;
        for (Command command: commands) {
            if (command.isVisible(subject != null ? subject.role : null)) {
                // Imagine this is not Java 8:
                //             " ".repeat(indent - command.name().length())
                String space = new String(new char[indent - command.name().length()]).replace("\0", " ");
                System.out.println(
                        "  "
                                + command.name()
                                + space
                                + command.description());
            }
        }
        System.out.println("Usage:");
        System.out.println("  > <command> [arguments...]");
        System.out.println("For command specific help, enter the command without arguments.");
    }

    // Parses dates in YYYY-MM-DD format, for example 2022-05-17.
    public static Calendar parseDate(String date) {
        String[] split = date.split("-");
        if (split.length != 3 || split[0].length() != 4 || split[1].length() != 2 || split[2].length() != 2) {
            throw new IllegalArgumentException("must follow yyyy-mm-dd format, for example 2022-04-25.");
        }
        try {
            return new GregorianCalendar(
                    Integer.parseInt(split[0]),
                    // Why -1 you ask? Because the Java library can not even be consistent within
                    // the arguments of a single constructor. Months are indexed from 0.
                    Integer.parseInt(split[1]) - 1,
                    Integer.parseInt(split[2])
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("must follow yyyy-mm-dd format, for example 2022-04-25.");
        }
    }

    public static Employee.Role parseRole(String role) {
        try {
            return Employee.Role.valueOf(role);
        } catch (IllegalArgumentException  e) {
            String roles = Arrays.stream(Employee.Role.values())
                    .map(Enum::toString)
                    .reduce((String acc, String cur) -> acc + ", " + cur)
                    .orElse("");
            throw new IllegalArgumentException("must be one of: \n" + roles + ".");
        }
    }

    public static Shift.Type parseShiftType(String type) {
        try {
            return Shift.Type.valueOf(type);
        } catch (IllegalArgumentException e) {
            String types = Arrays.stream(Shift.Type.values())
                    .map(Enum::toString)
                    .reduce((String acc, String cur) -> acc + ", " + cur)
                    .orElse("");
            throw new IllegalArgumentException("must be one of: \n" + types + ".");
        }
    }

    public static Employee.ShiftDateTime parseShiftDateTime(String dayTime) {
        try {
            return Employee.ShiftDateTime.valueOf(dayTime);
        } catch (IllegalArgumentException e) {
            String types = Arrays.stream(Employee.ShiftDateTime.values())
                    .map(Enum::toString)
                    .reduce((String acc, String cur) -> acc + ", " + cur)
                    .orElse("");
            throw new IllegalArgumentException("must be one of: \n" + types + ".");
        }
    }

    public static int parseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("must be an integer.");
        }
    }

    public static LocalDateTime parseLocalDateTime(String date, String time) {
        Calendar parsedDate = parseDate(date);
        String[] split = time.split(":");
        if (split.length != 2 || split[0].length() != 2 || split[1].length() != 2) {
            throw new IllegalArgumentException("must follow hh:mm format, for example 16:59.");
        }
        try {
            parsedDate.add(GregorianCalendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
            parsedDate.add(GregorianCalendar.MINUTE, Integer.parseInt(split[1]));
        } catch (Exception e) {
            throw new IllegalArgumentException("must follow hh:mm format, for example 16:59.");
        }
        // This introduces a bug when using the program across time zones. It is out of scope.
        return LocalDateTime.ofInstant(parsedDate.toInstant(), parsedDate.getTimeZone().toZoneId());
    }

    public static LocalDateTime parseLocalDateTime(String date) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime toReturn;
        try {
            toReturn = LocalDateTime.parse(date, dateFormat);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("must follow yyyy-MM-dd HH:mm format, for example 2022-12-31 16:59");
        }
        return toReturn;
    }
}

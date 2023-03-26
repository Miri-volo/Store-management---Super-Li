package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Shift;

import java.util.Calendar;

public class AddShiftStaff implements Command {
    @Override
    public String name() {
        return "add shift staff";
    }

    @Override
    public String description() {
        return "add employee as staff in shift";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("add shift staff");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.HumanResources;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 6) {
            System.out.println("Error: All arguments must be supplied.");
            System.out.println("Usage:");
            System.out.println("> add shift staff <date> <type> <id>");
            System.out.println("  date is formatted as yyyy-mm-dd, for example 2022-04-25.");
            System.out.println("  type is either Morning or Evening.");
            return;
        }

        Calendar date;
        try {
            date = CommandRunner.parseDate(command[3]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: date %s\n", e.getMessage());
            return;
        }

        Shift.Type type;
        try {
            type = CommandRunner.parseShiftType(command[4]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: type %s\n", e.getMessage());
            return;
        }

        String id = command[5];

        Response<Shift> changed = runner.getService().addEmployeeToShift(runner.getSubject(), date, type, id);
        if (changed.isError()) {
            System.out.printf("Error: %s\n", changed.getErrorMessage());
            return;
        }
        System.out.println("Employee added to shift.");
    }
}

package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Shift;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

public class CreateShift implements Command {
    @Override
    public String name() {
        return "create shift";
    }

    @Override
    public String description() {
        return "create a new shift";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("create shift");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.HumanResources;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length < 4) {
            System.out.println("Error: All arguments must be supplied.");
            System.out.println("Usage:");
            System.out.println("> create shift <date> <type> [staff]");
            System.out.println("  date is formatted as yyyy-mm-dd, for example 2022-04-25.");
            System.out.println("  type is either Morning or Evening.");
            return;
        }

        Calendar date;
        try {
            date = CommandRunner.parseDate(command[2]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: date %s\n", e.getMessage());
            return;
        }

        Shift.Type type;
        try {
            type = CommandRunner.parseShiftType(command[3]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: type %s\n", e.getMessage());
            return;
        }

        LinkedList<Employee> staff = new LinkedList<>();
        for (int i = 4; i < command.length; i++) {
            Response<Employee> current = runner.getService().readEmployee(runner.getSubject(), command[i]);
            if (current.isError()) {
                System.out.printf("Error: %s\n", current.getErrorMessage());
                return;
            }
            staff.push(current.getValue());
        }

        Response<Shift> created = runner.getService().createShift(runner.getSubject(), date, type, staff, new HashMap<>());
        if (created.isError()) {
            System.out.printf("Error: %s\n", created.getErrorMessage());
            return;
        }
        System.out.println("Shift created.");
    }
}

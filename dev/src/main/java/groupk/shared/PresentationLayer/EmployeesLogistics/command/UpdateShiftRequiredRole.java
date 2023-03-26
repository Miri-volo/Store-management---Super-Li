package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Shift;

import java.util.Calendar;

public class UpdateShiftRequiredRole implements Command {
    @Override
    public String name() {
        return "update shift required role";
    }

    @Override
    public String description() {
        return "update required employee roles in a shift";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("update shift required role");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.HumanResources;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 8) {
            System.out.println("Error: All arguments must be supplied.");
            System.out.println("Usage:");
            System.out.println("> update shift required role <date> <type> <role> <count>");
            return;
        }

        Calendar date;
        try {
            date = CommandRunner.parseDate(command[4]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: date %s\n", e.getMessage());
            return;
        }

        Shift.Type type;
        try {
            type = CommandRunner.parseShiftType(command[5]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: type %s\n", e.getMessage());
            return;
        }

        Employee.Role role;
        try {
            role = CommandRunner.parseRole(command[6]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: role %s\n", e.getMessage());
            return;
        }

        int count;
        try {
            count = CommandRunner.parseInt(command[7]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: count %s\n", e.getMessage());
            return;
        }

        Response<Shift> updated = runner.getService().updateRequiredRoleInShift(runner.getSubject(), date, type, role, count);
        if (updated.isError()) {
            System.out.printf("Error: %s\n", updated.getErrorMessage());
            return;
        }
        System.out.println("Updated required role in shift.");
    }
}

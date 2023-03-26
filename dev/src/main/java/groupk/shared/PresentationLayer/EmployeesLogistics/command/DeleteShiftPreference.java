package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

public class DeleteShiftPreference implements Command {
    @Override
    public String name() {
        return "delete shift preference";
    }

    @Override
    public String description() {
        return "remove day and time an employee can work";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("delete shift preference");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return true;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 4) {
            System.out.println("Error: All arguments must be supplied.");
            System.out.println("Usage:");
            System.out.println("> delete shift preference <shift>");
            return;
        }

        Employee.ShiftDateTime shift;
        try {
            shift = CommandRunner.parseShiftDateTime(command[3]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: shift %s\n", e.getMessage());
            return;
        }

        Response<Employee> updated = runner.getService().deleteEmployeeShiftPreference(runner.getSubject(), runner.getSubject(), shift);
        if (updated.isError()) {
            System.out.printf("Error: %s\n", updated.getErrorMessage());
            return;
        }
        System.out.println("Updated employee shift preference.");
    }
}

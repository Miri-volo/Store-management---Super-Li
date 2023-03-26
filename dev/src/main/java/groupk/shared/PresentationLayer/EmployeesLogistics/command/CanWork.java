package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

import java.util.List;

public class CanWork implements Command {
    @Override
    public String name() {
        return "can work";
    }

    @Override
    public String description() {
        return "list employees who can work at date and time";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("can work");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.HumanResources || role == Employee.Role.StoreManager;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 3) {
            System.out.println("Error: All arguments must be supplied.");
            System.out.println("Usage:");
            System.out.println("> can work <shift>");
            return;
        }

        Employee.ShiftDateTime shift;
        try {
            shift = CommandRunner.parseShiftDateTime(command[2]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: shift %s\n", e.getMessage());
            return;
        }

        Response<List<Employee>> employees = runner.getService().whoCanWork(runner.getSubject(), shift);
        if (employees.isError()) {
            System.out.printf("Error: %s\n", employees.getErrorMessage());
            return;
        }

        for (Employee e : employees.getValue()) {
            System.out.printf("%s, %s, %s\n", e.id, e.name, e.role);
        }
    }
}

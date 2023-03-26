package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Shift;

import java.util.Calendar;
import java.util.List;

public class CanWorkWithRole implements Command {
    @Override
    public String name() {
        return "can work with role";
    }

    @Override
    public String description() {
        return "list employees who can work at a day and time and are of a certain role";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("can work with role");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.HumanResources || role == Employee.Role.StoreManager;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 6) {
            System.out.println("Error: All arguments must be supplied.");
            System.out.println("Usage:");
            System.out.println("> can work with role <shift> <role>");
            return;
        }

        Employee.ShiftDateTime shift;
        try {
            shift = CommandRunner.parseShiftDateTime(command[4]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: shift %s\n", e.getMessage());
            System.out.println("Usage:");
            System.out.println("> can work with role <shift> <role>");
            return;
        }

        Employee.Role role;
        try {
            role = CommandRunner.parseRole(command[5]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: role %s\n", e.getMessage());
            System.out.println("Usage:");
            System.out.println("> can work with role <shift> <role>");
            return;
        }

        Response<List<Employee>> list = runner.getService().whoCanWorkWithRole(runner.getSubject(), shift, role);
        if (list.isError()) {
            System.out.printf("Error: %s\n", list.getErrorMessage());
            return;
        }

        for (Employee e : list.getValue()) {
            System.out.printf("%s, %s, %s\n", e.id, e.name, e.role);
        }
    }
}

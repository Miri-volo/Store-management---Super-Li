package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

import java.util.List;

public class ListEmployees implements Command {

    @Override
    public String name() {
        return "list employees";
    }

    @Override
    public String description() {
        return "list all employees";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("list employees");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.HumanResources || role == Employee.Role.StoreManager;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 2) {
            System.out.println("Error: Wrong number of arguments.");
            System.out.println("Usage:");
            System.out.println("> list employees");
            return;
        }

        Response<List<Employee>> list = runner.getService().listEmployees(runner.getSubject());
        if (list.isError()) {
            System.out.printf("Error: %s\n", list.getErrorMessage());
            return;
        }

        for (Employee e : list.getValue()) {
            System.out.printf("%s, %s, %s\n", e.id, e.name, e.role);
        }
    }
}

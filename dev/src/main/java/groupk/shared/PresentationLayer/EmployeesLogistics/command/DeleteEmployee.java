package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

public class DeleteEmployee implements Command {
    @Override
    public String name() {
        return "delete employee";
    }

    @Override
    public String description() {
        return "delete an employee";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("delete employee");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.HumanResources;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 3) {
            System.out.println("Error: Wrong number of arguments.");
            System.out.println("Usage:");
            System.out.println("> delete employee <id>");
            return;
        }

        String id = command[2];
        Response<Employee> deleted = runner.getService().deleteEmployee(runner.getSubject(), id);
        if (deleted.isError()) {
            System.out.printf("Error: %s\n", deleted.getErrorMessage());
            return;
        }

        System.out.printf("Deleted employee %s named %s.\n", deleted.getValue().id, deleted.getValue().name);
    }
}

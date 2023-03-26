package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

public class DeleteDelivery implements Command {
    @Override
    public String name() {
        return "delete delivery";
    }

    @Override
    public String description() {
        return "delete a delivery";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("delete delivery");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.LogisticsManager;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 3) {
            System.out.println("Error: Wrong number of arguments.");
            System.out.println("Usage:");
            System.out.println("> delete delivery <id>");
            return;
        }

        int id;
        try {
            id = CommandRunner.parseInt(command[2]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: id %s\n", e.getMessage());
            return;
        }

        Response<Boolean> deleted = runner.getService().deleteDelivery(runner.getSubject(), id);
        if (deleted.isError()) {
            System.out.printf("Error: %s\n", deleted.getErrorMessage());
            return;
        }
        if (!deleted.getValue()) {
            System.out.println("Error: Something went wrong.");
            return;
        }
        System.out.println("Delivery deleted.");
    }
}

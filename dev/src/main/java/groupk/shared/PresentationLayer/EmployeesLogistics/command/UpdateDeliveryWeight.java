package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

public class UpdateDeliveryWeight implements Command {
    @Override
    public String name() {
        return "update delivery weight";
    }

    @Override
    public String description() {
        return "set weighted weight of delivery";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("update delivery weight");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.Driver;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 5) {
            System.out.println("Error: Wrong number of arguments.");
            System.out.println("Usage:");
            System.out.println("> update delivery weight <id> <weight>");
            return;
        }

        int id;
        try {
            id = CommandRunner.parseInt(command[3]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: weight %s\n", e.getMessage());
            return;
        }

        int weight;
        try {
            weight = CommandRunner.parseInt(command[4]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: weight %s\n", e.getMessage());
            return;
        }

        Response<Boolean> response = runner.getService().setWeightForDelivery(runner.getSubject(), id, weight);
        if (response.isError()) {
            System.out.printf("Error: %s\n", response.getErrorMessage());
            return;
        }
        if (!response.getValue()) {
            System.out.println("Error: Something went wrong.");
            return;
        }
        System.out.println("Updated weight of delivery.");
    }
}

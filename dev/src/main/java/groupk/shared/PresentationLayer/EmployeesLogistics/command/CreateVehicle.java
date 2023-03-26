package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

public class CreateVehicle implements Command {
    @Override
    public String name() {
        return "create vehicle";
    }

    @Override
    public String description() {
        return "create a delivery vehicle";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("create vehicle");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.LogisticsManager;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 7) {
            System.out.println("Error: Wrong number of arguments.");
            System.out.println("Usage:");
            System.out.println("> create vehicle <license> <registration> <model> <weight> <max-weight>");
            return;
        }

        int weight;
        try {
            weight = CommandRunner.parseInt(command[5]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: weight %s\n", e.getMessage());
            return;
        }

        int maxWeight;
        try {
            maxWeight = CommandRunner.parseInt(command[6]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: max-weight %s\n", e.getMessage());
            return;
        }

        Response<Boolean> created = runner.getService().createVehicle(runner.getSubject(), command[2], command[3], command[4], weight, maxWeight);
        if (created.isError()) {
            System.out.printf("Error: %s\n", created.getErrorMessage());
            return;
        }
        if (!created.getValue()) {
            System.out.println("Error: Something went wrong.");
            return;
        }
        System.out.println("Vehicle created.");
    }
}

package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

import java.util.List;

public class ListVehicles implements Command {
    @Override
    public String name() {
        return "list vehicles";
    }

    @Override
    public String description() {
        return "print a list of all vehicles";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("list vehicles");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.LogisticsManager;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 2) {
            System.out.println("Error: Too many arguments.");
            System.out.println("Usage:");
            System.out.println("> list deliveries");
            return;
        }

        Response<List<String>> vehicles = runner.getService().listVehicles(runner.getSubject());
        if (vehicles.isError()) {
            System.out.printf("Error: %s\n", vehicles.getErrorMessage());
            return;
        }

        System.out.println("Delivery vehicles:");
        for (String plate : vehicles.getValue()) {
            System.out.println("- " + plate);
        }
    }
}

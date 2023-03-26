package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Product;

public class DeleteOrderFromDelivery implements Command {
    @Override
    public String name() {
        return "delete order from delivery";
    }

    @Override
    public String description() {
        return "remove an order id from delivery";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("delete order delivery");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.LogisticsManager;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 5) {
            System.out.println("Error: Wrong number of arguments.");
            System.out.println("Usage:");
            System.out.println("> delete order delivery <trucking-id> <order-id>");
            return;
        }

        int id;
        try {
            id = CommandRunner.parseInt(command[3]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: id %s\n", e.getMessage());
            return;
        }

        int orderID;
        try {
            orderID = CommandRunner.parseInt(command[5]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: count %s\n", e.getMessage());
            return;
        }

        Response<Boolean> response = runner.getService().moveOrdersFromTrucking(runner.getSubject(), id, orderID);
        if (response.isError()) {
            System.out.printf("Error: %s\n", response.getErrorMessage());
            return;
        }
        if (!response.getValue()) {
            System.out.println("Error: Something went wrong.");
            return;
        }
        System.out.println("Order deleted successfully.\nPlease validate that the order canceled, or add that order to another delivery.");
    }
}

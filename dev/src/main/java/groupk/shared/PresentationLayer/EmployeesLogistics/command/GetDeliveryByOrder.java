package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

import java.util.List;

public class GetDeliveryByOrder implements Command {

    @Override
    public String name() {
        return "get delivery by order";
    }

    @Override
    public String description() {
        return "return exist delivery id by order id";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("get delivery by order");
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
            System.out.println("> get delivery by order <order-id>");
            return;
        }

        int orderID;
        try {
            orderID = CommandRunner.parseInt(command[4]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: order-id %s\n", e.getMessage());
            return;
        }

        Response<Integer> response = runner.getService().getTruckingIDByOrderID(runner.getSubject(), orderID);
        if (response.isError()) {
            System.out.printf("Error: %s\n", response.getErrorMessage());
            return;
        }
        System.out.println("The delivery with id: " + response.getValue().intValue() + " has order " + orderID + ".");
    }
}

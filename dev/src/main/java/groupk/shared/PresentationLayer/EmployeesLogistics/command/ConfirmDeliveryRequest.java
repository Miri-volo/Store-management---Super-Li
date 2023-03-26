package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

import java.util.Scanner;

public class ConfirmDeliveryRequest implements Command {

    @Override
    public String name() {
        return "confirm delivery request";
    }

    @Override
    public String description() {
        return "confirm that you created delivery for delivery request";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("confirm delivery request");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.LogisticsManager;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 4) {
            System.out.println("Error: Wrong number of arguments.");
            System.out.println("Usage:");
            System.out.println("> confirm delivery request <order-id>");
            return;
        }

        int orderID;
        try {
            orderID = CommandRunner.parseInt(command[3]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: order-id %s\n", e.getMessage());
            return;
        }

        Response<Integer> truckingID = runner.getService().getTruckingIDByOrderID(runner.getSubject(), orderID);
        if (truckingID.isError()) {
            System.out.println("It seems like there is no delivery with that order. Are you sure that you want execute that action?");
            System.out.println("Enter \"yes\"/\"no\"");
            Scanner input = new Scanner(System.in);
            while (true) {
                String answer = input.nextLine();
                if (answer.equals("yes")) {
                    Response<Boolean> response = runner.getService().deleteTruckingRequest(runner.getSubject(), orderID);
                    if (response.isError()) {
                        System.out.printf("Error: %s\n", response.getErrorMessage());
                        return;
                    }
                    if (!response.getValue()) {
                        System.out.println("Error: we couldn't execute your action");
                        return;
                    }
                    System.out.println("Delivery request deleted successfully");
                    return;
                }
                else if (answer.equals("no")){
                    System.out.println("Your action canceled");
                    return;
                }
                else
                    System.out.println("Your answer is not valid. Enter \"yes\"/\"no\"");
            }
        }
        Response<Boolean> response = runner.getService().deleteTruckingRequest(runner.getSubject(), orderID);
        if (response.isError()) {
            System.out.printf("Error: %s\n", response.getErrorMessage());
            return;
        }
        if (!response.getValue()) {
            System.out.println("Error: we couldn't execute your action");
            return;
        }
        System.out.println("Delivery request deleted successfully");
        return;
    }
}


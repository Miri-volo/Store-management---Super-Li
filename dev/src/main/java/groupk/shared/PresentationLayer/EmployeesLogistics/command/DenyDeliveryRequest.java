package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.PresentationLayer.Suppliers.UserInput;
import groupk.shared.business.Facade;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

import java.util.Scanner;

public class DenyDeliveryRequest implements Command {

    @Override
    public String name() {
        return "deny delivery request";
    }

    @Override
    public String description() {
        return "deny an exist delivery request";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("deny delivery request");
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
            System.out.println("> deny delivery request <order-id>");
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
        if (!truckingID.isError()) {
            System.out.println("Error: the delivery " + truckingID.getValue() + " has that order.\n" +
                    "       If you want to deny that request, please delete that order from the delivery.\n" +
                    "       We actually recommend confirm this request.");
            return;
        }
        Facade.SI_Response ordersDone = runner.getService().cancelTruckingWithOrderID(orderID);
        if (!ordersDone.success) {
            System.out.println("We were unable to send the cancellation notice back to the supplier.");
            System.out.println("Do you want to delete the request anyway?");
            Boolean answer = takeYesOrNo();
            if (answer == null | !answer.booleanValue()) {
                UserInput.getInstance().nextInt("Insert stocker ID: ");
                UserInput.getInstance().nextInt("Insert Logistic Manager ID: ");
                UserInput.getInstance().nextInt("Insert HR Manager ID: ");
                System.out.println("The action is canceled");
                return;
            }
        }
        Response<Boolean> logisticsDone = runner.getService().deleteDelivery(runner.getSubject(), orderID);
        if (logisticsDone.isError()) {
            System.out.println("Error: we canceled the order, but we didn't delete the delivery request because: " + logisticsDone.getErrorMessage() + ".\nplease try again later.");
            return;
        }
        if (!logisticsDone.getValue()) {
            System.out.println("Error: we canceled the order, but we didn't delete the delivery request. please try again later.");
            return;
        }
        System.out.println("Delivery request denied successfully");
    }

    private Boolean takeYesOrNo() {
        Scanner input = new Scanner(System.in);
        int tries = 5;
        while (tries > 0) {
            System.out.println("Enter \"yes\" or \"no\"");
            System.out.print("> ");
            String command = input.nextLine();
            if(command.equals("yes"))
                return true;
            if (command.equals("no"))
                return false;
            tries--;
        }
        System.out.println("Error: 5 wrong inputs.");
        return null;
    }
}


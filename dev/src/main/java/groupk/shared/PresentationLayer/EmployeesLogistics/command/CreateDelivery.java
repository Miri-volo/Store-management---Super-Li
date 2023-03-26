package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Delivery;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Site;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CreateDelivery implements Command {
    @Override
    public String name() {
        return "create delivery";
    }

    @Override
    public String description() {
        return "create a new delivery";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("create delivery");
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
            System.out.println("> create delivery <registration> <date> <driver-id> <duration-in-minutes>");
            return;
        }

        LocalDateTime datetime;
        try {
            datetime = CommandRunner.parseLocalDateTime(command[3] + " " + command[4]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: date or time %s\n", e.getMessage());
            return;
        }

        int minuteDuration;
        try {
            minuteDuration = CommandRunner.parseInt(command[6]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: duration in minutes %s\n", e.getMessage());
            return;
        }

        System.out.println("Enter sources details!");
        boolean doneSources = false;
        List<Site> sources = new LinkedList<Site>();
        while (!doneSources) {
            Site source;
            try {
                source = makeSite();
            }
            catch (Exception e) {
                System.out.println("Error: something got wrong");
                return;
            }
            if (source == null)
                return;
            sources.add(source);
            System.out.println("Do you want to add another source?");
            Boolean answer = takeYesOrNo();
            if (answer == null)
                return;
            doneSources = !answer;
        }

        System.out.println("Enter destinations details!");
        boolean doneDestinations = false;
        List<Site> destinations = new LinkedList<Site>();
        while (!doneDestinations) {
            Site destination;
            try {
                destination = makeSite();
            }
            catch (Exception e) {
                System.out.println("Error: something got wrong");
                return;
            }
            if (destination == null)
                return;
            destinations.add(destination);
            System.out.println("Do you want to add another destination?");
            Boolean answer = takeYesOrNo();
            if (answer == null)
                return;
            doneDestinations = !answer;
        }

        System.out.println("Enter order-IDs!");
        List<Integer> ordersIDs = takeOrdersIDs();
        if (ordersIDs == null)
            return;

        Response<List<String>[]> response = runner.getService().createDelivery(
                runner.getSubject(),
                command[2],
                datetime,
                command[5],
                sources,
                destinations,
                ordersIDs,
                minuteDuration / 60,
                minuteDuration % 60);
        if (response.isError()) {
            System.out.printf("Error: %s\n", response.getErrorMessage());
            return;
        }
        if (response.getValue() == null | response.getValue().length != 3 | response.getValue()[0] == null | response.getValue()[1] == null | response.getValue()[2] == null)
            System.out.print("Error: something got wrong :(\n");
        else if (response.getValue()[0].size() == 0 & response.getValue()[1].size() == 0 & response.getValue()[2].size() == 0)
            System.out.println("Delivery created.");
        else {
            System.out.println("The delivery created with some erros: ");
            for (int i = 0; i < 3; i++) {
                List<String> Errors = response.getValue()[i];
                for (String error : Errors) {
                    System.out.println("   " + error);
                }
            }
        }
    }

    private Site makeSite() {
        String[] details = takeDetailsOfSite();
        if (details == null)
            return null;
        int houseNumber;
        try {
            houseNumber = CommandRunner.parseInt(details[5]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: house number %s\n", e.getMessage());
            return null;
        }
        int floor;
        try {
            floor = CommandRunner.parseInt(details[6]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: floor %s\n", e.getMessage());
            return null;
        }
        int apartment;
        try {
            apartment = CommandRunner.parseInt(details[7]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: apartment %s\n", e.getMessage());
            return null;
        }
        Site site = new Site(details[0], details[1], details[2], details[3], details[4], houseNumber, floor, apartment);
        return site;
    }

    private String[] takeDetailsOfSite() {
        System.out.println("Enter the details in the following structure:");
        System.out.println("(for address without floor and apartment, enter 0 at those details)");
        System.out.println("> <contact-guy-name>@<contact-guy-phone>@<area>@<city>@<street>@<house_number>@<floor>@<apartment>");
        System.out.println("For cancel the all command please enter \"no\"");
        Scanner input = new Scanner(System.in);
        System.out.print("> ");
        String command = input.nextLine();
        String[] details = command.split("@");
        while (details.length != 8) {
            if (details.length == 1) {
                if (details[0] == "no")
                    return null;
            }
            System.out.println("Error: Wrong number of arguments.");
            details = takeDetailsOfSite();
        }
        return details;
    }

    private List<Integer> takeOrdersIDs() {
        List<Integer> toReturn = new LinkedList<Integer>();
        String[] orders = takeOrdersList();
        if (orders == null)
            return null;
        for (int i = 0; i < orders.length; i++) {
            Integer orderID;
            try {
                orderID = CommandRunner.parseInt(orders[i]);
            } catch (IllegalArgumentException e) {
                System.out.printf("Error: order-id %s\n", e.getMessage());
                return null;
            }
            toReturn.add(orderID);
        }
        return toReturn;
    }

    private String[] takeOrdersList() {
        System.out.println("Enter the orders in the following structure:");
        System.out.println("> <first order-id>@<second order-id>@<third order-id>...");
        System.out.println("(with no '@' at the begin and at the end)");
        System.out.println("For cancel the all command please enter \"no\"");
        Scanner input = new Scanner(System.in);
        System.out.print("> ");
        String command = input.nextLine();
        String[] details = command.split("@");
        if (details.length == 1)
            if (details[0].equals("no"))
                return null;
        return details;
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
        System.out.println("Error: 5 wrong inputs. The action is canceled");
        return null;
    }
}

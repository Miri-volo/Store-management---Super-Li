package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Site;

import java.util.LinkedList;
import java.util.List;

public class AddDeliveryDestination implements Command {
    @Override
    public String name() {
        return "add delivery destination";
    }

    @Override
    public String description() {
        return "add destination to delivery";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("add delivery destination");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.LogisticsManager;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 12) {
            System.out.println("Error: Wrong number of arguments.");
            System.out.println("Usage:");
            System.out.println("> add delivery destination <id> <contact-name> <contact-phone> <area> <city> <street> <house-number> <apartment> <floor>");
            return;
        }

        int id;
        try {
            id = CommandRunner.parseInt(command[3]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: id %s\n", e.getMessage());
            return;
        }

        int houseNumber;
        try {
            houseNumber = CommandRunner.parseInt(command[9]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: house-number %s\n", e.getMessage());
            return;
        }

        int apartment;
        try {
            apartment = CommandRunner.parseInt(command[10]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: apartment %s\n", e.getMessage());
            return;
        }

        int floor;
        try {
            floor = CommandRunner.parseInt(command[11]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: floor %s\n", e.getMessage());
            return;
        }

        Site site = new Site(command[4], command[5], command[6], command[7], command[8], houseNumber, apartment, floor);
        LinkedList<Site> sites = new LinkedList<Site>();
        sites.add(site);

        Response<List<String>> response = runner.getService().addDestination(runner.getSubject(), id, sites);
        if (response.isError()) {
            System.out.printf("Error: %s\n", response.getErrorMessage());
            return;
        }
        System.out.println("Delivery destination added.");
    }
}

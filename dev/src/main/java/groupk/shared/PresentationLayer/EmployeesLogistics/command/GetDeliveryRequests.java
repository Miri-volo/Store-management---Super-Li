package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

import java.util.List;

public class GetDeliveryRequests implements Command {

    @Override
    public String name() {
        return "get delivery requests";
    }

    @Override
    public String description() {
        return "return delivery requests";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("get delivery requests");
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
            System.out.println("> get delivery requests");
            return;
        }
        Response<List<String>> response = runner.getService().getTruckingRequests(runner.getSubject());
        if (response.isError()) {
            System.out.printf("Error: %s\n", response.getErrorMessage());
            return;
        }
        List<String> requests = response.getValue();
        int i = 1;
        System.out.println("Delivery requests:");
        if (requests == null | requests.size() == 0) {
            System.out.println("[empty]");
            return;
        }
        for (String request : requests) {
            System.out.println(i + ". " + request);
            i++;
        }
    }
}

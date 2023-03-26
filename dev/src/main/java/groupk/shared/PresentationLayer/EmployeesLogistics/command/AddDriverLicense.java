package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

public class AddDriverLicense implements Command {
    @Override
    public String name() {
        return "add driver license";
    }

    @Override
    public String description() {
        return "add driving license to employee";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("add driver license");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.Driver;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 4) {
            System.out.println("Error: Wrong number of arguments.");
            System.out.println("Usage:");
            System.out.println("> add driver license <license>");
            return;
        }

        Response<Boolean> added = runner.getService().addLicenseForDriver(runner.getSubject(), command[3]);
        if (added.isError()) {
            System.out.printf("Error: %s\n", added.getErrorMessage());
            return;
        }
        if (!added.getValue()) {
            System.out.println("Error: Something went wrong.");
            return;
        }
        System.out.println("Added driver license to employee.");
    }
}

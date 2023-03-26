package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

import java.util.GregorianCalendar;
import java.util.LinkedHashSet;

public class CreateEmployee implements Command {
    @Override
    public String name() {
        return "create employee";
    }

    @Override
    public String description() {
        return "create a new employee";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("create employee");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return true;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 9) {
            System.out.println("Error: Wrong number of arguments.");
            usage();
            return;
        }

        String name = command[2];
        String id = command[3];
        String bank = command[4];
        int bankID;
        try {
            bankID = CommandRunner.parseInt(command[5]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: bank-id %s\n", e.getMessage());
            usage();
            return;
        }
        int bankBranch;
        try {
            bankBranch = CommandRunner.parseInt(command[6]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: bank-branch %s\n", e.getMessage());
            usage();
            return;
        }
        int salaryPerHour;
        try {
            salaryPerHour = CommandRunner.parseInt(command[7]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: salary-per-hour %s\n", e.getMessage());
            usage();
            return;
        }
        Employee.Role role;
        try {
            role = CommandRunner.parseRole(command[8]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Error: role %s\n", e.getMessage());
            usage();
            return;
        }

        Response<Employee> created = runner.getService().createEmployee(name, id, bank, bankID, bankBranch, new GregorianCalendar(), salaryPerHour, 0, 0, role, new LinkedHashSet<>());
        if (created.isError()) {
            System.out.printf("Error: %s\n", created.getErrorMessage());
            return;
        }
        System.out.printf("Created employee %s.\n", created.getValue().id);
    }

    private void usage() {
        System.out.println("Usage:");
        System.out.println("> create employee <name> <id> <bank> <bank-id> <bank-branch> <salary-per-hour> <role>");
    }
}

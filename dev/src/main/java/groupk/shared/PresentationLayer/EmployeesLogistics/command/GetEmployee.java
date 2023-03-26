package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;

import java.util.GregorianCalendar;

public class GetEmployee implements Command {
    @Override
    public String name() {
        return "get employee";
    }

    @Override
    public String description() {
        return "print an employee's information";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("get employee");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return true;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 3) {
            System.out.println("Error: Wrong number of arguments.");
            System.out.println("Usage:");
            System.out.println("> get employee <id>");
            return;
        }

        String id = command[2];
        Response<Employee> employee = runner.getService().readEmployee(runner.getSubject(), id);
        if (employee.isError()) {
            System.out.printf("Error: %s\n", employee.getErrorMessage());
            return;
        }
        Employee read = employee.getValue();
        String startDate = read.employmentStart.get(GregorianCalendar.DAY_OF_MONTH)
                + "/" + (read.employmentStart.get(GregorianCalendar.MONTH) + 1)
                + "/" + read.employmentStart.get(GregorianCalendar.YEAR);
        System.out.printf("name: %s\nid: %s\nrole: %s\nbank details:\n  bank: %s\n  branch: %s\n  account number: %s\nworking conditions:\n  hourly salary: %s\n  sick days used: %s\n  vacation days used: %s\n  employment start: %s\n", read.name, read.id, read.role, read.bank, read.bankBranch, read.bankID, read.salaryPerHour, read.sickDaysUsed, read.vacationDaysUsed, startDate);
        System.out.println("can work at:");
        for (Employee.ShiftDateTime shift: read.shiftPreferences) {
            System.out.println("  " + shift);
        }
    }
}

package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Shift;

import java.util.GregorianCalendar;
import java.util.List;

public class ListShifts implements Command {
    @Override
    public String name() {
        return "list shifts";
    }

    @Override
    public String description() {
        return "list past and future shifts";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("list shifts");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.HumanResources || role == Employee.Role.StoreManager;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 2) {
            System.out.println("Error: Too many arguments supplied.");
            System.out.println("Usage:");
            System.out.println("> list shifts");
            return;
        }

        Response<List<Shift>> shifts = runner.getService().listShifts(runner.getSubject());
        if (shifts.isError()) {
            System.out.printf("Error: %s\n", shifts.getErrorMessage());
            return;
        }

        for (Shift shift: shifts.getValue()) {
            String date = shift.getDate().get(GregorianCalendar.DAY_OF_MONTH)
                    + "/" + (shift.getDate().get(GregorianCalendar.MONTH) + 1)
                    + "/" + shift.getDate().get(GregorianCalendar.YEAR);
            System.out.printf("- date: %s\n  type: %s\n  required staff:\n", date, shift.getType());
            for (Employee.Role key: shift.getRequiredStaff().keySet()) {
                System.out.printf("  - role: %s\n    count: %s\n", key, shift.getRequiredStaff().get(key));
            }
            System.out.println("  staff:");
            for (Employee staff: shift.getStaff()) {
                System.out.printf("  - id: %s\n    name: %s\n    role: %s\n", staff.id, staff.name, staff.role);
            }
        }

    }
}

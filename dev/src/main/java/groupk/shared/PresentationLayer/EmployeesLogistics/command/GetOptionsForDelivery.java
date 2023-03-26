package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Shift;

import java.util.List;

public class GetOptionsForDelivery implements Command {
    @Override
    public String name() {
        return "get options for delivery";
    }

    @Override
    public String description() {
        return "return shifts with available drivers and logistics in the next week";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("get options for delivery");
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
            System.out.println("> get options for delivery");
            return;
        }


        Response<List<Shift>> response = runner.getService().optionsForDeleveryWithLogisitcsAndDriversInShift(runner.getSubject());
        if (response.isError()) {
            System.out.printf("Error: %s\n", response.getErrorMessage());
            return;
        }
        System.out.println("\nSHIFTS LIST:");
        List<Shift> shifts = response.getValue();
        if (shifts == null | shifts.size() == 0) {
            System.out.println("[empty]");
            return;
        }
        int i = 1;
        for (Shift shift : shifts) {
            if (shift == null)
                continue;
            System.out.println(i + ". " + shift.getDate() + " " + shift.getType().name() + ", Drivers:");
            List<Employee> employees = shift.getStaff();
            int index = 1;
            for (Employee employee : employees) {
                if (employee != null &employee.role.equals(Employee.Role.Driver)) {
                    System.out.println(index + ". name: " + employee.name + " id: " + employee.id);
                    index++;
                }
            }
            i++;
        }
    }
}

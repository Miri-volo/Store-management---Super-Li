package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.dto.Employee;

public interface Command {
    String name();
    String description();
    boolean isMatching(String line);
    boolean isVisible(Employee.Role role);
    void execute(String[] command, CommandRunner runner);
}

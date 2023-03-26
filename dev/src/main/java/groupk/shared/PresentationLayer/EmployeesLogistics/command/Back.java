package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.dto.Employee;

public class Back implements Command {
    @Override
    public String name() {
        return "back";
    }

    @Override
    public String description() {
        return "go back to the main menu";
    }

    @Override
    public boolean isMatching(String line) {
        return line.equals("back");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return true;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        runner.stop();
    }
}

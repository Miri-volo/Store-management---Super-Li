package groupk.shared.service.dto;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Shift {
    public enum Type {
        Morning,
        Evening
    }

    HashMap<Employee.Role, Integer> requiredStaff;
    List<Employee> staff;
    Calendar date;
    Type type;

    public Shift(Calendar date, Type type, List<Employee> staff, HashMap<Employee.Role, Integer> requiredStaff) {
        this.date = date;
        this.type = type;
        this.staff = staff;
        this.requiredStaff = requiredStaff;
    }

    public Type getType() {
        return type;
    }

    public Calendar getDate() {
        return date;
    }

    public HashMap<Employee.Role, Integer> getRequiredStaff() {
        return requiredStaff;
    }

    public List<Employee> getStaff() {
        return staff;
    }


}

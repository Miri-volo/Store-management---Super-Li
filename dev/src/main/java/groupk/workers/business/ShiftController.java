package groupk.workers.business;

import groupk.workers.data.DalController;
import groupk.workers.data.Employee;
import groupk.workers.data.Shift;
import groupk.workers.data.ShiftRepository;

import java.util.*;

public class ShiftController {
    private DalController dalColntroller;

    public ShiftController(DalController dalColntroller){
        this.dalColntroller = dalColntroller;
    }

    public Shift addShifts(groupk.workers.data.Shift shift){
        if(!ifShiftExist(shift)) {
            if(shift.getRequiredStaff().get(Employee.Role.ShiftManager) == 0)
                throw new IllegalArgumentException("In each shift there must be a Shift Manager");
            HashMap<Employee.Role, Integer> numOfRoles = new HashMap<>();
            for(Employee.Role r : Employee.Role.values())
                numOfRoles.put(r, 0);
            for(Employee e :shift.getStaff()) {
                numOfRoles.replace(e.getRole(), numOfRoles.get(e.getRole()) + 1);
            }
            for(Employee.Role r : shift.getRequiredStaff().keySet()){
                if(shift.getRequiredStaff().get(r) > numOfRoles.get(r))
                    throw new IllegalArgumentException("There are not enough workers to open this shift.");
            }
            return dalColntroller.addShift(shift);
        }
        else
            throw new IllegalArgumentException("Shift already exists.");
    }

    public Shift deleteShift(Shift.Type type, Calendar date){
        if(ifShiftExist(date, type))
            return dalColntroller.deleteShift(type, date);
        throw new IllegalArgumentException("Shift does not exists.");
    }

    public Shift getShift(Calendar date, Shift.Type type){
        LinkedList<Shift> shifts = dalColntroller.getShifts();
        for (Shift s: shifts) {
            if(s.getDate().equals(date) && s.getType().equals(type))
                return s;
        }
        throw new IllegalArgumentException("Shift does not exists.");
    }

    public boolean ifShiftExist(Shift shift){
        LinkedList<Shift> shifts = dalColntroller.getShifts();
        for (Shift s: shifts) {
            if(s.getDate().equals(shift.getDate()) && s.getType().equals(shift.getType()))
                return true;
        }
        return false;
    }

    public boolean ifShiftExist(Calendar date, Shift.Type type){
        LinkedList<Shift> shifts = dalColntroller.getShifts();
        for (Shift s: shifts) {
            if(s.getDate().equals(date) && s.getType().equals(type))
                return true;
        }
        return false;
    }

    public List<Shift> listShifts(){
        return new ArrayList<>(dalColntroller.getShifts());
    }

    public Shift setRequiredRoleInShift(Calendar date, Shift.Type type, Employee.Role role, int count) {
        if(role.equals(Employee.Role.ShiftManager) && count == 0)
            throw new IllegalArgumentException("In each shift there must be a Shift Manager");
        int numOfRoles = 0;
        Shift shift = getShift(date, type);
        for(Employee e :shift.getStaff()){
            if(e.getRole().equals(role))
                numOfRoles ++;
        }
        if(count < numOfRoles)
            throw new IllegalArgumentException("There are not enough workers with this role to run this shift.");
        return shift.setRequiredRoleInShift(role, count);
    }

    public Shift setRequiredStaffInShift(Calendar date, Shift.Type type, HashMap<Employee.Role, Integer> requiredStaff) {
        if(requiredStaff.get(Employee.Role.ShiftManager) == 0)
            throw new IllegalArgumentException("In each shift there must be a Shift Manager");
        HashMap<Employee.Role, Integer> numOfRoles = new HashMap<>();
        Shift shift = getShift(date, type);
        for(Employee.Role r : Employee.Role.values())
            numOfRoles.put(r, 0);
        for(Employee e :shift.getStaff()) {
            numOfRoles.replace(e.getRole(), numOfRoles.get(e.getRole()) + 1);
        }
        for(Employee.Role r : requiredStaff.keySet()){
            if(requiredStaff.get(r) > numOfRoles.get(r))
                throw new IllegalArgumentException("There are not enough workers to run this shift.");
        }
        return shift.setRequiredStaff(requiredStaff);
    }

}

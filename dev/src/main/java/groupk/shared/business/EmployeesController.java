package groupk.shared.business;

import groupk.shared.service.Response;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Shift;
import groupk.workers.business.WorkersFacade;
import groupk.workers.data.DalController;

import java.sql.Connection;
import java.util.*;

public class EmployeesController {
    public WorkersFacade employeeBusiness;

    public EmployeesController(DalController dalController) {
        employeeBusiness = new WorkersFacade(dalController);
    }

    public Response<Employee> createEmployee(
            String name,
            String id,
            String bank,
            int bankID,
            int bankBranch,
            Calendar employmentStart,
            int salaryPerHour,
            int sickDaysUsed,
            int vacationDaysUsed,
            Employee.Role role,
            Set<Employee.ShiftDateTime> shiftPreferences) {
        try {
            return new Response<>(employeeBusiness.addEmployee(name, id, bank, bankID, bankBranch, employmentStart, salaryPerHour, sickDaysUsed, vacationDaysUsed, role, shiftPreferences));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    //for test use
    public void deleteEmployeeDB() {
        employeeBusiness.deleteDB();
    }

    public void loadEmployeeDB(){ employeeBusiness.loadDB();}

    public Response<Shift> createShift(String subjectID, Calendar date, Shift.Type type,
                                       LinkedList<Employee> staff,
                                       HashMap<Employee.Role, Integer> requiredStaff) {
        try {
            return new Response<>(employeeBusiness.addShift(subjectID, date, type, staff, requiredStaff));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Employee> readEmployee(String subjectID, String employeeID) {
        try {
            return new Response<>(employeeBusiness.readEmployee(subjectID, employeeID));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Shift> readShift(String subjectID, Calendar date, Shift.Type type) {
        try {
            return new Response<>(employeeBusiness.readShift(subjectID, date, type));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Employee> deleteEmployee(String subjectID, String employeeID) {
        try {
            return new Response<>(employeeBusiness.deleteEmployee(subjectID, employeeID));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Shift> deleteShift(String subjectID, Shift.Type type, Calendar date){
        try {
            return new Response<>(employeeBusiness.deleteShift(subjectID, type, date));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<Employee>> listEmployees(String subjectID) {
        try {
            return new Response<>(employeeBusiness.listEmployees(subjectID));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Shift> addEmployeeToShift(String subjectID, Calendar date, Shift.Type type, String employeeID) {
        try {
            return new Response<>(employeeBusiness.addEmployeeToShift(subjectID, date, type, employeeID));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Shift> removeEmployeeFromShift(String subjectID, Calendar date, Shift.Type type, String employeeID) {
        try {
            return new Response<>(employeeBusiness.removeEmployeeFromShift(subjectID, date, type, employeeID));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Employee> updateEmployee(String subjectID, Employee changed) {
        try {
            return new Response<>(employeeBusiness.updateEmployee(subjectID, changed));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Employee> addEmployeeShiftPreference(String subjectID, String employeeID, Employee.ShiftDateTime shift) {
        try {
            return new Response<>(employeeBusiness.addEmployeeShiftPreference(subjectID, employeeID, shift));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Employee> setEmployeeShiftsPreference(String subjectID, String employeeID, Set<Employee.ShiftDateTime> shiftPreferences) {
        try {
            return new Response<>(employeeBusiness.setEmployeeShiftsPreference(subjectID, employeeID, shiftPreferences));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Employee> deleteEmployeeShiftPreference(String subjectID, String employeeID, Employee.ShiftDateTime shift) {
        try {
            return new Response<>(employeeBusiness.deleteEmployeeShiftPreference(subjectID, employeeID, shift));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<Shift>> listShifts(String subjectID) {
        try {
            return new Response<>(employeeBusiness.listShifts(subjectID));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<Shift>> listEmployeeShifts(String subjectID, String employeeID) {
        try {
            return new Response<>(employeeBusiness.listEmployeeShifts(subjectID, employeeID));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Integer> numOfEmployeeShifts(String subjectID, String employeeID) {
        try {
            return new Response<>(employeeBusiness.numOfShifts(subjectID, employeeID));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Shift> updateRequiredRoleInShift(String subjectId, Calendar date, Shift.Type type, Employee.Role role, int count) {
        try {
            return new Response<>(employeeBusiness.setRequiredRoleInShift(subjectId, date, type, role, count));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Shift> setRequiredStaffInShift(String subjectId, Calendar date, Shift.Type type, HashMap<Employee.Role, Integer> requiredStaff) {
        try {
            return new Response<>(employeeBusiness.setRequiredStaffInShift(subjectId, date, type, requiredStaff));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<Employee>> whoCanWorkWithRole(String subjectId, Employee.ShiftDateTime day, Employee.Role role) {
        try {
            return new Response<>(employeeBusiness.whoCanWorkWithRole(subjectId, day, role));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<Employee>> whoCanWork(String subjectId, Employee.ShiftDateTime day) {
        try {
            return new Response<>(employeeBusiness.whoCanWork(subjectId, day));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> isFromRole(String subjectId, Employee.Role role) {
        try {
            return new Response<>(employeeBusiness.isFromRole(subjectId, role));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }


}

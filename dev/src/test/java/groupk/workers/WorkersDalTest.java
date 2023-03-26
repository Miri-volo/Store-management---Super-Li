package groupk.workers;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Shift;
import groupk.workers.business.WorkersFacade;

import groupk.workers.data.DalController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

import static groupk.CustomAssertions.*;
public class WorkersDalTest {
    protected Connection connection;
    protected DalController dalController;

    @BeforeEach
    public void setService() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:testdatabase.db");
            dalController = new DalController(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @AfterEach
    public void afterService() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void loadDB(){
        Set<Employee.ShiftDateTime> availableShifts = new HashSet<>();
        availableShifts.add(Employee.ShiftDateTime.ThursdayEvening);
        WorkersFacade facade = new WorkersFacade(dalController);
        facade.deleteDB();
        Employee created = facade.addEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                availableShifts
        );
        Employee HR = facade.addEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        );
        Set<Employee.ShiftDateTime> shiftPreferences = new HashSet<>();
        for(Employee.ShiftDateTime s : Employee.ShiftDateTime.values())
            shiftPreferences.add(s);
        Employee SM = facade.addEmployee(
                "SM",
                "11411110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.ShiftManager,
                shiftPreferences
        );
        LinkedList<Employee> em = new LinkedList<>();
        em.add(SM);
        HashMap<Employee.Role, Integer> r = new HashMap<>();
        for(Employee.Role role : Employee.Role.values())
            r.put(role, 0);
        r.replace(Employee.Role.ShiftManager, 1);
        Shift shift = facade.addShift(HR.id, new GregorianCalendar(2022, Calendar.APRIL, 21),Shift.Type.Evening, em, r);
        try {
            connection.close();
            connection = DriverManager.getConnection("jdbc:sqlite:testdatabase.db");
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        dalController = new DalController(connection);
        facade = new WorkersFacade(dalController);
        facade.loadDB();
        assertEquals(facade.listShifts("111111110").size(), 1);
        assertEquals(facade.listShifts("111111110").get(0).getDate(), shift.getDate());
        assertEquals(facade.listEmployees("111111110").size(), 3);
    }

    @Test
    public void UpdateEmployeeDB(){
        Set<Employee.ShiftDateTime> availableShifts = new HashSet<Employee.ShiftDateTime>();
        availableShifts.add(Employee.ShiftDateTime.ThursdayEvening);
        WorkersFacade facade = new WorkersFacade(dalController);
        facade.deleteDB();
        Employee created = facade.addEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                availableShifts
        );
        Employee HR = facade.addEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        );
        Set<Employee.ShiftDateTime> shiftPreferences = new HashSet<>();
        for(Employee.ShiftDateTime s : Employee.ShiftDateTime.values())
            shiftPreferences.add(s);
        Employee SM = facade.addEmployee(
                "SM",
                "11411110",
                "FooBank",
                1, 1,
                new GregorianCalendar(2022, Calendar.JANUARY, 5),
                30,
                0, 0,
                Employee.Role.ShiftManager,
                shiftPreferences
        );
        LinkedList<Employee> em = new LinkedList<>();
        em.add(SM);
        HashMap<Employee.Role, Integer> r = new HashMap<>();
        for(Employee.Role role : Employee.Role.values())
            r.put(role, 0);
        r.replace(Employee.Role.ShiftManager, 1);
        Shift shift = facade.addShift(HR.id, new GregorianCalendar(2022, Calendar.APRIL, 21),Shift.Type.Evening, em, r);
        Employee createdUpdate = new Employee(
                "111111111",
                "Foo2",
                Employee.Role.LogisticsManager,
                "FooBank2",
                12, 12,
                302,
                2, 2,
                availableShifts,
                new GregorianCalendar(2022, Calendar.APRIL, 5)
        );
        facade.updateEmployee(HR.id, createdUpdate);
        facade = new WorkersFacade(dalController);
        facade.loadDB();
        assertEquals(facade.readEmployee("111111110", "111111111").name, createdUpdate.name);
        assertEquals(facade.readEmployee("111111110", "111111111").role, createdUpdate.role);
        assertEquals(facade.readEmployee("111111110", "111111111").bankBranch, createdUpdate.bankBranch);
        assertEquals(facade.readEmployee("111111110", "111111111").bank, createdUpdate.bank);
        assertEquals(facade.readEmployee("111111110", "111111111").bankID, createdUpdate.bankID);
        assertEquals(facade.readEmployee("111111110", "111111111").salaryPerHour, createdUpdate.salaryPerHour);
        assertEquals(facade.readEmployee("111111110", "111111111").vacationDaysUsed, createdUpdate.vacationDaysUsed);
        assertEquals(facade.readEmployee("111111110", "111111111").sickDaysUsed, createdUpdate.sickDaysUsed);
    }
}

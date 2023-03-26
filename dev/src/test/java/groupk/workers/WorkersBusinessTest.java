package groupk.workers;

import static org.junit.jupiter.api.Assertions.*;

import groupk.workers.business.WorkersFacade;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Shift;
import groupk.workers.data.DalController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import static groupk.CustomAssertions.*;
public class WorkersBusinessTest {
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
    public void testCreateEmployee()
    {
        WorkersFacade facade = new WorkersFacade(dalController);
        facade.deleteDB();
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
        List<Employee> list = facade.listEmployees("111111110");
        assertEquals(1, list.size());
        assertEquals("111111110", list.get(0).id);
        assertEquals("Foo", list.get(0).name);
    }

    @Test
    public void testCreateBasicShift()
    {
        Set<Employee.ShiftDateTime> shiftPreferences = new HashSet<>();
        for(Employee.ShiftDateTime shiftDateTime : Employee.ShiftDateTime.values())
            shiftPreferences.add(shiftDateTime);
        WorkersFacade facade = new WorkersFacade(dalController);
        facade.deleteDB();
        Employee HR = facade.addEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                shiftPreferences
        );
        Employee LM = facade.addEmployee(
                "Foo",
                "11111410",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.LogisticsManager,
                shiftPreferences
        );
        Employee SM = facade.addEmployee(
                "Foo",
                "11111210",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.StoreManager,
                shiftPreferences
        );
        Employee s = facade.addEmployee(
                "Foo",
                "1115210",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                shiftPreferences
        );
        Employee c = facade.addEmployee(
                "Foo",
                "115210",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Cashier,
                shiftPreferences
        );
        Employee d = facade.addEmployee(
                "Foo",
                "1915210",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Driver,
                shiftPreferences
        );
        Employee l = facade.addEmployee(
                "Foo",
                "1415210",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Logistics,
                shiftPreferences
        );

        Employee ShM = facade.addEmployee(
                "Foo",
                "11180",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.ShiftManager,
                shiftPreferences
        );
        Employee TM = facade.addEmployee(
                "Foo2",
                "111830",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.LogisticsManager,
                shiftPreferences
        );
        LinkedList<Employee> em = new LinkedList<>();
        em.add(SM); em.add(HR); em.add(LM); em.add(s); em.add(d); em.add(ShM); em.add(c); em.add(l); em.add(TM);
        Shift shift = facade.addShift(HR.id, new GregorianCalendar(),Shift.Type.Morning, em, new HashMap<>());
        assertEquals(facade.listShifts(HR.id).size(), 1);
        assertEquals(facade.readShift(HR.id, shift.getDate(), shift.getType()).getRequiredStaff().size(),8);
        assertEquals(facade.readShift(HR.id, shift.getDate(), shift.getType()).getRequiredStaff().get(Employee.Role.StoreManager),1);
        assertEquals(facade.readShift(HR.id, shift.getDate(), shift.getType()).getRequiredStaff().get(Employee.Role.LogisticsManager),1);
        assertEquals(facade.readShift(HR.id, shift.getDate(), shift.getType()).getRequiredStaff().get(Employee.Role.HumanResources),1);
    }

    @Test
    public void testDeleteEmployeeByHR() {
        WorkersFacade facade = new WorkersFacade(dalController);
        facade.deleteDB();
        facade.addEmployee(
                "Foo",
                "111111111",

                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        );
        facade.addEmployee(

                "Bar",
                "222222222",
                "BarBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        );
        facade.deleteEmployee("222222222", "111111111");
        List<Employee> list = facade.listEmployees("222222222");
        assertEquals(1, list.size());
    }

    @Test
    public void testAddEmployeeShiftPreference(){
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
                new HashSet<>()
        );
        facade.addEmployeeShiftPreference(created.id, created.id, Employee.ShiftDateTime.FridayMorning);
        Employee.ShiftDateTime createdShift = facade.readEmployee(created.id, created.id).shiftPreferences.iterator().next();
        assertEquals(Employee.ShiftDateTime.FridayMorning, createdShift);
    }

    @Test
    public void testAddEmployeeToShift(){
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
        facade.addEmployeeToShift(HR.id, shift.getDate(), Shift.Type.Evening, created.id);
        assertEquals(facade.readShift(HR.id, shift.getDate(), shift.getType()).getStaff().size(), 2);
    }

    @Test
    public void testAddEmployeeToShiftCanNotWork() {
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
                new HashSet<>()
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
        assertThrows(Exception.class, () -> {
            facade.addEmployeeToShift(HR.id, shift.getDate(), Shift.Type.Evening, created.id);
        });
    }

    @Test
    public void testRemoveEmployeeFromShift(){
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
        HashMap<Employee.Role, Integer> r = new HashMap<>();
        for(Employee.Role role : Employee.Role.values())
            r.put(role, 0);
        r.replace(created.role, 1);
        r.replace(Employee.Role.ShiftManager, 1);
        LinkedList<Employee> employees = new LinkedList<>();
        employees.add(created); employees.add(SM);
        Shift shift = facade.addShift(HR.id, new GregorianCalendar(2022, Calendar.APRIL, 21),
                Shift.Type.Evening, employees, r);
        assertThrows(Exception.class, () -> {
            facade.removeEmployeeFromShift(HR.id, shift.getDate(), Shift.Type.Evening, created.id);
        });
    }

    @Test
    public void testWhoCanWork() {
        WorkersFacade facade = new WorkersFacade(dalController);
        facade.deleteDB();
        Employee HR = facade.addEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        );
        Employee created = facade.addEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        );
        Employee created2 = facade.addEmployee(
                "Foo",
                "111111100",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Cashier,
                new HashSet<>()
        );
        facade.addEmployeeShiftPreference(created.id, created.id, Employee.ShiftDateTime.FridayEvening);
        facade.addEmployeeShiftPreference(created2.id, created2.id, Employee.ShiftDateTime.FridayEvening);
        assertEquals(2, facade.whoCanWork(HR.id, Employee.ShiftDateTime.FridayEvening).size());
        assertEquals(1, facade.whoCanWorkWithRole(HR.id, Employee.ShiftDateTime.FridayEvening, Employee.Role.Cashier).size());
    }

    @Test
    public void testAddEmployeeToShiftNoEmployee() {
        WorkersFacade facade = new WorkersFacade(dalController);
        facade.deleteDB();
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
                "111141110",
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
        r.replace(Employee.Role.ShiftManager,1);
        Shift shift = facade.addShift(HR.id, new GregorianCalendar(2022, Calendar.APRIL, 21), Shift.Type.Evening, em, r);
        assertThrows(Exception.class, () -> {
            facade.addEmployeeToShift(HR.id, shift.getDate(), Shift.Type.Evening, "111111111");
        });
    }

    @Test
    public void testSetRequiredStaffInShift(){
        Set<Employee.ShiftDateTime> availableShifts = new HashSet<Employee.ShiftDateTime>();
        availableShifts.add(Employee.ShiftDateTime.ThursdayEvening);
        WorkersFacade facade = new WorkersFacade(dalController);
        facade.deleteDB();
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
        Employee created = facade.addEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Cashier,
                availableShifts
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
        em.add(created); em.add(SM);
        HashMap<Employee.Role, Integer> r = new HashMap<>();
        for(Employee.Role role : Employee.Role.values())
            r.put(role, 0);
        r.replace(Employee.Role.ShiftManager, 1);
        r.replace(Employee.Role.Cashier, 1);
        Shift shift = facade.addShift(HR.id, new GregorianCalendar(2022, Calendar.APRIL, 21),Shift.Type.Evening, em, r);
        r.replace(Employee.Role.Cashier, 2);
        assertThrows(Exception.class, () -> {
            facade.setRequiredStaffInShift(HR.id, shift.getDate(), shift.getType(), r);
        });
        r.replace(Employee.Role.Cashier, 0);
        facade.setRequiredStaffInShift(HR.id, shift.getDate(), shift.getType(), r);
        assertEquals(facade.readShift(HR.id, shift.getDate(), shift.getType()).getRequiredStaff().get(Employee.Role.Cashier),0);
    }
}

package groupk.workers;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import groupk.inventory_suppliers.SchemaInit;
import groupk.inventory_suppliers.dataLayer.dao.PersistenceController;
import groupk.logistics.DataLayer.myDataBase;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Shift;
import groupk.shared.service.Service;
import groupk.workers.data.DalController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class WorkersServiceTest {
    protected Connection connection;
    protected DalController dalController;
    protected myDataBase logisitcsDalController;
    protected PersistenceController persistenceController;

    @BeforeEach
    public void setService() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:testdatabase.db");
            SchemaInit.init(connection);
            persistenceController = new PersistenceController(connection);
            dalController = new DalController(connection);
            logisitcsDalController = new myDataBase(connection);
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
    public void testCreateShiftMorning()
    {
        Set<Employee.ShiftDateTime> shiftPreferences = new HashSet<>();
        shiftPreferences.addAll(Arrays.asList(Employee.ShiftDateTime.values()));
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee HR = (service.createEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                shiftPreferences)).getValue();
        Employee LM = service.createEmployee(
                "Foo",
                "11115110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.LogisticsManager,
                shiftPreferences).getValue();
        Employee shiftM = service.createEmployee(
                "Foo",
                "11115170",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.ShiftManager,
                shiftPreferences).getValue();
        Employee s = service.createEmployee(
                "Foo",
                "11115120",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                shiftPreferences).getValue();
        Employee c = service.createEmployee(
                "Foo",
                "21115120",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Cashier,
                shiftPreferences).getValue();
        Employee d = service.createEmployee(
                "Foo",
                "21115121",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Driver,
                shiftPreferences).getValue();
        Employee l = service.createEmployee(
                "Foo",
                "211005121",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Logistics,
                shiftPreferences).getValue();
        Employee SM = service.createEmployee(
                "Foo",
                "211085121",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.StoreManager,
                shiftPreferences).getValue();
        Employee TM = service.createEmployee(
                "F3oo",
                "214444121",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.LogisticsManager,
                shiftPreferences).getValue();
        LinkedList<Employee> em = new LinkedList<>();
        em.add(SM); em.add(s); em.add(l); em.add(d); em.add(HR); em.add(shiftM); em.add(LM); em.add(c); em.add(TM);
        Shift shift = service.createShift(HR.id, new GregorianCalendar(),Shift.Type.Morning, em, new HashMap<>()).getValue();
        assertEquals(service.listShifts(HR.id).getValue().size(), 1);
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getRequiredStaff().size(),8);
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getRequiredStaff().get(Employee.Role.StoreManager),1);
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getRequiredStaff().get(Employee.Role.LogisticsManager),1);
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getRequiredStaff().get(Employee.Role.HumanResources),1);
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getStaff().size(),9);

    }

    @Test
    public void testCreateShiftEvening()
    {
        Set<Employee.ShiftDateTime> shiftPreferences = new HashSet<>();
        shiftPreferences.addAll(Arrays.asList(Employee.ShiftDateTime.values()));
        Service service = new Service(persistenceController, dalController, logisitcsDalController);        service.deleteEmployeeDB();
        Employee HR = service.createEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                shiftPreferences).getValue();
        Employee shiftM = service.createEmployee(
                "Foo",
                "11115170",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.ShiftManager,
                shiftPreferences).getValue();
        Employee s = service.createEmployee(
                "Foo",
                "11115120",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                shiftPreferences).getValue();
        Employee c = service.createEmployee(
                "Foo",
                "21115120",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Cashier,
                shiftPreferences).getValue();
        Employee d = service.createEmployee(
                "Foo",
                "21115121",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Driver,
                shiftPreferences).getValue();
        Employee l = service.createEmployee(
                "Foo",
                "211005121",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Logistics,
                shiftPreferences).getValue();
        Employee TM = service.createEmployee(
                "Foo",
                "21102051231",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.LogisticsManager,
                shiftPreferences).getValue();
        LinkedList<Employee> em = new LinkedList<>();
        em.add(s); em.add(l); em.add(d); em.add(shiftM); em.add(c); em.add(TM);
        Shift shift = service.createShift(HR.id, new GregorianCalendar(),Shift.Type.Evening, em, new HashMap<>()).getValue();
        assertEquals(service.listShifts(HR.id).getValue().size(), 1);
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getRequiredStaff().size(),8);
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getRequiredStaff().get(Employee.Role.StoreManager),0);
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getRequiredStaff().get(Employee.Role.LogisticsManager),0);
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getRequiredStaff().get(Employee.Role.HumanResources),0);
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getStaff().size(),6);
    }

    @Test
    public void testCreateShiftNotUnauthorized()
    {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee NotHR = service.createEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()).getValue();
        assertTrue(service.createShift(NotHR.id, new GregorianCalendar(), Shift.Type.Evening, new LinkedList<>(), new LinkedHashMap<>()).isError());
    }

    @Test
    public void testCreateEmployee()
    {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        ).getValue();
        List<Employee> list = service.listEmployees("111111111").getValue();
        assertEquals(1, list.size());
        assertEquals("111111111", list.get(0).id);
        assertEquals("Foo", list.get(0).name);
    }

    @Test
    public void testCreateEmployeeSameID() {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        ).getValue();
        assertTrue(service.createEmployee(
                "Bar",
                "111111111",
                "BarBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        ).isError());
        List<Employee> list = service.listEmployees("111111111").getValue();
        assertEquals(1, list.size());
    }

    @Test
    public void testReadEmployeesUnauthorized() {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        ).getValue();
        assertTrue(service.listEmployees("111111111").isError());
    }

    @Test
    public void testDeleteEmployeeByHR() {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        service.createEmployee(
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
        service.createEmployee(
                "Bar",
                "222222222",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        );
        service.deleteEmployee("222222222", "111111111");
        List<Employee> list = service.listEmployees("222222222").getValue();
        assertEquals(1, list.size());
    }

    @Test
    public void testDeleteEmployeeUnauthorized() {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        service.createEmployee(
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
        assertTrue(service.deleteEmployee("111111111", "999999999").isError());
    }

    @Test
    public void testReadEmployee() {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        service.createEmployee(
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
        Employee self = service.readEmployee("111111111", "111111111").getValue();
        assertEquals("111111111", self.id);
        assertEquals("Foo", self.name);
    }

    @Test
    public void testReadEmployeeByHR() {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee created = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        ).getValue();
        Employee HR = service.createEmployee(
                "Foo",
                "222222222",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        ).getValue();
        Employee self = service.readEmployee("222222222", "111111111").getValue();
        assertEquals("111111111", self.id);
        assertEquals("Foo", self.name);
    }

    @Test
    public void testReadEmployeeUnauthorized() {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee created = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        ).getValue();
        Employee created2 = service.createEmployee(
                "Foo",
                "222222222",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Cashier,
                new HashSet<>()
        ).getValue();
        assertTrue(service.readEmployee("222222222", "111111111").isError());
    }

    @Test
    public void testUpdateEmployeeByHR() {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee created = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        ).getValue();
        Employee created2 = service.createEmployee(
                "Foo",
                "222222222",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        ).getValue();
        created.name = "Changed";
        service.updateEmployee("222222222", created);
        Employee foo = service.readEmployee("222222222", "111111111").getValue();
        assertEquals("Changed", foo.name);
    }

    @Test
    public void testUpdateEmployeeUnauthorized() {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee created = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        ).getValue();
        Employee created2 = service.createEmployee(
                "Foo",
                "222222222",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Cashier,
                new HashSet<>()
        ).getValue();
        assertTrue(service.updateEmployee("222222222", created).isError());
    }

    @Test
    public void testAddEmployeeShiftPreference(){
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee created = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        ).getValue();
        service.addEmployeeShiftPreference(created.id, created.id, Employee.ShiftDateTime.FridayMorning);
        Employee.ShiftDateTime createdShift = service.readEmployee(created.id, created.id).getValue().shiftPreferences.iterator().next();
        assertEquals(Employee.ShiftDateTime.FridayMorning, createdShift);
    }

    @Test
    public void testAddEmployeeShiftPreferenceFromAnotherId(){
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee created = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        ).getValue();
        Employee created2 = service.createEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        ).getValue();
        assertTrue(service.addEmployeeShiftPreference(created2.id, created.id, Employee.ShiftDateTime.FridayMorning).isError());
    }

    @Test
    public void testDeleteEmployeeShiftPreference(){
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee created = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        ).getValue();
        service.addEmployeeShiftPreference(created.id, created.id, Employee.ShiftDateTime.FridayMorning);
        service.deleteEmployeeShiftPreference(created.id, created.id, Employee.ShiftDateTime.FridayMorning);
        assertEquals(0, service.readEmployee(created.id, created.id).getValue().shiftPreferences.size());
    }

    @Test
    public void testSetEmployeeShiftsPreference(){
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee created = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        ).getValue();
        Set<Employee.ShiftDateTime> set = new HashSet<>();
        set.add(Employee.ShiftDateTime.FridayMorning);
        set.add(Employee.ShiftDateTime.MondayEvening);
        service.setEmployeeShiftsPreference(created.id, created.id, set);
        assertEquals(2, service.readEmployee(created.id, created.id).getValue().shiftPreferences.size());
    }

    @Test
    public void testAddEmployeeToShift(){
        Set<Employee.ShiftDateTime> availableShifts = new HashSet<>();
        availableShifts.add(Employee.ShiftDateTime.ThursdayEvening);
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee created = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Cashier,
                availableShifts
        ).getValue();
        Employee HR = service.createEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        ).getValue();
        Employee shiftM = service.createEmployee(
                "Foo",
                "11115170",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.ShiftManager,
                availableShifts
        ).getValue();
        LinkedList<Employee> employees = new LinkedList<>();
        employees.add(shiftM);
        HashMap<Employee.Role, Integer> r = new HashMap<>();
        for(Employee.Role role : Employee.Role.values())
            r.put(role, 0);
        r.replace(Employee.Role.ShiftManager, 1);
        Shift shift = service.createShift(HR.id, new GregorianCalendar(2022, Calendar.APRIL, 21),Shift.Type.Evening, employees, r).getValue();
        service.addEmployeeToShift(HR.id, shift.getDate(), Shift.Type.Evening, created.id);
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getStaff().size(), 2);
    }

    @Test
    public void testSetRequiredStaffInShift(){
        Set<Employee.ShiftDateTime> availableShifts = new HashSet<>();
        availableShifts.add(Employee.ShiftDateTime.ThursdayEvening);
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee created = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Cashier,
                availableShifts
        ).getValue();
        Employee HR = service.createEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        ).getValue();
        Employee shiftM = service.createEmployee(
                "Foo",
                "11115170",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.ShiftManager,
                availableShifts
        ).getValue();
        LinkedList<Employee> em = new LinkedList<>();
        em.add(created); em.add(shiftM);
        HashMap<Employee.Role, Integer> r = new HashMap<>();
        for(Employee.Role role : Employee.Role.values())
            r.put(role, 0);
        r.replace(Employee.Role.Cashier, 1);
        r.replace(Employee.Role.ShiftManager, 1);
        Shift shift = service.createShift(HR.id, new GregorianCalendar(2022, Calendar.APRIL, 21),Shift.Type.Evening, em, r).getValue();
        r.replace(Employee.Role.Cashier, 2);
        assertTrue(service.setRequiredStaffInShift(HR.id, shift.getDate(), shift.getType(), r).isError());
        r.replace(Employee.Role.Cashier, 0);
        service.setRequiredStaffInShift(HR.id, shift.getDate(), shift.getType(), r);
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getRequiredStaff().get(Employee.Role.Cashier),0);
    }

    @Test
    public void testAddEmployeeToShiftCanNotWork() {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee created = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        ).getValue();
        Employee HR = service.createEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        ).getValue();
        Set<Employee.ShiftDateTime> shiftPreferences = new HashSet<>();
        shiftPreferences.addAll(Arrays.asList(Employee.ShiftDateTime.values()));
        Employee shiftM = service.createEmployee(
                "Foo",
                "11115170",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.ShiftManager,
                shiftPreferences
        ).getValue();
        LinkedList<Employee> employees = new LinkedList<>();
        employees.add(shiftM);
        HashMap<Employee.Role, Integer> r = new HashMap<>();
        for(Employee.Role role : Employee.Role.values())
            r.put(role, 0);
        r.replace(Employee.Role.ShiftManager, 1);
        Shift shift = service.createShift(HR.id, new GregorianCalendar(2022, Calendar.APRIL, 21),Shift.Type.Evening, employees, r).getValue();
        assertTrue(service.addEmployeeToShift(HR.id, shift.getDate(), Shift.Type.Evening, created.id).isError());
    }

    @Test
    public void testAddEmployeeToShiftNoEmployee() {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee HR = service.createEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        ).getValue();
        Set<Employee.ShiftDateTime> shiftPreferences = new HashSet<>();
        shiftPreferences.addAll(Arrays.asList(Employee.ShiftDateTime.values()));
        Employee shiftM = service.createEmployee(
                "Foo",
                "11115170",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.ShiftManager,
                shiftPreferences
        ).getValue();
        LinkedList<Employee> employees = new LinkedList<>();
        employees.add(shiftM);
        HashMap<Employee.Role, Integer> r = new HashMap<>();
        for(Employee.Role role : Employee.Role.values())
            r.put(role, 0);
        r.replace(Employee.Role.ShiftManager, 1);
        Shift shift = service.createShift(HR.id, new GregorianCalendar(2022, Calendar.APRIL, 21),Shift.Type.Evening, employees, r).getValue();
        assertTrue(service.addEmployeeToShift(HR.id, shift.getDate(), Shift.Type.Evening, "111111111").isError());
    }

    @Test
    public void testRemoveEmployeeFromShift(){
        Set<Employee.ShiftDateTime> availableShifts = new HashSet<>();
        availableShifts.add(Employee.ShiftDateTime.ThursdayEvening);
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee created = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                availableShifts
        ).getValue();
        Employee HR = service.createEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        ).getValue();
        Set<Employee.ShiftDateTime> shiftPreferences = new HashSet<>();
        shiftPreferences.addAll(Arrays.asList(Employee.ShiftDateTime.values()));
        Employee shiftM = service.createEmployee(
                "Foo",
                "11115170",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.ShiftManager,
                availableShifts
        ).getValue();
        HashMap<Employee.Role, Integer> r = new HashMap<>();
        for(Employee.Role role : Employee.Role.values())
            r.put(role, 0);
        r.put(created.role, 1);
        r.replace(Employee.Role.ShiftManager, 1);
        LinkedList<Employee> em = new LinkedList<>();
        em.add(created); em.add(shiftM);
        Shift shift = service.createShift(HR.id, new GregorianCalendar(2022, Calendar.APRIL, 21),
                Shift.Type.Evening, em, r).getValue();
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getStaff().size(), 2);
        assertTrue(service.removeEmployeeFromShift(HR.id, shift.getDate(), Shift.Type.Evening, created.id).isError());
        assertEquals(service.readShift(HR.id, shift.getDate(), shift.getType()).getValue().getStaff().size(), 2);
    }

    @Test
    public void testRemoveEmployeeFromShiftNoEmployee() {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee HR = service.createEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        ).getValue();
        Set<Employee.ShiftDateTime> shiftPreferences = new HashSet<>();
        shiftPreferences.addAll(Arrays.asList(Employee.ShiftDateTime.values()));
        Employee shiftM = service.createEmployee(
                "Foo",
                "11115170",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.ShiftManager,
                shiftPreferences
        ).getValue();
        HashMap<Employee.Role, Integer> r = new HashMap<>();
        for(Employee.Role role : Employee.Role.values())
            r.put(role, 0);
        r.replace(Employee.Role.ShiftManager, 1);
        LinkedList<Employee> employees = new LinkedList<>();
        employees.add(shiftM);
        Shift shift = service.createShift(HR.id, new GregorianCalendar(2022, Calendar.APRIL, 21),Shift.Type.Evening, employees, r).getValue();
        assertTrue(service.removeEmployeeFromShift(HR.id, shift.getDate(), Shift.Type.Evening, "111111111").isError());
    }

    @Test
    public void testWhoCanWork() {
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee HR = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()
        ).getValue();
        Employee created = service.createEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        ).getValue();
        Employee created2 = service.createEmployee(
                "Foo",
                "111111100",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Cashier,
                new HashSet<>()
        ).getValue();
        service.addEmployeeShiftPreference(created.id, created.id, Employee.ShiftDateTime.FridayEvening);
        service.addEmployeeShiftPreference(created2.id, created2.id, Employee.ShiftDateTime.FridayEvening);
        assertEquals(2, service.whoCanWork(HR.id, Employee.ShiftDateTime.FridayEvening).getValue().size());
        assertEquals(1, service.whoCanWorkWithRole(HR.id, Employee.ShiftDateTime.FridayEvening, Employee.Role.Cashier).getValue().size());
    }

    @Test
    public void testNumOfShifts(){
        Service service = new Service(persistenceController, dalController, logisitcsDalController);
        service.deleteEmployeeDB();
        Employee created = service.createEmployee(
                "Foo",
                "111111111",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.Stocker,
                new HashSet<>()
        ).getValue();
        Employee HR = service.createEmployee(
                "Foo",
                "111111110",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.HumanResources,
                new HashSet<>()).getValue();
        Set<Employee.ShiftDateTime> shiftPreferences = new HashSet<>();
        shiftPreferences.addAll(Arrays.asList(Employee.ShiftDateTime.values()));
        Employee shiftM = service.createEmployee(
                "Foo",
                "11115170",
                "FooBank",
                1, 1,
                new GregorianCalendar(),
                30,
                0, 0,
                Employee.Role.ShiftManager,
                shiftPreferences).getValue();
        LinkedList<Employee> employees = new LinkedList<>();
        employees.add(shiftM);
        HashMap<Employee.Role, Integer> r = new HashMap<>();
        for(Employee.Role role : Employee.Role.values())
            r.put(role, 0);
        r.replace(Employee.Role.ShiftManager, 1);
        Shift shift = service.createShift(HR.id, new GregorianCalendar(2022, Calendar.APRIL, 21),Shift.Type.Evening, employees, r).getValue();
        Shift shift2 = service.createShift(HR.id, new GregorianCalendar(2022, Calendar.APRIL, 20),Shift.Type.Evening, employees, r).getValue();
        service.addEmployeeShiftPreference(created.id, created.id, Employee.ShiftDateTime.ThursdayEvening);
        service.addEmployeeShiftPreference(created.id, created.id, Employee.ShiftDateTime.WednesdayEvening);
        service.addEmployeeToShift(HR.id, shift.getDate(), Shift.Type.Evening, created.id);
        service.addEmployeeToShift(HR.id, shift2.getDate(), Shift.Type.Evening, created.id);
        assertEquals(service.numOfEmployeeShifts(HR.id, created.id).getValue(), 2);
        assertEquals(service.numOfEmployeeShifts(HR.id, HR.id).getValue(), 0);
    }


}

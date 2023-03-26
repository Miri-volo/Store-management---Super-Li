package groupk.workers.data;
import javax.imageio.IIOException;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class DalController {
    public static String url;
    private ShiftRepository shiftRepository;
    private EmployeeRepository employeeRepository;
    public static Connection connection;
    public static File file;

    public DalController(Connection conn) {
        connection = conn;
//        file = new File("database.db");
//        url = ("jdbc:sqlite:").concat(file.getAbsolutePath());
        try{
            createTables();
            load();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        shiftRepository = new ShiftRepository();
        employeeRepository = new EmployeeRepository();
    }

    public void createTables() {
        LinkedList<String> tables = new LinkedList<>();
        tables.add("CREATE TABLE IF NOT EXISTS \"Employee\" (\n" +
                "\t\"ID\"\tTEXT,\n" +
                "\t\"Name\"\tTEXT,\n" +
                "\t\"BankName\"\tTEXT,\n" +
                "\t\"BankBranch\"\tINTEGER,\n" +
                "\t\"BankID\"\tINTEGER,\n" +
                "\t\"EmploymentStart\"\tTEXT,\n" +
                "\t\"SickDaysUsed\"\tINTEGER,\n" +
                "\t\"VacationDaysUsed\"\tINTEGER,\n" +
                "\t\"SalaryPerHour\"\tINTEGER,\n" +
                "\t\"Role\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"ID\"),\n" +
                "\tFOREIGN KEY(\"Role\") REFERENCES \"Role\"(\"Name\") ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");");
        tables.add("CREATE TABLE IF NOT EXISTS \"RequiredStaff\" (\n" +
                "\t\"Count\"\tINTEGER,\n" +
                "\t\"Role\"\tTEXT,\n" +
                "\t\"Type\"\tTEXT,\n" +
                "\t\"Date\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"Date\",\"Type\",\"Role\"),\n" +
                "\tFOREIGN KEY(\"Date\") REFERENCES \"Shift\"(\"Date\") ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "\tFOREIGN KEY(\"Role\") REFERENCES \"Shift\"(\"Role\") ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "\tFOREIGN KEY(\"Role\") REFERENCES \"Role\"(\"Name\") ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");");
        tables.add("CREATE TABLE IF NOT EXISTS \"Role\" (\n" +
                "\t\"Name\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"Name\")\n" +
                ");");
        tables.add("CREATE TABLE IF NOT EXISTS \"Shift\" (\n" +
                "\t\"Type\"\tTEXT,\n" +
                "\t\"Date\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"Date\",\"Type\")\n" +
                ");");
        tables.add("CREATE TABLE IF NOT EXISTS \"ShiftPreference\" (\n" +
                "\t\"EmployeeID\"\tTEXT,\n" +
                "\t\"ShiftType\"\tTEXT,\n" +
                "\tFOREIGN KEY(\"ShiftType\") REFERENCES \"ShiftSlot\"(\"Type\") ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "\tPRIMARY KEY(\"EmployeeID\",\"ShiftType\"),\n" +
                "\tFOREIGN KEY(\"EmployeeID\") REFERENCES \"Employee\"(\"ID\") ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");");
        tables.add("CREATE TABLE IF NOT EXISTS \"ShiftSlot\" (\n" +
                "\t\"Type\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"Type\")\n" +
                ");");
        tables.add("CREATE TABLE IF NOT EXISTS \"Workers\" (\n" +
                "\t\"Type\"\tTEXT,\n" +
                "\t\"Date\"\tTEXT,\n" +
                "\t\"EmployeeID\"\tTEXT,\n" +
                "\tFOREIGN KEY(\"Date\") REFERENCES \"Shift\"(\"Date\") ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "\tFOREIGN KEY(\"EmployeeID\") REFERENCES \"Employee\"(\"ID\") ON DELETE SET NULL ON UPDATE CASCADE\n" +
                "\tFOREIGN KEY(\"Type\") REFERENCES \"Shift\"(\"Type\") ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "\tPRIMARY KEY(\"Date\",\"Type\",\"EmployeeID\")\n" +
                ");");
        try (
            Statement statement = connection.createStatement()){
            for (String table : tables) {
                statement.addBatch(table);
            }
            statement.executeBatch();
        } catch (SQLException s) {
            throw new IllegalArgumentException(s.getMessage());
        }
    }

    public void load(){
        try{
            Statement statement = connection.createStatement();
            for (Employee.Role role: Employee.Role.values()) {
                String insertRole = "INSERT OR IGNORE INTO Role VALUES ('"+ role.name()+"')";
                statement.executeUpdate(insertRole);
            }
            for (Employee.ShiftDateTime shift: Employee.ShiftDateTime.values()) {
                String insertShift = "INSERT OR IGNORE INTO ShiftSlot VALUES ('" + shift.name() + "')";
                statement.executeUpdate(insertShift);
            }
        }
        catch (SQLException s){
            throw new IllegalArgumentException(s.getMessage());
        }
    }

    public void deleteDataBase(){
        shiftRepository.delete();
        employeeRepository.delete();
        LinkedList<String> tables = new LinkedList<>();
        tables.add("DELETE FROM Employee");
        tables.add("DELETE FROM RequiredStaff");
        tables.add("DELETE FROM Role");
        tables.add("DELETE FROM Shift");
        tables.add("DELETE FROM ShiftPreference");
        tables.add("DELETE FROM ShiftSlot");
        tables.add("DELETE FROM Workers");
        try (
                Statement statement = connection.createStatement()){
            for (String table : tables) {
                statement.addBatch(table);
            }
            statement.executeBatch();
        } catch (SQLException s) {
            throw new IllegalArgumentException(s.getMessage());
        }
        createTables();
    }

    public List<Employee> getEmployees() {
        return employeeRepository.getEmployees();
    }

    public Employee getEmployee(String id) {
        return employeeRepository.getEmployee(id);
    }

    public Employee addEmployee(String name, String id, String bank, int bankID, int bankBranch,
                                Calendar employmentStart, int salaryPerHour, int sickDaysUsed, int vacationDaysUsed, Employee.Role role, Set<Employee.ShiftDateTime> shiftPreferences) {
        return employeeRepository.addEmployee(name, id, bank, bankID, bankBranch, employmentStart, salaryPerHour, sickDaysUsed, vacationDaysUsed, role, shiftPreferences);
    }

    public Employee deleteEmployee(String id) {
        try{
            String deleteEmployee = "DELETE FROM Employee WHERE ID = '" + id + "';";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteEmployee);
            preparedStatement.executeUpdate();
        }
        catch (SQLException s){
            throw new IllegalArgumentException(s.getMessage());
        }
        return employeeRepository.deleteEmployee(id);
    }

    public Shift deleteShift(Shift.Type type, Calendar date){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Shift WHERE Type = ? and Date = ?");
            preparedStatement.setString(1 , type.name());
            preparedStatement.setString(2 , date.get(Calendar.DATE) + "/" + (date.get(Calendar.MONTH)+1)  + "/" + date.get(Calendar.YEAR));
            preparedStatement.executeUpdate();
        }
        catch (SQLException s){
            throw new IllegalArgumentException(s.getMessage());
        }
        return shiftRepository.deleteShift(type, date);
    }

    public LinkedList<Shift> getShifts() {
        return shiftRepository.getShifts();
    }

    public Shift addShift(Shift shift) {
        return shiftRepository.addShift(shift);
    }

    public void loadDataFromDB(){
        try {
            PreparedStatement prepStat = connection.prepareStatement("SELECT * FROM Employee");
            ResultSet employees = prepStat.executeQuery();
            while(employees.next()){
                String id = employees.getString(1);
                String shiftPrefWithId = "SELECT * FROM ShiftPreference WHERE EmployeeID = " + id;
                PreparedStatement prepStat2 = connection.prepareStatement(shiftPrefWithId);
                ResultSet shiftPref = prepStat2.executeQuery();
                Employee employee = employeeRepository.addEmployee(employees);
                employee.setAvailableShifts(shiftPref);
            }
            PreparedStatement prepStat3 = connection.prepareStatement("SELECT * FROM Shift");
            ResultSet shifts = prepStat3.executeQuery();
            while(shifts.next()){
                String type = shifts.getString(1);
                String date = shifts.getString(2);
                String shiftRequired = "SELECT * FROM RequiredStaff where Type = '" + type + "' and Date = '" + date + "'";
                PreparedStatement prepStat4 = connection.prepareStatement(shiftRequired);
                ResultSet shiftReqLoad = prepStat4.executeQuery();
                String workers = "SELECT EmployeeID FROM Workers where Type = '" + type + "' and Date = '" + date + "'";
                PreparedStatement prepStat5 = connection.prepareStatement(workers);
                ResultSet workersLoad = prepStat5.executeQuery();
                LinkedList<Employee> listOfWorkers = new LinkedList<>();
                while(workersLoad.next()){
                    listOfWorkers.add(employeeRepository.getEmployee(workersLoad.getString(1)));
                }
                shiftRepository.addShift(shifts, shiftReqLoad, listOfWorkers);
            }
        }
        catch (SQLException s){
            throw new IllegalArgumentException(s.getMessage());
        }
    }
}


package groupk.workers.data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

public class WorkingConditions {
    private Calendar employmentStart;
    private int salaryPerHour;
    private int sickDaysUsed;
    private int vacationDaysUsed;

    public void setEmploymentStart(String id, Calendar employmentStart) {
        try {
            PreparedStatement preparedStatement = DalController.connection.prepareStatement("UPDATE Employee set EmploymentStart = ? where ID = ?");
            preparedStatement.setString(1, employmentStart.get(Calendar.DATE) + "/" + (employmentStart.get(Calendar.MONTH)+1)  + "/" + employmentStart.get(Calendar.YEAR));
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
            this.employmentStart = employmentStart;
        } catch (SQLException s) {
            throw new IllegalArgumentException(s.getMessage());
        }
    }

    public void setSalaryPerHour(String id, int salaryPerHour) {
        try {
            PreparedStatement preparedStatement = DalController.connection.prepareStatement("UPDATE Employee set SalaryPerHour = ? where ID = ?");
            preparedStatement.setInt(1, salaryPerHour);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
            this.salaryPerHour = salaryPerHour;
        } catch (SQLException s) {
            throw new IllegalArgumentException(s.getMessage());
        }
    }

    public void setSickDaysUsed(String id, int sickDaysUsed) {
        try {
            PreparedStatement preparedStatement = DalController.connection.prepareStatement("UPDATE Employee set SickDaysUsed = ? where ID = ?");
            preparedStatement.setInt(1, sickDaysUsed);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
            this.sickDaysUsed = sickDaysUsed;
        } catch (SQLException s) {
            throw new IllegalArgumentException(s.getMessage());
        }
    }

    public void setVacationDaysUsed(String id, int vacationDaysUsed) {
        try {
            PreparedStatement preparedStatement = DalController.connection.prepareStatement("UPDATE Employee set VacationDaysUsed = ? where ID = ?");
            preparedStatement.setInt(1, vacationDaysUsed);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
            this.vacationDaysUsed = vacationDaysUsed;
        } catch (SQLException s) {
            throw new IllegalArgumentException(s.getMessage());
        }
    }

    public WorkingConditions(Calendar employmentStart, int salaryPerHour, int sickDaysUsed, int vacationDaysUsed){
        this.employmentStart = employmentStart;
        this.salaryPerHour = salaryPerHour;
        this.sickDaysUsed = sickDaysUsed;
        this. vacationDaysUsed = vacationDaysUsed;

    }
    public Calendar getEmploymentStart() {
        return employmentStart;
    }
    public int getSalaryPerHour() {
        return salaryPerHour;
    }
    public int getSickDaysUsed() {
        return sickDaysUsed;
    }
    public int getVacationDaysUsed() {
        return vacationDaysUsed;
    }

}

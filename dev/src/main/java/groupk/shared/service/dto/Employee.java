package groupk.shared.service.dto;

import java.util.Calendar;
import java.util.Set;

public class Employee {
    public enum Role {
        Logistics,
        HumanResources,
        Stocker,
        Cashier,
        LogisticsManager,
        ShiftManager,
        Driver,
        StoreManager
    }

    public static enum ShiftDateTime {
        SundayMorning,
        SundayEvening,
        MondayMorning,
        MondayEvening,
        TuesdayMorning,
        TuesdayEvening,
        WednesdayMorning,
        WednesdayEvening,
        ThursdayMorning,
        ThursdayEvening,
        FridayMorning,
        FridayEvening,
        SaturdayMorning,
        SaturdayEvening
    }

    public String id;
    public String name;
    public Role role;

    public Employee(
            String id,
            String name,
            Role role,
            String bank,
            int bankID,
            int bankBranch,
            int salaryPerHour,
            int sickDaysUsed,
            int vacationDaysUsed,
            Set<ShiftDateTime> shiftPreferences,
            Calendar employmentStart
    ) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.bank = bank;
        this.bankID = bankID;
        this.bankBranch = bankBranch;
        this.salaryPerHour = salaryPerHour;
        this.sickDaysUsed = sickDaysUsed;
        this.vacationDaysUsed = vacationDaysUsed;
        this.shiftPreferences = shiftPreferences;
        this.employmentStart = employmentStart;
    }

    public String bank;
    public int bankID;
    public int bankBranch;
    public int salaryPerHour;
    public int sickDaysUsed;
    public int vacationDaysUsed;
    public Set<ShiftDateTime> shiftPreferences;
    public Calendar employmentStart;
}

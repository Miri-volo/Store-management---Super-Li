package groupk.workers.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

public class ShiftRepository {
    private LinkedList<Shift> shifts;

    public ShiftRepository(){
        shifts = new LinkedList<>();
    }

    public void delete(){
        shifts = new LinkedList<>();
    }

    public LinkedList<Shift> getShifts() {
        return shifts;
    }

    public Shift addShift(Shift shift){
        shifts.add(shift);
        return  shift;
    }

    public Shift addShift(ResultSet shift, ResultSet requiredStaff, LinkedList<Employee> workers){
        Shift loadShift = new Shift(shift, requiredStaff, workers);
        shifts.add(loadShift);
        return loadShift;
    }

    public Shift deleteShift(Shift.Type type, Calendar date){
        Shift returned = null;
        for(Shift shift : shifts){
            if(shift.getType().equals(type) && shift.getDate().equals(date)){
                returned = shift;
                shifts.remove(returned);
            }
        }
        return returned;
    }
}

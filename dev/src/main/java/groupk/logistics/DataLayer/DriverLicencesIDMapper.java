package groupk.logistics.DataLayer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DriverLicencesIDMapper {
    private Map<Integer, List<String>> driverLicencesIDMapper ;
    public List<Integer> updatedDriversIDs;
    private static DriverLicencesIDMapper singleton = null;
    private DriverLicencesIDMapper() {
        driverLicencesIDMapper  = new ConcurrentHashMap<Integer, List<String>>();
        updatedDriversIDs = new LinkedList<Integer>();
    }

    public void resetData() {
        driverLicencesIDMapper  = new ConcurrentHashMap<Integer, List<String>>();
        updatedDriversIDs = new LinkedList<Integer>();
    }

    public void updateDriver(int driverID) {
        if (!updatedDriversIDs.contains(driverID))
           updatedDriversIDs.add(driverID);
    }

    public void addDLicense(int driverID, String dLicense) {
        if (driverLicencesIDMapper.containsKey(driverID))
            driverLicencesIDMapper.get(driverID).add(dLicense);
        else {
            LinkedList<String> newList = new LinkedList<String>();
            newList.add(dLicense);
            driverLicencesIDMapper.put(driverID, newList);
        }
    }

    public List<String> getMyLicenses(int driverID) {
        return driverLicencesIDMapper.get(driverID);
    }

    public boolean isDriverUpdated(int driverID) {
        return updatedDriversIDs.contains(driverID);
    }

    public static DriverLicencesIDMapper getInstance() {
        if (singleton == null)
            singleton = new DriverLicencesIDMapper();
        return singleton;
    }

    public boolean contains(int username) {
        return driverLicencesIDMapper.containsKey(username);
    }
}

package groupk.logistics.DataLayer;

import groupk.logistics.business.DLicense;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DriverLicencesMapper {
    private DriverLicencesIDMapper driverLicencesIDMapper;

    public DriverLicencesMapper() {
        driverLicencesIDMapper = DriverLicencesIDMapper.getInstance();
    }

    public void deleteDB() {
        driverLicencesIDMapper.resetData();
    }

    public List<String> getMyLicenses(int username) {
        List<String> DTOList = new LinkedList<String>();
        String query = "SELECT licence FROM Drivers_Licences Where username = '" + username + "'";
        try {
            PreparedStatement pstmt = myDataBase.connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                DTOList.add(rs.getString(1));
                driverLicencesIDMapper.addDLicense(username, rs.getString(1));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Oops, there was unexpected problem with get the licenses from the driver: \"" + username + "\"\nError description: " + e.getMessage());
        }
        driverLicencesIDMapper.updateDriver(username);
        return DTOList;
    }

    public boolean addLicence(int username, DLicense license) {
        int n = 0;
        String query = "INSERT INTO Drivers_Licences(username,licence) VALUES(?,?)";
        try {
            PreparedStatement prepStat = myDataBase.connection.prepareStatement(query);
            prepStat.setInt(1, username);
            prepStat.setString(2, license.name());
            n = prepStat.executeUpdate();
        }
        catch (SQLException e){
            throw new IllegalArgumentException("Oops, there was unexpected problem with add the license '" + license.name() + "' to the driver: '" + username + "'\nError description: " + e.getMessage());
        }
        return n == 1;
    }
}


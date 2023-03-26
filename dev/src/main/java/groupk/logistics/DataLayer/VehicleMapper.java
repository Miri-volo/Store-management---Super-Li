package groupk.logistics.DataLayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleMapper {
    private VehiclesIDMap vehicleIDMapper = VehiclesIDMap.getInstance();

    public VehicleMapper() {
    }

    public void deleteDB() {
        vehicleIDMapper.resetData();
    }

    private String ConvertResultSetToDTO(ResultSet rs) throws SQLException {
        return rs.getString(1);
    }

    public boolean addVehicle(String lisence, String registrationPlate, String model, int weight, int maxWeight) {
        int n = 0;
        String query = "INSERT INTO Vehicles(registration_plate, model,license, weight,max_weight) VALUES(?,?,?,?,?)";

        try {
            PreparedStatement prepStat = myDataBase.connection.prepareStatement(query);
            prepStat.setString(1, registrationPlate);
            prepStat.setString(2, model);
            prepStat.setString(3, lisence);
            prepStat.setInt(4, weight);
            prepStat.setInt(5, maxWeight);
            n = prepStat.executeUpdate();
        } catch (Exception e){
            if (e.getMessage().equals("[SQLITE_CONSTRAINT_PRIMARYKEY]  A PRIMARY KEY constraint failed (UNIQUE constraint failed: Vehicles.registration_plate)"))
                throw new IllegalArgumentException("Oops, there is another vehicle with that registration plate");
            throw new IllegalArgumentException("There was an error: " + e.getMessage());
        }
        return n == 1;
    }

    public void addVehicle(VehicleDTO newVehicle) {
        vehicleIDMapper.vehicleMap.put(newVehicle.getRegistationPlate(), newVehicle);
    }

    public String getLicense(String registrationPlateOfVehicle) {
        if (vehicleIDMapper.vehicleMap.containsKey(registrationPlateOfVehicle))
            return vehicleIDMapper.vehicleMap.get(registrationPlateOfVehicle).getLisence();
        else {
            String query = "SELECT license FROM Vehicles " +
                    "WHERE registration_plate='" + registrationPlateOfVehicle + "'";
            try {
                Statement stmt = myDataBase.connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    return rs.getString(1);
                } else {
                    throw new IllegalArgumentException("Oops, there is no vehicle with this registration plate");
                }
            } catch (SQLException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    public List<String> getAllRegistrationPlates() {
        String query = "SELECT * FROM Vehicles";
        List<String> DTOList = new ArrayList<String>();
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                DTOList.add(ConvertResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return DTOList;
    }

    public VehicleDTO getVehicle(String registrationPlateOfVehicle) {
        if (vehicleIDMapper.vehicleMap.containsKey(registrationPlateOfVehicle))
            return vehicleIDMapper.vehicleMap.get(registrationPlateOfVehicle);
        else {
            String query = "SELECT * FROM Vehicles " +
                    "WHERE registration_plate='" + registrationPlateOfVehicle + "'";
            try {
                Statement stmt = myDataBase.connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    VehicleDTO vehicle = new VehicleDTO(rs.getString(3), rs.getString(1), rs.getString(2),
                            rs.getInt(4), rs.getInt(5));
                    vehicleIDMapper.vehicleMap.put(registrationPlateOfVehicle, vehicle);
                    return vehicle;
                }
            } catch (SQLException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        return null;
    }
}


package groupk.logistics.DataLayer;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class TruckingMapper {
    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private TruckingIDMap truckingIDMap;

    public TruckingMapper() {
        truckingIDMap = TruckingIDMap.getInstance();
    }

    public void deleteDB() {
        truckingIDMap.resetData();
    }

    public List<String>[] addTrucking(TruckingDTO trucking) {
        List<String> sourcesExceptions;
        List<String> destinationsExceptions;
        List<String> ordersExceptions;
        List<String> toReturn[] = new List[3];
        if (!addTruckingToTruckingTable(trucking))
            throw new IllegalArgumentException("Oops something got wrong with adding that trucking");
        try {
            sourcesExceptions = addTruckingSources(trucking.getId(), trucking.getSources());
            if (sourcesExceptions.size() == trucking.getSources().size())
                throwExceptionOfSitesList(sourcesExceptions, "sources");
            toReturn[0] = sourcesExceptions;
        } catch (Exception e) {
            removeTruckingDetails(trucking.getId());
            throw e;
        }
        try {
            destinationsExceptions = addTruckingDestinations(trucking.getId(), trucking.getDestinations());
            if (destinationsExceptions.size() == trucking.getSources().size())
                throwExceptionOfSitesList(destinationsExceptions, "sources");
            toReturn[1] = destinationsExceptions;
        } catch (Exception e) {
            removeTruckingDetails(trucking.getId());
            removeSourcesTrucking(trucking.getId());
            throw e;
        }
        try {
            ordersExceptions = addTruckingOrders(trucking.getId(), trucking.getOrders());
            toReturn[2] = ordersExceptions;
        } catch (Exception e) {
            removeTruckingDetails(trucking.getId());
            removeSourcesTrucking(trucking.getId());
            removeDestinationsTrucking(trucking.getId());
            throw e;
        }
        try {
            TruckingDTO truckingToMap = getTruckingByID(trucking.getId());
            truckingIDMap.insertTrucking(truckingToMap);
        }
        catch (Exception e) {
            removeTruckingDetails(trucking.getId());
            removeSourcesTrucking(trucking.getId());
            removeDestinationsTrucking(trucking.getId());
            removeTruckingOrders(trucking.getId());
            throw new IllegalArgumentException("Oops, something got wrong with adding the trucking to the memory");
        }
        return toReturn;
    }

    private void throwExceptionOfSitesList(List<String> exceptions, String nameOfList) {
        String exceptionError = "Oops, we couldnt add any site from your " + nameOfList + ", because that reasons:\n";
        for (String exception : exceptions) {
            exceptionError += exception + "\n";
        }
        throw new IllegalArgumentException(exceptionError);
    }

    private boolean addTruckingToTruckingTable(TruckingDTO trucking) {
        int n = 0;
        String query = "INSERT INTO Truckings(TID,truck_manager,registration_plate,driver_username,date,hours,minutes,weight) VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement prepStat = myDataBase.connection.prepareStatement(query);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            String formattedDateTime = trucking.getDate().format(formatter);
            formattedDateTime = formattedDateTime.replace("T", " ");
            prepStat.setInt(1, trucking.getId());
            prepStat.setInt(2, trucking.getTruckManager());
            prepStat.setString(3, trucking.getVehicleRegistrationPlate());
            prepStat.setInt(4, trucking.getDriverUsername());
            prepStat.setString(5, formattedDateTime);
            prepStat.setLong(6, trucking.getHours());
            prepStat.setLong(7, trucking.getMinutes());
            prepStat.setInt(8, trucking.getWeight());
            n = prepStat.executeUpdate();
        }
        catch (SQLException e){
            throw new IllegalArgumentException("Oops, something got wrong and we couldn't add your trucking :( \nbecause: " + e.getMessage());
        }
        return n == 1;
    }

    public boolean removeTrucking(int truckingID) {
        removeSourcesTrucking(truckingID);
        removeDestinationsTrucking(truckingID);
        removeTruckingOrders(truckingID);
        boolean toReturn = removeTruckingDetails(truckingID);
        if (toReturn)
            truckingIDMap.removeTrucking(truckingID);
        return toReturn;
    }

    private boolean removeTruckingDetails(int truckingID) {
        String Query = "DELETE FROM Truckings WHERE TID = '" + truckingID + "'";
        int n = 0;
        try {
            PreparedStatement pstmt = myDataBase.connection.prepareStatement(Query);
            n = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        truckingIDMap.removeTrucking(truckingID);
        return n > 0;
    }

    public boolean setWeightForTrucking(int truckingId, int weight) {
        String sql = "UPDATE Truckings SET weight=" + weight + " WHERE TID='" + truckingId + "'";
        int n = 0;
        try {
            PreparedStatement pstmt = myDataBase.connection.prepareStatement(sql);
            n = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (n > 0)
            truckingIDMap.getTruckingById(truckingId).setWeight(weight);
        return n > 0;
    }

    public boolean updateVehicle(int truckingId, String vehicleRegistrationPlate) {
        String sql = "UPDATE Truckings SET registration_plate = '" + vehicleRegistrationPlate + "' WHERE TID = '" + truckingId + "'";
        int n = 0;
        try {
            PreparedStatement pstmt = myDataBase.connection.prepareStatement(sql);
            n = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (n > 0)
            truckingIDMap.getTruckingById(truckingId).updateVehicle(vehicleRegistrationPlate);
        return n > 0;
    }

    public boolean updateDriver(int truckingId, int driverUsername) {
        int n = 0;
        try {
            PreparedStatement preparedStatement = myDataBase.connection.prepareStatement("UPDATE Truckings set driver_username = ? where TID = ?");
            preparedStatement.setInt(1, driverUsername);
            preparedStatement.setInt(2, truckingId);
            n = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (n > 0)
            truckingIDMap.getTruckingById(truckingId).updateDriverUsername(driverUsername);
        return n > 0;
    }

    public boolean updateDate(int truckingID, LocalDateTime date) {
        String sql = "UPDATE Truckings SET date = '" + date.format(dateFormat).replace('T', ' ') + "' WHERE TID = '" + truckingID + "'";
        int n = 0;
        try {
            PreparedStatement pstmt = myDataBase.connection.prepareStatement(sql);
            n = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (n > 0)
            truckingIDMap.getTruckingById(truckingID).updateDate(date.plusHours(0));
        return n > 0;
    }

    public String getRegistrationPlateOfTrucking(int TruckingId) {
        if (truckingIDMap.contains(TruckingId))
            return truckingIDMap.getTruckingById(TruckingId).getVehicleRegistrationPlate();
        String toReturn = "";
        String query = "SELECT registration_plate FROM Truckings " +
                "WHERE TID = '" + TruckingId + "'";
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                toReturn += rs.getString(1);
            } else
                throw new IllegalArgumentException("There is no trucking with id: " + TruckingId);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Oops something got wrong: \n" + e.getMessage());
        }
        return toReturn;
    }

    public int getDriverUsernameOfTrucking(int TruckingId) {
        if (truckingIDMap.contains(TruckingId))
            return truckingIDMap.getTruckingById(TruckingId).getDriverUsername();
        int toReturn = 0;
        String query = "SELECT driver_username FROM Truckings " +
                "WHERE TID = '" + TruckingId + "'";
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                toReturn += rs.getInt(1);
            } else
                throw new IllegalArgumentException("There is no trucking with id: " + TruckingId);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Oops something got wrong: \n" + e.getMessage());
        }
        return toReturn;
    }

    public TruckingDTO getTruckingByID(int truckingID) {
        if (truckingIDMap.contains(truckingID))
            return truckingIDMap.getTruckingById(truckingID);
        TruckingDTO toReturn;
        List<SiteDTO> sources = getSourcesByTruckingId(truckingID);
        List<SiteDTO> destinations = getDestinationsByTruckingId(truckingID);
        List<Integer> orders = getOrders(truckingID);
        String query = "SELECT * FROM Truckings " +
                "WHERE TID = '" + truckingID + "'";
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                toReturn = new TruckingDTO(rs.getInt(1), rs.getString(5), rs.getInt(2), rs.getInt(4), rs.getString(3), rs.getInt(6), rs.getInt(7), rs.getInt(8), sources, destinations, orders);
                truckingIDMap.insertTrucking(toReturn);
            } else
                throw new IllegalArgumentException("There is no trucking with that id");
        } catch (SQLException e) {
            throw new IllegalArgumentException("Oops something in verification process got wrong: \n" + e.getMessage());
        }
        return toReturn;
    }

    public List<String> addTruckingSources(int truckingIdCounter, List<SiteDTO> sources) {
        //return the exceptions of every destination that got error
        List<String> Exceptions = new LinkedList<String>();
        String query = "INSERT INTO Truckings_Sources(TID,contact_guy,city,phone_number,street,area,house_number,floor,apartment_number) VALUES(?,?,?,?,?,?,?,?,?)";

        for (SiteDTO source : sources) {
            try {
                PreparedStatement prepStat = myDataBase.connection.prepareStatement(query);
                prepStat.setInt(1, truckingIdCounter);
                prepStat.setString(2, source.getContactGuy());
                prepStat.setString(3, source.getCity());
                prepStat.setString(4, source.getPhoneNumber());
                prepStat.setString(5, source.getStreet());
                prepStat.setString(6, source.getArea());
                prepStat.setInt(7, source.getHouseNumber());
                prepStat.setInt(8, source.getFloor());
                prepStat.setInt(9, source.getApartment());
                if (prepStat.executeUpdate() < 1)
                    throw new IllegalArgumentException("The destination is already exist");
                if (truckingIDMap.contains(truckingIdCounter))
                    truckingIDMap.getTruckingById(truckingIdCounter).getSources().add(source);
            } catch (Exception e){
                Exceptions.add("There was a problem with the destination with the contact guy: " + source.getContactGuy() + "\nthe error description: " + e.getMessage());
            }
        }
        return Exceptions;
    }

    public boolean removeSourcesTrucking(int truckingID) {
        String Query = "DELETE FROM Truckings_Sources WHERE TID = '" + truckingID + "'";
        int n = 0;
        try {
            PreparedStatement pstmt = myDataBase.connection.prepareStatement(Query);
            n = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return n > 0;
    }

    public List<SiteDTO> getSourcesByTruckingId(int TruckingID) {
        if (truckingIDMap.contains(TruckingID))
            return truckingIDMap.getTruckingById(TruckingID).getSources();
        List<SiteDTO> sites = new LinkedList<SiteDTO>();
        String query = "SELECT * FROM Truckings_Sources Where TID = '" + TruckingID + "'";
        try {
            PreparedStatement pstmt = myDataBase.connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sites.add(new SiteDTO(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getString(6)));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Oops, there was unexpected problem with get the sources of trucking: \"" + TruckingID + "\"\nError description: " + e.getMessage());
        }
        return sites;
    }

    public List<String> addTruckingDestinations(int truckingIdCounter, List<SiteDTO> destinations) {
        //return the exceptions of every destination that got error
        List<String> Exceptions = new LinkedList<String>();
        String query = "INSERT INTO Truckings_Destinations(TID,contact_guy,city,phone_number,street,area,house_number,floor,apartment_number) VALUES(?,?,?,?,?,?,?,?,?)";
        for (SiteDTO destination : destinations) {
            try {
                PreparedStatement prepStat = myDataBase.connection.prepareStatement(query);
                prepStat.setInt(1, truckingIdCounter);
                prepStat.setString(2, destination.getContactGuy());
                prepStat.setString(3, destination.getCity());
                prepStat.setString(4, destination.getPhoneNumber());
                prepStat.setString(5, destination.getStreet());
                prepStat.setString(6, destination.getArea());
                prepStat.setInt(7, destination.getHouseNumber());
                prepStat.setInt(8, destination.getFloor());
                prepStat.setInt(9, destination.getApartment());
                if (prepStat.executeUpdate() < 1)
                    throw new IllegalArgumentException("The destination is already exist");
                if (truckingIDMap.contains(truckingIdCounter))
                    truckingIDMap.getTruckingById(truckingIdCounter).getDestinations().add(destination);
            } catch (Exception e){
                Exceptions.add("There was a problem with the destination with the contact guy: " + destination.getContactGuy() + "\nthe error description: " + e.getMessage());
            }
        }
        return Exceptions;
    }

    public boolean removeDestinationsTrucking(int truckingID) {
        String Query = "DELETE FROM Truckings_Destinations WHERE TID = '" + truckingID + "'";
        int n = 0;
        try {
            PreparedStatement pstmt = myDataBase.connection.prepareStatement(Query);
            n = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return n > 0;
    }

    public List<SiteDTO> getDestinationsByTruckingId(int TruckingID) {
        if (truckingIDMap.contains(TruckingID))
            return truckingIDMap.getTruckingById(TruckingID).getDestinations();
        List<SiteDTO> sites = new LinkedList<SiteDTO>();
        String query = "SELECT * FROM Truckings_Destinations Where TID = '" + TruckingID + "'";
        try {
            PreparedStatement pstmt = myDataBase.connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sites.add(new SiteDTO(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getString(6)));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Oops, there was unexpected problem with get the destinations of trucking: \"" + TruckingID + "\"\nError description: " + e.getMessage());
        }
        return sites;
    }

    public List<String> addTruckingOrders(int truckingIdCounter, List<Integer> ordersID) {
        List<String> toReturn = new LinkedList<String>();
        for (Integer orderID : ordersID) {
            try {
                addTruckingOrder(truckingIdCounter, orderID.intValue());
            } catch (Exception e) {
                toReturn.add(e.getMessage());
            }
        }
        if (toReturn.size() == ordersID.size()) {
            String Error = "Oops, we were unable to add any of the orders you entered for trucking because: ";
            for (String error : toReturn) {
                Error += "   " + error + "\n";
            }
            throw new IllegalArgumentException(Error);
        }
        else
            return toReturn;
    }

    public void addTruckingOrder(int truckingIdCounter, int orderID) {
        int n = 0;
        String query = "INSERT INTO Truckings_Orders(TID,orderID) VALUES(?,?)";
        try {
            PreparedStatement prepStat = myDataBase.connection.prepareStatement(query);
            prepStat.setInt(1, truckingIdCounter);
            prepStat.setInt(2, orderID);
            n = prepStat.executeUpdate();
            if (n > 0 & truckingIDMap.contains(truckingIdCounter))
                truckingIDMap.getTruckingById(truckingIdCounter).getOrders().add(orderID);
        } catch (SQLException e) {
            if (e.getMessage().equals("[SQLITE_CONSTRAINT_PRIMARYKEY]  A PRIMARY KEY constraint failed (UNIQUE constraint failed: Truckings_Orders.orderID)"))
                throw new IllegalArgumentException("Oops, there was a problem to add order " + orderID + " into trucking " + truckingIdCounter + " because the order is already exist in another delivery");
            throw new IllegalArgumentException("Oops, there was a problem to add order " + orderID + " into trucking " + truckingIdCounter + " because, " + e.getMessage());
        }

    }

    public List<Integer> getOrders(int truckingID) {
        if (truckingIDMap.contains(truckingID))
            return truckingIDMap.getTruckingById(truckingID).getOrders();
        List<Integer> orderIDs = new LinkedList<Integer>();
        String query = "SELECT * FROM Truckings_Orders WHERE TID = '" + truckingID + "'";
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                orderIDs.add(rs.getInt(2));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return orderIDs;
    }


    public boolean removeOrderFromTrucking(int TruckingID, int orderID) {
        List<Integer> orderIDs = getOrders(TruckingID);
        boolean found = false;
        for (Integer id : orderIDs) {
            if (id.intValue() == orderID)
                found = true;
        }
        if (found) {
            if (orderIDs.size() == 1)
                throw new IllegalArgumentException("Oops, order " + orderID + " is the only order in trucking " + TruckingID + ".\nYou can delete the entire trucking instead, but do not leave a trucking without orders.");
        }
        else {
            throw new IllegalArgumentException("Oops, there is no order with this orderID in the trucking");
        }
        String Query = "DELETE FROM Truckings_Orders WHERE TID = '" + TruckingID + "'" + " and orderID = '" + orderID + "'";
        int n = 0;
        try {
            PreparedStatement pstmt = myDataBase.connection.prepareStatement(Query);
            n = pstmt.executeUpdate();
            if (n > 0) {
                if (truckingIDMap.contains(TruckingID))
                    truckingIDMap.getTruckingById(TruckingID).getOrders().remove(new Integer(orderID));
                return true;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Oops, something got wrong. We couldn't delete order " + orderID + " from trucking "+ TruckingID + ": " + e.getMessage());
        }
        return false;
    }

    private boolean removeTruckingOrders(int truckingID) {
        String Query = "DELETE FROM Truckings_Orders WHERE TID = '" + truckingID + "'";
        int n = 0;
        try {
            PreparedStatement pstmt = myDataBase.connection.prepareStatement(Query);
            n = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return n > 0;
    }

    public boolean existOrder(int TruckingID, int orderID) {
        if (truckingIDMap.contains(TruckingID))
            return truckingIDMap.getTruckingById(TruckingID).getOrders().contains(orderID);
        String Query = "SELECT * FROM Truckings_Orders WHERE TID = '" + TruckingID + "'" + " and orderID = '" + orderID + "'";
        int n = 0;
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(Query);
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Oops, we couldn't execute your request");
        }
        return false;

    }

    public int getTruckingIDByOrderID(int orderID) {
        int toReturn = 0;
        String query = "SELECT TID FROM Truckings_Orders " +
                "WHERE orderID = '" + orderID + "'";
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                toReturn += rs.getInt(1);
            } else
                throw new IllegalArgumentException("There is no trucking with order id: " + orderID);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Oops, something got wrong: " + e.getMessage());
        }
        return toReturn;
    }

    public List<TruckingDTO> getTruckManagerBoard(int truckManagerUsername) {
        if (truckingIDMap.isUserHasUpdatedData(truckManagerUsername))
            return truckingIDMap.getTruckingsOfTruckManager(truckManagerUsername);
        else {
            List<TruckingDTO> toReturn = getBoardOfUserOrVehicle("truck_manager", "" + truckManagerUsername);
            truckingIDMap.updateUser(truckManagerUsername);
            return toReturn;
        }
    }

    public List<TruckingDTO> getTruckManagerFutureTruckings(int truckManagerUsername) {
        if (truckingIDMap.isUserHasUpdatedData(truckManagerUsername))
            return truckingIDMap.getFutureTruckingsOfTruckManager(truckManagerUsername);
        else
            return getFutureTruckingsOfUserOrVehicle("truck_manager", "" + truckManagerUsername);
    }

    public List<TruckingDTO> getTruckManagerHistoryTruckings(int truckManagerUsername) {
        if (truckingIDMap.isUserHasUpdatedData(truckManagerUsername))
            return truckingIDMap.getHistoryOfTruckManager(truckManagerUsername);
        else
            return getHistoryTruckingsOfUserOrVehicle("truck_manager", "" + truckManagerUsername);
    }

    public List<TruckingDTO> getVehicleBoard(String regristrationPlate) {
        if (truckingIDMap.isVehicleHasUpdatedData(regristrationPlate)) {
            return truckingIDMap.getTruckingsOfVehicle(regristrationPlate);
        }
        else {
            List<TruckingDTO> toReturn = getBoardOfUserOrVehicle("registration_plate", regristrationPlate);
            truckingIDMap.updateVehicle(regristrationPlate);
            return toReturn;
        }
    }

    public List<TruckingDTO> getVehicleFutureTruckings(String regristrationPlate) {
        if (truckingIDMap.isVehicleHasUpdatedData(regristrationPlate))
            return truckingIDMap.getFutureTruckingsOfVehicle(regristrationPlate);
        else
            return getFutureTruckingsOfUserOrVehicle("registration_plate", regristrationPlate);
    }

    public List<TruckingDTO> getVehicleHistoryTruckings(String regristrationPlate) {
        if (truckingIDMap.isVehicleHasUpdatedData(regristrationPlate))
            return truckingIDMap.getHistoryOfVehicle(regristrationPlate);
        else
            return getHistoryTruckingsOfUserOrVehicle("registration_plate", regristrationPlate);
    }

    public List<TruckingDTO> getDriverBoard(int driverUsername) {
        if (truckingIDMap.isUserHasUpdatedData(driverUsername))
            return truckingIDMap.getTruckingsOfDriver(driverUsername);
        else {
            List<TruckingDTO> toReturn = getBoardOfUserOrVehicle("driver_username", "" + driverUsername);
            truckingIDMap.updateUser(driverUsername);
            return toReturn;
        }
    }

    public List<TruckingDTO> getDriverFutureTruckings(int driverUsername) {
        if (truckingIDMap.isUserHasUpdatedData(driverUsername))
            return truckingIDMap.getFutureTruckingsOfDriver(driverUsername);
        else
            return getFutureTruckingsOfUserOrVehicle("driver_username", "" + driverUsername);
    }

    public List<TruckingDTO> getDriverHistoryTruckings(int driverUsername) {
        if (truckingIDMap.isUserHasUpdatedData(driverUsername))
            return truckingIDMap.getHistoryOfDriver(driverUsername);
        else
            return getHistoryTruckingsOfUserOrVehicle("driver_username", "" + driverUsername);
    }

    public List<TruckingDTO> getRelevantTruckings(LocalDateTime date) {
        List<TruckingDTO> toReturn = new LinkedList<TruckingDTO>();
        String query = "SELECT * FROM Truckings " +
                "WHERE strftime(date) between strftime('" + date.minusHours(7).format(dateFormat) + "') and strftime('" + date.plusHours(7).format(dateFormat) + "') ORDER BY date";
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int truckingId = rs.getInt(1);
                List<SiteDTO> sources = getSourcesByTruckingId(truckingId);
                List<SiteDTO> destinations = getDestinationsByTruckingId(truckingId);
                List<Integer> orders = getOrders(truckingId);
                TruckingDTO newTrucking = new TruckingDTO(rs.getInt(1), rs.getString(5), rs.getInt(2), rs.getInt(4), rs.getString(3), rs.getInt(6), rs.getInt(7), rs.getInt(8), sources, destinations, orders);
                toReturn.add(newTrucking);
                truckingIDMap.insertTrucking(newTrucking);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Oops, something in verification process got wrong: \n" + e.getMessage());
        }
        return toReturn;
    }

    private List<TruckingDTO> getBoardOfUserOrVehicle(String fieldName, String usernameOrRegistration) {
        List<TruckingDTO> toReturn = new LinkedList<TruckingDTO>();
        String query = "SELECT * FROM Truckings " +
                "WHERE " + fieldName + " = '" + usernameOrRegistration + "'" +
                " ORDER BY date";
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int truckingId = rs.getInt(1);
                List<SiteDTO> sources = getSourcesByTruckingId(truckingId);
                List<SiteDTO> destinations = getDestinationsByTruckingId(truckingId);
                List<Integer> orders = getOrders(truckingId);
                TruckingDTO newTrucking = new TruckingDTO(rs.getInt(1), rs.getString(5), rs.getInt(2), rs.getInt(4), rs.getString(3), rs.getInt(6), rs.getInt(7), rs.getInt(8), sources, destinations, orders);
                toReturn.add(newTrucking);
                truckingIDMap.insertTrucking(newTrucking);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Oops, something got wrong: \n" + e.getMessage());
        }
        return toReturn;
    }

    private List<TruckingDTO> getFutureTruckingsOfUserOrVehicle(String fieldName, String usernameOrRegistration) {
        List<TruckingDTO> toReturn = new LinkedList<TruckingDTO>();
        String query = "SELECT * FROM Truckings " +
                "WHERE strftime(date) > DATE('now') and " + fieldName + " = '" + usernameOrRegistration + "'" +
                " ORDER BY date";
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int truckingId = rs.getInt(1);
                List<SiteDTO> sources = getSourcesByTruckingId(truckingId);
                List<SiteDTO> destinations = getDestinationsByTruckingId(truckingId);
                List<Integer> orders = getOrders(truckingId);
                TruckingDTO newTrucking = new TruckingDTO(rs.getInt(1), rs.getString(5), rs.getInt(2), rs.getInt(4), rs.getString(3), rs.getInt(6), rs.getInt(7), rs.getInt(8), sources, destinations, orders);
                toReturn.add(newTrucking);
                truckingIDMap.insertTrucking(newTrucking);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Oops, something got wrong: \n" + e.getMessage());
        }
        return toReturn;
    }

    private List<TruckingDTO> getHistoryTruckingsOfUserOrVehicle(String fieldName, String usernameOrRegistration) {
        List<TruckingDTO> toReturn = new LinkedList<TruckingDTO>();
        String query = "SELECT * FROM Truckings " +
                "WHERE strftime(date) <= DATE('now') and " + fieldName + " = '" + usernameOrRegistration + "'" +
                " ORDER BY date";
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int truckingId = rs.getInt(1);
                List<SiteDTO> sources = getSourcesByTruckingId(truckingId);
                List<SiteDTO> destinations = getDestinationsByTruckingId(truckingId);
                List<Integer> orders = getOrders(truckingId);
                TruckingDTO newTrucking = new TruckingDTO(rs.getInt(1), rs.getString(5), rs.getInt(2), rs.getInt(4), rs.getString(3), rs.getInt(6), rs.getInt(7), rs.getInt(8), sources, destinations, orders);
                toReturn.add(newTrucking);
                truckingIDMap.insertTrucking(newTrucking);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Oops, something got wrong: \n" + e.getMessage());
        }
        return toReturn;
    }

    public int getNextIdForTrucking() {
        int toReturn = 1;
        String query = "SELECT TID FROM Truckings ORDER BY TID DESC";
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                toReturn += rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Oops, something got wrong: \n" + e.getMessage());
        }
        return toReturn;
    }

    public String getLicencePlate(int truckingId) {
        String regisrationPlate = "";
        String query = "SELECT * FROM Truckings WHERE TID='" + truckingId + "'";
        int n = 0;
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                regisrationPlate = rs.getString(3);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return regisrationPlate;
    }
}

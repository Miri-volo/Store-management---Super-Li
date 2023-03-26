package groupk.logistics.DataLayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class TruckingRequestMapper {
    public TruckingRequestMapper() {}

    public boolean removeTruckingRequest(int orderID) {
        String Query = "DELETE FROM Truckings_Requests WHERE orderID = '" + orderID + "'";
        int n = 0;
        try {
            PreparedStatement pstmt = myDataBase.connection.prepareStatement(Query);
            n = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Oops, we couldn't delete your trucking request for order " + orderID + " ,because: " + e.getMessage());
        }
        return n > 0;
    }

    public boolean addTruckingRequest(int orderID, LocalDateTime createDate, String sourceDetails, String destinationDetails) {
        int n = 0;
        String query = "INSERT INTO Truckings_Requests(date,orderID,source,destination) VALUES(?,?,?,?)";
        try {
            PreparedStatement prepStat = myDataBase.connection.prepareStatement(query);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            String formattedDateTime = createDate.format(formatter).substring(0, 16);
            formattedDateTime = formattedDateTime.replace("T", " ");
            prepStat.setString(1, formattedDateTime);
            prepStat.setInt(2, orderID);
            prepStat.setString(3, sourceDetails);
            prepStat.setString(4, destinationDetails);
            n = prepStat.executeUpdate();
        }
        catch (SQLException e){
            throw new IllegalArgumentException("Oops, something got wrong and we couldn't add your trucking request :( \nbecause: " + e.getMessage());
        }
        return n == 1;
    }

    public List<String> getTruckingRequests() {
        List<String> toReturn = new LinkedList<String>();
        String query = "SELECT * FROM Truckings_Requests";
        try {
            Statement stmt = myDataBase.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                toReturn.add("Trucking request from: " + rs.getString(1) + " of order " + rs.getInt(2) + "\n" +
                        "   The source details: " + rs.getString(3) + "\n   The destination details: " + rs.getString(4));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Oops something got wrong: \n" + e.getMessage());
        }
        return toReturn;
    }
}

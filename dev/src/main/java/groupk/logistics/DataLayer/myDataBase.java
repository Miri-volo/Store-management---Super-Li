package groupk.logistics.DataLayer;

import groupk.logistics.business.Area;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class myDataBase {
    public static String finalCurl;
    public static File file;
    public static Connection connection;

    public myDataBase(Connection conn) {
        connection = conn;
//        file = new File("database.db");
//        finalCurl = ("jdbc:sqlite:").concat(file.getAbsolutePath());
        try {
            createNewTable();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    public void deleteDB() {
        LinkedList<String> tables = new LinkedList<>();
        tables.add("DELETE FROM Vehicles");
        tables.add("DELETE FROM Drivers_Licences");
        tables.add("DELETE FROM Truckings");
        tables.add("DELETE FROM Truckings_Destinations");
        tables.add("DELETE FROM Truckings_Sources");
        tables.add("DELETE FROM Truckings_Orders");
        tables.add("DELETE FROM Truckings_Requests");
        try (
                Statement statement = connection.createStatement()) {
            for (String table : tables) {
                statement.addBatch(table);
            }
            statement.executeBatch();
        } catch (SQLException s) {
            throw new IllegalArgumentException(s.getMessage());
        }
        createNewTable();
    }

    public void createNewTable() {
        String vehiclesTable = "CREATE TABLE IF NOT EXISTS " + "Vehicles" + " (\n" +
                "\tregistration_plate INTEGER PRIMARY KEY,\n" +
                "\tmodel TEXT NOT NULL,\n" +
                "\tlicense TEXT NOT NULL,\n" +
                "\tweight INTEGER NOT NULL,\n" +
                "\tmax_weight INTEGER NOT NULL\n" +
                "\t);\n" +
                "\n";
        String dirversLicencesTable = "CREATE TABLE IF NOT EXISTS " + "Drivers_Licences" + " (\n" +
                "\tusername INTEGER NOT NULL,\n" +
                "\tlicence TEXT NOT NULL,\n" +
                "\tPRIMARY KEY (username,licence)\n" +
                "\t);\n" +
                "\n";
        String Truckings = "CREATE TABLE IF NOT EXISTS " + "Truckings" + " (\n" +
                "\tTID INTEGER PRIMARY KEY,\n" +
                "\ttruck_manager INTEGER NOT NULL,\n" +
                "\tregistration_plate TEXT NOT NULL,\n" +
                "\tdriver_username INTEGER NOT NULL,\n" +
                "\tdate TEXT NOT NULL,\n" +
                "\thours INTEGER NOT NULL,\n" +
                "\tminutes INTEGER NOT NULL,\n" +
                "\tweight INTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(registration_plate) REFERENCES Vehicles(registration_plate)\n" +
                "\t);\n" +
                "\n";
        String Truckings_Destinations = "CREATE TABLE IF NOT EXISTS " + "Truckings_Destinations" + " (\n" +
                "\tTID INTEGER PRIMARY KEY,\n" +
                "\tcontact_guy TEXT NOT NULL,\n" +
                "\tcity TEXT NOT NULL,\n" +
                "\tphone_number TEXT NOT NULL,\n" +
                "\tstreet TEXT NOT NULL,\n" +
                "\tarea TEXT NOT NULL,\n" +
                "\thouse_number INTEGER NOT NULL,\n" +
                "\tfloor INTEGER NOT NULL,\n" +
                "\tapartment_number INTEGER NOT NULL,\n" +
                "FOREIGN KEY(TID) REFERENCES Truckings(TID)\n" +
                "\t);\n" +
                "\n";
        String Truckings_Sources = "CREATE TABLE IF NOT EXISTS " + "Truckings_Sources" + " (\n" +
                "\tTID INTEGER PRIMARY KEY,\n" +
                "\tcontact_guy TEXT NOT NULL,\n" +
                "\tcity TEXT NOT NULL,\n" +
                "\tphone_number TEXT NOT NULL,\n" +
                "\tstreet TEXT NOT NULL,\n" +
                "\tarea TEXT NOT NULL,\n" +
                "\thouse_number INTEGER NOT NULL,\n" +
                "\tfloor INTEGER NOT NULL,\n" +
                "\tapartment_number INTEGER NOT NULL,\n" +
                "FOREIGN KEY(TID) REFERENCES Truckings(TID)\n" +
                "\t);\n" +
                "\n";
        String Truckings_Orders = "CREATE TABLE IF NOT EXISTS " + "Truckings_Orders" + " (\n" +
                "\tTID INTEGER  NOT NULL,\n" +
                "\torderID TEXT NOT NULL,\n" +
                "\tPRIMARY KEY (orderID)" +
                "\t);\n" +
                "\n";
        String Truckings_Requests = "CREATE TABLE IF NOT EXISTS " + "Truckings_Requests" + " (\n" +
                "\tdate TEXT NOT NULL,\n" +
                "\torderID TEXT NOT NULL,\n" +
                "\tsource TEXT NOT NULL,\n" +
                "\tdestination TEXT NOT NULL,\n" +
                "\tPRIMARY KEY (orderID)" +
                "\t);\n" +
                "\n";
        try (Statement s = connection.createStatement()) {
            s.addBatch(vehiclesTable);
            s.addBatch(dirversLicencesTable);
            s.addBatch(Truckings);
            s.addBatch(Truckings_Destinations);
            s.addBatch(Truckings_Orders);
            s.addBatch(Truckings_Sources);
            s.addBatch(Truckings_Requests);
            s.executeBatch();
        } catch (Exception e) {
            throw new IllegalArgumentException("There was a problem to connect the database: " + e.getMessage());
        }
    }
}

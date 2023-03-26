package groupk.logistics;


import static org.junit.jupiter.api.Assertions.*;

import groupk.logistics.DataLayer.TruckingDTO;
import groupk.logistics.DataLayer.TruckingIDMap;
import groupk.logistics.DataLayer.TruckingMapper;
import groupk.logistics.DataLayer.myDataBase;
import groupk.logistics.business.DLicense;
import groupk.logistics.business.DriverController;
import groupk.logistics.business.TruckManagerController;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static groupk.CustomAssertions.*;
public class LogisticsTests {

    private TruckManagerController truckManagerController ;
    private DriverController driverController;
    private myDataBase myDataBase;
    protected Connection connection;

    @BeforeEach
    public void setService() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:testdatabase.db");
            myDataBase = new myDataBase(connection);
            truckManagerController = TruckManagerController.getInstance();
            driverController = DriverController.getInstance();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @AfterEach
    public void afterService() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Test
    public void addLicenses() {
        myDataBase.deleteDB();
        assertEquals(true, driverController.addLicense(319034121, "B"));
        assertEquals(true, driverController.addLicense(319034121, "C"));
        try {
            assertEquals(true, driverController.addLicense(319034121, "B"));
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Oops, there was unexpected problem with add the license 'B' to the driver: '319034121'\n" +
                    "Error description: [SQLITE_CONSTRAINT_PRIMARYKEY]  A PRIMARY KEY constraint failed (UNIQUE constraint failed: Drivers_Licences.username, Drivers_Licences.licence)");
        }
        assertEquals(true, driverController.getMyLicenses(319034121).size() == 2);
    }

    @Test
    public void addVehicle() {
        myDataBase.deleteDB();
        truckManagerController.addVehicle("B","12345678","volvo",20,40);
        try {
            truckManagerController.addVehicle("B","12345678","volvo",20,40);
        }
        catch (Exception e) {
            assertEquals(e.getMessage(), "Oops, there is another vehicle with that registration plate");
        }
        truckManagerController.addVehicle("B", "12315678", "mercedes", 4, 32);
        assertEquals(true, truckManagerController.getVehiclesRegistrationPlates().size() == 2);
    }




    @Test
    public void addTrucking() {
        myDataBase.deleteDB();
        driverController.addLicense(319034121, "B");
        truckManagerController.addVehicle("B","12345678","volvo",20,40);
        LocalDate date =  LocalDate.of(2023,10,18);
        LocalTime time =  LocalTime.of(20,0,0);
        LocalDateTime localDateTime =  LocalDateTime.of(date,time);
        localDateTime.plusSeconds(10);
        String[] source = {"tamirHouse","batYam","054-3397995","tamirStr","13","2","3","center"};
        String[]  destination = {"idoHouse","herzliya","052-4321231","idoStr","100","1","6","center"};
        List<String[]> sources = new LinkedList<>();
        List<String[]> destinations = new LinkedList<>();
        sources.add(source);
        destinations.add(destination);
        List<Integer> ordersIDs = new LinkedList<>();
        ordersIDs.add(1);
        ordersIDs.add(2);
        truckManagerController.addTrucking(1,"12345678",localDateTime,319034121,sources,destinations,ordersIDs,2,0);
        truckManagerController.forTests();
        try {
            truckManagerController.addTrucking(1,"12345678",localDateTime,319034121,sources,destinations,ordersIDs,2,0);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Oops, there is another trucking at the same date and with the same driver");
        }
        assertEquals(driverController.printMyTruckings(319034121).contains("tamirStr"), true);
    }

    private void setDetails() {
        driverController.addLicense(319034121, "B");
        truckManagerController.addVehicle("B","12345678","volvo",20,40);
        truckManagerController.addVehicle("B","12315678","skoda",10,50);
        LocalDate date =  LocalDate.of(2023,10,18);
        LocalTime time =  LocalTime.of(20,0,0);
        LocalDateTime localDateTime =  LocalDateTime.of(date,time);
        localDateTime.plusSeconds(10);
        String[] source = {"tamirHouse","batYam","054-3397995","tamirStr","13","2","3","center"};
        String[]  destination = {"idoHouse","herzliya","052-4321231","idoStr","100","1","6","center"};
        List<String[]> sources = new LinkedList<>();
        List<String[]> destinations = new LinkedList<>();
        sources.add(source);
        destinations.add(destination);
        List<Integer> ordersIDs = new LinkedList<>();
        ordersIDs.add(3);
        ordersIDs.add(4);
        truckManagerController.addTrucking(1,"12345678",localDateTime,319034121,sources,destinations,ordersIDs,2,0);
    }

    @Test
    public void updateVehicle() {
        myDataBase.deleteDB();
        setDetails();
        try {
            truckManagerController.updateVehicleOnTrucking(1,1,"12315678");
        }
        catch (Exception e) {
            assertEquals(e.getMessage(), null);
        }
        assertEquals(driverController.printMyTruckings(319034121).contains("12315678"),true);
    }

    @Test
    public void setWeight() {
        myDataBase.deleteDB();
        setDetails();
        try {
            driverController.setWeightForTrucking(319034121,5,9);
        }
        catch (Exception e) {
            assertEquals(e.getMessage(),"There is a user already connected to the system");
        }
        assertEquals(truckManagerController.getTruckingById(1, 5).getWeight() == 9,true);
    }

    @Test
    public void addOrders() {
        myDataBase.deleteDB();
        setDetails();
        try {
            truckManagerController.addOrderToTrucking(1,2,5);
        }
        catch (Exception e) {
            assertEquals(true, false);
        }
        assertEquals(true,truckManagerController.getTruckingById(1, 2).getOrders().contains(5));
        try {
            truckManagerController.addOrderToTrucking(1,2,5);
            assertEquals(true, false);
        }
        catch (Exception e){
            assertEquals(true, true);
        }
    }
    //
//
    @Test
    public void removeOrders() {
        myDataBase.deleteDB();
        setDetails();
        truckManagerController.moveOrdersToTrucking(1,2,3);
        assertEquals(true, truckManagerController.getTruckingById(1, 2).getOrders().size() == 1 & truckManagerController.getTruckingById(1, 2).getOrders().get(0).intValue() == 4);

    }
    //
    @Test
    public void removeTrucking() {
        myDataBase.deleteDB();
        setDetails();
        try {
            truckManagerController.removeTrucking(1,4);
        }
        catch (Exception e) {
            assertEquals("no exception",e.getMessage());
        }
        assertEquals(driverController.printMyTruckings(319034121).contains("Product: " + "Eggs_4902505139314"),false);
    }

    @Test
    public void getRegistrationPlates() {
        myDataBase.deleteDB();
        setDetails();
        truckManagerController.addVehicle("B","12315679","mercedes",4,32);
        assertEquals(truckManagerController.getVehiclesRegistrationPlates().size()==3,true);
        truckManagerController.deleteDB();
        myDataBase.deleteDB();
    }
    //
    @Test
    public void checkSites() {
        String[] site1 = {"tamirHouse","batYam","054-3397995","tamirStr","13","2","3","center"};
        String[]  site2 = {"idoHouse","herzliya","052-4321231","idoStr","100","1","6","north"};
        List<String[]> sites = new LinkedList<>();
        sites.add(site1);
        sites.add(site2);
        try {
            truckManagerController.checkSites(sites);
        }
        catch (Exception e) {
            assertEquals(e.getMessage(),"Not all sites from the same area");
        }
    }


}
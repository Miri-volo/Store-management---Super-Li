package groupk.shared.business;

import groupk.logistics.DataLayer.SiteDTO;
import groupk.logistics.DataLayer.TruckingDTO;
import groupk.logistics.DataLayer.myDataBase;
import groupk.logistics.business.*;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Delivery;
import groupk.shared.service.dto.Product;
import groupk.shared.service.dto.Site;
import groupk.workers.data.DalController;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LogisticsController {
    private myDataBase db;

    public LogisticsController(myDataBase myDataBase) {
        db = myDataBase;
    }

    public Response<Boolean> deleteDelivery(int truckManagerID, int deliveryID) {
        try {
            TruckManagerController.getInstance().removeTrucking(truckManagerID, deliveryID);
            return new Response<Boolean>(true);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Delivery> getTruckinfByID(int truckManagerID, int truckingID) {
        try {
            return new Response<Delivery>(truckingDTOToDelivery(TruckManagerController.getInstance().getTruckingById(truckManagerID, truckingID)));
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<Delivery>> listDeliveries(int truckManagerID) {
        try {
            List<TruckingDTO> truckings = TruckManagerController.getInstance().printBoard(truckManagerID);
            List<Delivery> toReturn = new LinkedList<Delivery>();
            for (TruckingDTO trucking : truckings) {
                toReturn.add(truckingDTOToDelivery(trucking));
            }
            return new Response<List<Delivery>>(toReturn);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<Delivery>> listFutureDeliveries(int truckManagerID) {
        try {
            List<TruckingDTO> truckings = TruckManagerController.getInstance().printFutureTruckings(truckManagerID);
            List<Delivery> toReturn = new LinkedList<Delivery>();
            for (TruckingDTO trucking : truckings) {
                toReturn.add(truckingDTOToDelivery(trucking));
            }
            return new Response<List<Delivery>>(toReturn);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<Delivery>> listDeliveriesByDriver(int driverID) {
        try {
            List<TruckingDTO> truckings = TruckManagerController.getInstance().printBoardOfDriver(driverID);
            List<Delivery> toReturn = new LinkedList<Delivery>();
            for (TruckingDTO trucking : truckings) {
                toReturn.add(truckingDTOToDelivery(trucking));
            }
            return new Response<>(toReturn);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<Delivery>> listFutureDeliveriesByDriver(int driverID) {
        try {
            List<TruckingDTO> truckings = TruckManagerController.getInstance().printFutureTruckingsOfDriver(driverID);
            List<Delivery> toReturn = new LinkedList<Delivery>();
            for (TruckingDTO trucking : truckings) {
                toReturn.add(truckingDTOToDelivery(trucking));
            }
            return new Response<List<Delivery>>(toReturn);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<String>> listVehicles() {
        try {
            return new Response<List<String>>(TruckManagerController.getInstance().getVehiclesRegistrationPlates());
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<String>[]> createDelivery(int truckManagerID, String registrationPlateOfVehicle, LocalDateTime date, int driverUsername, List<Site> sources, List<Site> destinations, List<Integer> orders, long hours, long minutes) {
        try {
            List<String[]> sources_ = new LinkedList<String[]>();
            List<String[]> destinations_ = new LinkedList<String[]>();
            for (Site source : sources) {
                sources_.add(SiteToArray(source));
            }
            for (Site destination : destinations) {
                destinations_.add(SiteToArray(destination));
            }
            return new Response<>(TruckManagerController.getInstance().addTrucking(truckManagerID, registrationPlateOfVehicle, date, driverUsername, sources_, destinations_, orders, hours, minutes));
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<Delivery>> listDeliveriesWithVehicle(String registration) {
        try {
            List<TruckingDTO> truckings = TruckManagerController.getInstance().printBoardOfVehicle(registration);
            List<Delivery> toReturn = new LinkedList<Delivery>();
            for (TruckingDTO trucking : truckings) {
                toReturn.add(truckingDTOToDelivery(trucking));
            }
            return new Response<List<Delivery>>(toReturn);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> addOrdersToTrucking(int truckManagerID, int truckingID, int orderID) {
        try {
            TruckManagerController.getInstance().addOrderToTrucking(truckManagerID, truckingID, orderID);
            return new Response<Boolean>(true);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<String>> updateSources(int truckManagerID, int truckingID, List<Site> sources) {
        try {
            List<String[]> sources_ = new LinkedList<String[]>();
            for (Site source : sources) {
                sources_.add(SiteToArray(source));
            }
            return new Response<List<String>>(TruckManagerController.getInstance().updateSourcesOnTrucking(truckManagerID, truckingID, sources_));
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<String>> updateDestination(int truckManagerID, int truckingID, List<Site> destinations) {
        try {
            List<String[]> destinations_ = new LinkedList<String[]>();
            for (Site destination : destinations) {
                destinations_.add(SiteToArray(destination));
            }
            return new Response<List<String>>(TruckManagerController.getInstance().updateDestinationsOnTrucking(truckManagerID, truckingID, destinations_));
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<String>> addSources(int truckManagerID, int truckingID, List<Site> sources) {
        try {
            List<String[]> sources_ = new LinkedList<String[]>();
            for (Site source : sources) {
                sources_.add(SiteToArray(source));
            }
            return new Response<List<String>>(TruckManagerController.getInstance().addSourcesToTrucking(truckManagerID, truckingID, sources_));
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<String>> addDestination(int truckManagerID, int truckingID, List<Site> destinations) {
        try {
            List<String[]> destinations_ = new LinkedList<String[]>();
            for (Site destination : destinations) {
                destinations_.add(SiteToArray(destination));
            }
            return new Response<List<String>>(TruckManagerController.getInstance().addDestinationToTrucking(truckManagerID, truckingID, destinations_));
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> moveOrderFromTrucking(int truckManagerID, int truckingID, int orderID) {
        try {
            TruckManagerController.getInstance().moveOrdersToTrucking(truckManagerID, truckingID, orderID);
            return new Response<Boolean>(true);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> updateVehicleOnTrucking(int truckManagerID, int truckingID, String registrationPlate) {
        try {
            TruckManagerController.getInstance().updateVehicleOnTrucking(truckManagerID, truckingID, registrationPlate);
            return new Response<Boolean>(true);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> updateDriverOnTrucking(int truckManagerID, int truckingID, int driverID) {
        try {
            TruckManagerController.getInstance().updateDriverOnTrucking(truckManagerID, truckingID, driverID);
            return new Response<Boolean>(true);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> updateDateOnTrucking(int truckManagerID, int truckingID, LocalDateTime newDate) {
        try {
            TruckManagerController.getInstance().updateDateOnTrucking(truckManagerID, truckingID, newDate);
            return new Response<Boolean>(true);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> deleteDB() {
        try {
            db.deleteDB();
            TruckManagerController.getInstance().deleteDB();
            return new Response<Boolean>(true);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> createVehicle(String license, String registrationPlate, String model, int weight, int maxWeight) {
        try {
            TruckManagerController.getInstance().addVehicle(license, registrationPlate, model, weight, maxWeight);
            return new Response<Boolean>(true);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> addLicenseForDriver(int driverID, String license) {
        try {
            boolean toReturn = DriverController.getInstance().addLicense(driverID, license);
            return new Response<Boolean>(toReturn);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<String>> getDriverLicenses(int driverID) {
        try {
            return new Response<List<String>>(DriverController.getInstance().getMyLicenses(driverID));
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<String[]> getLicensesList() {
        try {
            return new Response<String[]>(DLicense.getDLicenseList());
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> setWeightForDelivery(int driverID, int deliveryID, int weight) {
        try {
            boolean toReturn = DriverController.getInstance().setWeightForTrucking(driverID, deliveryID, weight);
            return new Response<Boolean>(toReturn);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<String[]> getAreasList() {
        try {
            return new Response<String[]>(Area.getAreasList());
        }
        catch (Exception e) {
            return new Response<>("Oops, something got wrong with getting products list");
        }
    }

    public Response<Integer> getTruckingIDByOrderID(int orderID) {
        try {
            return new Response<Integer>(TruckManagerController.getInstance().getTruckingIDByOrderID(orderID));
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> addTruckingRequest(int orderID, String sourceDetails, String destinationDetails) {
        try {
            return new Response<Boolean>(TruckManagerController.getInstance().addTruckingRequest(orderID, LocalDateTime.now(), sourceDetails, destinationDetails));
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> deleteTruckingRequest(int orderID) {
        try {
            boolean removed = TruckManagerController.getInstance().removeTruckingRequest(orderID);

            return new Response<Boolean>(removed);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<String>> getTruckingRequests() {
        try {
            return new Response<List<String>>(TruckManagerController.getInstance().getTruckingRequests());
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    private Delivery truckingDTOToDelivery(TruckingDTO truckingDTO) {
        List<Site> sources = new LinkedList<Site>();
        List<Site> destinations = new LinkedList<Site>();
        List<Integer> orders = new LinkedList<Integer>();
        for (SiteDTO source : truckingDTO.getSources()) {
            sources.add(new Site(source.getContactGuy(), source.getPhoneNumber(), source.getArea(), source.getCity(), source.getStreet(), source.getHouseNumber(), source.getFloor(), source.getApartment()));
        }
        for (SiteDTO destination : truckingDTO.getDestinations()) {
            destinations.add(new Site(destination.getContactGuy(), destination.getPhoneNumber(), destination.getArea(), destination.getCity(), destination.getStreet(), destination.getHouseNumber(), destination.getFloor(), destination.getApartment()));
        }
        for (Integer orderID : truckingDTO.getOrders())
            orders.add(new Integer(orderID.intValue()));
        return new Delivery(truckingDTO.getId(), truckingDTO.getDate(), truckingDTO.getTruckManager(), truckingDTO.getDriverUsername(), truckingDTO.getVehicleRegistrationPlate(), sources, destinations, truckingDTO.getWeight(), orders, truckingDTO.getHours(), truckingDTO.getMinutes());
    }

    private Map<String, Integer> ProductToMap(List<Product> Products) {
        Map<String, Integer> toReturn = new ConcurrentHashMap<String, Integer>();
        for (Product product : Products) {
            if (toReturn.containsKey(product.id)) {
                Integer oldValue = toReturn.get(product.id);
                toReturn.put(product.id, oldValue + product.count);
            }
            else {
                toReturn.put(product.id, product.count);
            }
        }
        return toReturn;
    }

    private String[] SiteToArray(Site site) {
        return new String[]{site.contactName, site.city, site.contactPhone, site.street, "" + site.houseNumber, "" + site.floor, "" + site.apartment, site.area};
    }
}

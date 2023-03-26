package groupk.logistics.business;

import groupk.logistics.DataLayer.*;
import groupk.shared.service.dto.Delivery;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class TruckManagerController {

    private static TruckManagerController singletonTruckManagerControllerInstance = null;
    private int truckingIdCounter;
    private VehicleMapper vehicleMapper;
    private TruckingMapper truckingMapper;
    private DriverLicencesMapper driverLicensesMapper;
    private TruckingRequestMapper truckingRequestMapper;

    public static TruckManagerController getInstance() {
        if (singletonTruckManagerControllerInstance == null)
            singletonTruckManagerControllerInstance = new TruckManagerController();
        return singletonTruckManagerControllerInstance;
    }

    private TruckManagerController() {
        truckingMapper = new TruckingMapper();
        truckingIdCounter = truckingMapper.getNextIdForTrucking();
        vehicleMapper = new VehicleMapper();
        driverLicensesMapper = new DriverLicencesMapper();
        truckingRequestMapper = new TruckingRequestMapper();
    }

    public void reserForTests() {
        truckingIdCounter = truckingMapper.getNextIdForTrucking();
    }

    public void deleteDB() {
        truckingIdCounter = 1;
        truckingMapper.deleteDB();
        driverLicensesMapper.deleteDB();
        vehicleMapper.deleteDB();
    }

    public void addVehicle(String lisence, String registrationPlate, String model, int weight, int maxWeight) {
        checkVehicle(lisence, registrationPlate, model, weight, maxWeight);
        VehicleDTO newVehicle = new VehicleDTO(lisence, registrationPlate, model, weight, maxWeight);
        boolean success = vehicleMapper.addVehicle(lisence, registrationPlate, model, weight, maxWeight);
        if(success)
            vehicleMapper.addVehicle(newVehicle);
    }

    private boolean checkVehicle(String lisence, String registrationPlate, String model, int weight, int maxWeight) {
        if (weight <= 0)
            throw new IllegalArgumentException("Weight is positive");
        if (!validateRegistationPlate(registrationPlate))
            throw new IllegalArgumentException("Invalid registration plate");
        if (maxWeight <= weight)
            throw new IllegalArgumentException("Max wight is bigger then weight");
        if (!validateModel(model))
            throw new IllegalArgumentException("Invalid model");
        return true;
    }

    public List<String> getVehiclesRegistrationPlates() {
        return vehicleMapper.getAllRegistrationPlates();
    }

    public List<String>[] addTrucking(int truckManagerId, String registrationPlateOfVehicle, LocalDateTime date, int driverUsername, List<String[]> sources, List<String[]> destinations, List<Integer> orders, long hours, long minutes) {
        boolean checkTrucking = (checkTrucking(truckingIdCounter, registrationPlateOfVehicle, date, driverUsername, sources, destinations, orders, hours, minutes));
        if (!checkDriverLicenseMatch(driverUsername, registrationPlateOfVehicle))
            throw new IllegalArgumentException("Oops, the driver does not have a driver's license compatible with this vehicle");
        checkConflicts(driverUsername, registrationPlateOfVehicle, date, hours, minutes);
        if(checkTrucking) {
            List<SiteDTO> sources_ = checkSites(sources);
            List<SiteDTO> destinations_ = checkSites(destinations);
            TruckingDTO trucking = new TruckingDTO(truckingIdCounter,date,truckManagerId,driverUsername,registrationPlateOfVehicle,hours,minutes,0,sources_,destinations_,orders);
            List<String>[] exceptions = truckingMapper.addTrucking(trucking);
            if (exceptions == null | exceptions.length != 3 | exceptions[0] == null | exceptions[1] == null | exceptions[2] == null)
                throw new IllegalArgumentException("Oops, something got wrong");
            truckingIdCounter++;
            return exceptions;
        }
        throw new IllegalArgumentException("Oops, we couldn't execute your request");
    }

    public List<SiteDTO> checkSites(List<String[]> Sites) {
        List<SiteDTO> sites = new LinkedList<SiteDTO>();
        Area area = null;
        for(String[] site : Sites) {
            if(site == null | site.length != 8)
                throw new IllegalArgumentException("Oops, one or more details about the site is empty");
            try {
                checkSite(site[0], site[1], site[2], site[3], Integer.parseInt(site[4]), Integer.parseInt(site[5]), Integer.parseInt(site[6]), site[7]);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException("Oops, you needed to enter number at the site details");
            }
            sites.add(new SiteDTO(site[0], site[1], site[2], site[3], Integer.parseInt(site[4]), Integer.parseInt(site[5]), Integer.parseInt(site[6]), site[7]));
            if (area == null)
                area = Area.castStringToArea(site[7]);
            else {
                if (area.equals(Area.castStringToArea(site[7])))
                    throw new IllegalArgumentException("Not all sites from the same area");
            }
        }
        return sites;
    }

    public void removeTrucking(int truckManagerID, int truckingId) {
        TruckingDTO trucking = truckingMapper.getTruckingByID(truckingId);
        if (trucking.getTruckManager() != truckManagerID)
            throw new IllegalArgumentException("Oops, you have not any trucking with that id");
        if(!truckingMapper.removeTrucking(truckingId))
            throw new IllegalArgumentException("seem like there is no trucking with that id");
    }

    public List<TruckingDTO> printBoard(int truckManagerID) {
        return truckingMapper.getTruckManagerBoard(truckManagerID);
    }

    public List<TruckingDTO> printTruckingsHistory(int truckManagerID) {
        return truckingMapper.getTruckManagerHistoryTruckings(truckManagerID);
    }

    public List<TruckingDTO> printFutureTruckings(int truckManagerID) {
        return truckingMapper.getTruckManagerFutureTruckings(truckManagerID);
    }

    public List<TruckingDTO> printBoardOfDriver(int driverUsername) {
        return truckingMapper.getDriverBoard(driverUsername);
    }

    public List<TruckingDTO> printTruckingsHistoryOfDriver(int driverUsername) {
        return truckingMapper.getDriverHistoryTruckings(driverUsername);
    }

    public List<TruckingDTO> printFutureTruckingsOfDriver(int driverUsername) {
        return truckingMapper.getDriverFutureTruckings(driverUsername);
    }

    public List<TruckingDTO> printBoardOfVehicle(String registrationPlate) {
        String registrationPlate_ = "";
        try {
            registrationPlate_ += Integer.parseInt(registrationPlate);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Oops, registration plate must be number");
        }
        if(!validateRegistationPlate(registrationPlate_))
            throw new IllegalArgumentException("Oops, invalid registration plate");
        return truckingMapper.getVehicleBoard(registrationPlate);
    }

    public List<TruckingDTO> printTruckingsHistoryOfVehicle(String registrationPlate) {
        String registrationPlate_ = "";
        try {
            registrationPlate_ += Integer.parseInt(registrationPlate);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Oops, registration plate must be number");
        }
        if(!validateRegistationPlate(registrationPlate_))
            throw new IllegalArgumentException("Oops, invalid registration plate");
        return truckingMapper.getVehicleHistoryTruckings(registrationPlate);
    }

    public List<TruckingDTO> printFutureTruckingsOfVehicle(String registrationPlate) {
        String registrationPlate_ = "";
        try {
            registrationPlate_ += Integer.parseInt(registrationPlate);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Oops, registration plate must be number");
        }
        if(!validateRegistationPlate(registrationPlate_))
            throw new IllegalArgumentException("Oops, invalid registration plate");
        return truckingMapper.getVehicleFutureTruckings(registrationPlate);
    }

    public List<String> addSourcesToTrucking(int truckManagerID, int truckingId, List<String[]> sources) {
        TruckingDTO trucking = truckingMapper.getTruckingByID(truckingId);
        if (trucking.getTruckManager() != truckManagerID)
            throw new IllegalArgumentException("Oops, you have not any trucking with that id");
        List<SiteDTO> sources_ = checkSites(sources);
        if(!sources_.get(0).getArea().equals(trucking.getSources().get(0).getArea()))
            throw new IllegalArgumentException("Oops, the sources that you want to add don't have same area as the current sources");
        List<String> exceptions = truckingMapper.addTruckingSources(truckingId, sources_);
        if(exceptions.size() == sources_.size())
            throw new IllegalArgumentException("Oops, We could not add any source");
        return exceptions;
    }

    public List<String> addDestinationToTrucking(int truckManagerID, int truckingId, List<String[]> destinations) {
        TruckingDTO trucking = truckingMapper.getTruckingByID(truckingId);
        if (trucking.getTruckManager() != truckManagerID)
            throw new IllegalArgumentException("Oops, you have not any trucking with that id");
        List<SiteDTO> destinations_ = checkSites(destinations);
        if(!destinations_.get(0).getArea().equals(trucking.getDestinations().get(0).getArea()))
            throw new IllegalArgumentException("Oops, the destinations that you want to add don't have same area as the current destinations");
        List<String> exceptions = truckingMapper.addTruckingDestinations(truckingId, destinations_);
        if(exceptions.size() == destinations_.size())
            throw new IllegalArgumentException("Oops, We could not add any destination");
        return exceptions;
    }

    public void addOrderToTrucking(int truckManagerID, int truckingId, int orderID) {
        TruckingDTO trucking = truckingMapper.getTruckingByID(truckingId);
        if (trucking.getTruckManager() != truckManagerID)
            throw new IllegalArgumentException("Oops, you have not any trucking with that id");
        if(truckingMapper.existOrder(truckingId,orderID))
            throw new IllegalArgumentException("The order is already exist in the trucking");
        else {
            truckingMapper.addTruckingOrder(truckingId, orderID);
        }
    }

    public List<String> updateSourcesOnTrucking(int truckManagerID, int truckingId, List<String[]> sources) {
        TruckingDTO trucking = truckingMapper.getTruckingByID(truckingId);
        if (trucking.getTruckManager() != truckManagerID)
            throw new IllegalArgumentException("Oops, you have not any trucking with that id");
        List<SiteDTO> oldSources = truckingMapper.getSourcesByTruckingId(truckingId);
        List<SiteDTO> sources_ = checkSites(sources);
        truckingMapper.removeSourcesTrucking(truckingId);
        try {
            return truckingMapper.addTruckingSources(truckingId, sources_);
        }
        catch (Exception e) {
            try {
                truckingMapper.addTruckingSources(truckingId, oldSources);
                throw new IllegalArgumentException("Oops, we tried to update the sources but there was an error: \"" + e.getMessage() + "\nThe trucking didn't change");
            }
            catch (Exception ex) {
                if (ex.getMessage().substring(0,14).equals("Oops, we tried"))
                    throw ex;
                truckingMapper.removeTrucking(truckingId);
                throw new IllegalArgumentException("There was an unexpected error. We were unable to recover the trucking data so we deleted it.");
            }
        }
    }

    public List<String> updateDestinationsOnTrucking(int truckManagerID, int truckingId, List<String[]> destinations) {
        TruckingDTO trucking = truckingMapper.getTruckingByID(truckingId);
        if (trucking.getTruckManager() != truckManagerID)
            throw new IllegalArgumentException("Oops, you have not any trucking with that id");
        List<SiteDTO> oldDetinations = truckingMapper.getDestinationsByTruckingId(truckingId);
        List<SiteDTO> destinations_ = checkSites(destinations);
        truckingMapper.removeDestinationsTrucking(truckingId);
        try {
            return truckingMapper.addTruckingDestinations(truckingId, destinations_);
        }
        catch (Exception e) {
            try {
                truckingMapper.addTruckingDestinations(truckingId, oldDetinations);
                throw new IllegalArgumentException("Oops, we tried to update the destinations but there was an error: \"" + e.getMessage() + "\nThe trucking didn't change");
            }
            catch (Exception ex) {
                if (ex.getMessage().substring(0,14).equals("Oops, we tried"))
                    throw ex;
                truckingMapper.removeTrucking(truckingId);
                throw new IllegalArgumentException("There was an unexpected error. We were unable to recover the trucking data so we deleted it.");
            }
        }
    }

    public boolean moveOrdersToTrucking(int truckManagerID, int truckingId, int orderID) {
        TruckingDTO trucking = truckingMapper.getTruckingByID(truckingId);
        if (trucking.getTruckManager() != truckManagerID)
            throw new IllegalArgumentException("Oops, you have not any trucking with that id");
        if(!truckingMapper.existOrder(truckingId, orderID))
            throw new IllegalArgumentException("Oops, this order doesn't exist in the trucking");
        return truckingMapper.removeOrderFromTrucking(truckingId, orderID);
    }

    public List<Integer> getOrdersByTruckingID(int truckManagerID, int truckingId) {
        TruckingDTO trucking = truckingMapper.getTruckingByID(truckingId);
        if (trucking.getTruckManager() != truckManagerID)
            throw new IllegalArgumentException("Oops, you have not any trucking with that id");
        return truckingMapper.getOrders(truckingId);
    }

    public void updateVehicleOnTrucking(int truckManagerID, int truckingId, String registrationPlateOfVehicle) {
        TruckingDTO trucking = truckingMapper.getTruckingByID(truckingId);
        if (trucking == null)
            throw new IllegalArgumentException("There is no trucking with id: " + truckingId);
        if (trucking.getTruckManager() != truckManagerID)
            throw new IllegalArgumentException("Oops, you have not any trucking with that id");
        if (!checkDriverLicenseMatch(trucking.getDriverUsername(), registrationPlateOfVehicle))
            throw new IllegalArgumentException("Oops, the driver does not have a driver's license compatible with this vehicle");
        checkConflictsVehicle(trucking.getDriverUsername(), registrationPlateOfVehicle, trucking.getDate(), trucking.getHours(), trucking.getMinutes());
        if (!truckingMapper.updateVehicle(truckingId, registrationPlateOfVehicle))
            throw new IllegalArgumentException("No change of vehicle was made to order: " + truckingId + ". It maybe the same vehicle of before the change.");
    }

    public void updateDriverOnTrucking(int truckManagerID, int truckingId, int driverUsername) {
        TruckingDTO trucking = truckingMapper.getTruckingByID(truckingId);
        if (trucking == null)
            throw new IllegalArgumentException("There is no trucking with id: " + truckingId);
        if (trucking.getTruckManager() != truckManagerID)
            throw new IllegalArgumentException("Oops, you have not any trucking with that id");
        if (!checkDriverLicenseMatch(driverUsername, trucking.getVehicleRegistrationPlate()))
            throw new IllegalArgumentException("Oops, the driver does not have a driver's license compatible with this vehicle");
        checkConflictsDriver(driverUsername, trucking.getVehicleRegistrationPlate(), trucking.getDate(), trucking.getHours(), trucking.getMinutes());
        if (!truckingMapper.updateDriver(truckingId, driverUsername))
            throw new IllegalArgumentException("No change of driver was made to order: " + truckingId + ". It maybe the same driver of before the change.");
    }

    public void updateDateOnTrucking(int truckManagerID, int truckingId, LocalDateTime date) {
        checkDate(date);
        TruckingDTO trucking = truckingMapper.getTruckingByID(truckingId);
        if (trucking == null)
            throw new IllegalArgumentException("There is no trucking with id: " + truckingId);
        if (trucking.getTruckManager() != truckManagerID)
            throw new IllegalArgumentException("Oops, you have not any trucking with that id");
        checkConflicts(trucking.getDriverUsername(), trucking.getVehicleRegistrationPlate(), date, trucking.getHours(), trucking.getMinutes());
        if (!truckingMapper.updateDate(truckingId, date))
            throw new IllegalArgumentException("No change of date was made to order: " + truckingId + ". It maybe the same driver of before the change.");
    }

    public TruckingDTO getTruckingById(int truckManagerID, int truckingID) {
        TruckingDTO trucking = truckingMapper.getTruckingByID(truckingID);
        if (trucking == null)
            throw new IllegalArgumentException("There is no trucking with id: " + truckingID);
        if (trucking.getTruckManager() != truckManagerID)
            throw new IllegalArgumentException("Oops, you have not any trucking with that id");
        return trucking;
    }

    public int getTruckingIDByOrderID(int orderID) {
        return truckingMapper.getTruckingIDByOrderID(orderID);
    }

    public boolean addTruckingRequest(int orderID, LocalDateTime createDate, String sourceDetails, String destinationDetails) {
        return truckingRequestMapper.addTruckingRequest(orderID, createDate, sourceDetails, destinationDetails);
    }

    public boolean removeTruckingRequest(int orderID) {
        return truckingRequestMapper.removeTruckingRequest(orderID);
    }

    public List<String> getTruckingRequests() {
        return truckingRequestMapper.getTruckingRequests();
    }

    public String printTrucking(TruckingDTO trucking) {
        String toReturn = "TRUCKING - " + trucking.getId() + "\n\n";
        toReturn += "TRUCKING DETAILS:\n";
        toReturn += "Date: " + trucking.getDate().getDayOfMonth() + "/" + trucking.getDate().getMonthValue() + "/" + trucking.getDate().getYear() + "\n";
        toReturn += "Start Hour: " + printHour(trucking.getDate()) + "\n";
        toReturn += "End Hour: " + printHour(trucking.getDate().plusHours(trucking.getHours()).plusMinutes(trucking.getMinutes())) + "\n";
        toReturn += "Vehicle registration plate: " + trucking.getVehicleRegistrationPlate() + "\n";
        toReturn += "Driver: " + trucking.getDriverUsername() + "\n";
        toReturn += printSources(trucking);
        toReturn += printDestinations(trucking);
        toReturn += printOrders(trucking) + "\n";
        if (trucking.getWeight() > 0)
            toReturn += "Total weight: " + trucking.getWeight() + "\n";
        else
            toReturn += "There is no data about the trucking weight\n";
        return toReturn;
    }

    private String printHour(LocalDateTime date) {
        String toReturn = "";
        if(date.getHour()<10)
            toReturn += "0" + date.getHour() + ":";
        else
            toReturn += date.getHour() + ":";
        if(date.getMinute()<10)
            toReturn += "0" + date.getMinute();
        else
            toReturn += date.getHour();
        return toReturn;
    }

    private String printSources(TruckingDTO trucking) {
        String toReturn = "\nSOURCE DETAILS:\n";
        toReturn += printSitesList(trucking.getSources());
        return toReturn;
    }

    private String printDestinations(TruckingDTO trucking) {
        String toReturn = "\nDESTINATION DETAILS:\n";
        toReturn += printSitesList(trucking.getDestinations());
        return toReturn;
    }

    private String printOrders(TruckingDTO trucking) {
        return "\nOrder-IDs:\n"  + printOrdersList(trucking.getOrders());
    }

    private String printSitesList(List<SiteDTO> sourcesOrDestinations) {
        String toReturn  = "";
        int siteCounter = 1;
        for (SiteDTO site : sourcesOrDestinations) {
            toReturn += siteCounter + ". " + printSite(site);
            siteCounter++;
        }
        return toReturn;
    }

    private String printSite(SiteDTO site) {
        String toReturn = "Area: " + site.getArea() + "\n";
        toReturn += "Address: " + site.getStreet() + " " + site.getHouseNumber() + ", " + site.getCity() + "\n";
        if (site.getApartment() != 0 | site.getFloor() != 0)
            toReturn += "floor: " + site.getFloor() + " apartment: " + site.getApartment() + "\n";
        toReturn += "Contact guy: " + site.getContactGuy() + "  phone number: " + site.getPhoneNumber() + "\n";
        return toReturn;
    }

    private String printOrdersList(List<Integer> orders) {
        String toReturn  = "";
        int siteCounter = 1;
        for (Integer orderID : orders) {
            toReturn += siteCounter + ". " + orderID.intValue() + "\n";
            siteCounter++;
        }
        return toReturn;
    }

    private boolean checkDate(LocalDateTime date) {
        if (date.compareTo(LocalDateTime.now()) <= 0)
            throw new IllegalArgumentException("Oops, the date must be in the future");
        return true;
    }

    private boolean checkDriverLicenseMatch(int driverUserName, String RegistrationPlate){
        List<String> driverLicenses = driverLicensesMapper.getMyLicenses(driverUserName);
        String license = vehicleMapper.getLicense(RegistrationPlate);
        if (driverLicenses == null)
            throw new IllegalArgumentException("There is not licenses we can see of that driver");
        if (license == null)
            throw new IllegalArgumentException("We cannot found the vehicle's license");
        if (driverLicenses.contains(license))
            return true;
        return false;
    }

    private boolean checkConflicts(int driverUserName, String VehicleRegristationPlate, LocalDateTime date, long hoursOfTrucking, long minutesOfTrucking) {
        List<TruckingDTO> conflictingEvents = truckingMapper.getRelevantTruckings(date);
        LocalDateTime endTruck = date.plusHours(hoursOfTrucking).plusMinutes(minutesOfTrucking);
        ListIterator<TruckingDTO> truckingListIterator = conflictingEvents.listIterator();
        while (truckingListIterator.hasNext()) {
            TruckingDTO currentTrucking = truckingListIterator.next();
            LocalDateTime startCurr = currentTrucking.getDate();
            LocalDateTime endCurr = currentTrucking.getDate().plusHours(currentTrucking.getHours()).plusMinutes(currentTrucking.getMinutes());
            if (!(endTruck.isBefore(startCurr) | date.isAfter(endCurr))) {
                checkAvailibility(currentTrucking.getVehicleRegistrationPlate() ,VehicleRegristationPlate, currentTrucking.getDriverUsername(),driverUserName);
            }
        }
        return true;
    }

    private boolean checkConflictsVehicle(int driverUserName, String VehicleRegristationPlate, LocalDateTime date, long hoursOfTrucking, long minutesOfTrucking) {
        List<TruckingDTO> conflictingEvents = truckingMapper.getRelevantTruckings(date);
        LocalDateTime endTruck = date.plusHours(hoursOfTrucking).plusMinutes(minutesOfTrucking);
        ListIterator<TruckingDTO> truckingListIterator = conflictingEvents.listIterator();
        while (truckingListIterator.hasNext()) {
            TruckingDTO currentTrucking = truckingListIterator.next();
            LocalDateTime startCurr = currentTrucking.getDate();
            LocalDateTime endCurr = currentTrucking.getDate().plusHours(currentTrucking.getHours()).plusMinutes(currentTrucking.getMinutes());
            if (!(endTruck.isBefore(startCurr) | date.isAfter(endCurr))) {
                checkAvailibilityVehicle(currentTrucking.getVehicleRegistrationPlate() ,VehicleRegristationPlate);
            }
        }
        return true;
    }

    private boolean checkConflictsDriver(int driverUserName, String VehicleRegristationPlate, LocalDateTime date, long hoursOfTrucking, long minutesOfTrucking) {
        List<TruckingDTO> conflictingEvents = truckingMapper.getRelevantTruckings(date);
        LocalDateTime endTruck = date.plusHours(hoursOfTrucking).plusMinutes(minutesOfTrucking);
        ListIterator<TruckingDTO> truckingListIterator = conflictingEvents.listIterator();
        while (truckingListIterator.hasNext()) {
            TruckingDTO currentTrucking = truckingListIterator.next();
            LocalDateTime startCurr = currentTrucking.getDate();
            LocalDateTime endCurr = currentTrucking.getDate().plusHours(currentTrucking.getHours()).plusMinutes(currentTrucking.getMinutes());
            if (!(endTruck.isBefore(startCurr) | date.isAfter(endCurr))) {
                checkAvailibilityDriver( currentTrucking.getDriverUsername(),driverUserName);
            }
        }
        return true;
    }

    private boolean checkAvailibilityVehicle(String registrationPlate1, String registrationPlate2) {
        if (registrationPlate1 == registrationPlate2)
            throw new IllegalArgumentException("Oops, there is another trucking at the same date and with the same vehicle");
        return true;
    }

    private boolean checkAvailibilityDriver(int driverUserName1,int driverUserName2) {
        if (driverUserName1 == driverUserName2)
            throw new IllegalArgumentException("Oops, there is another trucking at the same date and with the same driver");
        return true;
    }


    private boolean checkAvailibility(String registrationPlate1, String registrationPlate2,int driverUserName1,int driverUserName2) {
        if (registrationPlate1 == registrationPlate2)
            throw new IllegalArgumentException("Oops, there is another trucking at the same date and with the same vehicle");
        if (driverUserName1 == driverUserName2)
            throw new IllegalArgumentException("Oops, there is another trucking at the same date and with the same driver");
        return true;
    }

    private boolean checkTrucking(int id, String registrationPlateOfVehicle, LocalDateTime date, int driverUsername, List<String[]> sources, List<String[]> destinations, List<Integer> orders,long hours, long minutes) {
        if (registrationPlateOfVehicle == null)
            throw new IllegalArgumentException("The registration plate is empty");
        if (date == null)
            throw new IllegalArgumentException("The date is empty");
        if (sources == null | sources.size() == 0)
            throw new IllegalArgumentException("The sources list is empty");
        if (destinations == null | destinations.size() == 0)
            throw new IllegalArgumentException("The destinations list is empty");
        if (orders == null | orders.size() == 0)
            throw new IllegalArgumentException("The orders list is empty");
        for (Integer order : orders) {
            if (order == null)
                throw new IllegalArgumentException("Oops, one or more of your orderIDs are empty");
        }
        return true;
    }

    private void checkSite(String contactGuy, String city, String phoneNumber, String street, int houseNumber, int floor, int apartment, String area) {
        if (contactGuy == null | contactGuy.length() == 0 | city == null | city.length() == 0 | phoneNumber == null | phoneNumber.length() == 0 | street == null | street.length() == 0)
            throw new IllegalArgumentException("One or more of the site details are empty");
        validateInt(floor, "floor", 0, 100);
        validateInt(apartment, "apartment", 0, 100);
        validateInt(houseNumber, "house number", 1, 300);
        validateString(city, "city", 2, 20);
        validateString(city, "street", 2, 20);
        validateString(city, "contact guy", 2, 15);
        validatePhoneNumber(phoneNumber);
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        String phoneNumberWithoutHyphens = "";
        for (int index = 0; index < phoneNumber.length(); index++) {
            char charAt = phoneNumber.charAt(index);
            if (charAt > 47 && charAt < 58)
                phoneNumberWithoutHyphens += charAt;
            else if (charAt != 45)
                throw new IllegalArgumentException("The phone number is not validate. the phone number must contains only digits");
        }
        if (phoneNumberWithoutHyphens.length() < 8 | phoneNumberWithoutHyphens.length() > 12)
            throw new IllegalArgumentException("The phone number is too short/long");
        phoneNumber = phoneNumberWithoutHyphens;
        return true;
    }

    private boolean validateInt(int fieldToCheck, String fieldToCheckName, int min, int max) {
        if(fieldToCheck >= min & fieldToCheck <= max)
            return true;
        throw new IllegalArgumentException(fieldToCheckName + " isn't valid. Need to be between " + String.valueOf(min) + "-" + String.valueOf(max) + ".");
    }

    private boolean validateString(String fieldToCheck, String fieldToCheckName, int minLength, int maxLength) {
        if (fieldToCheck == null)
            throw new IllegalArgumentException(fieldToCheckName + " is empty");
        if (fieldToCheck.length() < minLength | fieldToCheck.length() > maxLength)
            throw new IllegalArgumentException(fieldToCheckName + " isn't valid");
        return true;
    }

    private boolean validateRegistationPlate(String registationPlate) {
        if (registationPlate == null)
            throw new IllegalArgumentException("Oops, registration plate is empty");
        if (registationPlate.length() != 8 & registationPlate.length() != 7)
            return  false;
        for(int i = 0 ; i < registationPlate.length(); i++) {
            if (!(Character.isDigit(registationPlate.charAt(i))))
                throw new IllegalArgumentException("Registration plate must has only digits");
        }
        return true;
    }

    private boolean validateModel(String model) {
        if (model == null)
            throw new IllegalArgumentException("model is empty");
        if (model.length() <3 | model.length() > 15)
            return  false;
        for (int i = 0; i < model.length(); i++) {
            if (!(Character.isLetter(model.charAt(i)) | Character.isDigit(model.charAt(i)) | model.charAt(i) == ' '))
                return  false;
        }
        return true;
    }

    public void forTests() {
        truckingIdCounter=1;
    }
}

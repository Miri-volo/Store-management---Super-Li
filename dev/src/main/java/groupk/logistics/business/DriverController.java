package groupk.logistics.business;

import groupk.logistics.DataLayer.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class DriverController {

    private static DriverController singletonDriverControllerInstance = null;
    private DriverLicencesMapper driverLicencesMapper;
    private TruckingMapper truckingMapper;
    private VehicleMapper vehicleMapper;


    public static DriverController getInstance() {
        if (singletonDriverControllerInstance == null)
            singletonDriverControllerInstance = new DriverController();
        return singletonDriverControllerInstance;
    }

    private DriverController() {
        driverLicencesMapper = new DriverLicencesMapper();
        truckingMapper = new TruckingMapper();
        vehicleMapper = new VehicleMapper();
    }

    public List<String> getMyLicenses(int driverUsername) {
        List<String> toReturn = new LinkedList<String>();
        List<String> licenses = driverLicencesMapper.getMyLicenses(driverUsername);
        for (String license : licenses) {
            toReturn.add(license);
        }
        return toReturn;
    }

    public String printMyTruckings(int driverID) {
        String toReturn = "TRUCKINGS BOARD\n\n";
        List<TruckingDTO> truckings = truckingMapper.getDriverBoard(driverID);
        if (truckings.size() == 0 | truckings == null) {
            toReturn += "[empty]";
            return toReturn;
        }
        for (TruckingDTO trucking : truckings)
            toReturn += printTrucking(trucking);
        return toReturn;
    }

    public String printMyTruckingsHistory(int driverID) {
        String toReturn = "            TRUCKINGS HISTORY\n\n";
        List<TruckingDTO> truckings = truckingMapper.getDriverHistoryTruckings(driverID);
        if (truckings.size() == 0 | truckings == null) {
            toReturn += "[empty]";
            return toReturn;
        }
        for (TruckingDTO trucking : truckings)
            toReturn += printTrucking(trucking);
        return toReturn;
    }

    public String printMyFutureTruckings(int driverID) {
        String toReturn = "            FUTURE TRUCKINGS\n\n";
        List<TruckingDTO> truckings = truckingMapper.getDriverFutureTruckings(driverID);
        if (truckings.size() == 0 | truckings == null) {
            toReturn += "[empty]";
            return toReturn;
        }
        for (TruckingDTO trucking : truckings)
            toReturn += printTrucking(trucking);
        return toReturn;

    }

    public boolean addLicense(int driverID, String license) {
        DLicense _license = DLicense.castStringToDlLicense(license);
        return driverLicencesMapper.addLicence(driverID ,_license);
    }

    public boolean setWeightForTrucking(int driverID, int truckingId, int weight) {
        TruckingDTO trucking = truckingMapper.getTruckingByID(truckingId);
        if (trucking.getDriverUsername() != driverID)
            throw new IllegalArgumentException("Oops, you have not any trucking with that id");
        String registrationPlate = truckingMapper.getLicencePlate(truckingId);
        VehicleDTO vehicle = vehicleMapper.getVehicle(registrationPlate);
        checkWeight(vehicle,weight);
        return truckingMapper.setWeightForTrucking(truckingId,weight);
    }

    private void checkWeight(VehicleDTO vehicle,int weight) {
        if(weight<=0) throw new IllegalArgumentException("Negative weight ? are you drunk?");
        if(vehicle.getMaxWeight()-weight<vehicle.getWeight()) throw new IllegalArgumentException("To heavy boss");
    }

    private String printTrucking(TruckingDTO trucking) {
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
}


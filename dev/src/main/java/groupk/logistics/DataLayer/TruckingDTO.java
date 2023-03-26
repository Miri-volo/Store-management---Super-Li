package groupk.logistics.DataLayer;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TruckingDTO {
    private final int id;
    private int truckManager;
    private LocalDateTime date;
    private int driverUsername;
    private String vehicleRegistrationPlate;
    private long hours;
    private long minutes;
    private int weight;
    private List<SiteDTO> sources;
    private List<SiteDTO> destinations;
    private List<Integer> orders;

    public TruckingDTO(int id, String date,int truckManager, int driverUsername, String vehicleRegistrationPlate, long hours, long minutes, int weight, List<SiteDTO> sources, List<SiteDTO> destinations, List<Integer> orders) {
        this.id = id;
        this.date = convertDate(date, id);
        this.truckManager = truckManager;
        this.driverUsername = driverUsername;
        this.vehicleRegistrationPlate = vehicleRegistrationPlate;
        this.hours = hours;
        this.minutes = minutes;
        this.weight = weight;
        this.sources = sources;
        this.destinations = destinations;
        this.orders = orders;
    }

    public TruckingDTO(int id, LocalDateTime date, int truckManager, int driverUsername, String vehicleRegistrationPlate, long hours, long minutes, int weight, List<SiteDTO> sources, List<SiteDTO> destinations, List<Integer> orders) {
        this.id = id;
        this.date = date;
        this.truckManager = truckManager;
        this.driverUsername = driverUsername;
        this.vehicleRegistrationPlate = vehicleRegistrationPlate;
        this.hours = hours;
        this.minutes = minutes;
        this.weight = weight;
        this.sources = sources;
        this.destinations = destinations;
        this.orders = orders;
    }
    private LocalDateTime convertDate(String date, int TruckingID){
        LocalDateTime toReturn;
        try {
            date = date.substring(0,date.length()-3);
            toReturn = LocalDateTime.parse(date, TruckingMapper.dateFormat);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("The date value of trucking: " + TruckingID + " is illegal. please fix that.");
        }
        return toReturn;
    }

    public int getId() {
        return id;
    }

    public long getHours() {
        return hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public int getDriverUsername() {
        return driverUsername;
    }

    public int getTruckManager() {
        return truckManager;
    }

    public String getVehicleRegistrationPlate() {
        return vehicleRegistrationPlate;
    }

    public LocalDateTime getDate() {
        return date.plusHours(0);
    }

    public int getWeight() {
        return weight;
    }

    public List<SiteDTO> getSources() { return sources; }

    public List<SiteDTO> getDestinations() { return destinations; }

    public List<Integer> getOrders() { return orders; }

    public void setWeight(int newWeight) {this.weight = newWeight; }

    public void updateVehicle(String vehicleRegistrationPlate) {this.vehicleRegistrationPlate = vehicleRegistrationPlate;}

    public void updateDriverUsername(int driverUsername) { this.driverUsername = driverUsername;}

    public void updateDate(LocalDateTime date) { this.date = date;}

    public void updateSources(List<SiteDTO> sources) { this.sources = sources; }

    public void updateDestinations(List<SiteDTO> destinations) { this.destinations = destinations; }

    public void updateProducts(List<Integer> orders) { this.orders = orders; }
}
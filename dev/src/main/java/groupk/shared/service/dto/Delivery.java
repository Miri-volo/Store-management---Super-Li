package groupk.shared.service.dto;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

public class Delivery {
    public int id;
    public LocalDateTime date;
    public int truckManagerID;
    public int driverID;
    public String vehicleRegistration;
    public List<Site> sources;
    public List<Site> destinations;
    public int totalWeight;
    public List<Integer> orders;
    public long durationInMinutes;

    public Delivery (int id, LocalDateTime date, int truckManagerID, int driverID, String vehicleRegistration, List<Site> sources, List<Site> destinations, int totalWeight, List<Integer> orders, long hours, long minutes) {
        this.id = id;
        this.date = date;
        this.truckManagerID = truckManagerID;
        this.driverID = driverID;
        this.vehicleRegistration = vehicleRegistration;
        this.sources = sources;
        this.destinations = destinations;
        this.totalWeight = totalWeight;
        this.orders = orders;
        this.durationInMinutes = hours * 60 + minutes;
    }
}

package groupk.shared.PresentationLayer.EmployeesLogistics.command;

import groupk.logistics.DataLayer.SiteDTO;
import groupk.logistics.DataLayer.TruckingDTO;
import groupk.shared.PresentationLayer.EmployeesLogistics.CommandRunner;
import groupk.shared.service.Response;
import groupk.shared.service.dto.Delivery;
import groupk.shared.service.dto.Employee;
import groupk.shared.service.dto.Site;

import java.time.LocalDateTime;
import java.util.List;

public class ListDeliveries implements Command {
    @Override
    public String name() {
        return "list deliveries";
    }

    @Override
    public String description() {
        return "print a list of deliveries";
    }

    @Override
    public boolean isMatching(String line) {
        return line.startsWith("list deliveries");
    }

    @Override
    public boolean isVisible(Employee.Role role) {
        return role == Employee.Role.LogisticsManager | role == Employee.Role.Driver;
    }

    @Override
    public void execute(String[] command, CommandRunner runner) {
        if (command.length != 2) {
            System.out.println("Error: Too many arguments.");
            System.out.println("Usage:");
            System.out.println("> list deliveries");
            return;
        }

        Response<List<Delivery>> response = runner.getService().listDeliveries(runner.getSubject());
        if (response.isError()) {
            System.out.printf("Error: %s\n", response.getErrorMessage());
            return;
        }
        List<Delivery> deliveries = response.getValue();
        String toReturn = "DELIVERIES BOARD\n\n";
        if (deliveries.size() == 0 | deliveries == null) {
            toReturn += "[empty]";
            System.out.println(toReturn);
            return;
        }
        for (Delivery d : response.getValue()) {
            toReturn += printTrucking(d);
        }
        System.out.println(toReturn);
    }

    private String printTrucking(Delivery trucking) {
        String toReturn = "TRUCKING - " + trucking.id + "\n\n";
        toReturn += "TRUCKING DETAILS:\n";
        toReturn += "Date: " + trucking.date.getDayOfMonth() + "/" + trucking.date.getMonthValue() + "/" + trucking.date.getYear() + "\n";
        toReturn += "Start Hour: " + printHour(trucking.date) + "\n";
        toReturn += "End Hour: " + printHour(trucking.date.plusHours(trucking.durationInMinutes/60).plusMinutes(trucking.durationInMinutes%60)) + "\n";
        toReturn += "Vehicle registration plate: " + trucking.vehicleRegistration + "\n";
        toReturn += "Driver: " + trucking.driverID + "\n";
        toReturn += printSources(trucking);
        toReturn += printDestinations(trucking);
        toReturn += printOrders(trucking) + "\n";
        if (trucking.totalWeight > 0)
            toReturn += "Total weight: " + trucking.totalWeight + "\n";
        else
            toReturn += "There is no data about the trucking weight\n";
        toReturn += "\n";
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

    private String printSources(Delivery trucking) {
        String toReturn = "\nSOURCE DETAILS:\n";
        toReturn += printSitesList(trucking.sources);
        return toReturn;
    }

    private String printDestinations(Delivery trucking) {
        String toReturn = "\nDESTINATION DETAILS:\n";
        toReturn += printSitesList(trucking.destinations);
        return toReturn;
    }

    private String printOrders(Delivery trucking) {
        return "\nOrder-IDs:\n"  + printOrdersList(trucking.orders);
    }

    private String printSitesList(List<Site> sourcesOrDestinations) {
        String toReturn  = "";
        int siteCounter = 1;
        for (Site site : sourcesOrDestinations) {
            toReturn += siteCounter + ". " + printSite(site);
            siteCounter++;
        }
        return toReturn;
    }

    private String printSite(Site site) {
        String toReturn = "Area: " + site.area + "\n";
        toReturn += "Address: " + site.street + " " + site.houseNumber + ", " + site.city + "\n";
        if (site.apartment != 0 | site.floor != 0)
            toReturn += "floor: " + site.floor + " apartment: " + site.apartment + "\n";
        toReturn += "Contact guy: " + site.contactName + "  phone number: " + site.contactPhone + "\n";
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



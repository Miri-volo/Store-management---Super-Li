package groupk.shared.service.dto;

public class Site {
    public Site(String contactName, String contactPhone, String area, String city, String street, int houseNumber, int floor, int apartment) {
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.area = area;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartment = apartment;
        this.floor = floor;
    }

    public String contactName;
    public String contactPhone;
    public String area;
    public String city;
    public String street;
    public int houseNumber;
    public int apartment;
    public int floor;
}

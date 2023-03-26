package groupk.logistics.DataLayer;

public class SiteDTO {
    private String contactGuy;
    private String city;
    private String phoneNumber;
    private String street;
    private int houseNumber;
    private int floor;
    private int apartment;
    private String area;

    public SiteDTO(String contactGuy, String city, String phoneNumber, String street, int houseNumber, int floor, int apartment, String area) {
        this.contactGuy = contactGuy;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.houseNumber = houseNumber;
        this.street = street;
        this.floor = floor;
        this.apartment = apartment;
        this.area = area;
    }

    public String getStreet() {
        return street;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getContactGuy() {
        return contactGuy;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public int getFloor() {
        return floor;
    }

    public int getApartment() {
        return apartment;
    }
}


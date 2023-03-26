package groupk.logistics.DataLayer;

import java.util.List;

public class DriverDTO {
    private int id;
    private List<String> licenses;

    public DriverDTO(int id, List<String> licenses) {
        this.id = id;
        this.licenses = licenses;
    }

    public int getId() {
        return id;
    }

    public List<String> getLicenses() {
        return licenses;
    }

    public void addLicense(String license) {
        if(!licenses.contains(license))
            licenses.add(license);
    }
}

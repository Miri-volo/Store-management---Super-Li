package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ContactData;

public class ContactRecord implements ContactData {
    String name;
    String address;
    String phoneNumber;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ContactRecord(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return String.format(
                "[CONTACT: %s | %s | %s]",
                name, address, phoneNumber
        );
    }


}

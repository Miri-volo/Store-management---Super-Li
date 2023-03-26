package groupk.inventory_suppliers.dataLayer.dao.records;


import groupk.inventory_suppliers.shared.dto.CreateSupplierDTO;
import groupk.inventory_suppliers.shared.utils.Utils;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.SupplierData;

import java.time.DayOfWeek;

public class SupplierRecord extends BaseRecord<Integer> implements SupplierData {
    int ppn;
    int bankNumber;
    String name;
    boolean isDelivering;
    PaymentCondition paymentCondition;
    DayOfWeek regularSupplyingDays;
    ContactRecord contact;

    public SupplierRecord(CreateSupplierDTO dto){
        this.ppn = dto.ppn;
        this.bankNumber = dto.bankNumber;
        this.name = dto.name;
        this.isDelivering = dto.isDelivering;
        this.paymentCondition = dto.paymentCondition;
        this.regularSupplyingDays = dto.regularSupplyDays;
        this.contact = new ContactRecord(dto.contactName, dto.contactAddress, dto.contactPhone);
    }

    public SupplierRecord(int ppn, int bankNumber, String name, boolean isDelivering, PaymentCondition pm,
                          DayOfWeek rsd, ContactRecord contact){
        this.ppn = ppn;
        this.bankNumber = bankNumber;
        this.name = name;
        this.isDelivering = isDelivering;
        this.paymentCondition = pm;
        this.regularSupplyingDays = rsd;
        this.contact = contact;
    }

    @Override
    public int getPpn() {
        return ppn;
    }

    @Override
    public int getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(int bankNumber) {
        this.bankNumber = bankNumber;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isDelivering() {
        return isDelivering;
    }

    public void setDelivering(boolean delivering) {
        isDelivering = delivering;
    }

    @Override
    public PaymentCondition getPaymentCondition() {
        return paymentCondition;
    }

    public void setPaymentCondition(PaymentCondition paymentCondition) {
        this.paymentCondition = paymentCondition;
    }

    @Override
    public DayOfWeek getRegularSupplyingDays() {
        return regularSupplyingDays;
    }

    public void setRegularSupplyingDays(DayOfWeek regularSupplyingDays) {
        this.regularSupplyingDays = regularSupplyingDays;
    }

    @Override
    public ContactRecord getContact() {
        return contact;
    }

    public void setContact(ContactRecord contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return Utils.table(
                2, 25, true,
                "    **** SUPPLIER **** ", "",
                "PPN", ppn,
                "Name", name,
                "Contact name", contact.name,
                "Contact phone", contact.phoneNumber,
                "Contact address", contact.address,
                "Bank #", bankNumber,
                "Delivering", isDelivering,
                "Payment condition", paymentCondition,
                "Supplies on", (regularSupplyingDays == null ? "N/A" : regularSupplyingDays)
        );
    }

    @Override
    public Integer key() {
        return ppn;
    }
}

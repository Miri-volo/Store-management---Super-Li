package groupk.inventory_suppliers.shared.dto;

import groupk.inventory_suppliers.dataLayer.dao.records.PaymentCondition;

import java.time.DayOfWeek;

public class CreateSupplierDTO {
    public final int ppn;
    public final int bankNumber;
    public final String name;
    public final boolean isDelivering;
    public final PaymentCondition paymentCondition;
    public final DayOfWeek regularSupplyDays;
    public final String contactAddress;
    public final String contactName;
    public final String contactPhone;

    public CreateSupplierDTO(int ppn, int bankNumber, String name, boolean isDelivering, PaymentCondition paymentCondition, DayOfWeek regularSupplyDays, String contactAddress, String contactName, String contactPhone) {
        this.ppn = ppn;
        this.bankNumber = bankNumber;
        this.name = name;
        this.isDelivering = isDelivering;
        this.paymentCondition = paymentCondition;
        this.regularSupplyDays = regularSupplyDays;
        this.contactAddress = contactAddress;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
    }

}

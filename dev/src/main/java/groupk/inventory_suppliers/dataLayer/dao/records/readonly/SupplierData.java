package groupk.inventory_suppliers.dataLayer.dao.records.readonly;

import groupk.inventory_suppliers.dataLayer.dao.records.PaymentCondition;

import java.time.DayOfWeek;

public interface SupplierData {
    int getPpn();

    int getBankNumber();

    String getName();

    boolean isDelivering();

    PaymentCondition getPaymentCondition();

    DayOfWeek getRegularSupplyingDays();

    ContactData getContact();
}

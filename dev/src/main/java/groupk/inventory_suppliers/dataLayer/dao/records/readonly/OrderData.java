package groupk.inventory_suppliers.dataLayer.dao.records.readonly;

import groupk.inventory_suppliers.dataLayer.dao.records.OrderType;

import java.time.LocalDate;

public interface OrderData {
    int getId();

    int getSupplierPPN();

    OrderType getOrderType();

    float getPrice();

    LocalDate getOrdered();

    LocalDate getProvided();
}

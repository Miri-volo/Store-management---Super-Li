package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.dataLayer.dao.records.readonly.OrderData;

import java.time.LocalDate;

public class OrderRecord extends BaseRecord<Integer> implements OrderData {

    LocalDate ordered;
    LocalDate provided;
    float price;
    public final int ppn;
    public final int id;

    public OrderRecord(int id, int ppn,  float price, LocalDate ordered, LocalDate provided){
        this.id = id;
        this.ppn = ppn;
        this.ordered = ordered;
        this.price = price;
        this.provided = provided;
    }

    @Override
    public OrderType getOrderType() {
        return null;
    }

    public void setOrdered(LocalDate ordered) {
        this.ordered = ordered;
    }

    public void setProvided(LocalDate provided) {
        this.provided = provided;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public LocalDate getOrdered() {
        return ordered;
    }

    @Override
    public LocalDate getProvided() {
        return provided;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getSupplierPPN() {
        return ppn;
    }

    @Override
    public Integer key() {
        return id;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

package groupk.shared.business.Inventory;

import groupk.inventory_suppliers.dataLayer.dao.records.readonly.DiscountPairData;

import java.time.LocalDate;

public class DiscountPair {
    private int id;
    private LocalDate start_date;
    private LocalDate end_date;
    private double discount;

    //CONSTRUCTORS
    public DiscountPair(DiscountPairData dp) {
        this.id=dp.getDiscountPairId();
        this.start_date = dp.getStartDate().toLocalDate();
        this.end_date = dp.getEndDate().toLocalDate();
        this.discount = dp.getDiscount();
    }

    //getters and setters
    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public int getId() {
        return id;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}

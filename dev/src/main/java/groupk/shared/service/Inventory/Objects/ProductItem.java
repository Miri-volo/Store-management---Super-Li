package groupk.shared.service.Inventory.Objects;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductItem {
    public int getId() {
        return id;
    }

    private final int product_id;
    private final int id;
    private final String store;
    private final String location;
    private final int supplier;
    private final LocalDate expirationDate;
    private final List<DiscountPair> cus_discount;
    private final boolean is_defect;
    private final String defect_reporter;
    private final String productName;


    public ProductItem(groupk.shared.business.Inventory.ProductItem PItem) {
        product_id = PItem.getProduct_id();
        id = PItem.getId();
        store = PItem.getStore();
        productName = PItem.getProductName();
        location = PItem.getLocation();
        supplier = PItem.getSupplier();
        expirationDate = PItem.getExpirationDate();
        is_defect = PItem.is_defect();
        defect_reporter = PItem.getDefect_reporter();
        cus_discount = new ArrayList<>();
        List<groupk.shared.business.Inventory.DiscountPair> BusinessCus_discount = PItem.getCus_discount();
        for (groupk.shared.business.Inventory.DiscountPair dp : BusinessCus_discount)
            cus_discount.add(new DiscountPair(dp));
    }

    public String getProductName() {
        return productName;
    }

    public int getSupplier() {
        return supplier;
    }

    @Override
    public String toString() {
        return
                "Product ID & Name:\t" + product_id + " | " + productName +
                        "ID:\t" + id +
                        "Store:\t" + store +
                        "Location:\t" + location +
                        "Supplier PPN:\t" + supplier +
                        "Expiration date:\t" + expirationDate
                ;
    }
}

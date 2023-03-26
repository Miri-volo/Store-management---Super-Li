package groupk.shared.business.Inventory;

import groupk.inventory_suppliers.dataLayer.dao.PersistenceController;
import groupk.inventory_suppliers.dataLayer.dao.records.DiscountPairRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.DiscountPairData;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ProductItemData;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductItem {
    private final int product_id;
    private final int id;

    private int discount_ids;
    private final String store;
    private String location;
    private int supplier;
    private final LocalDate expirationDate;
    private final List<DiscountPair> cus_discount;
    private boolean is_defect;
    private boolean on_shelf;
    private String defect_reporter;
    private String productName;

    private final PersistenceController pc;

    //CONSTRUCTORS
    public ProductItem(ProductItemData productItem, PersistenceController pc) {
        product_id = productItem.getProductId();
        id = productItem.getId();
        store = productItem.getStore();
        location = productItem.getLocation();
        supplier = productItem.getSupplier();
        expirationDate = productItem.getExpirationDate().toLocalDate();
        cus_discount = new ArrayList<>();
        pc.getDiscountPairs().all().filter(dp -> dp.getProductId() == product_id && dp.getProductItemId() == id).forEach(this::addFromExisting);
        is_defect = productItem.getIsDefect();
        on_shelf = productItem.getOnShelf();
        defect_reporter = productItem.getDefectReporter();
        this.pc = pc;
    }

    //METHODS
    public void addCusDiscount(LocalDate start_date, LocalDate end_date, float discount) throws Exception {
        DiscountPairData r = pc.getDiscountPairs().create(
                product_id,
                id,
                discount_ids,
                java.sql.Date.valueOf(start_date),
                java.sql.Date.valueOf(end_date),
                discount
        );
        cus_discount.add(new DiscountPair(r));
        setDiscount_ids(discount_ids+1);
    }

    public double calculateDiscount() {
        Iterator<DiscountPair> i = cus_discount.iterator();
        double discount = 1;
        while (i.hasNext()) {
            DiscountPair pair = i.next();
            boolean between_dates = pair.getStart_date().isBefore(LocalDate.now()) && pair.getEnd_date().isAfter(LocalDate.now());
            boolean start_today = LocalDate.now().equals(pair.getStart_date());
            boolean end_today = LocalDate.now().equals(pair.getEnd_date());
            if (between_dates || start_today || end_today)
                discount *= (1 - pair.getDiscount());
            else
                i.remove();
        }
        return discount;
    }

    //GETTERS AND SETTERS
    public int getProduct_id() {
        return product_id;
    }

    public void setDiscount_ids(int discount_ids) throws Exception {
        int r = pc.getProductItems().updateDiscountIds(product_id, id, discount_ids);
        if (r == -1)
            throw new Exception("Error setting storage_qty in DAL");
        this.discount_ids = discount_ids;
    }

    public int getId() {
        return id;
    }

    public String getStore() {
        return store;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) throws Exception {
        int r = pc.getProductItems().updateItemLocation(product_id, id, location);
        if (r == -1)
            throw new Exception("Error setting location in DAL");
        this.location = location;
    }

    public int getSupplier() {
        return supplier;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public List<DiscountPair> getCus_discount() {
        return cus_discount;
    }

    public boolean is_defect() {
        return is_defect;
    }

    public void setIs_defect(boolean is_defect) throws Exception {
        int r = pc.getProductItems().updateIsDefect(product_id, id, is_defect);
        if (r == -1)
            throw new Exception("Error setting is_defect in DAL");
        this.is_defect = is_defect;
    }

    public String getDefect_reporter() {
        return defect_reporter;
    }

    public void setDefect_reporter(String defect_reporter) throws Exception {
        int r = pc.getProductItems().updateDefectReporter(product_id, id, defect_reporter);
        if (r == -1)
            throw new Exception("Error setting defect_reporter in DAL");
        this.defect_reporter = defect_reporter;
    }

    public boolean isOn_shelf() {
        return on_shelf;
    }

    public void setOn_shelf(boolean on_shelf) throws Exception {
        int r = pc.getProductItems().updateOnShelf(product_id, id, on_shelf);
        if (r == -1)
            throw new Exception("Error setting is_defect in DAL");
        this.on_shelf = on_shelf;
    }

    public String getProductName() {
        return productName;
    }

    private void addFromExisting(DiscountPairRecord dp) {
        cus_discount.add(new DiscountPair(dp));
    }
}

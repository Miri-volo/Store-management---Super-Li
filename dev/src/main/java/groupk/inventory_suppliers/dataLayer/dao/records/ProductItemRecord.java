package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ProductItemData;

import java.sql.Date;
import java.util.Objects;

public class ProductItemRecord extends BaseRecord<ProductItemRecord.ProductItemKey> implements ProductItemData {
    private int product_id;
    private int id;
    private String store;
    private String location;
    private int supplier;
    private Date expiration_date;
    private boolean is_defect;
    private boolean on_shelf;
    private String defect_reporter;

    public ProductItemRecord(int product_id, int id, String store, String location, int supplier, Date expiration_date, boolean is_defect, boolean on_shelf, String defect_reporter) {
        this.product_id = product_id;
        this.id = id;
        this.store = store;
        this.location = location;
        this.supplier = supplier;
        this.expiration_date = expiration_date;
        this.is_defect = is_defect;
        this.on_shelf = on_shelf;
        this.defect_reporter = defect_reporter;
    }

    @Override
    public int getProductId() {
        return product_id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getStore() {
        return store;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public int getSupplier() {
        return supplier;
    }

    @Override
    public Date getExpirationDate() {
        return expiration_date;
    }

    @Override
    public boolean getIsDefect() {
        return is_defect;
    }

    @Override
    public boolean getOnShelf() {
        return on_shelf;
    }

    @Override
    public String getDefectReporter() {
        return defect_reporter;
    }

    @Override
    public ProductItemKey key() {
        return new ProductItemKey(product_id, id);
    }

    public static class ProductItemKey {

        public final int product_id, id;

        @Override
        public int hashCode() {
            return Objects.hash(product_id, id);
        }


        public ProductItemKey(int product_id, int id) {
            this.product_id = product_id;
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ProductItemKey that = (ProductItemKey) o;
            return Objects.equals(product_id, that.product_id) &&
                    Objects.equals(id, that.id);
        }
    }
}

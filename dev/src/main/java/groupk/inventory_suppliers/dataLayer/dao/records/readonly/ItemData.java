package groupk.inventory_suppliers.dataLayer.dao.records.readonly;

public interface ItemData {
    int getSupplierPPN();

    int getCatalogNumber();

    int getProductId();

    float getPrice();
}

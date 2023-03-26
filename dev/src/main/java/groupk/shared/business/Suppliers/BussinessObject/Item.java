package groupk.shared.business.Suppliers.BussinessObject;

import groupk.shared.business.ItemController;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ItemData;
import groupk.inventory_suppliers.shared.utils.Utils;

public class Item {

    private final ItemController controller;
    private ItemData data;
    private Supplier supplier;

    public Item(ItemData data, Supplier supplier, ItemController controller) {
        this.supplier = supplier;
        this.data = data;
        this.controller = controller;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public int getCatalogNumber() {
        return data.getCatalogNumber();
    }

    public float getPrice() {
        return data.getPrice();
    }

    @Override
    public String toString() {
        return Utils.table(
                2, 25, true,
                "    **** ITEM **** ", "",
                // ----
                "Product Id", data.getProductId(),
                "Price", data.getPrice(),
                "Supplier PPN", supplier.getPpn(),
                "Supplier name", supplier.getName(),
                "Catalog number", data.getCatalogNumber()
        );
    }

    public int getProductId() {
        return data.getProductId();
    }


}

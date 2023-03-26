package groupk.shared.business.Inventory;

import groupk.shared.PresentationLayer.Suppliers.UserOutput;
import groupk.inventory_suppliers.dataLayer.dao.PersistenceController;
import groupk.inventory_suppliers.dataLayer.dao.records.DiscountPairRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.ProductItemRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ProductData;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ProductItemData;


import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Product {
    private int item_ids;
    private final int product_id;
    private final String name;
    private int shelf_qty;
    private int storage_qty;
    private float cus_price;
    private final int min_qty;
    private final Map<Integer, ProductItem> items;

    private final String cat;
    private final String sub_cat;
    private final String sub_sub_cat;

    private final PersistenceController pc;

    //CONSTRUCTORS
    public Product(ProductData product, PersistenceController pc) {
        this.pc = pc;
        item_ids = product.getItemIds();
        product_id = product.getId();
        name = product.getName();
        shelf_qty = product.getShelfQty();
        storage_qty = product.getStorageQty();
        cus_price = product.getCustomerPrice();
        min_qty = product.getMinQty();
        items = new HashMap<>();
        pc.getProductItems().all().filter(pi -> pi.getProductId() == product_id).forEach(this::addFromExisting);
        cat = product.getCategory();
        sub_cat = product.getSubcategory();
        sub_sub_cat = product.getSubSubcategory();
    }

    //METHODS
    public void updateItemCusDiscount(int item_id, float discount, LocalDate start_date, LocalDate end_date) throws Exception {
        itemExists(item_id);
        items.get(item_id).addCusDiscount(start_date, end_date, discount);
    }

    public void updateItemDefect(int id, boolean is_defect, String defect_reporter) throws Exception {
        itemExists(id);
        items.get(id).setIs_defect(is_defect);
        items.get(id).setDefect_reporter(defect_reporter);
    }

    public ProductItem addItem(String store, String location, int supplier, LocalDate expiration_date, boolean on_shelf) throws Exception {
        try {
            if (store == null || store.equals(""))
                throw new Exception("store name empty");
            if (location == null || location.equals(""))
                throw new Exception("location name empty");
            if (supplier < 0)
                throw new Exception("supplier name empty");
            if (expiration_date == null)
                throw new Exception("expiration date is null");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        ProductItemData r = pc.getProductItems().create(
                product_id,
                item_ids,
                store,
                location,
                supplier,
                Date.valueOf(expiration_date),
                false,
                on_shelf,
                ""
        );
        ProductItem pItem = new ProductItem(r, pc);
        items.put(item_ids, pItem);
        if (on_shelf)
            setShelf_qty(shelf_qty + 1);
        else
            setStorage_qty(storage_qty + 1);
        item_ids++;

        return pItem;
    }

    public boolean removeItem(int item_id) throws Exception {
        itemExists(item_id);
        for (DiscountPair dp : items.get(item_id).getCus_discount()) {
            pc.getDiscountPairs().delete(new DiscountPairRecord.DiscountPairKey(product_id, item_id, dp.getId()));
        }
        pc.getProductItems().delete(new ProductItemRecord.ProductItemKey(product_id, item_id));
        if (items.get(item_id).isOn_shelf())
            setShelf_qty(shelf_qty - 1);
        else
            setStorage_qty(storage_qty - 1);
        items.remove(item_id);
        if(shelf_qty+storage_qty<min_qty){
            UserOutput.getInstance().println("Product ID: " + product_id + " has reached below minimum quantity, creating order.");
        }
        return shelf_qty+storage_qty<min_qty;
    }

    public void changeItemLocation(int item_id, String location) throws Exception {
        itemExists(item_id);
        if (location == null || location.equals(""))
            throw new Exception("location name empty");
        items.get(item_id).setLocation(location);
    }

    public void changeItemOnShelf(int item_id, boolean on_shelf) throws Exception {
        itemExists(item_id);
        if (items.get(item_id).isOn_shelf() && !on_shelf) {
            setShelf_qty(shelf_qty - 1);
            setStorage_qty(storage_qty + 1);
            items.get(item_id).setOn_shelf(false);
        } else if (!items.get(item_id).isOn_shelf() && on_shelf) {
            setShelf_qty(shelf_qty + 1);
            setStorage_qty(storage_qty - 1);
            items.get(item_id).setOn_shelf(true);
        }
    }

    public String getItemLocation(int item_id) throws Exception {
        itemExists(item_id);
        return items.get(item_id).getLocation();
    }

    //GETTERS AND SETTERS
    public int getItem_ids() {
        return item_ids;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public int getShelf_qty() {
        return shelf_qty;
    }

    public int getStorage_qty() {
        return storage_qty;
    }

    public double getCus_price() {
        return cus_price;
    }

    public int getMin_qty() {
        return min_qty;
    }

    public Map<Integer, ProductItem> getItems() {
        return items;
    }

    public String getCat() {
        return cat;
    }

    public String getSub_cat() {
        return sub_cat;
    }

    public String getSub_sub_cat() {
        return sub_sub_cat;
    }

    public void setCus_price(float cus_price) throws Exception {
        int r = pc.getProducts().updateCusPrice(product_id, cus_price);
        if (r == -1)
            throw new Exception("Error setting shelf_qty in DAL");
        this.cus_price = cus_price;
    }

    public void setShelf_qty(int shelf_qty) throws Exception {
        int r = pc.getProducts().updateShelfQty(product_id, shelf_qty);
        if (r == -1)
            throw new Exception("Error setting shelf_qty in DAL");
        this.shelf_qty = shelf_qty;
    }

    public void setStorage_qty(int storage_qty) throws Exception {
        int r = pc.getProducts().updateStorageQty(product_id, storage_qty);
        if (r == -1)
            throw new Exception("Error setting storage_qty in DAL");
        this.storage_qty = storage_qty;
    }

    public void setItem_ids(int item_ids) throws Exception {
        int r = pc.getProducts().updateStorageQty(product_id, storage_qty);
        if (r == -1)
            throw new Exception("Error setting storage_qty in DAL");
        this.item_ids = item_ids;
    }

    //PRIVATE METHODS
    private void itemExists(int item_id) throws Exception {
        if (!items.containsKey(item_id))
            throw new Exception("item id doesn't exist");
    }

    private void addFromExisting(ProductItemRecord product_item) {
        items.put(product_item.getId(), new ProductItem(product_item, pc));
    }
}

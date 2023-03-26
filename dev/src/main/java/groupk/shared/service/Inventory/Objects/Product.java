package groupk.shared.service.Inventory.Objects;

import java.util.HashMap;
import java.util.Map;

public class Product {

    private final int item_ids;
    private final int product_id;
    private final String name;
    private final int shelf_qty;
    private final int storage_qty;
    private final double cus_price;
    private final int min_qty;
    private final Map<Integer, ProductItem> items;
    private final String cat;
    private final String sub_cat;
    private final String sub_sub_cat;

    public Product(groupk.shared.business.Inventory.Product p) {
        item_ids = p.getItem_ids();
        product_id = p.getProduct_id();
        name = p.getName();
        shelf_qty = p.getShelf_qty();
        storage_qty = p.getStorage_qty();
        cus_price = p.getCus_price();
        min_qty = p.getMin_qty();
        cat = p.getCat();
        sub_cat = p.getCat();
        sub_sub_cat = p.getSub_sub_cat();
        Map<Integer, groupk.shared.business.Inventory.ProductItem> BusinessItemsMap = p.getItems();
        items = new HashMap<>();
        for (Map.Entry<Integer, groupk.shared.business.Inventory.ProductItem> entry : BusinessItemsMap.entrySet()) {
            items.put(entry.getKey(), new ProductItem(entry.getValue()));
        }
    }

    public int getShelf_qty() {
        return shelf_qty;
    }

    public int getStorage_qty() {
        return storage_qty;
    }

    public String getCat() {
        return cat;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("product_id: " + product_id + "\n" + "product name: " + name + "\n"
                + "category: " + cat + "\n" + "sub category: " + sub_cat + "\n" +
                "sub sub category: " + sub_sub_cat + "\n" +
                "customer price: " + cus_price + "\n" +
                "minimum quantity: " + min_qty + "\n" +
                "shelf quantity: " + shelf_qty + "\n" + "storage quantity: " + storage_qty + "\n" +
                "The items id in this product are:\n");
        for (Map.Entry<Integer, ProductItem> entry : items.entrySet()) {
            s.append(entry.getValue().getId()).append("\n");
        }
        return s.toString();
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }
}

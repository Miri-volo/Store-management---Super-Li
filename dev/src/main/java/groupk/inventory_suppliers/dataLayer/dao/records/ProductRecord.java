package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ProductData;

public class ProductRecord extends BaseRecord<Integer> implements ProductData {
    public int item_ids;
    public final int id;
    private String name;
    private float customerPrice;
    private int minQty;
    private int storageQty;
    private int shelfQty;
    private String category, subcategory, subSubcategory;

    public ProductRecord(int item_ids, int id, String name, float customerPrice, int minQty, int storageQty, int shelfQty, String category, String subcategory, String subSubcategory) {
        this.item_ids = item_ids;
        this.id = id;
        this.name = name;
        this.customerPrice = customerPrice;
        this.minQty = minQty;
        this.storageQty = storageQty;
        this.shelfQty = shelfQty;
        this.category = category;
        this.subcategory = subcategory;
        this.subSubcategory = subSubcategory;
    }

    @Override
    public int getItemIds() {
        return item_ids;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public float getCustomerPrice() {
        return customerPrice;
    }

    public void setCustomerPrice(float customerPrice) {
        this.customerPrice = customerPrice;
    }

    @Override
    public int getMinQty() {
        return minQty;
    }

    public void setMinQty(int minQty) {
        this.minQty = minQty;
    }

    @Override
    public int getStorageQty() {
        return storageQty;
    }

    public void setStorageQty(int storageQty) {
        this.storageQty = storageQty;
    }

    @Override
    public int getShelfQty() {
        return shelfQty;
    }

    public void setShelfQty(int shelfQty) {
        this.shelfQty = shelfQty;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    @Override
    public String getSubSubcategory() {
        return subSubcategory;
    }

    public void setSubSubcategory(String subSubcategory) {
        this.subSubcategory = subSubcategory;
    }

    @Override
    public Integer key() {
        return id;
    }
}

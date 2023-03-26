package groupk.shared.business;

import groupk.inventory_suppliers.dataLayer.dao.records.OrderMapRecord;
import groupk.shared.business.Inventory.Product;
import groupk.shared.business.Inventory.ProductItem;
import groupk.inventory_suppliers.dataLayer.dao.PersistenceController;
import groupk.inventory_suppliers.dataLayer.dao.records.ProductRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ProductData;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ProductController {
    public static final String BRANCH_NAME = "Tel Aviv";

    private final Map<Integer, Product> products;
    private final Map<Integer, Map<Integer, Integer>> orders;
    private final CategoryController category_controller;
    private final PersistenceController pc;


    //constructors
    public ProductController(PersistenceController pc, CategoryController category_controller) {
        products = new HashMap<>();
        orders = new HashMap<>();
        this.pc = pc;
        this.category_controller = category_controller;
        pc.getProducts().all().forEach(this::addFromExisting);
        pc.getOrderMaps().all().forEach(this::addFromExisting);
    }

    public void updateCategoryCusDiscount(float discount, LocalDate start_date, LocalDate end_date, String category, String sub_category, String subsub_category) throws Exception {
        if (category != null && !category.equals("")) {
            if (sub_category != null && !sub_category.equals("")) {
                if (subsub_category != null && !subsub_category.equals("")) {
                    for (Product p : products.values())
                        //for sub-sub-category
                        if (p.getCat().equals(category) && p.getSub_cat().equals(sub_category) && p.getSub_sub_cat().equals(subsub_category))
                            for (int i : p.getItems().keySet())
                                p.updateItemCusDiscount(i, discount, start_date, end_date);
                } else
                    for (Product p : products.values())
                        //for sub-category
                        if (p.getCat().equals(category) && p.getSub_cat().equals(sub_category))
                            for (int i : p.getItems().keySet())
                                p.updateItemCusDiscount(i, discount, start_date, end_date);
            } else
                for (Product p : products.values())
                    //for category
                    if (p.getCat().equals(category))
                        for (int i : p.getItems().keySet())
                            p.updateItemCusDiscount(i, discount, start_date, end_date);
        } else {
            if (sub_category == null || sub_category.equals("") && subsub_category == null || subsub_category.equals(""))
                for (Product p : products.values())
                    //for all
                    for (int i : p.getItems().keySet())
                        p.updateItemCusDiscount(i, discount, start_date, end_date);
            else throw new Exception("missing category input");
        }
    }

    public void updateProductCusDiscount(float discount, LocalDate start_date, LocalDate end_date, int product_id) throws Exception {
        productExists(product_id);
        for (int i : products.get(product_id).getItems().keySet())
            products.get(product_id).updateItemCusDiscount(i, discount, start_date, end_date);
    }

    public void updateItemCusDiscount(int product_id, int item_id, float discount, LocalDate start_date, LocalDate end_date) throws Exception {
        discountLegal(discount);
        checkDates(start_date, end_date);
        productExists(product_id);
        products.get(product_id).updateItemCusDiscount(item_id, discount, start_date, end_date);
    }

    public void updateProductCusPrice(int product_id, float price) throws Exception {
        priceLegal(price);
        productExists(product_id);
        products.get(product_id).setCus_price(price);
    }

    public void updateItemDefect(int product_id, int item_id, boolean is_defect, String defect_reporter) throws Exception {
        if (defect_reporter == null) throw new Exception("defect reporter is null");
        if (defect_reporter.equals("")) throw new Exception("defect reporter is empty");
        productExists(product_id);
        products.get(product_id).updateItemDefect(item_id, is_defect, defect_reporter);
    }

    public String getItemLocation(int product_id, int item_id) throws Exception {
        productExists(product_id);
        return products.get(product_id).getItemLocation(item_id);
    }

    public int getMinQty(int product_id) throws Exception {
        productExists(product_id);
        return products.get(product_id).getMin_qty();
    }

    public Product addProduct(String name, float cus_price, int min_qty, int supply_time, String category, String sub_category, String subsub_category) throws Exception {
        try {
            if (!category_controller.getCategories().containsKey(category))
                throw new Exception("category doesn't exist");
            if (!category_controller.getCategories().get(category).getSubC().containsKey(sub_category))
                throw new Exception("sub-category doesn't exist");
            if (!category_controller.getCategories().get(category).getSubC().get(sub_category).getSubSubCategories().containsKey(subsub_category))
                throw new Exception("sub-sub-category doesn't exist");
            if (name == null || name.equals(""))
                throw new Exception("product name empty");
            priceLegal(cus_price);
            if (min_qty < 0)
                throw new Exception("min quantity smaller than 0");
            if (supply_time < 0)
                throw new Exception("supply time smaller than 0");
            if (category == null || category.equals(""))
                throw new Exception("category name empty");
            if (sub_category == null || sub_category.equals(""))
                throw new Exception("sub_category name empty");
            if (subsub_category == null || subsub_category.equals(""))
                throw new Exception("sub-sub-category name empty");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        int id = pc.getProducts().getMaxId() + 1;
        ProductData r = pc.getProducts().create(
                0,
                id,
                name,
                cus_price,
                min_qty,
                0,
                0,
                category,
                sub_category,
                subsub_category
        );
        Product ret = new Product(r, pc);
        products.put(id, ret);
        return ret;
    }

    public void removeProduct(int product_id) throws Exception {
        productExists(product_id);
        for (int pi : products.get(product_id).getItems().keySet())
            removeItem(product_id, pi);
        int r = pc.getProducts().runDeleteQuery(product_id);
        if (r == -1)
            throw new Exception("Error deleting Product from DB");
        products.remove(product_id);
    }

    public ProductItem addItem(int product_id, String store, String location, int supplier, LocalDate expiration_date, boolean on_shelf) throws Exception {
        productExists(product_id);
        return products.get(product_id).addItem(store, location, supplier, expiration_date, on_shelf);
    }

    public boolean removeItem(int product_id, int item_id) throws Exception {
        productExists(product_id);
        return products.get(product_id).removeItem(item_id);
    }

    public void changeItemLocation(int product_id, int item_id, String location) throws Exception {
        productExists(product_id);
        products.get(product_id).changeItemLocation(item_id, location);
    }

    public void changeItemOnShelf(int product_id, int item_id, boolean on_shelf) throws Exception {
        productExists(product_id);
        products.get(product_id).changeItemOnShelf(item_id, on_shelf);
    }

    public Product getProduct(int id) {
        return products.get(id);
    }

    //FOR REPORTS
    public List<Product> getMissingProducts() {
        List<Product> missing_products = new ArrayList<>();
        for (Product p : products.values())
            if (p.getItems().size() < p.getMin_qty())
                missing_products.add(p);
        return missing_products;
    }

    public List<Product> getSurplusProducts() {
        List<Product> missing_products = new ArrayList<>();
        for (Product p : products.values())
            if (p.getItems().size() >= p.getMin_qty()) missing_products.add(p);
        return missing_products;
    }

    public List<ProductItem> getExpiredItems() {
        List<ProductItem> expired_items = new ArrayList<>();
        for (Product p : products.values())
            for (ProductItem i : p.getItems().values())
                if (i.getExpirationDate().isBefore(LocalDate.now())) expired_items.add(i);
        return expired_items;
    }

    public List<ProductItem> getDefectiveItems() {
        List<ProductItem> defective_items = new ArrayList<>();
        for (Product p : products.values())
            for (ProductItem i : p.getItems().values())
                if (i.is_defect()) defective_items.add(i);
        return defective_items;
    }

    public List<ProductItem> getItemsBySupplier(int supplier) {
        List<ProductItem> items = new ArrayList<>();
        for (Product p : products.values())
            for (ProductItem i : p.getItems().values())
                if (i.getSupplier() == supplier) items.add(i);
        return items;
    }

    public List<ProductItem> getItemsByProduct(String name) {
        List<ProductItem> items = new ArrayList<>();
        for (Product p : products.values())
            if (p.getName().equals(name))
                items = new ArrayList<>(p.getItems().values());
        return items;
    }

    public List<Product> getItemsByCategory(String category, String subCategory, String subSubCategory) {
        List<Product> ret_products = new ArrayList<>();
        if (category != null && !category.equals("")) {
            if (subCategory != null && !subCategory.equals("")) {
                if (subSubCategory != null && !subSubCategory.equals("")) {
                    for (Product p : products.values())
                        if (p.getCat().equals(category) && p.getSub_cat().equals(subCategory) && p.getSub_sub_cat().equals(subSubCategory))
                            ret_products.add(p);
                } else for (Product p : products.values())
                    if (p.getCat().equals(category) && p.getSub_cat().equals(subCategory)) ret_products.add(p);
            } else for (Product p : products.values())
                if (p.getCat().equals(category)) ret_products.add(p);
        } else ret_products = new ArrayList<>(products.values());
        return ret_products;
    }

    public boolean productsInCategory(String category) {
        for (Product p : products.values())
            if (p.getCat().equals(category)) return false;
        return true;
    }

    public boolean productsInSubCategory(String category, String sub_category) {
        for (Product p : products.values())
            if (p.getCat().equals(category) && p.getSub_cat().equals(sub_category)) return false;
        return true;
    }

    public boolean productsInSubSubCategory(String category, String sub_category, String sub_sub_category) {
        for (Product p : products.values())
            if (p.getCat().equals(category) && p.getSub_cat().equals(sub_category) && p.getSub_sub_cat().equals(sub_sub_category))
                return false;
        return true;
    }

    public void addProductToOrder(int order_id, int product_id, int amount) {
        if (!orders.containsKey(order_id))
            orders.put(order_id, new HashMap<>());
        orders.get(order_id).put(product_id, amount);
        pc.getOrderMaps().create(order_id, product_id, amount);
    }

    public void addOrderRecord(int orderId, Map<Integer, Integer> productAmount) {
        for (Map.Entry<Integer, Integer> pair : productAmount.entrySet())
            addProductToOrder(orderId, pair.getKey(), pair.getValue());
    }

    public Map<Integer, Integer> confirmOrder(int order_id) {
        return orders.get(order_id);
    }

    public List<ProductItem> confirmOrderAmount(int order_id, Map<Integer, Integer> actual_amount, int supplier_ppn) throws Exception {
        List<ProductItem> items = new ArrayList<>();
        for (Map.Entry<Integer, Integer> pair : actual_amount.entrySet()) {
            int product_id = pair.getKey(), amount = pair.getValue();
            for (int i = 0; i < amount; i++)
                items.add(randomizeProductItem(product_id, supplier_ppn));
            if (amount < orders.get(order_id).get(product_id))
                updateOrderAmount(order_id, product_id, orders.get(order_id).get(product_id) - amount);
            else
                deleteProductFromOrder(order_id, product_id);
        }
        return items;
    }

    private void updateOrderAmount(int order_id, int product_id, int new_amount) {
        orders.get(order_id).put(product_id, new_amount);
        pc.getOrderMaps().update(new OrderMapRecord.OrderMapKey(order_id, product_id), new_amount);
    }

    private void deleteProductFromOrder(int order_id, int product_id) {
        orders.get(order_id).remove(product_id);
        pc.getOrderMaps().runDeleteQuery(new OrderMapRecord.OrderMapKey(order_id, product_id));
    }


    //getters and setters

    //private methods
    public void productExists(int product_id) throws Exception {
        if (!products.containsKey(product_id)) throw new Exception("product id does not exist");
    }

    private void checkDates(LocalDate start_date, LocalDate end_date) throws Exception {
        if (start_date == null) throw new Exception("start date is null");
        if (end_date == null) throw new Exception("end date is null");
        if (end_date.isBefore(start_date)) throw new Exception("end date earlier than start date");
        if (end_date.isBefore(LocalDate.now())) throw new Exception("end date has passed");
    }

    private void discountLegal(double discount) throws Exception {
        if (discount > 1 || discount <= 0) throw new Exception("discount percentage illegal");
    }

    private void priceLegal(double price) throws Exception {
        if (price < 0) throw new Exception("price illegal");
    }

    private void addFromExisting(ProductRecord product) {
        products.put(product.getId(), new Product(product, pc));
    }

    private void addFromExisting(OrderMapRecord product_order) {
        if (!orders.containsKey(product_order.getOrder_id()))
            orders.put(product_order.getOrder_id(), new HashMap<>());
        orders.get(product_order.getOrder_id()).put(product_order.getProduct_id(), product_order.getAmount());
    }

    public List<String> getProductNames() {
        List<String> ProductIdes = new LinkedList<>();
        for (Map.Entry<Integer, Product> entry : products.entrySet()) {
            ProductIdes.add(entry.getValue().getName());
        }
        return ProductIdes;
    }

    public int getMinAmount(String proName) {
        Product p = products.get(proName);
        return p.getMin_qty();
    }

    public Map<String, Integer> getDeficiency() {
        Map<String, Integer> deficiency = new HashMap<>();
        for (Product p : products.values()) {
            if (p.getMin_qty() > p.getItems().size()) deficiency.put(p.getName(), p.getMin_qty());
        }
        return deficiency;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products.values());
    }

    private ProductItem randomizeProductItem(int product_id, int supplier_ppn) throws Exception {
        String random_location = "Shelf " + new Random().nextInt(10);
        LocalDate random_date = LocalDate.now().plusDays(new Random().nextInt(14));
        boolean random_onShelf = new Random().nextBoolean();
        return addItem(product_id, BRANCH_NAME, random_location, supplier_ppn, random_date, random_onShelf);
    }
}

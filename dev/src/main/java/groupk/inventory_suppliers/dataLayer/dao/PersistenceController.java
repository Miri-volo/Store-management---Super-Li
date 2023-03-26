package groupk.inventory_suppliers.dataLayer.dao;

import groupk.inventory_suppliers.dataLayer.dao.records.OrderMapRecord;

import java.sql.Connection;

public class PersistenceController {
    private final Connection conn;

    private final CategoryDAO categories;
    private final SubcategoryDAO subcategories;
    private final SubSubcategoryDAO subSubCategories;
    private final SupplierDAO suppliers;
    private final ItemDAO items;
    private final ProductDAO products;
    private final ProductItemDAO productItems;
    private final DiscountPairDAO discountPairs;
    private final QuantityDiscountDAO quantityDiscounts;
    private final OrderDAO orders;
    private final ItemInOrderDAO itemInOrder;
    private final ReportDAO reports;
    private final ProductInReportDAO productsInReports;
    private final ProductItemInReportDAO productItemsInReports;
    private final OrderMapDAO orderMaps;

    public PersistenceController(Connection conn) {
        this.conn = conn;
        categories = new CategoryDAO(conn);
        subcategories = new SubcategoryDAO(conn);
        subSubCategories = new SubSubcategoryDAO(conn);
        products = new ProductDAO(conn);
        items = new ItemDAO(conn);
        suppliers = new SupplierDAO(conn);
        quantityDiscounts = new QuantityDiscountDAO(conn);
        orders = new OrderDAO(conn);
        itemInOrder = new ItemInOrderDAO(conn);
        productItems = new ProductItemDAO(conn);
        discountPairs = new DiscountPairDAO(conn);
        reports = new ReportDAO(conn);
        productsInReports = new ProductInReportDAO(conn);
        productItemsInReports = new ProductItemInReportDAO(conn);
        orderMaps = new OrderMapDAO(conn);
    }

    public Connection getConn() {
        return conn;
    }

    public CategoryDAO getCategories() {
        return categories;
    }

    public SubcategoryDAO getSubcategories() {
        return subcategories;
    }

    public SubSubcategoryDAO getSubSubCategories() {
        return subSubCategories;
    }

    public SupplierDAO getSuppliers() {
        return suppliers;
    }

    public ItemDAO getItems() {
        return items;
    }

    public ProductDAO getProducts() {
        return products;
    }

    public DiscountPairDAO getDiscountPairs() {
        return discountPairs;
    }

    public QuantityDiscountDAO getQuantityDiscounts() {
        return quantityDiscounts;
    }

    public ProductItemDAO getProductItems() {
        return productItems;
    }

    public OrderDAO getOrders() {
        return orders;
    }

    public ItemInOrderDAO getItemsInOrders() {
        return itemInOrder;
    }

    public ReportDAO getReports() {
        return reports;
    }

    public ProductInReportDAO getProductsInReports() {
        return productsInReports;
    }

    public ProductItemInReportDAO getProductItemsInReports() {
        return productItemsInReports;
    }

    public OrderMapDAO getOrderMaps() {
        return orderMaps;
    }
}

package groupk.shared.business.Inventory;

import groupk.inventory_suppliers.dataLayer.dao.PersistenceController;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ReportData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Report {

    public enum report_type {
        byCategory,
        byProduct,
        bySupplier,
        Defective,
        Expired,
        Missing,
        Surpluses,
    }

    private final Integer id;
    private final String name;
    private final LocalDate date;
    private final String report_producer;
    private final report_type reportType;
    private final List<Product> productList;
    private final List<ProductItem> productItemList;

    public Report(ReportData report, PersistenceController pc) {
        this.name = report.getName();
        this.id = report.getId();
        this.date = report.getDate().toLocalDate();
        this.reportType = report_type.values()[report.getType()];
        this.report_producer = report.getReportProducer();
        this.productList = new ArrayList<>();
        this.productItemList = new ArrayList<>();
    }

    public void addProduct(Product p) {
        productList.add(p);
    }

    public void addProductItem(ProductItem pi) {
        productItemList.add(pi);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public List<ProductItem> getProductItemList() {
        return productItemList;
    }

    public report_type getReportType() {
        return reportType;
    }

    public String getReport_producer() {
        return report_producer;
    }

}

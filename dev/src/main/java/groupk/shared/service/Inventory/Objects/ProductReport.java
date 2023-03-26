package groupk.shared.service.Inventory.Objects;

import groupk.shared.business.Inventory.Product;

import java.util.List;

public class ProductReport extends Report {
    List<groupk.shared.service.Inventory.Objects.Product> productList;

    public ProductReport(groupk.shared.business.Inventory.Report report) {
        super(report);
        List<groupk.shared.business.Inventory.Product> BusinessProductList = report.getProductList();
        for (Product p : BusinessProductList) {
            productList.add(new groupk.shared.service.Inventory.Objects.Product(p));
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder("Id: " + id + "\n" + "name: " + name + "\n" + "date: " + date + "\n" + "report_producer: " + report_producer + "\n");
        switch (reportType) {
            case byCategory:
                return s + "The products in " + productList.get(0).getCat() + " category are:" + "\n" + productList();
            case Surpluses:
                return s + "The surpluses products items are:\n" + productList();
            case Missing: {
                s.append("The missing products items are:\n");
                for (groupk.shared.service.Inventory.Objects.Product p : productList) {
                    s.append(p.getName()).append(", current Shelf_qty: ").append(p.getShelf_qty()).append(", current Storage_qty: ").append(p.getStorage_qty()).append("\n");
                }
                return s.toString();
            }
        }
        return null;
    }

    private String productList() {
        StringBuilder s = new StringBuilder();
        for (groupk.shared.service.Inventory.Objects.Product p : productList) {
            s.append(p.getName()).append("\n");
        }
        return s.toString();
    }

}

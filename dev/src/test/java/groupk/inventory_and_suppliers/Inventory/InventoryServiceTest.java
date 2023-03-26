package groupk.inventory_and_suppliers.Inventory;

import groupk.inventory_and_suppliers.InventorySuppliersTestsBase;

import groupk.shared.business.Facade;
import groupk.shared.business.Inventory.Categories.Category;
import groupk.shared.service.Inventory.Objects.Report;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static groupk.CustomAssertions.*;

class InventoryServiceTest extends InventorySuppliersTestsBase {

    @Test
    void addCategory() {
       Assertions.assertTrue(service.getCategoriesNames().data.isEmpty());
        assertSuccess(service.addCategory("Dairy Products"));
        Assertions.assertTrue(service.getCategoriesNames().data.contains("Dairy Products"));
        assertFailure(service.addCategory("Dairy Products"));
    }

    @Test
    void removeCategory() {
        service.addCategory("Dairy Products");
        service.removeCategory("Dairy Products");
        Assertions.assertFalse(service.getCategoriesNames().data.contains("Dairy Products"));
        assertFailure(service.removeCategory("Dairy Products"));
    }

    @Test
    void getCategory() {
        try {
            Facade.SI_Response dairy_products = service.addCategory("Dairy Products");
            Category category = assertSuccess(service.getCategory("Dairy Products"));
            Assertions.assertEquals(category.getName(), "Dairy Products");
            service.removeCategory("Dairy Products");
            service.getCategory("Dairy Products");
        } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "Category doesn't exists");
        }
    }

    @Test
    void addProduct() {
            Assertions.assertTrue(service.getProductNames().data.isEmpty());
            initCategories();
            service.addProduct("Milk", "Tnova", 4, 5.9f, 350, 6,
                    "Dairy Products","Milks","Cow Milk");
            Assertions.assertTrue(service.getProductNames().data.contains("Milk"));
            assertFailure(service.addProduct("", "Tnova", 4, 5.9f, 350, 6,
                    "Dairy Products", "Milks", "Cow Milk"));

    }

    private void initCategories() {
        service.addCategory("Dairy Products");
        service.addSubCategory("Dairy Products", "Milks");
        service.addSubSubCategory("Dairy Products", "Milks", "Cow Milk");
    }

    @Test
    void removeProduct() {
            initCategories();
            String productName = service.addProduct("Milk", "Tnova", 4, 5.9f,
                    350, 6, "Dairy Products","Milks","Cow Milk")
                    .data.getName();
            Assertions.assertTrue(service.getProductNames().data.contains("Milk"));
            service.removeProduct(1);
            Assertions.assertFalse(service.getProductNames().data.contains("Milk"));
            assertFailure(service.removeProduct(0));

    }

    @Test
    void removeReport() {
        Facade.ResponseT<Report> missingReport = service.createMissingReport("MissingReport", "Michel");
            int reportId = assertSuccess(missingReport).getId();
            Assertions.assertTrue(service.getReportListIds().data.contains(reportId));
            Assertions.assertTrue(service.removeReport(reportId).success);
            Assertions.assertFalse(service.getProductNames().data.contains(reportId));
            Assertions.assertFalse(service.removeProduct(reportId).success);
    }

    @Test
    void getReport() {
            Integer id = assertSuccess(
                    service.createMissingReport("MissingReport", "Michel")
            ).getId();
            Report report = assertSuccess(service.getReport(id));
            Assertions.assertEquals(report.getName(), "MissingReport");
            service.removeReport(id);
            assertFailure(service.getReport(id));

    }

    @Test
    void createMissingReport() {
           service.createMissingReport("MissingReport", "Michel");
            
            Assertions.assertTrue(service.getReportListIds().data.contains(1));
            service.createMissingReport("MissingReport", "Michel");
            service.removeReport(1);
            assertSuccess(service.createMissingReport("MissingReport", "Michel"));

    }

    @Test
    void createExpiredReport() {
            Integer id = service.createExpiredReport("ExpiredReport", "Michel").data.getId();
            Assertions.assertTrue(service.getReportListIds().data.contains(id));
            service.removeReport(id);
            assertSuccess(service.createExpiredReport("ExpiredReport", "Michel"));

    }

    @Test
    void createBySupplierReport() {
           assertSuccess(service.createBySupplierReport("BySupplierReport", "Michel", 0));
            Assertions.assertTrue(service.getReportListIds().data.contains(1));
            assertSuccess(service.createBySupplierReport("MissingReport", "Michel", 0));
            assertSuccess(service.removeReport(1));
            assertSuccess(service.createBySupplierReport("ExpiredReport", "Michel", 0));
    }
}
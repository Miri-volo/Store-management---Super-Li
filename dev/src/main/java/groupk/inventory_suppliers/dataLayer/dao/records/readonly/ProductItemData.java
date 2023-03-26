package groupk.inventory_suppliers.dataLayer.dao.records.readonly;

import java.sql.Date;

/**
 * "ProductId" INTEGER NOT NULL,
 * "Id" NUMERIC NOT NULL,
 * "Store" TEXT NOT NULL,
 * "Location" TEXT NOT NULL,
 * "Supplier" INTEGER NOT NULL,
 * "ExpirationDate" DATETIME NOT NULL,
 * "IsDefect" TINYINT NOT NULL,
 * "OnShelf" TINYINT NOT NULL,
 * "DefectReporter" TEXT,
 */
public interface ProductItemData {
    int getProductId();
    int getId();
    String getStore();
    String getLocation();
    int getSupplier();
    Date getExpirationDate();
    boolean getIsDefect();
    boolean getOnShelf();
    String getDefectReporter();
}

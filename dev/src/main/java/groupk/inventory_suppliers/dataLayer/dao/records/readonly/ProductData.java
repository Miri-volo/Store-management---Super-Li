package groupk.inventory_suppliers.dataLayer.dao.records.readonly;

/**
 * "itemIds" INTEGER NOT NULL,
 * "id"	INTEGER NOT NULL,
 * 	"name"	TEXT NOT NULL,
 * 	"customerPrice"	REAL NOT NULL,
 * 	"minQty"	INTEGER NOT NULL,
 * 	"storageQty"	INTEGER NOT NULL,
 * 	"shelfQty"	INTEGER NOT NULL,
 * 	"subSubCategoryName"	TEXT NOT NULL,
 * 	"subSubCategorySubcategory"	TEXT NOT NULL,
 * 	"subSubCategoryCategory"	TEXT NOT NULL,
 */
public interface ProductData {
    int getItemIds();
    int getId();
    String getName();
    float getCustomerPrice();
    int getMinQty();
    int getStorageQty();
    int getShelfQty();
    String getSubSubcategory();
    String getSubcategory();
    String getCategory();
}

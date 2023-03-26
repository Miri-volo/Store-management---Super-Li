package groupk.shared.business.Inventory.Categories;

import groupk.inventory_suppliers.dataLayer.dao.records.SubSubCategoryRecord;

public class SubSubCategory {

    private String name;

    //CONSTRUCTORS
    public SubSubCategory(String subSubCatName) {
        name = subSubCatName;
    }

    public SubSubCategory(SubSubCategoryRecord sub_sub_category) {
        this.name = sub_sub_category.getName();
    }

    //GETTERS AND SETTERS
    public String getName() {
        return name;
    }
}

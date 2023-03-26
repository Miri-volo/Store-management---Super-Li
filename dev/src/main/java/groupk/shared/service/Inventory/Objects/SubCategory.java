package groupk.shared.service.Inventory.Objects;

import groupk.shared.business.Inventory.Categories.SubSubCategory;

import java.util.HashMap;
import java.util.Map;

public class SubCategory {
    protected Map<String, groupk.shared.service.Inventory.Objects.SubSubCategory> subSubCategories;
    public String name;

    public SubCategory(groupk.shared.business.Inventory.Categories.SubCategory subCategory) {
        name = subCategory.getName();
        subSubCategories = new HashMap<>();
        Map<String, SubSubCategory> BusinessSubSubCategories = subCategory.getSubSubCategories();
        for (Map.Entry<String, SubSubCategory> entry : BusinessSubSubCategories.entrySet()) {
            groupk.shared.service.Inventory.Objects.SubSubCategory cat = new groupk.shared.service.Inventory.Objects.SubSubCategory(entry.getValue());
            subSubCategories.put(entry.getKey(), cat);
        }
    }
}

package groupk.shared.business.Inventory.Categories;

import groupk.inventory_suppliers.dataLayer.dao.PersistenceController;
import groupk.inventory_suppliers.dataLayer.dao.records.CategoryRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.SubcategoryRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.SubcategoryData;


import java.util.HashMap;
import java.util.Map;

public class Category {

    private Map<String, SubCategory> subC;
    private final String name;
    private final PersistenceController pc;

    //CONSTRUCTORS

    //from addCategory
    public Category(String name, PersistenceController pc) {
        this.name = name;
        subC = new HashMap<>();
        this.pc = pc;
    }

    //from DAL
    public Category(CategoryRecord category, PersistenceController pc) {
        this.name = category.getName();
        this.pc = pc;
        subC = new HashMap<>();
        pc.getSubcategories().all().filter(c -> c.getCategory().equals(category.getName())).forEach(this::addFromExisting);
    }

    //METHODS
    public void addSubCategory(String name) throws Exception {
        if (subC.containsKey(name))
            throw new IllegalArgumentException("The SubCategory already exists in the system");
        SubcategoryData r = pc.getSubcategories().create(this.name, name);
        subC.put(name, new SubCategory(name, pc));
    }

    public void removeSubCategory(String name) throws Exception {
        subCategoryExists(name);
        int r = pc.getSubcategories().runDeleteQuery(this.name, name);
        if (r == -1)
            throw new Exception("Error deleting subCategory from DB");
        for (String ssc : subC.get(name).getSubSubCategories().keySet())
            subC.get(name).removeSubSubCategory(this.name, ssc);
        subC.remove(name);
    }

    //GETTERS AND SETTERS
    public SubCategory getSubCategory(String name) {
        subCategoryExists(name);
        return subC.get(name);
    }

    public Map<String, SubCategory> getSubC() {
        return subC;
    }

    public String getName() {
        return name;
    }

    //PRIVATE METHODS
    private void subCategoryExists(String name) {
        if (!subC.containsKey(name))
            throw new IllegalArgumentException("Category doesn't exists: " + name);
    }

    private void addFromExisting(SubcategoryRecord sub_category) {
        subC.put(sub_category.getName(), new SubCategory(sub_category, pc));
    }
}


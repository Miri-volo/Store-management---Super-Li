package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.dataLayer.dao.records.readonly.SubSubcategoryData;

import java.util.Objects;

public class SubSubCategoryRecord extends BaseRecord<SubSubCategoryRecord.SubSubcategoryKey> implements SubSubcategoryData {
    private String name, category, subcategory;

    public SubSubCategoryRecord(String category, String subcategory, String name) {
        this.name = name;
        this.category = category;
        this.subcategory = subcategory;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    @Override
    public SubSubcategoryKey key() {
        return new SubSubcategoryKey(category, subcategory, name);
    }


    public static class SubSubcategoryKey {

        public final String name, category, subcategory;

        @Override
        public int hashCode() {
            return Objects.hash(name, category, subcategory);
        }


        public SubSubcategoryKey(String category, String subcategory, String name) {
            this.name = name;
            this.category = category;
            this.subcategory = subcategory;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SubSubCategoryRecord that = (SubSubCategoryRecord) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(category, that.category) &&
                    Objects.equals(subcategory, that.subcategory);
        }
    }
}

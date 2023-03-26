package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.dataLayer.dao.records.readonly.SubcategoryData;

import java.util.Objects;

public class SubcategoryRecord extends BaseRecord<SubcategoryRecord.SubcategoryKey> implements SubcategoryData {
    public final String category, name;

    public SubcategoryRecord(String category, String name) {
        this.category = category;
        this.name = name;
    }

    @Override
    public SubcategoryKey key() {
        return new SubcategoryKey(category, name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "[Subcategory: " + category + " > " + name + "]";
    }

    public static class SubcategoryKey {

        public final String name, category;

        public SubcategoryKey(String category, String name) {
            this.name = name;
            this.category = category;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, category);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SubcategoryRecord that = (SubcategoryRecord) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(category, that.category);
        }
    }
}

package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.shared.utils.Utils;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ItemData;

import java.util.Objects;

public class ItemRecord extends BaseRecord<ItemRecord.ItemKey> implements ItemData {

    int ppn;
    int catalogNumber;
    int productId;
    float price;

    public ItemRecord(int ppn, int catalogNumber, int productId, float price) {
        this.ppn = ppn;
        this.catalogNumber = catalogNumber;
        this.productId = productId;
        this.price = price;
    }

    @Override
    public int getSupplierPPN() {
        return ppn;
    }

    @Override
    public int getCatalogNumber() {
        return catalogNumber;
    }

    @Override
    public int getProductId() {
        return productId;
    }

    public void setCatalogNumber(int catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    @Override
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public ItemKey key() {
        return new ItemKey(ppn, catalogNumber);
    }

    public static class ItemKey {
        public final int ppn, catalogNumber;

        public ItemKey(int ppn, int catalogNumber) {
            this.ppn = ppn;
            this.catalogNumber = catalogNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ItemKey itemKey = (ItemKey) o;
            return ppn == itemKey.ppn &&
                    catalogNumber == itemKey.catalogNumber;
        }

        @Override
        public int hashCode() {
            return Objects.hash(ppn, catalogNumber);
        }
    }
}

package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ItemInOrderData;

import java.util.Objects;

public class ItemInOrderRecord extends BaseRecord<ItemInOrderRecord.ItemInOrderKey> implements ItemInOrderData {
    private int ppn, catalogNumber, orderId, amount;

    public ItemInOrderRecord(int ppn, int catalogNumber, int orderId, int amount) {
        this.ppn = ppn;
        this.catalogNumber = catalogNumber;
        this.orderId = orderId;
        this.amount = amount;
    }

    public int getPpn() {
        return ppn;
    }

    public void setPpn(int ppn) {
        this.ppn = ppn;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(int catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public ItemInOrderKey key() {
        return new ItemInOrderKey(ppn, catalogNumber, orderId);
    }

    public static class ItemInOrderKey {

        public final int ppn, catalogNumber, orderId;

        public ItemInOrderKey(int ppn, int catalogNumber, int orderId) {
            this.ppn = ppn;
            this.catalogNumber = catalogNumber;
            this.orderId = orderId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ItemInOrderKey that = (ItemInOrderKey) o;
            return ppn == that.ppn &&
                    catalogNumber == that.catalogNumber &&
                    orderId == that.orderId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(ppn, catalogNumber, orderId);
        }
    }
}

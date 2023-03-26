package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.shared.utils.Utils;

public class QuantityDiscountRecord extends BaseRecord<Integer> {
    public final int id;
    public final ItemRecord.ItemKey itemKey;
    public final int quantity;
    public final float discount;

    public QuantityDiscountRecord(int id, ItemRecord.ItemKey itemKey, int quantity, float discount){
        this.id = id;
        this.itemKey = itemKey;
        this.quantity = quantity;
        this.discount = discount;
    }

    @Override
    public Integer key() {
        return id;
    }
}

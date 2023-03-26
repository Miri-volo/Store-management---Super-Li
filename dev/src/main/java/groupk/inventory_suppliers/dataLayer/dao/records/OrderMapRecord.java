package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.dataLayer.dao.records.readonly.OrderMapData;

import java.util.Objects;

public class OrderMapRecord extends BaseRecord<OrderMapRecord.OrderMapKey> implements OrderMapData {
    int order_id;
    int product_id;
    int amount;

    public OrderMapRecord(int order_id, int product_id, int amount) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.amount = amount;
    }

    @Override
    public OrderMapKey key() {
        return new OrderMapKey(order_id, product_id);
    }

    @Override
    public int getOrder_id() {
        return order_id;
    }

    @Override
    public int getProduct_id() {
        return product_id;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    public static class OrderMapKey {

        public final int order_id, product_id;

        @Override
        public int hashCode() {
            return Objects.hash(order_id, product_id);
        }


        public OrderMapKey(int order_id, int product_id) {
            this.order_id = order_id;
            this.product_id = product_id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            OrderMapRecord.OrderMapKey that = (OrderMapRecord.OrderMapKey) o;
            return Objects.equals(product_id, that.product_id) &&
                    Objects.equals(order_id, that.order_id);
        }
    }
}

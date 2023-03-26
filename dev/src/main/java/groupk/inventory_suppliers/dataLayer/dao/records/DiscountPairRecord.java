package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.dataLayer.dao.records.readonly.DiscountPairData;

import java.sql.Date;
import java.util.Objects;

public class DiscountPairRecord extends BaseRecord<DiscountPairRecord.DiscountPairKey> implements DiscountPairData {

    private int product_id;
    private int productItem_id;
    private int id;
    private Date start_date;
    private Date end_date;
    private float discount;

    public DiscountPairRecord(int product_id, int productItem_id, int id, Date start_date, Date end_date, float discount) {
        this.product_id = product_id;
        this.productItem_id = productItem_id;
        this.id = id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.discount = discount;
    }

    @Override
    public DiscountPairKey key() {
        return new DiscountPairKey(product_id, productItem_id, id);
    }

    @Override
    public int getProductId() {
        return product_id;
    }

    @Override
    public int getProductItemId() {
        return productItem_id;
    }

    @Override
    public int getDiscountPairId() {
        return id;
    }

    @Override
    public float getDiscount() {
        return discount;
    }

    @Override
    public Date getStartDate() {
        return start_date;
    }

    @Override
    public Date getEndDate() {
        return end_date;
    }

    public static class DiscountPairKey {

        public final int product_id, productItem_id, id;

        @Override
        public int hashCode() {
            return Objects.hash(product_id, productItem_id, id);
        }


        public DiscountPairKey(int product_id, int ProductItem_id, int id) {
            this.product_id = product_id;
            this.productItem_id = ProductItem_id;
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            DiscountPairRecord.DiscountPairKey that = (DiscountPairRecord.DiscountPairKey) o;
            return Objects.equals(product_id, that.product_id) &&
                    Objects.equals(productItem_id, that.productItem_id) &&
                    Objects.equals(id, that.id);
        }
    }
}

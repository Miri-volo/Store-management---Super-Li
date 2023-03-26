package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ProductItemInReportData;

public class ProductItemInReportRecord extends BaseRecord<Integer> implements ProductItemInReportData {
    private int report_id;
    private int product_id;
    private int productItem_id;

    public ProductItemInReportRecord(int report_id, int product_id, int productItem_id) {
        this.report_id = report_id;
        this.product_id = product_id;
        this.productItem_id = productItem_id;
    }

    @Override
    public Integer key() {
        return report_id;
    }

    @Override
    public int getReportId() {
        return report_id;
    }

    @Override
    public int getProductId() {
        return product_id;
    }

    @Override
    public int getProductItemId() {
        return productItem_id;
    }
}

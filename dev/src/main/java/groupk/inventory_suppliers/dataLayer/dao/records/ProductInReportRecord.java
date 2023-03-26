package groupk.inventory_suppliers.dataLayer.dao.records;

import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ProductInReportData;

public class ProductInReportRecord extends BaseRecord<Integer> implements ProductInReportData {
    private int report_id;
    private int product_id;

    public ProductInReportRecord(int report_id, int product_id) {
        this.report_id = report_id;
        this.product_id = product_id;
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
}

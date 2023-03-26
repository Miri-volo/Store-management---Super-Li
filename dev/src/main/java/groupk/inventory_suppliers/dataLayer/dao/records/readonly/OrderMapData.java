package groupk.inventory_suppliers.dataLayer.dao.records.readonly;

import java.util.Map;

/**
 * "orderId" INTEGER NOT NULL,
 * "productId"	INTEGER NOT NULL,
 * "amount"	INTEGER NOT NULL,
 */

public interface OrderMapData {
    int getOrder_id();

    int getProduct_id();

    int getAmount();
}

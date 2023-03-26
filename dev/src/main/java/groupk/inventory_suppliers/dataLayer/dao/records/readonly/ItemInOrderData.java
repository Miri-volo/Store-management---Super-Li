package groupk.inventory_suppliers.dataLayer.dao.records.readonly;

public interface ItemInOrderData {
    int getPpn();
    int getCatalogNumber();
    int getOrderId();
    int getAmount();
}

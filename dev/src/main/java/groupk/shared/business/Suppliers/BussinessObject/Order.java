package groupk.shared.business.Suppliers.BussinessObject;

import groupk.shared.business.ItemController;
import groupk.shared.business.QuantityDiscountController;
import groupk.inventory_suppliers.dataLayer.dao.PersistenceController;
import groupk.inventory_suppliers.dataLayer.dao.records.ItemInOrderRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.OrderData;
import groupk.shared.business.Suppliers.BusinessLogicException;
import groupk.inventory_suppliers.shared.utils.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private final QuantityDiscountController discounts;
    OrderData source;
    private ItemController itemsController;
    float totalPrice;
    Map<Item, Integer> itemsAmounts;
    public final Supplier supplier;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private PersistenceController dal;

    public Order(Supplier supplier, OrderData source,
                 PersistenceController dal, QuantityDiscountController discounts,
                 ItemController itemsController) {
        this.supplier = supplier;
        this.source = source;
        this.itemsController = itemsController;
        this.itemsAmounts = new HashMap<>();
        this.dal = dal;
        this.discounts = discounts;
        this.pullItemsFromDB();
    }

    private void pullItemsFromDB() {
        dal.getItemsInOrders()
                .all()
                .filter(x -> x.getOrderId() == getId())
                .forEach(x -> itemsAmounts.put(
                        itemsController.get(x.getPpn(), x.getCatalogNumber()),
                        x.getAmount()
                    ));
        refreshPrice();
    }

    public void removeItemIfExists(Item item) {
        itemsAmounts.remove(item);
        refreshPrice();
    }

    public void refreshPrice() {
        float price = 0;
        for(Map.Entry<Item, Integer> e: itemsAmounts.entrySet()) {
            price += discounts.priceForAmount(e.getKey(), e.getValue());
        }
        totalPrice = price;
        dal.getOrders().setPrice(getId(), price);
    }

    public void orderItem(Item item, int amount) {
        int catalogNumber = item.getCatalogNumber();
        int ppn = item.getSupplier().getPpn();

        if(amount == 0) {
            itemsAmounts.remove(item);
            ItemInOrderRecord.ItemInOrderKey key = new ItemInOrderRecord.ItemInOrderKey(
                    ppn, catalogNumber, getId()
            );
            dal.getItemsInOrders().delete(key);
        } else {
            itemsAmounts.put(item, amount);
            dal.getItemsInOrders().updateAmount(getId(), ppn, catalogNumber, amount);
            
        }
        refreshPrice();
    }

    public void updateOrdered(LocalDate ordered) throws BusinessLogicException {
        if(ordered.isAfter(getProvided())) {
            throw new BusinessLogicException("ordered date can't be after provided date.");
        }
        dal.getOrders().updateOrdered(getId(), ordered);
    }

    public void updateProvided(LocalDate provided) throws BusinessLogicException {
        if(getOrdered().isAfter(provided)) {
            throw new BusinessLogicException("provided date can't be before ordered date.");
        }
        dal.getOrders().updateProvided(getId(), provided);
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public LocalDate getOrdered() {
        return source.getOrdered();
    }

    public LocalDate getProvided() {
        return source.getProvided();
    }

    @Override
    public String toString() {
        ArrayList<String> table = new ArrayList<>();
        table.add(" **** ORDER **** "); table.add(" "); table.add(" "); table.add(" ");

        table.add("Order id: "); table.add(String.valueOf(getId())); table.add(" "); table.add(" ");
        table.add("Supplier name: "); table.add(supplier.getName());
        table.add("Supplier PPN: "); table.add(supplier.getPpn() + "");

        table.add("Ordered: "); table.add(getOrdered().format(DATE_FORMAT));
        table.add("Provided: "); table.add(getProvided().format(DATE_FORMAT));

        table.add("----");
        table.add("----");
        table.add("----");
        table.add("----");

        for(Map.Entry<Item, Integer> entry: itemsAmounts.entrySet()) {
            Item item = entry.getKey();
            int amount = entry.getValue();

            table.add("");
            table.add("Catalog #: " + item.getCatalogNumber());
            table.add(amount + " units");
            table.add(String.format("$%.2f / ea", item.getPrice()));
        }

        table.add("----");
        table.add("----");
        table.add("----");
        table.add("----");

        table.add("Total price: ");
        table.add(String.format("$%.2f", totalPrice));
        table.add("");
        table.add("");

        return Utils.table(
                4, 25, true,
                table.toArray()
        );
    }

    public int getId(){
        return source.getId();
    }

    public boolean containsItem(Item item) {
        return itemsAmounts.containsKey(item);
    }
}

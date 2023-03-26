package groupk.shared.business;

import groupk.inventory_suppliers.dataLayer.dao.records.OrderRecord;
import groupk.shared.PresentationLayer.Suppliers.UserOutput;
import groupk.shared.business.Suppliers.BusinessLogicException;
import groupk.shared.business.Suppliers.BussinessObject.Item;

import groupk.shared.business.Suppliers.BussinessObject.Order;
import groupk.shared.business.Suppliers.BussinessObject.Supplier;
import groupk.inventory_suppliers.dataLayer.dao.PersistenceController;
import groupk.inventory_suppliers.dataLayer.dao.records.OrderType;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.OrderData;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class OrderController {
    private final QuantityDiscountController discounts;
    private final LogisticsController logistics;
    private final SupplierController suppliers;
    ArrayList<Order> orders;
    private PersistenceController dal;
    private ItemController items;

    public OrderController(PersistenceController dal,
                           QuantityDiscountController discounts,
                           LogisticsController logistics, SupplierController suppliers,
                           ItemController items) {
        this.dal = dal;
        this.items = items;
        orders = new ArrayList<>();
        this.discounts = discounts;
        this.logistics = logistics;
        this.suppliers = suppliers;
        this.dal.getOrders().all().forEach(this::createFromExisting);
    }

    private void createFromExisting(OrderRecord orderRecord) {
        Supplier supplier = suppliers.get(orderRecord.ppn);
        orders.add(new Order(supplier, orderRecord, dal, discounts, items));
    }

    public Order get(int id) throws BusinessLogicException {
        return orders.stream()
                .filter(o -> o.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException("No order exists with id "+ id));
    }

    public Order create(Supplier supplier, OrderType type, LocalDate ordered, LocalDate delivered) throws BusinessLogicException {
        if(ordered.isAfter(delivered)) {
            throw new BusinessLogicException("delivery date can't be before ordering date.");
        }
        OrderData data = dal.getOrders().createOrder(supplier.getPpn(), type, ordered, delivered);
        Order order = new Order(supplier, data, dal, discounts, items);
        orders.add(order);
        createFittingTrucking(ProductController.BRANCH_NAME, order);
        return order;
    }

    public void removeItemFromOrders(Item item) {
        orders.forEach(o -> o.removeItemIfExists(item));
    }

    public void refreshPricesAndDiscounts(Item item) {
        for(Order order: orders) {
            if(order.containsItem(item)) {
                order.refreshPrice();
            }
        }
    }

    public void orderItem(Order order, Item item, int amount) {
        order.orderItem(item, amount);
    }

    public void deleteAllFromSupplier(Supplier s) {
        int ppn = s.getPpn();

        orders.removeIf(order -> {
            if(order.supplier.getPpn() == ppn) {
                dal.getItemsInOrders().deleteAllInOrder(order.getId());
                dal.getOrders().delete(order.getId());
                UserOutput.println("Order " + order.getId() +" is deleted.");
                return true;
            }
            return false;
        });
    }

    public void delete(int orderId) throws BusinessLogicException {
        Order order = orders.stream()
                .filter(x -> x.getId() == orderId)
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException("Tried to delete order, but it doesn't " +
                "seem to exist (maybe it was already deleted?)"));
        orders.remove(order);
        dal.getOrders().delete(orderId);
    }

    public Collection<Order> all() {
        return new ArrayList<>(orders);
    }

    public void setOrdered(Order order, LocalDate ordered) throws BusinessLogicException {
        order.updateOrdered(ordered);
    }

    public void setProvided(Order order, LocalDate provided) throws BusinessLogicException {
        order.updateProvided(provided);
    }


    public int updateAmount(Order order, Item item, int amount) { // needs to check if the item amount is >= the min amount
        order.orderItem(item, amount);
        refreshPricesAndDiscounts(item);
        return order.getId();
    }

    public int createShortage(Supplier supplier, Item item, int amountOfProd, OrderType type, LocalDate ordered, LocalDate delivered){
        if(ordered.isAfter(delivered)) {
            throw new BusinessLogicException("delivery date can't be before ordering date.");
        }

        OrderData data = dal.getOrders().createOrder(supplier.getPpn(), type, ordered, delivered);
        Order order = new Order(supplier, data, dal, discounts, items);

        if(type == OrderType.Shortages){
            order.orderItem(item, amountOfProd);
        }

        orders.add(order);
        String source = order.supplier.getContact().getAddress();
        logistics.addTruckingRequest(order.getId(), source, ProductController.BRANCH_NAME);
        return order.getId();
    }

    public void orderItemFromMap(Order order, Map<Item, Integer> itemsWithAmount) {
        for(int i=0; i<itemsWithAmount.size(); i++){
            order.orderItem((Item) itemsWithAmount.keySet().toArray()[i],
                    itemsWithAmount.get(itemsWithAmount.keySet().toArray()[i]));

        }
    }

    public void createFittingTrucking(String destination, Order order) {
        String source = order.supplier.getContact().getAddress();
        logistics.addTruckingRequest(order.getId(), source, destination);
    }

    public void cancelOrder(int orderID) {
        dal.getItemsInOrders().deleteAllInOrder(orderID);
        dal.getOrders().delete(orderID);
        orders.removeIf(order -> order.getId() == orderID);
    }
}

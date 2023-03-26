package groupk.shared.business;

import groupk.shared.PresentationLayer.Suppliers.UserOutput;
import groupk.shared.business.Suppliers.BusinessLogicException;
import groupk.shared.business.Suppliers.BussinessObject.Item;
import groupk.shared.business.Suppliers.BussinessObject.QuantityDiscount;
import groupk.shared.business.Suppliers.BussinessObject.Supplier;
import groupk.inventory_suppliers.dataLayer.dao.PersistenceController;
import groupk.inventory_suppliers.dataLayer.dao.records.ItemRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.QuantityDiscountRecord;

import java.util.*;
import java.util.stream.Collectors;

public class QuantityDiscountController {
    private PersistenceController dal;
    private Map<Item, Map<Integer, QuantityDiscount>> map;

    public QuantityDiscountController(PersistenceController dal, ItemController items) {
        this.dal = dal;
        map = new HashMap<>();

        dal.getQuantityDiscounts().all().forEach(record -> {
            try {
                Item item = items.get(record.itemKey.ppn, record.itemKey.catalogNumber);
                getList(item).put(record.id, new QuantityDiscount(
                        record.id, item, record.quantity, record.discount
                ));
            } catch (BusinessLogicException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Map<Integer, QuantityDiscount> getList(Item item) {
        return map.computeIfAbsent(item, key -> new HashMap<>());
    }

    public void deleteAllFor(Item item) {
        dal.getQuantityDiscounts().deleteAllForItem(
                new ItemRecord.ItemKey(item.getSupplier().getPpn(), item.getCatalogNumber())
        );
        UserOutput.println("Quantity discounts for item: supplier PPN " +  item.getSupplier().getPpn() +
                ", catalog number " + item.getCatalogNumber() + " were deleted");
    }

    public Collection<QuantityDiscount> discountsFor(Item item) {
        return new ArrayList<>(getList(item).values());
    }

    public void delete(int id) {
        dal.getQuantityDiscounts().delete(id);
        map.values().forEach(list -> list.remove(id));
    }


    public float priceForAmount(Item item, int amount) {
        float discount = 0;
        List<QuantityDiscount> discounts = getListOfDiscountsIncreasing(item);
        for(QuantityDiscount qd: discounts) {
            if(qd.quantity > amount) { break; }
            discount = qd.discount;
        }
        return amount * item.getPrice() * (1 - discount);
    }

    public Collection<QuantityDiscount> getAllDiscounts() {
        return map.values().stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.toList());
    }


    public QuantityDiscount createDiscount(Item item, int amount, float discount) throws BusinessLogicException {
        List<QuantityDiscount> discounts = getListOfDiscountsIncreasing(item);
        QuantityDiscount previous = null;
        for(int i = 0; i < discounts.size(); i++) {
            QuantityDiscount current = discounts.get(i);
            if(current.quantity == amount) {
                throw new BusinessLogicException("Item " + this + " already has discount for " + amount);
            }
            if(current.quantity > amount) {
                break;
            }
            previous = current;
        }
        if(previous != null && previous.discount > discount) {
            throw new BusinessLogicException(
                    "can't add a discount of " + (100 * discount) + "% for amount over " + amount +
                            " for item " + this + "! Already has a discount of " + (int)(previous.discount * 100)
                            + "% for over " + previous.quantity
            );
        }
        QuantityDiscountRecord dbCreated = dal.getQuantityDiscounts().createQuantityDiscount(
                amount, discount,
                new ItemRecord.ItemKey(item.getSupplier().getPpn(), item.getCatalogNumber())
        );
        QuantityDiscount created = new QuantityDiscount(dbCreated.id, item, amount, discount);
        getList(item).put(created.id, created);
        return created;
    }

    private List<QuantityDiscount> getListOfDiscountsIncreasing(Item item) {
        return getList(item).values()
                .stream().sorted(Comparator.comparing(x -> x.quantity)).collect(Collectors.toList());
    }

    public Supplier findCheapestSupplierFor(int productID, int amount) {
        return map.entrySet().stream()
                .filter(e -> e.getKey().getProductId() == productID)
                .min(Comparator.comparing(e -> priceForAmount(e.getKey(), amount)))
                .map(x -> x.getKey().getSupplier()).orElseThrow(NoSuchElementException::new);
    }

}

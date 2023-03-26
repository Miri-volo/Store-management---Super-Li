package groupk.inventory_suppliers.dataLayer.dao.records;

import java.util.NoSuchElementException;

public enum OrderType {
    Shortages(1),
    Periodical(2);

    public final int value;

    OrderType(int value) {
        this.value = value;
    }

    public static OrderType of(int value) {
        switch (value) {
            case 1: return Shortages;
            case 2: return Periodical;
            default: throw new NoSuchElementException("no such OrderType");
        }
    }
}

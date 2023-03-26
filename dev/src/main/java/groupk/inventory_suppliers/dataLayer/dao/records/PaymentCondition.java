package groupk.inventory_suppliers.dataLayer.dao.records;

import java.util.NoSuchElementException;

public enum PaymentCondition {
    DirectDebit(0), Credit(1);

    public final int value;

    PaymentCondition(int value) {
        this.value = value;
    }


    public static PaymentCondition valueOf(int paymentCondition) {
        switch (paymentCondition) {
            case 0: return DirectDebit;
            case 1: return Credit;
            default: throw new NoSuchElementException("no such enum value");
        }
    }
}

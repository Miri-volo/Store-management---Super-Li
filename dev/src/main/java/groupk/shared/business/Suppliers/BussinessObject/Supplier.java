package groupk.shared.business.Suppliers.BussinessObject;


import groupk.inventory_suppliers.dataLayer.dao.SupplierDAO;
import groupk.inventory_suppliers.dataLayer.dao.records.PaymentCondition;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ContactData;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.SupplierData;
import groupk.inventory_suppliers.shared.utils.Utils;
import java.time.DayOfWeek;

public class Supplier {

    private final SupplierDAO dao;
    private SupplierData source;

    public Supplier(SupplierData source, SupplierDAO dao){
        this.source = source;
        this.dao = dao;
    }

    public ContactData getContact() {
        return source.getContact();
    }

    public int getPpn() {
        return source.getPpn();
    }

    public int getBankNumber() {
        return source.getBankNumber();
    }
    public String getName() {
        return source.getName();
    }

    public boolean isDelivering() {
        return source.isDelivering();
    }

    public PaymentCondition getPaymentCondition() {
        return source.getPaymentCondition();
    }

    public void setPaymentCondition(PaymentCondition condition) {
        dao.updatePaymentCondition(getPpn(), condition);
    }

    public DayOfWeek getRegularSupplyingDays() {
        return source.getRegularSupplyingDays();
    }
    @Override
    public String toString() {
        DayOfWeek regularSupplyingDays = source.getRegularSupplyingDays();
        return Utils.table(
                2, 25, true,
                "    **** SUPPLIER **** ", "",
                "PPN", getPpn(),
                "Name", getName(),
                "Contact name", source.getContact().getName(),
                "Contact phone", source.getContact().getPhoneNumber(),
                "Contact address", source.getContact().getAddress(),
                "Bank #", source.getBankNumber(),
                "Delivering", source.isDelivering(),
                "Payment condition", source.getPaymentCondition(),
                "Supplies on", (regularSupplyingDays == null ? "N/A" : regularSupplyingDays)
        );
    }

    public void setIsDelivering(boolean newValue) {
        dao.updateIsDelivering(getPpn(), newValue);
    }

    public void setBankAccount(int bankAct) {
       dao.updateBankAccount(getPpn(), bankAct);
    }

    public void setName(String newName) {
        dao.updateName(getPpn(), newName);
    }
}

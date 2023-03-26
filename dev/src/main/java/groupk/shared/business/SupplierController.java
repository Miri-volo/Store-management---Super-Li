package groupk.shared.business;

import groupk.shared.business.Suppliers.BusinessLogicException;
import groupk.shared.business.Suppliers.BussinessObject.Supplier;
import groupk.inventory_suppliers.dataLayer.dao.PersistenceController;
import groupk.inventory_suppliers.dataLayer.dao.records.PaymentCondition;
import groupk.inventory_suppliers.dataLayer.dao.records.SupplierRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.SupplierData;
import groupk.inventory_suppliers.shared.dto.CreateSupplierDTO;
import groupk.shared.inputValidity.InputValidity;

import java.time.DayOfWeek;
import java.util.*;

public class SupplierController {
    Map<Integer, Supplier> suppliers;
    private PersistenceController dal;

    public SupplierController(PersistenceController dal) {
        suppliers = new HashMap<>();
        this.dal = dal;
        dal.getSuppliers().all().forEach(this::addFromExisting);
    }

    public Supplier create(CreateSupplierDTO dto)
            throws BusinessLogicException {
        validatePPNIsNew(dto.ppn);
        InputValidity.phone.throwIfNotMatch(dto.contactPhone);
        InputValidity.address.throwIfNotMatch(dto.contactAddress);
        SupplierData source = dal.getSuppliers().createSupplier(dto);
        Supplier object = new Supplier(source, dal.getSuppliers());
        suppliers.put(dto.ppn, object);
        return object;
    }

    private void validatePPNIsNew(int ppn) throws BusinessLogicException {
        if (suppliers.containsKey(ppn)) {
            throw new BusinessLogicException("A supplier with this ppn already exists: " + ppn);
        }
    }

    public Supplier delete(int ppn) throws BusinessLogicException {
        if (!suppliers.containsKey(ppn)) {
            throw new BusinessLogicException("no such ppn: " + ppn);
        }
        Supplier s = suppliers.remove(ppn);
      //  orders.deleteAllFromSupplier(s);
//        items.deleteAllFromSupplier(s);
        dal.getSuppliers().delete(ppn);
        return s;
    }

    public Supplier get(int ppn) throws BusinessLogicException {
        if (!dal.getSuppliers().exists(ppn)) {
            throw new BusinessLogicException("No suppliers with this ppn: " + ppn);
        }
        return suppliers.computeIfAbsent(ppn,
                k -> {
                    SupplierData data = dal.getSuppliers().get(ppn);

                    return new Supplier(data, dal.getSuppliers());
                });
    }

    public void setPaymentCondition(int ppn, PaymentCondition payment) {
        dal.getSuppliers().updatePaymentCondition(ppn, payment);
    }

    private void addFromExisting(SupplierRecord supplierRecord) {
        suppliers.put(supplierRecord.getPpn(), new Supplier(supplierRecord, dal.getSuppliers()));
    }

    public Collection<Supplier> all() {
        return suppliers.values();
    }

    public void setBankAccount(int supplierPPN, int bankAct) {
        suppliers.get(supplierPPN).setBankAccount(bankAct);
    }

    public void setIsDelivering(int supplierPPN, boolean newValue) {
        suppliers.get(supplierPPN).setIsDelivering(newValue);
    }

    public void setSupplierName(int supplierPPN, String newName) {
        suppliers.get(supplierPPN).setName(newName);
    }

    public void setRegularSupplyingDays(int supplierPPN, DayOfWeek dayOfWeek) {
    }

    public void setContact(int supplierPPN, String name, String phoneNumber, String address) {
    }



}

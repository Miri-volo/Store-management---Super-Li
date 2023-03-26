package groupk.shared.service;

import groupk.inventory_suppliers.dataLayer.dao.PersistenceController;
import groupk.inventory_suppliers.dataLayer.dao.records.OrderType;
import groupk.inventory_suppliers.dataLayer.dao.records.PaymentCondition;
import groupk.logistics.DataLayer.myDataBase;
import groupk.shared.business.Facade;
import groupk.shared.business.Inventory.Categories.Category;
import groupk.shared.service.Inventory.Objects.ProductItem;
import groupk.shared.service.Inventory.Objects.Report;
import groupk.shared.service.Inventory.Objects.SubCategory;
import groupk.shared.business.Suppliers.BusinessLogicException;
import groupk.shared.business.Suppliers.BussinessObject.Item;
import groupk.shared.business.Suppliers.BussinessObject.Order;
import groupk.shared.business.Suppliers.BussinessObject.QuantityDiscount;
import groupk.shared.business.Suppliers.BussinessObject.Supplier;
import groupk.shared.service.dto.*;
import groupk.workers.data.DalController;

import java.sql.Connection;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Service {
    private Facade facade;

    public Service(PersistenceController persistenceController, DalController dalController, myDataBase myDataBase) {
        facade = new Facade(persistenceController, dalController, myDataBase);
    }

    public void deleteEmployeeDB() {
        facade.deleteEmployeeDB();
    }

    public void deleteLogisticsDB() {
        facade.deleteLogisticsDB();
    }

    public void loadEmployeeDB() {
        facade.loadEmployeeDB();
    }

    public Response<Employee> createEmployee(
            String name,
            String id,
            String bank,
            int bankID,
            int bankBranch,
            Calendar employmentStart,
            int salaryPerHour,
            int sickDaysUsed,
            int vacationDaysUsed,
            Employee.Role role,
            Set<Employee.ShiftDateTime> shiftPreferences) {
        return facade.createEmployee(name, id, bank, bankID, bankBranch, employmentStart, salaryPerHour, sickDaysUsed, vacationDaysUsed, role, shiftPreferences);
    }

    public Response<Shift> createShift(String subjectID, Calendar date, Shift.Type type,
                                       LinkedList<Employee> staff,
                                       HashMap<Employee.Role, Integer> requiredStaff) {
        return facade.createShift(subjectID, date, type, staff, requiredStaff);
    }

    public Response<Employee> readEmployee(String subjectID, String employeeID) {
        return facade.readEmployee(subjectID, employeeID);
    }

    public Response<Shift> readShift(String subjectID, Calendar date, Shift.Type type) {
        return facade.readShift(subjectID, date, type);
    }

    public Response<Employee> deleteEmployee(String subjectID, String employeeID) {
        return facade.deleteEmployee(subjectID, employeeID);
    }

    //dont use
    public Response<Shift> deleteShift(String subjectID, Shift.Type type, Calendar date) {
        return facade.deleteShift(subjectID, type, date);
    }

    public Response<List<Employee>> listEmployees(String subjectID) {
        return facade.listEmployees(subjectID);
    }

    public Response<Shift> addEmployeeToShift(String subjectID, Calendar date, Shift.Type type, String employeeID) {
        return facade.addEmployeeToShift(subjectID, date, type, employeeID);
    }

    public Response<Shift> removeEmployeeFromShift(String subjectID, Calendar date, Shift.Type type, String employeeID) {
        return facade.removeEmployeeFromShift(subjectID, date, type, employeeID);
    }

    public Response<Employee> updateEmployee(String subjectID, Employee changed) {
        return facade.updateEmployee(subjectID, changed);
    }

    public Response<Employee> addEmployeeShiftPreference(String subjectID, String employeeID, Employee.ShiftDateTime shift) {
        return facade.addEmployeeShiftPreference(subjectID, employeeID, shift);
    }

    public Response<Employee> setEmployeeShiftsPreference(String subjectID, String employeeID, Set<Employee.ShiftDateTime> shiftPreferences) {
        return facade.setEmployeeShiftsPreference(subjectID, employeeID, shiftPreferences);
    }

    public Response<Employee> deleteEmployeeShiftPreference(String subjectID, String employeeID, Employee.ShiftDateTime shift) {
        return facade.deleteEmployeeShiftPreference(subjectID, employeeID, shift);
    }

    public Response<List<Shift>> listShifts(String subjectID) {
        return facade.listShifts(subjectID);
    }

    public Response<List<Shift>> listEmployeeShifts(String subjectID, String employeeID) {
        return facade.listEmployeeShifts(subjectID, employeeID);
    }

    public Response<Integer> numOfEmployeeShifts(String subjectID, String employeeID) {
        return facade.numOfEmployeeShifts(subjectID, employeeID);
    }

    public Response<List<Employee>> whoCanWorkWithRole(String subjectId, Employee.ShiftDateTime day, Employee.Role role) {
        return facade.whoCanWorkWithRole(subjectId, day, role);
    }

    public Response<List<Employee>> whoCanWork(String subjectId, Employee.ShiftDateTime day) {
        return facade.whoCanWork(subjectId, day);
    }

    public Response<Shift> updateRequiredRoleInShift(String subjectId, Calendar date, Shift.Type type, Employee.Role role, int count) {
        return facade.updateRequiredRoleInShift(subjectId, date, type, role, count);
    }

    public Response<Shift> setRequiredStaffInShift(String subjectId, Calendar date, Shift.Type type, HashMap<Employee.Role, Integer> requiredStaff) {
        return facade.setRequiredStaffInShift(subjectId, date, type, requiredStaff);
    }

    public Response<List<Shift>> optionsForDeleveryWithLogisitcsAndDriversInShift(String subjectID) {
        return facade.optionsForDeleveryWithLogisitcsAndDriversInShift(subjectID);
    }

    public Response<Boolean> deleteDelivery(String subjectID, int deliveryID) {
        return facade.deleteDelivery(subjectID, deliveryID);
    }

    public Response<List<Delivery>> listDeliveries(String subjectID) {
        return facade.listDeliveries(subjectID);
    }

    public Response<List<String>> listVehicles(String subjectID) {
        return facade.listVehicles(subjectID);
    }

    public Response<List<String>[]> createDelivery(String subjectID, String registrationPlateOfVehicle, LocalDateTime date, String driverUsername, List<Site> sources, List<Site> destinations, List<Integer> orders, long hours, long minutes) {
        return facade.createDelivery(subjectID, registrationPlateOfVehicle, date, driverUsername, sources, destinations, orders, hours, minutes);
    }

    public Response<List<Delivery>> listDeliveriesWithVehicle(String subjectID, String registration) {
        return facade.listDeliveriesWithVehicle(subjectID, registration);
    }

    public Response<Boolean> createVehicle(String subjectID, String license, String registrationPlate, String model, int weight, int maxWeight) {
        return facade.createVehicle(subjectID, license, registrationPlate, model, weight, maxWeight);
    }

    public Response<Boolean> setWeightForDelivery(String subjectID, int deliveryID, int weight) {
        return facade.setWeightForDelivery(subjectID, deliveryID, weight);
    }

    public Response<Boolean> addOrdersToTrucking(String subjectID, int truckingID, int orderID) {
        return facade.addOrdersToTrucking(subjectID, truckingID, orderID);
    }

    public Response<List<String>> updateSources(String subjectID, int truckingID, List<Site> sources) {
        return facade.updateSources(subjectID, truckingID, sources);
    }

    public Response<List<String>> updateDestination(String subjectID, int truckingID, List<Site> destinations) {
        return facade.updateDestination(subjectID, truckingID, destinations);
    }

    public Response<List<String>> addSources(String subjectID, int truckingID, List<Site> sources) {
        return facade.addSources(subjectID, truckingID, sources);
    }

    public Response<List<String>> addDestination(String subjectID, int truckingID, List<Site> destinations) {
        return facade.addDestination(subjectID, truckingID, destinations);
    }

    public Response<Boolean> moveOrdersFromTrucking(String subjectID, int truckingID, int orderID) {
        return facade.moveOrdersFromTrucking(subjectID, truckingID, orderID);
    }

    public Response<Boolean> updateVehicleOnTrucking(String subjectID, int truckingID, String registrationPlate) {
        return facade.updateVehicleOnTrucking(subjectID, truckingID, registrationPlate);
    }

    public Response<Boolean> updateDriverOnTrucking(String subjectID, int truckingID, String driverUsername) {
        return facade.updateDriverOnTrucking(subjectID, truckingID, driverUsername);
    }

    public Response<Boolean> updateDateOnTrucking(String subjectID, int truckingID, LocalDateTime newDate) {
        return facade.updateDateOnTrucking(subjectID, truckingID, newDate);
    }

    public Response<Boolean> addLicenseForDriver(String subjectID, String license) {
        return facade.addLicenseForDriver(subjectID, license);
    }

    public Response<List<String>> getDriverLicenses(String subjectID) {
        return facade.getDriverLicenses(subjectID);
    }

    public Response<String[]> getLicensesList() {
        return facade.getLicensesList();
    }

    public Response<String[]> getAreasList() {
        return facade.getAreasList();
    }

    public Response<Integer> getTruckingIDByOrderID(String subjectID, int orderID) {
        return facade.getTruckingIDByOrderID(subjectID, orderID);
    }

    public Response<Boolean> deleteTruckingRequest(String subjectID, int orderID) {
        return facade.deleteTruckingRequest(subjectID, orderID);
    }

    public Response<List<String>> getTruckingRequests(String subjectID) {
        return facade.getTruckingRequests(subjectID);
    }

    //inventory & suppliers methods


    public Facade.SI_Response addCategory(String name) {
        return facade.addCategory(name);
    }

    public Facade.SI_Response addSubCategory(String categoryName, String SubCategoryName) {
        return facade.addSubCategory(categoryName, SubCategoryName);
    }

    public Facade.SI_Response addSubSubCategory(String category, String sub_category, String name) {
        return facade.addSubSubCategory(category, sub_category, name);
    }

    public Facade.SI_Response removeCategory(String name) {
        return facade.removeCategory(name, facade.productsInCategory(name));
    }

    public Facade.SI_Response removeSubCategory(String category, String name) {
        return facade.removeSubCategory(category, name, facade.productsInSubCategory(category, name));
    }

    public Facade.SI_Response removeSubSubCategory(String category, String sub_category, String name) {
        return facade.removeSubSubCategory(category, sub_category, name, facade.productsInSubSubCategory(category, sub_category, name));
    }

    public Facade.ResponseT<Category> getCategory(String name) {
        return facade.getCategory(name);
    }

    public Facade.ResponseT<SubCategory> getSubCategory(String category, String name) {
        return facade.getSubCategory(category, name);
    }

    public Facade.SI_Response updateCategoryCusDiscount(float discount, LocalDate start_date, LocalDate end_date, String category, String sub_category, String subsub_category) {
        return facade.updateCategoryCusDiscount(discount, start_date, end_date, category, sub_category, subsub_category);
    }

    public Facade.ResponseT<List<String>> getCategoriesNames() {
        return facade.getCategoriesNames();
    }

    //Product methods
    public Facade.ResponseT<groupk.shared.service.Inventory.Objects.Product> addProduct(String name, String manufacturer, double man_price, float cus_price, int min_qty, int supply_time, String category, String sub_category, String subsub_category) {
        return facade.addProduct(name, cus_price, min_qty, supply_time, category, sub_category, subsub_category);
    }

    public Facade.SI_Response removeProduct(int id) {
        return facade.removeProduct(id);
    }

    public Facade.SI_Response updateProductCusDiscount(float discount, LocalDate start_date, LocalDate end_date, int product_id) {
        return facade.updateProductCusDiscount(discount, start_date, end_date, product_id);
    }

    public Facade.SI_Response updateProductCusPrice(int product_id, float price) {
        return facade.updateProductCusPrice(product_id, price);
    }

    public Facade.ResponseT<List<String>> getProductNames() {
        return facade.getProductNames();
    }

    public Facade.ResponseT<List<groupk.shared.business.Inventory.Product>> getProducts() {
        // return facade.getProducts();
        return null;
    }

    //Item methods
    public Facade.ResponseT<ProductItem> addItem(int product_id, String store, String location, int supplier, LocalDate expiration_date, boolean on_shelf) {
        return facade.addItem(product_id, store, location, supplier, expiration_date, on_shelf);
    }

    public Facade.SI_Response removeItem(int product_id, int item_id) {
        Facade.ResponseT<Boolean> r = facade.removeItem(product_id, item_id);
        int min_qty = facade.getMinQty(product_id).data;
        Facade.ResponseT<Integer> r2 = createOrderShortage(r, product_id, min_qty);
        if (!r2.success)
            return r2;
        int order_id = r2.data;
        return addProductToOrder(order_id, product_id, min_qty);
    }

    public Facade.SI_Response updateItemCusDiscount(float discount, LocalDate start_date, LocalDate end_date, int product_id, int item_id) {
        return facade.updateItemCusDiscount(product_id, item_id, discount, start_date, end_date);
    }

    public Facade.SI_Response updateItemDefect(int product_id, int item_id, boolean is_defect, String defect_reporter) {
        return facade.updateItemDefect(product_id, item_id, is_defect, defect_reporter);
    }

    public Facade.ResponseT<String> getItemLocation(int product_id, int item_id) {
        return facade.getItemLocation(product_id, item_id);
    }

    public Facade.SI_Response setItemLocation(int product_id, int item_id, String location) {
        return facade.changeItemLocation(product_id, item_id, location);
    }

    public Facade.SI_Response setItemOnShelf(int product_id, int item_id, boolean on_shelf) {
        return facade.changeItemOnShelf(product_id, item_id, on_shelf);
    }

    public Facade.SI_Response addProductToOrder(int order_id, int product_id, int amount) {
        return facade.addProductToOrder(order_id, product_id, amount);
    }

    //Report methods

    public Facade.ResponseT<Report> createMissingReport(String name, String report_producer) {
        return facade.createMissingReport(name, report_producer);
    }

    public Facade.ResponseT<Report> createExpiredReport(String name, String report_producer) {
        return facade.createExpiredReport(name, report_producer);
    }

    public Facade.ResponseT<Report> createSurplusesReport(String name, String report_producer) {
        return facade.createSurplusesReport(name, report_producer);
    }

    public Facade.ResponseT<Report> createDefectiveReport(String name, String report_producer) {
        return facade.createDefectiveReport(name, report_producer);
    }

    public Facade.ResponseT<Report> createBySupplierReport(String name, String report_producer, int suppName) {
        return facade.createBySupplierReport(name, report_producer, suppName);
    }

    public Facade.ResponseT<Report> createByProductReport(String name, String report_producer, String proName) {
        return facade.createByProductReport(name, report_producer, proName);
    }

    public Facade.ResponseT<Report> createByCategoryReport(String name, String report_producer, String CatName, String subCatName, String subSubCatName) {
        return facade.createByCategoryReport(name, report_producer, CatName, subCatName, subSubCatName);
    }

    public Facade.SI_Response removeReport(int id) {
        return facade.removeReport(id);
    }

    public Facade.ResponseT<Report> getReport(int id) {
        return facade.getReport(id);
    }

    //Order methods
    public Facade.ResponseT<Integer> createPeriodicOrder(Map<Integer, Integer> productAmount, int weekDay) {
        return facade.createOrderPeriodic(productAmount, weekDay);
    }

    public Facade.ResponseT<Map<Integer, Integer>> confirmOrder(int order_id) {
        return facade.confirmOrder(order_id);
    }

    public Facade.ResponseT<List<ProductItem>> confirmOrderAmount(int order_id, Map<Integer, Integer> actual_amount) {
        return facade.confirmOrderAmount(order_id, actual_amount);
    }

    //SUPPLIER METHODS
    public Facade.ResponseT<Order> getOrder(int id) {
        return facade.getOrder(id);
    }

    public Facade.ResponseT<Supplier> createSupplier(int ppn, int bankAccount, String name,
                                                     boolean isDelivering, PaymentCondition paymentCondition,
                                                     DayOfWeek regularSupplyingDays, String contactName,
                                                     String contactPhone, String contactAddress) {
        return facade.createSupplier(
                ppn, bankAccount, name, isDelivering,
                paymentCondition, regularSupplyingDays, contactName,
                contactPhone, contactAddress
        );
    }

    public Collection<Supplier> getSuppliers() {
        return facade.getSuppliers();
    }

    public Facade.ResponseT<Supplier> getSupplier(int ppn) throws BusinessLogicException {
        return facade.getSupplier(ppn);
    }

    public Facade.SI_Response deleteSupplier(int ppn) {
        return facade.deleteSupplier(ppn);
    }

    public Facade.ResponseT<Item> createItem(int supplierPPN, int catalogNumber, int productID, float price) {
        return facade.createItem(supplierPPN, catalogNumber, productID, price);
    }

    public Collection<Item> getItems() {
        return facade.getItems();
    }

    public Facade.ResponseT<Item> getItem(int ppn, int catalog) {
        return facade.getItem(ppn, catalog);
    }

    public Facade.ResponseT<QuantityDiscount> createDiscount(int supplierPPN, int catalogN, int amount, float discount) {
        return facade.createDiscount(supplierPPN, catalogN, amount, discount);
    }

    public Facade.SI_Response deleteDiscount(QuantityDiscount discount) {
        return facade.deleteDiscount(discount);
    }

    public Facade.ResponseT<Order> createOrder(int supplierPPN, LocalDate ordered, LocalDate delivered, OrderType type) {
        return facade.createOrder(supplierPPN, ordered, delivered, type);
    }

    public Collection<Order> getOrders() {
        return facade.getOrders();
    }

    public Facade.SI_Response deleteOrder(int orderId) {
        return facade.deleteOrder(orderId);
    }

    public QuantityDiscount getDiscount(int amount, int ppn, int catalog) throws BusinessLogicException {
        return facade.getDiscount(amount, ppn, catalog);
    }

    public Facade.SI_Response orderItem(int orderId, int supplier, int catalogNumber, int amount) {
        return facade.orderItem(orderId, supplier, catalogNumber, amount);
    }

    public Facade.SI_Response setPrice(int supplier, int catalogNumber, float price) {
        return facade.setPrice(supplier, catalogNumber, price);
    }

    public Collection<QuantityDiscount> getDiscounts() {
        return facade.getDiscounts();
    }

    public Facade.SI_Response setOrderOrdered(int orderID, LocalDate ordered) throws BusinessLogicException {
        return facade.setOrderOrdered(orderID, ordered);
    }

    public Facade.SI_Response setOrderProvided(int orderID, LocalDate provided) throws BusinessLogicException {
        return facade.setOrderProvided(orderID, provided);
    }

    public Facade.SI_Response setSupplierBankAccount(int supplierPPN, int bankAct) {
        return facade.setSupplierBankAccount(supplierPPN, bankAct);
    }

    public Facade.SI_Response setSupplierCompanyName(int supplierPPN, String newName) {
        return facade.setSupplierCompanyName(supplierPPN, newName);
    }

    public Facade.SI_Response setSupplierIsDelivering(int supplierPPN, boolean newValue) {
        return facade.setSupplierIsDelivering(supplierPPN, newValue);
    }

    public Facade.SI_Response setSupplierPaymentCondition(int supplierPPN, PaymentCondition payment) {

        return facade.setSupplierPaymentCondition(supplierPPN, payment);
    }

    public Facade.SI_Response setSupplierRegularSupplyingDays(int supplierPPN, DayOfWeek dayOfWeek) {
        return facade.setSupplierRegularSupplyingDays(supplierPPN, dayOfWeek);
    }

    public Facade.SI_Response setSupplierContact(int supplierPPN, String name, String phoneNumber, String address) {

        return facade.setSupplierContact(supplierPPN, name, phoneNumber, address);
    }

    public Facade.SI_Response updateOrderAmount(int orderID, int supplier, int catalogNumber, int amount) {
        return facade.updateOrderAmount(orderID, supplier, catalogNumber, amount);
    }

    public Facade.ResponseT<Integer> createOrderShortage(Facade.ResponseT<Boolean> r, int product_id, int min_qty) {
        return facade.createOrderShortage(r, product_id, min_qty);
    }

    public Facade.ResponseT<List<Integer>> getReportListIds() {
        return facade.getReportListIds();
    }

    public Facade.SI_Response cancelTruckingWithOrderID(int orderID){
        return facade.cancelTruckingWithOrderID(orderID);
    }
}

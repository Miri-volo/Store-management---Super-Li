package groupk.shared.PresentationLayer.Suppliers;

import groupk.shared.business.Facade;
import groupk.shared.business.Suppliers.BussinessObject.Item;
import groupk.shared.business.Suppliers.BussinessObject.Supplier;
import groupk.inventory_suppliers.dataLayer.dao.records.PaymentCondition;
import groupk.shared.service.Service;
import groupk.shared.service.dto.Employee;

import static groupk.shared.service.ServiceBase.*;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Scanner;

public class SupplierPresentationFacade {
    private UserInput input = UserInput.getInstance();
    private UserOutput output = UserOutput.getInstance();
    private final Service service;
    private Facade.SI_Response r;

    public SupplierPresentationFacade(Service service) {
        this.service = service;
    }

    public void startSupplierMenu(Employee currentUser) {
        // TODO Use currentUser.role to check the role of the active user, when currentUser is null no user is logged in.
        while (true) {
            int userInput = input.nextInt(Menu.getMainMenu());
            switch (userInput) {
                case (1): {//Supplier Menu
                    try {
                        if (currentUser == null || !(currentUser.role.equals(Employee.Role.Stocker) ||
                                currentUser.role.equals(Employee.Role.StoreManager)))
                            throw new IllegalAccessException("Only Store Manager or Stocker can handle supplier cards");

                    } catch (Exception e) {
                        output.println(e.getMessage());
                        break;
                    }
                    userInput = input.nextInt(Menu.getSupplierSubmenu());
                    switch (userInput) {
                        case (1): {
                            //Create Supplier Card
                            int ppn = input.nextInt("Enter supplier's ppn number: ");
                            int bankAccount = input.nextInt("Enter supplier's bank account number: ");
                            String name = input.nextString("Enter supplier's company name: ");
                            boolean isDelivering = input.nextBoolean("Is the supplier delivering by himself?");
                            PaymentCondition paymentCondition = choosePayment("Which way will the supplier pay? ");
                            DayOfWeek day = isDelivering ? chooseDay() : null;
                            String contactName = input.nextString("Enter the supplier's contact name: ");
                            String address = input.nextAddress("Enter the supplier's contact address: ");
                            String phoneNum = input.nextPhone("Enter the supplier's contact phone number: ");
                            Facade.ResponseT<Supplier> supplier = service.createSupplier(ppn, bankAccount, name, isDelivering,
                                    paymentCondition, day, contactName, phoneNum, address);
                            if (supplier.success) {
                                output.print(supplier.data.toString());
                            } else {
                                output.println(supplier.error);
                            }
                            break;
                        }
                        case (2): {
                            //Edit existing supplier card
                            int ppn = checkPPN("Enter the ppn number: ");
                            output.println("What do you want to edit? ");
                            int edit = input.nextInt(Menu.getSupplierEditSubmenu());
                            try {
                                switch (edit) {
                                    case (1): {
                                        //Edit bank account
                                        int bankAct = input.nextInt("Enter bank account: ");
                                        r = service.setSupplierBankAccount(ppn, bankAct);
                                        if (!r.success)
                                            output.println(r.error);
                                        break;
                                    }
                                    case (2): {
                                        //Edit company name
                                        String newName = input.nextString("Enter name: ");
                                        r = service.setSupplierCompanyName(ppn, newName);
                                        if (!r.success)
                                            output.println(r.error);
                                        break;
                                    }
                                    case (3): {
                                        //Edit delivery
                                        boolean newValue = input.nextBoolean("Is delivering?");
                                        r = service.setSupplierIsDelivering(ppn, newValue);
                                        if (!r.success)
                                            output.println(r.error);
                                        break;
                                    }
                                    case (4): {
                                        //edit payment condition
                                        PaymentCondition payment = choosePayment(
                                                "Which way will the supplier pay? ");
                                        r = service.setSupplierPaymentCondition(ppn, payment);
                                        if (!r.success)
                                            output.println(r.error);
                                        break;
                                    }
                                    case (5): {
                                        //edit supplying days
                                        r = service.setSupplierRegularSupplyingDays(ppn, chooseDay());
                                        if (!r.success)
                                            output.println(r.error);
                                        break;
                                    }
                                    case (6): {
                                        //Edit contact
                                        String contactName = input.nextString("Enter the supplier's contact name: ");
                                        String address = input.nextAddress("Enter the supplier's contact address: ");
                                        String phoneNum = input.nextPhone(
                                                "Enter the supplier's contact phone number: ");
                                        r = service.setSupplierContact(ppn, contactName, phoneNum, address);
                                        if (!r.success)
                                            output.println(r.error);
                                        break;
                                    }

                                }

                            } catch (Exception e) {
                                output.print(e.getMessage());
                            }
                            break;
                        }
                        case (3): {
                            //Delete existing supplier
                            int ppn = checkPPN("Enter the ppn number: ");
                            r = service.deleteSupplier(ppn);
                            if (!r.success)
                                output.println(r.error);
                            break;
                        }
                        case (4): {
                            //See summery of all suppliers
                            service.getSuppliers().forEach(s -> output.println(s.toString()));
                            break;
                        }
                    }
                    break;
                }
                case (2): {//Item menu
                    try {
                        if (currentUser == null || !currentUser.role.equals(Employee.Role.Stocker))
                            throw new IllegalAccessException("Only Stocker can handle items");
                    } catch (Exception e) {
                        output.println(e.getMessage());
                        break;
                    }
                    userInput = input.nextInt(Menu.getItemSubmenu());
                    switch (userInput) {
                        case (1): {
                            //Create new item
                            int ppn = checkPPN("Enter the supplier's ppn number: ");
                            int catalog = input.nextInt("Enter the item's catalog number: ");
                            int productNumber = input.nextInt("Enter product number:");
                            float price = (float) input.nextInt("Enter the item's price: ");
                            r = service.createItem(ppn, catalog, productNumber, price);
                            if(!r.success){
                                output.println(r.error);
                                break;
                            }else {
                                output.print(r.toString());
                                break;
                            }
                        }
                        case (2): {
                            //edit price of existing item
                            int[] arr = checkItem();
                            Item item = service.getItem(arr[0], arr[1]).data;
                            r = service.setPrice(item.getSupplier().getPpn(), item.getCatalogNumber(),
                                    input.nextFloat("Enter new price: "));
                            if (!r.success)
                                output.println(r.error);
                            break;
                        }
                        case (3): {
                            //see summery of items
                            output.println(service.getItems().toString());
                            break;
                        }
                    }
                    break;
                }
                case (3): { //Order menu
                    try {
                        if (currentUser == null || !currentUser.role.equals(Employee.Role.Stocker))
                            throw new IllegalAccessException("Only Stocker can handle orders");
                    } catch (Exception e) {
                        output.println(e.getMessage());
                        break;
                    }
                    userInput = input.nextInt(Menu.getOrderSubmenu());
                    switch (userInput) {
                        case (1): {
                            //delete existing order
                            int id = input.nextInt("Enter order ID: ");
                            if (!service.getOrder(id).success) {
                                output.println(service.getOrder(id).error);
                                break;
                            }
                            r = service.deleteOrder(service.getOrder(id).data.getId());
                            if (!r.success)
                                output.println(r.error);
                            break;
                        }
                        case (2): {
                            //edit ordered date
                            int id = input.nextInt("Enter order's id number, see summery for info ");
                            checkId(id);
                            LocalDate delivered = input.nextDate("When is the order ordered? ");
                            r = service.setOrderOrdered(id, delivered);
                            if (!r.success)
                                output.println(r.error);
                            break;
                        }
                        case (3): {
                            //edit delivery date
                            int id = input.nextInt("Enter order's id number, see summery for info ");
                            checkId(id);
                            LocalDate delivered = input.nextDate("When is the order supposed to be delivered? ");
                            r = service.setOrderProvided(id, delivered);
                            if (!r.success)
                                output.println(r.error);
                            break;
                        }
                        case (4): {
                            int id = input.nextInt("Enter order's id number, see summery for info ");
                            checkId(id);
                            int[] itemCoords = checkItem();
                            Item item = service.getItem(itemCoords[0], itemCoords[1]).data;
                            r = service.updateOrderAmount(id, item.getSupplier().getPpn(),
                                    item.getCatalogNumber(), input.nextInt("Enter amount to order"));
                            if (!r.success)
                                output.println(r.error);
                            break;
                        }
                        case (5): {
                            //see summery of all orders
                            output.print(service.getOrders().toString());
                            break;
                        }
                    }
                    break;
                }
                case (4): {//Quantity agreement
                    try {
                        if (currentUser == null || !currentUser.role.equals(Employee.Role.Stocker))
                            throw new IllegalAccessException("Only Stocker can handle quantity agreements");
                    } catch (Exception e) {
                        output.println(e.getMessage());
                        break;
                    }
                    userInput = input.nextInt(Menu.getQuantityAgreementSubmenu());
                    switch (userInput) {
                        case (1): {
                            //create new quantity agreement
                            createDiscount();
                            break;
                        }
                        case (2): {
                            //edit existing quantity agreement
                            if (deleteDiscount())
                                createDiscount();
                            else break;
                        }
                        case (3): {
                            //delete existing quantity agreement
                            deleteDiscount();
                            break;
                        }
                        case (4): {
                            //summery of quantity discount
                            output.println(service.getDiscounts().toString());
                            break;
                        }
                    }
                    break;
                }
                case (5): {
                    return;
                }
                default: {
                    UserOutput.getInstance().println("Please select valid option.");
                }
            }

        }
    }

    private void createDiscount() {
        int[] arr = checkItem();
        int amount = input.nextInt("For which amount is the discount applicable?: ");
        float discount = input.nextFloat("What would be the discount for this amount?: ");
        output.print(service.createDiscount(service.getItem(arr[0], arr[1]).data.getSupplier().getPpn(),
                        service.getItem(arr[0], arr[1]).data.getCatalogNumber(), amount, discount)
                .data.toString());

    }

    private boolean deleteDiscount() {
        Facade.SI_Response r;
        int[] arr = checkDiscount();
        r = service.deleteDiscount(service.getDiscount(arr[2], arr[0], arr[1]));
        if (!r.success) {
            UserOutput.println(r.error);
            return false;
        }
        return true;

    }

    private int[] checkDiscount() {
        boolean retry = true;
        int[] arr = new int[]{};
        int amount = 0;
        while (retry) {
            arr = checkItem();
            amount = input.nextInt("Enter the discount amount you would like to delete: ");
            try {
                service.getDiscount(amount, arr[0], arr[1]);
                retry = false;
            } catch (Exception e) {
                output.println("This discount doesn't exist, try again.");
            }
        }
        return new int[]{arr[0], arr[1], amount};
    }

    private void checkId(int id) {
        boolean retry = true;
        int nextInt = 0;
        int curId = id;
        while (retry) {
            if (service.getOrder(curId).success)
                retry = false;
            else {
                curId = input.nextInt("The id doesn't exist please try again\n");
            }
        }
    }


    private int[] checkItem() {
        boolean retry = true;
        int nextInt = 0;
        int ppn = 0, catalog = 0;
        while (retry) {
            ppn = checkPPN("Enter the supplier's ppn number: ");
            catalog = input.nextInt("Enter the item's catalog number: ");
            if (service.getItem(ppn, catalog).success) {
                retry = false;
            } else output.println("The item doesn't exist, please try again.");

        }
        return new int[]{ppn, catalog};

    }

    private int checkPPN(String message) {
        boolean retry = true;
        int nextInt = 0;
        while (retry) {
            nextInt = input.nextInt(message);
            r = service.getSupplier(nextInt);
            if (r.success)
                retry = false;
            else
                UserOutput.println(r.error);
        }
        return nextInt;
    }


    private DayOfWeek chooseDay() {
        boolean retry = true;
        DayOfWeek day = null;
        String nextString = "";
        while (retry) {
            try {
                nextString = input.nextString("Enter constant day of week: ");
                DayOfWeek.valueOf(nextString.toUpperCase());
                retry = false;
            } catch (Exception e) {
                output.println("Please try again.");
            }
        }
        return DayOfWeek.valueOf(nextString.toUpperCase());
    }

    private PaymentCondition choosePayment(String s) {
        output.println(s);
        int pay = input.nextInt("1. Direct Debit\n2. Credit\n");
        return pay == 1 ? PaymentCondition.DirectDebit : PaymentCondition.Credit;
    }
}

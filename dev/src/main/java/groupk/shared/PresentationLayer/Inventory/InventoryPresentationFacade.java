package groupk.shared.PresentationLayer.Inventory;

import groupk.shared.PresentationLayer.Suppliers.UserOutput;
import groupk.shared.business.Facade;
import groupk.shared.service.Inventory.Objects.Product;
import groupk.shared.service.Inventory.Objects.ProductItem;
import groupk.shared.service.Inventory.Objects.Report;
import groupk.shared.service.Service;
import groupk.shared.service.dto.Employee;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InventoryPresentationFacade {
    private String[] args;
    private final Service service;

    public InventoryPresentationFacade(Service service) {
        this.service = service;
    }

    public void execute(String input, Employee currentUser) {
        // TODO Use currentUser.role to check the role of the active user, when currentUser is null no user is logged in.
        try {
            if (!input.equals("exit")) {
                String command = input.substring(0, input.indexOf(" "));
                args = input.substring(input.indexOf(" ") + 1).split(",", -1);
                switch (command) {
                    case "addCategory":
                        if (checkUserStorage(currentUser)) addCategory();
                        break;
                    case "removeCategory":
                        if (checkUserStorage(currentUser)) removeCategory();
                        break;
                    case "addSubCategory":
                        if (checkUserStorage(currentUser)) addSubCategory();
                        break;
                    case "removeSubCategory":
                        if (checkUserStorage(currentUser)) removeSubCategory();
                        break;
                    case "addSubSubCategory":
                        if (checkUserStorage(currentUser)) addSubSubCategory();
                        break;
                    case "removeSubSubCategory":
                        if (checkUserStorage(currentUser)) removeSubSubCategory();
                        break;
                    case "updateCategoryCusDiscount":
                        if (checkUserStorage(currentUser)) updateCategoryCusDiscount();
                        break;
                    case "updateProductCusDiscount":
                        if (checkUserStorage(currentUser)) updateProductCusDiscount();
                        break;
                    case "updateItemCusDiscount":
                        if (checkUserStorage(currentUser)) updateItemCusDiscount();
                        break;
                    case "updateProductCusPrice":
                        if (checkUserStorage(currentUser)) updateProductCusPrice();
                        break;
                    case "addProduct":
                        if (checkUserStorage(currentUser)) addProduct();
                        break;
                    case "removeProduct":
                        if (checkUserStorage(currentUser)) removeProduct();
                        break;
                    case "addItem":
                        if (checkUserStorage(currentUser)) addItem();
                        break;
                    case "removeItem":
                        if (checkUserStorage(currentUser)) removeItem();
                        break;
                    case "updateItemDefect":
                        if (checkUserStorage(currentUser)) updateItemDefect(currentUser);
                        break;
                    case "getItemLocation":
                        if (checkUserStorage(currentUser)) getItemLocation();
                        break;
                    case "changeItemLocation":
                        if (checkUserStorage(currentUser)) changeItemLocation();
                        break;
                    case "changeItemOnShelf":
                        if (checkUserStorage(currentUser)) changeItemOnShelf();
                        break;
                    case "createMissingReport":
                        if (checkUserStoreManager(currentUser)) createMissingReport(currentUser);
                        break;
                    case "createExpiredReport":
                        if (checkUserStoreManager(currentUser)) createExpiredReport(currentUser);
                        break;
                    case "createSurplusesReport":
                        if (checkUserStoreManager(currentUser)) createSurplusesReport(currentUser);
                        break;
                    case "createDefectiveReport":
                        if (checkUserStoreManager(currentUser)) createDefectiveReport(currentUser);
                        break;
                    case "createBySupplierReport":
                        if (checkUserStoreManager(currentUser)) createBySupplierReport(currentUser);
                        break;
                    case "createByProductReport":
                        if (checkUserStoreManager(currentUser)) createByProductReport(currentUser);
                        break;
                    case "createByCategoryReport":
                        if (checkUserStoreManager(currentUser)) createByCategoryReport(currentUser);
                        break;
                    case "removeReport":
                        if (checkUserStoreManager(currentUser)) removeReport();
                        break;
                    case "getReport":
                        if (checkUserStoreManager(currentUser)) getReport();
                        break;
                    case "createPeriodicOrder":
                        if (checkUserStorage(currentUser)) createPeriodicOrder();
                        break;
                    case "confirmOrder":
                        if (checkUserStorage(currentUser)) confirmOrder();
                        break;
                    default:
                        System.out.println("unknown command, aborting..");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid syntax for inventory module command");
        }
    }

    private boolean checkUserStoreManager(Employee currentUser) {
        if (currentUser == null || !currentUser.role.equals(Employee.Role.StoreManager)) {
            UserOutput.println("Only store manager can manage reports");
            return false;
        }
        return true;
    }

    private boolean checkUserStorage(Employee currentUser) {
        if (currentUser == null || !currentUser.role.equals(Employee.Role.Stocker)) {
            UserOutput.println("Only stock worker can handle orders and items");
            return false;
        }
        return true;
    }


    //service callers
    private void addCategory(/*addCategory [category]*/) {
        Facade.SI_Response r;
        if (args.length == 1) {
            r = service.addCategory(args[0]);
            if (!r.success)
                System.out.println(r.error);
        }
    }

    private void removeCategory(/*removeCategory [category]*/) {
        Facade.SI_Response r;
        if (args.length == 1) {
            r = service.removeCategory(args[0]);
            if (!r.success)
                System.out.println(r.error);
        }
    }

    private void addSubCategory(/*addSubCategory [category],[sub_category]*/) {
        Facade.SI_Response r;
        if (args.length == 2) {
            r = service.addSubCategory(args[0], args[1]);
            if (!r.success)
                System.out.println(r.error);
        }
    }

    private void removeSubCategory(/*removeSubCategory [category],[sub_category]*/) {
        Facade.SI_Response r;
        if (args.length == 2) {
            r = service.removeSubCategory(args[0], args[1]);
            if (!r.success)
                System.out.println(r.error);
        }
    }

    private void addSubSubCategory(/*addSubSubCategory [category],[sub_category],[sub_sub_category]*/) {
        Facade.SI_Response r;
        if (args.length == 3) {
            r = service.addSubSubCategory(args[0], args[1], args[2]);
            if (!r.success)
                System.out.println(r.error);
        }
    }

    private void removeSubSubCategory(/*removeSubSubCategory [category],[sub_category],[sub_sub_category]*/) {
        Facade.SI_Response r;
        if (args.length == 3) {
            r = service.removeSubSubCategory(args[0], args[1], args[2]);
            if (!r.success)
                System.out.println(r.error);
        }
    }

    private void updateCategoryCusDiscount(/*updateCategoryCusDiscount [discount],[start(YYYY-MM-DD)],[end(YYYY-MM-DD)],[category],[sub_category],[sub_sub_category]*/) {
        Facade.SI_Response r;
        if (args.length == 6 && convertDouble(args[0]) != -1.0 && convertDate(args[1]) != null && convertDate(args[2]) != null) {
            r = service.updateCategoryCusDiscount(convertFloat(args[0]), convertDate(args[1]), convertDate(args[2]), args[3], args[4], args[5]);
            if (!r.success)
                System.out.println(r.error);
        }
    }

    private void updateProductCusDiscount(/*updateProductCusDiscount [discount],[start(YYYY-MM-DD)],[end(YYYY-MM-DD)],[product_id]*/) {
        Facade.SI_Response r;
        if (args.length == 4 && convertDouble(args[0]) != -1.0 && convertDate(args[1]) != null && convertDate(args[2]) != null && convertDouble(args[3]) != -1) {
            r = service.updateProductCusDiscount(convertFloat(args[0]), convertDate(args[1]), convertDate(args[2]), convertInt(args[3]));
            if (!r.success)
                System.out.println(r.error);
        }
    }

    private void updateItemCusDiscount(/*updateItemCusDiscount [discount],[start(YYYY-MM-DD)],[end(YYYY-MM-DD)],[product_id],[item_id]*/) {
        Facade.SI_Response r;
        if (args.length == 5 && convertDouble(args[0]) != -1.0 && convertDate(args[1]) != null && convertDate(args[2]) != null && convertDouble(args[3]) != -1 && convertDouble(args[4]) != -1) {
            r = service.updateItemCusDiscount(convertFloat(args[0]), convertDate(args[1]), convertDate(args[2]), convertInt(args[3]), convertInt(args[4]));
            if (!r.success)
                System.out.println(r.error);
        }
    }

    private void updateProductCusPrice(/*updateProductCusPrice [product_id],[price]*/) {
        Facade.SI_Response r;
        if (args.length == 2 && convertDouble(args[1]) != -1.0 && convertInt(args[0]) != -1) {
            r = service.updateProductCusPrice(convertInt(args[0]), convertFloat(args[1]));
            if (!r.success)
                System.out.println(r.error);
        }
    }

    private void addProduct(/*addProduct [name],[customer_price],[minimum_quantity],[supply_time],[category],[sub_category],[sub_sub_category]*/) {
        Facade.ResponseT<Product> r;
        float cus_price = convertFloat(args[1]);
        int min_qty = convertInt(args[2]);
        int supply_time = convertInt(args[3]);
        if (args.length == 7 && cus_price != -1.0 && min_qty != -1 && supply_time != -1) {
            r = service.addProduct(args[0], null, 0, cus_price, min_qty, supply_time, args[4], args[5], args[6]);
            if (!r.success)
                System.out.println(r.error);
            else
                System.out.println(r.data);
        }
    }

    private void removeProduct(/*removeProduct [product_id]*/) {
        Facade.SI_Response r;
        if (args.length == 1 && convertInt(args[0]) != -1) {
            r = service.removeProduct(convertInt(args[0]));
            if (!r.success)
                System.out.println(r.error);
        }
    }

    private void addItem(/*addItem [product_id],[store],[location],[supplier],[expiration_date(YYYY-MM-DD)],[on_shelf]*/) {
        Facade.SI_Response r;
        int product_id = convertInt(args[0]);
        LocalDate expiration_date = convertDate(args[4]);
        if (args.length == 6 && product_id != -1 && expiration_date != null) {
            r = service.addItem(product_id, args[1], args[2], convertInt(args[3]), expiration_date, Boolean.TRUE.equals(convertBoolean(args[5])));
            if (!r.success)
                System.out.println(r.error);
        }
    }

    private void removeItem(/*removeItem [product_id],[item_id]*/) {
        Facade.SI_Response r;
        if (args.length == 2 && convertInt(args[0]) != -1 && convertInt(args[1]) != -1) {
            r = service.removeItem(convertInt(args[0]), convertInt(args[0]));
            if (!r.success)
                System.out.println(r.error);
        }
    }

    public void updateItemDefect(Employee e /*updateItemDefect [product_id],[item_id],[is_defect]*/) {
        Facade.SI_Response r;
        int product_id = convertInt(args[0]);
        int item_id = convertInt(args[1]);
        if (args.length == 3 && product_id != -1 && item_id != -1) {
            r = service.updateItemDefect(product_id, item_id, Boolean.TRUE.equals(convertBoolean(args[2])), e.name);
            if (!r.success)
                System.out.println(r.error);
        }
    }

    public void getItemLocation(/*getItemLocation [product_id],[item_id]*/) {
        Facade.ResponseT<String> r;
        if (args.length == 2 && convertInt(args[0]) != -1 && convertInt(args[1]) != -1) {
            r = service.getItemLocation(convertInt(args[0]), convertInt(args[0]));
            if (!r.success)
                System.out.println(r.error);
            else
                System.out.println(r.data);
        }
    }

    public void changeItemLocation(/*changeItemLocation [product_id],[item_id],[location]*/) {
        Facade.SI_Response r;
        if (args.length == 3 && convertInt(args[0]) != -1 && convertInt(args[1]) != -1) {
            r = service.setItemLocation(convertInt(args[0]), convertInt(args[0]), args[2]);
            if (!r.success)
                System.out.println(r.error);
        }
    }

    public void changeItemOnShelf(/*changeItemOnShelf [product_id],[item_id],[on_shelf]*/) {
        Facade.SI_Response r;
        if (args.length == 3 && convertInt(args[0]) != -1 && convertInt(args[1]) != -1) {
            r = service.setItemOnShelf(convertInt(args[0]), convertInt(args[0]), Boolean.TRUE.equals(convertBoolean(args[2])));
            if (!r.success)
                System.out.println(r.error);
        }
    }

    public void createMissingReport(Employee e /*createMissingReport [name]*/) {
        Facade.ResponseT<Report> r;
        if (args.length == 1) {
            r = service.createMissingReport(args[0], e.name);
            if (!r.success)
                System.out.println(r.error);
            else
                System.out.println(r.data);
        }
    }

    public void createExpiredReport(Employee e /*createExpiredReport [name]*/) {
        Facade.ResponseT<Report> r;
        if (args.length == 1) {
            r = service.createExpiredReport(args[0], e.name);
            if (!r.success)
                System.out.println(r.error);
            else
                System.out.println(r.data);
        }
    }

    public void createSurplusesReport(Employee e /*createSurplusesReport [name]*/) {
        Facade.ResponseT<Report> r;
        if (args.length == 1) {
            r = service.createSurplusesReport(args[0], e.name);
            if (!r.success)
                System.out.println(r.error);
            else
                System.out.println(r.data);
        }
    }

    public void createDefectiveReport(Employee e /*createDefectiveReport [name]*/) {
        Facade.ResponseT<Report> r;
        if (args.length == 1) {
            r = service.createDefectiveReport(args[0], e.name);
            if (!r.success)
                System.out.println(r.error);
            else
                System.out.println(r.data);
        }
    }

    public void createBySupplierReport(Employee e /*createBySupplierReport [name],[supplier_ppn]*/) {
        Facade.ResponseT<Report> r;
        if (args.length == 2) {
            r = service.createBySupplierReport(args[0], e.name, convertInt(args[1]));
            if (!r.success)
                System.out.println(r.error);
            else
                System.out.println(r.data);
        }
    }

    public void createByProductReport(Employee e /*createByProductReport [category],[product_name]*/) {
        Facade.ResponseT<Report> r;
        if (args.length == 2) {
            r = service.createByProductReport(args[0], e.name, args[1]);
            if (!r.success)
                System.out.println(r.error);
            else
                System.out.println(r.data);
        }
    }

    public void createByCategoryReport(Employee e /*createByCategoryReport [name],[category],[sub_category],[sub_sub_category]*/) {
        Facade.ResponseT<Report> r;
        if (args.length == 5) {
            r = service.createByCategoryReport(args[0], e.name, args[2], args[3], args[4]);
            if (!r.success)
                System.out.println(r.error);
            else
                System.out.println(r.data);
        }
    }

    public void removeReport(/*removeReport [report_id]*/) {
        Facade.SI_Response r;
        if (args.length == 1 && convertInt(args[0]) != -1) {
            r = service.removeReport(convertInt(args[0]));
            if (!r.success)
                System.out.println(r.error);
        }
    }

    public void getReport(/*getReport [report_id]*/) {
        Facade.ResponseT<Report> r;
        if (args.length == 1 && convertInt(args[0]) != -1) {
            r = service.getReport(convertInt(args[0]));
            if (!r.success)
                System.out.println(r.error);
            else
                System.out.println(r.data);
        }
    }

    private void createPeriodicOrder(/*createPeriodicOrder [product_id_0]-[amount_0]_[product_id_1]-[amount_1]...,[weekday]*/) {
        Facade.ResponseT<Integer> r;
        if (args.length == 2 && convertInt(args[1]) != -1) {
            r = service.createPeriodicOrder(convertMap(args[0]), convertInt(args[1]));
            if (!r.success)
                System.out.println(r.error);
            else
                System.out.println("Your order number is " + r.data);
        }
    }

    private void confirmOrder(/*confirmOrder [order_id]*/) {
        Facade.ResponseT<Map<Integer, Integer>> r = service.confirmOrder(convertInt(args[0]));
        if (!r.success)
            System.out.println(r.error);
        else {
            Map<Integer, Integer> order_details = r.data;
            System.out.println("Order details");
            for (Map.Entry<Integer, Integer> pair : order_details.entrySet())
                System.out.println(pair.getKey() + " - " + pair.getValue());
            System.out.println("please enter actual amount delivered\n(example format: \"[id0]-[amount0]_[id1]-[amount1]...\"):");
            Scanner scan = new Scanner(System.in);
            String input = scan.nextLine();
            Facade.ResponseT<List<ProductItem>> r2 = service.confirmOrderAmount(convertInt(args[0]), convertMap(input));
            if (!r2.success)
                System.out.println(r2.error);
            else {
                System.out.println("Successfully registered the following items:\n");
                for (ProductItem p : r2.data) {
                    System.out.println(p);
                }
            }
        }
    }


    //converters
    private Map<Integer, Integer> convertMap(String input) {
        Map<Integer, Integer> values = new HashMap<>();
        String[] pairs = input.split("_");
        for (String pair : pairs) {
            String[] kv = pair.split("-");
            values.put(convertInt(kv[0]), convertInt(kv[1]));
        }
        return values;
    }

    private Boolean convertBoolean(String input) {
        try {
            return Boolean.parseBoolean(input);
        } catch (Exception e) {
            System.out.println("failed to parse boolean, " + e.getMessage());
            return null;
        }
    }

    private double convertDouble(String input) {
        try {
            return Double.parseDouble(input);
        } catch (Exception e) {
            System.out.println("failed to parse double, " + e.getMessage());
            return -1.0;
        }
    }

    private float convertFloat(String input) {
        try {
            return Float.parseFloat(input);
        } catch (Exception e) {
            System.out.println("failed to parse float, " + e.getMessage());
            return -1;
        }
    }

    private LocalDate convertDate(String input) {
        try {
            DateTimeFormatter formatter;
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(input, formatter);
        } catch (Exception e) {
            System.out.println("failed to parse date");
            return null;
        }
    }

    private int convertInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (Exception e) {
            System.out.println("failed to parse int, " + e.getMessage());
            return -1;
        }
    }


}

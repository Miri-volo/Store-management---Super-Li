package groupk.shared.PresentationLayer.Suppliers;

public class Menu {

    public static String getMainMenu() {
        return new String("1. Supplier menu\n" +
                "2. Item Menu\n" +
                "3. Order Menu\n" +
                "4. Quantity Agreement Menu\n" +
                "5. Go back to main menu\n");
    }

    public static String getItemSubmenu() {
        return new String(
                "1. Create new item\n" +
                        "2. Edit price of existing item\n" +
                        "3. See summery of all items\n");
    }

    public static String getOrderSubmenu() {
        return new String(
                        "1. Delete existing order\n" +
                        "2. Edit the ordered date\n" +
                        "3. Edit the delivery data\n" +
                        "4. Edit the item's amount\n" +
                        "5. See summery of all orders\n");
    }

    public static String getSupplierSubmenu() {
        return new String(
                "1. Create new supplier card\n" +
                        "2. Edit existing supplier card\n" +
                        "3. Delete existing supplier\n" +
                        "4. See summery of all suppliers\n");
    }

    public static String getQuantityAgreementSubmenu() {
        return new String(
                "1. Create new quantity agreement\n" +
                        "2. Edit existing quantity agreement\n" +
                        "3. Delete existing quantity agreement\n" +
                        "4. See summery of all quantity agreements\n");
    }

    public static String getSupplierEditSubmenu() {
        return new String(
                        "1. Edit bank account number\n" +
                        "2. Edit supplier's company name\n" +
                        "3. Edit supplier's isDelivery status\n" +
                        "4. Edit supplier's payment condition\n" +
                        "5. Edit supplier's day of delivery\n" +
                        "6. Edit supplier's contact\n");
    }
}

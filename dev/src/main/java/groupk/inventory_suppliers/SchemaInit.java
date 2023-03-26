package groupk.inventory_suppliers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SchemaInit {

    public static void init(Connection conn) {
        try {
            String[] statements = schemaInit.split(";");
            Statement st = null;
            try {
                st = conn.createStatement();
                for (String line : statements) {

                    if (line.trim().length() > 0) {
                        st.execute(line);
                    }
                }
            } finally {
                if (st != null) st.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public final static String schemaInit =
            "BEGIN TRANSACTION;\n" +
                    "CREATE TABLE IF NOT EXISTS \"SubSubCategory\" (\n" +
                    "\t\"name\"\tTEXT NOT NULL,\n" +
                    "\t\"subcategory\"\tTEXT NOT NULL,\n" +
                    "\t\"category\"\tTEXT NOT NULL\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"Order\" (\n" +
                    "\t\"id\"\tINTEGER NOT NULL,\n" +
                    "\t\"orderType\"\tINTEGER NOT NULL,\n" +
                    "\t\"price\"\tREAL NOT NULL,\n" +
                    "\t\"ordered\"\tDATETIME NOT NULL,\n" +
                    "\t\"provided\"\tDATETIME NOT NULL,\n" +
                    "\t\"ppn\"\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(\"id\")\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"ItemInOrder\" (\n" +
                    "\t\"qty\"\tINTEGER NOT NULL,\n" +
                    "\t\"itemSupplierPPN\"\tINTEGER NOT NULL,\n" +
                    "\t\"itemCatalogNumber\"\tINTEGER NOT NULL,\n" +
                    "\t\"orderId\"\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(\"itemSupplierPPN\",\"itemCatalogNumber\",\"orderId\")\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"QuantityDiscount\" (\n" +
                    "\t\"id\"\tINTEGER NOT NULL,\n" +
                    "\t\"quantity\"\tINTEGER NOT NULL,\n" +
                    "\t\"discount\"\tREAL NOT NULL,\n" +
                    "\t\"itemSupplierPPN\"\tINTEGER NOT NULL,\n" +
                    "\t\"itemCatalogNumber\"\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(\"id\")\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"Subcategory\" (\n" +
                    "\t\"Category\"\tTEXT NOT NULL,\n" +
                    "\t\"Name\"\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(\"Name\",\"Category\")\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"Category\" (\n" +
                    "\t\"Name\"\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(\"Name\")\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"Supplier\" (\n" +
                    "\t\"ppn\"\tINTEGER NOT NULL,\n" +
                    "\t\"bankNumber\"\tINTEGER NOT NULL,\n" +
                    "\t\"name\"\tTEXT NOT NULL,\n" +
                    "\t\"isDelivering\"\tTINYINT NOT NULL,\n" +
                    "\t\"paymentCondition\"\tINTEGER NOT NULL,\n" +
                    "\t\"regularSupplyingDay\"\tINTEGER NOT NULL,\n" +
                    "\t\"contactAddress\"\tTEXT NOT NULL,\n" +
                    "\t\"contactName\"\tTEXT NOT NULL,\n" +
                    "\t\"contactPhone\"\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(\"ppn\")\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"Item\" (\n" +
                    "\t\"supplierPPN\"\tINTEGER NOT NULL,\n" +
                    "\t\"catalogNumber\"\tINTEGER NOT NULL,\n" +
                    "\t\"productId\"\tINTEGER NOT NULL,\n" +
                    "\t\"price\"\tFLOAT NOT NULL,\n" +
                    "\tPRIMARY KEY(\"supplierPPN\",\"catalogNumber\")\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"DiscountPair\" (\n" +
                    "\t\"ProductId\"\tINTEGER NOT NULL,\n" +
                    "\t\"ProductItemId\"\tINTEGER NOT NULL,\n" +
                    "\t\"DiscountPairId\"\tINTEGER NOT NULL,\n" +
                    "\t\"Discount\"\tFLOAT NOT NULL,\n" +
                    "\t\"StartDate\"\tDateTime NOT NULL,\n" +
                    "\t\"EndDate\"\tDateTime NOT NULL,\n" +
                    "\tPRIMARY KEY(\"DiscountPairId\",\"ProductItemId\",\"ProductId\")\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"ProductItem\" (\n" +
                    "\t\"ProductId\"\tINTEGER NOT NULL,\n" +
                    "\t\"Id\"\tNUMERIC NOT NULL,\n" +
                    "\t\"Store\"\tTEXT NOT NULL,\n" +
                    "\t\"Location\"\tTEXT NOT NULL,\n" +
                    "\t\"Supplier\"\tINTEGER NOT NULL,\n" +
                    "\t\"ExpirationDate\"\tDATETIME NOT NULL,\n" +
                    "\t\"IsDefect\"\tTINYINT NOT NULL,\n" +
                    "\t\"OnShelf\"\tTINYINT NOT NULL,\n" +
                    "\t\"DefectReporter\"\tTEXT,\n" +
                    "\tPRIMARY KEY(\"ProductId\",\"Id\")\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"ProductInReport\" (\n" +
                    "\t\"reportId\"\tINTEGER NOT NULL,\n" +
                    "\t\"productId\"\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(\"reportId\",\"productId\")\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"ProductItemInReport\" (\n" +
                    "\t\"reportId\"\tINTEGER NOT NULL,\n" +
                    "\t\"productId\"\tINTEGER NOT NULL,\n" +
                    "\t\"productItemId\"\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(\"reportId\",\"productId\",\"productItemId\")\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"Product\" (\n" +
                    "\t\"id\"\tINTEGER NOT NULL,\n" +
                    "\t\"name\"\tTEXT NOT NULL,\n" +
                    "\t\"customerPrice\"\tREAL NOT NULL,\n" +
                    "\t\"minQty\"\tINTEGER NOT NULL,\n" +
                    "\t\"storageQty\"\tINTEGER NOT NULL,\n" +
                    "\t\"shelfQty\"\tINTEGER NOT NULL,\n" +
                    "\t\"subSubcategory\"\tTEXT NOT NULL,\n" +
                    "\t\"subcategory\"\tTEXT NOT NULL,\n" +
                    "\t\"category\"\tTEXT NOT NULL,\n" +
                    "\t\"item_ids\"\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(\"id\")\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"Report\" (\n" +
                    "\t\"id\"\tINTEGER NOT NULL,\n" +
                    "\t\"reportProducer\"\tTEXT NOT NULL,\n" +
                    "\t\"name\"\tTEXT NOT NULL,\n" +
                    "\t\"date\"\tDATETIME NOT NULL,\n" +
                    "\t\"type\"\tINTEGER NOT NULL,\n" +
                    "\t\"query\"\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(\"id\")\n" +
                    ");\n" +

                    "CREATE TABLE IF NOT EXISTS \"OrderMap\" (\n" +
                    "\t\"OrderId\"\tINTEGER NOT NULL,\n" +
                    "\t\"ProductId\"\tINTEGER NOT NULL,\n" +
                    "\t\"Amount\"\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(\"OrderId\",\"ProductId\")\n" +
                    ");\n" +
                    "COMMIT;\n";
}

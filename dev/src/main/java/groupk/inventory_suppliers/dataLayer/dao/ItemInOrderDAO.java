package groupk.inventory_suppliers.dataLayer.dao;

import groupk.inventory_suppliers.dataLayer.dao.records.ItemInOrderRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class ItemInOrderDAO extends BaseDAO<ItemInOrderRecord.ItemInOrderKey, ItemInOrderRecord> {

    public ItemInOrderDAO(Connection conn) {
        super(conn);
    }


    @Override
    Stream<ItemInOrderRecord> fetchAll() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM ItemInOrder");
        ResultSet query = ps.executeQuery();
        ArrayList<ItemInOrderRecord> list = new ArrayList<>();
        while (query.next()) {
            ItemInOrderRecord record = new ItemInOrderRecord(
                    query.getInt("itemSupplierPPN"),
                    query.getInt("itemCatalogNumber"),
                    query.getInt("orderId"),
                    query.getInt("qty")
            );
            list.add(record);
        }
        return list.stream();
    }

    @Override
    ItemInOrderRecord fetch(ItemInOrderRecord.ItemInOrderKey key) throws SQLException, NoSuchElementException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM ItemInOrder " +
                "WHERE itemSupplierPPN=? AND itemCatalogNumber=? AND orderId=?");
        ps.setInt(1, key.ppn);
        ps.setInt(2, key.catalogNumber);
        ps.setInt(3, key.orderId);
        ResultSet query = ps.executeQuery();
        if (query.next()) {
            ItemInOrderRecord record = new ItemInOrderRecord(key.ppn,
                    key.catalogNumber, key.orderId, query.getInt("qty"));
            return record;
        }
        throw new NoSuchElementException();
    }

    /*
    CREATE TABLE "ItemInOrder" (
	"qty"	INTEGER NOT NULL,
	"itemSupplierPPN"	INTEGER NOT NULL,
	"itemCatalogNumber"	INTEGER NOT NULL,
	"orderId"	INTEGER NOT NULL,
	PRIMARY KEY("itemSupplierPPN","itemCatalogNumber","orderId")
     */

    @Override
    protected int runDeleteQuery(ItemInOrderRecord.ItemInOrderKey key) {
        return runUpdate("DELETE FROM ItemInOrder WHERE itemSupplierPPN=? " +
                        "AND itemCatalogNumber=? AND orderId=?",
                ps -> ps.setInt(1, key.ppn),
                ps -> ps.setInt(2, key.catalogNumber),
                ps -> ps.setInt(3, key.orderId));
    }

    public void updateAmount(int orderId, int ppn, int catalogNumber, int amount) {
        ItemInOrderRecord record = getOrCreate(orderId, ppn, catalogNumber);
        record.setAmount(amount);
        runUpdate("UPDATE ItemInOrder SET qty=?" +
                        "WHERE itemSupplierPPN=? " +
                        " AND itemCatalogNumber=? AND orderId=?",
                ps -> ps.setInt(1, amount),
                ps -> ps.setInt(2, ppn),
                ps -> ps.setInt(3, catalogNumber),
                ps -> ps.setInt(4, orderId)
        );
    }

    public ItemInOrderRecord getOrCreate(int orderId, int ppn, int catalogNumber) {
        ItemInOrderRecord.ItemInOrderKey key = new ItemInOrderRecord.ItemInOrderKey(ppn, catalogNumber, orderId);
        if (exists(key)) {
            return get(key);
        }
        return create(ppn, catalogNumber, orderId);
    }

    private ItemInOrderRecord create(int ppn, int catalogNumber, int orderId) {
        return create(
                () -> new ItemInOrderRecord(ppn, catalogNumber, orderId, 0),

                "INSERT INTO ItemInOrder(itemSupplierPPN, itemCatalogNumber," +
                        "orderId, qty) VALUES(?, ?, ?, 0)",

                ps -> ps.setInt(1, ppn),
                ps -> ps.setInt(2, catalogNumber),
                ps -> ps.setInt(3, orderId)
        );
    }

    public void deleteAllInOrder(int orderID) {
        runUpdate("DELETE FROM ItemInOrder WHERE orderId=?",
                ps -> ps.setInt(1, orderID));
    }

    public void deleteItem(int ppn, int catalogNumber) {
        runUpdate(
                "DELETE FROM ItemInOrder where itemSupplierPPN=? AND itemCatalogNumber=?",
                ps -> ps.setInt(1, ppn),
                ps -> ps.setInt(2, catalogNumber)
        );
    }
}

package groupk.inventory_suppliers.dataLayer.dao;

import groupk.inventory_suppliers.dataLayer.dao.records.ItemRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ItemData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
public class ItemDAO extends BaseDAO<ItemRecord.ItemKey, ItemRecord> {
    public ItemDAO(Connection conn) {
        super(conn);
    }

    public ItemData create(int supplierPPN, int catalogNumber,
                   int productId, float price) {
        return create(
                () -> new ItemRecord(supplierPPN, catalogNumber, productId, price),
                "INSERT INTO Item(supplierPPN, catalogNumber, productId, price) " +
                        "VALUES(?, ?, ?, ?)",
                ps -> ps.setInt(1, supplierPPN),
                ps -> ps.setInt(2, catalogNumber),
                ps -> ps.setInt(3, productId),
                ps -> ps.setFloat(4, price)
        );
    }

    @Override
    ItemRecord fetch(ItemRecord.ItemKey key) throws SQLException, NoSuchElementException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM Item WHERE supplierPPN=? AND catalogNumber=?"
        );
        ps.setInt(1, key.ppn);
        ps.setInt(2, key.catalogNumber);
        ResultSet query = ps.executeQuery();
        if(!query.next()) {
            throw new NoSuchElementException("Item: " + key.ppn + ", " + key.catalogNumber);
        }
        return new ItemRecord(
                key.ppn, key.catalogNumber,
                query.getInt("productId"),
                query.getFloat("price")
        );
    }

    public void updatePrice(ItemRecord.ItemKey item, float price) {
        runUpdate("UPDATE Item SET price=? WHERE supplierPPN=? AND catalogNumber=?",
                ps -> ps.setFloat(1, price),
                ps -> ps.setInt(2, item.ppn),
                ps -> ps.setInt(3, item.catalogNumber)
        );
        get(item).setPrice(price);
    }

    @Override
    Stream<ItemRecord> fetchAll() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM Item"
        );
        ResultSet query = ps.executeQuery();
        ArrayList<ItemRecord> ret = new ArrayList<>();

        while(query.next()) {
            ret.add(new ItemRecord(
                    query.getInt("supplierPPN"),
                    query.getInt("catalogNumber"),
                    query.getInt("productId"),
                    query.getFloat("price")
            ));
        }
        return ret.stream();
    }

    @Override
    protected int runDeleteQuery(ItemRecord.ItemKey itemKey) {
        return runUpdate("DELETE FROM Item WHERE supplierPPN=? AND catalogNumber=?",
                ps -> ps.setInt(1, itemKey.ppn),
                ps -> ps.setInt(2, itemKey.catalogNumber));
    }
}

package groupk.inventory_suppliers.dataLayer.dao;

import groupk.inventory_suppliers.dataLayer.dao.records.ProductRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ProductData;


import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * CREATE TABLE "Product" (
 * "id"	INTEGER NOT NULL,
 * "name"	TEXT NOT NULL,
 * "customerPrice"	REAL NOT NULL,
 * "minQty"	INTEGER NOT NULL,
 * "storageQty"	INTEGER NOT NULL,
 * "shelfQty"	INTEGER NOT NULL,
 * "subSubcategory"	TEXT NOT NULL,
 * "subcategory"	TEXT NOT NULL,
 * "category"	TEXT NOT NULL,
 * )
 **/
public class ProductDAO extends BaseDAO<Integer, ProductRecord> {

    private int maxId;

    public ProductDAO(Connection conn) {
        super(conn);
        maxId = oneResultQuery("SELECT MAX(Id) FROM Product", rs -> rs.getInt(1));
    }

    public ProductData create(int item_ids, int id, String name, float customerPrice, int minQty, int storageQty, int shelfQty, String category, String subcategory, String subSubcategory) {
        ProductRecord record = create(
                () -> new ProductRecord(item_ids, id, name, customerPrice, minQty, storageQty, shelfQty, category, subcategory, subSubcategory),
                "INSERT INTO Product(" + "item_ids,id,name,customerPrice," + "minQty,storageQty,shelfQty," + "subSubcategory,subcategory,category" + ")" +
                        " VALUES(?,?,?,?,?,?,?,?,?,?)",
                ps -> ps.setInt(1, item_ids),
                ps -> ps.setInt(2, id),
                ps -> ps.setString(3, name),
                ps -> ps.setFloat(4, customerPrice),
                ps -> ps.setInt(5, minQty),
                ps -> ps.setInt(6, storageQty),
                ps -> ps.setInt(7, shelfQty),
                ps -> ps.setString(8, subSubcategory),
                ps -> ps.setString(9, subcategory),
                ps -> ps.setString(10, category)
        );
        if (id > maxId)
            maxId = id;
        return record;
    }

    @Override
    ProductRecord fetch(Integer id) throws SQLException, NoSuchElementException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Product WHERE id=?");
        stmt.setInt(1, id);
        ResultSet query = stmt.executeQuery();
        if (!query.next()) {
            throw new NoSuchElementException("Product: " + id);
        }
        return readOne(id, query);
    }

    @Override
    Stream<ProductRecord> fetchAll() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Product");
        ResultSet query = stmt.executeQuery();
        ArrayList<ProductRecord> res = new ArrayList<>();
        while (query.next()) {
            res.add(readOne(query.getInt("id"), query));
        }
        return res.stream();
    }

    @Override
    public int runDeleteQuery(Integer id) {
        return runUpdate("DELETE FROM Product WHERE id=?", ps -> ps.setInt(1, id));
    }

    public int updateCusPrice(int id, float cus_price) {
        return update(id, "customerPrice", cus_price, Types.FLOAT);
    }

    public int updateShelfQty(int id, int shelfQty) {
        return update(id, "shelfQty", shelfQty, Types.INTEGER);
    }

    public int updateStorageQty(int id, int storageQty) {
        return update(id, "storageQty", storageQty, Types.INTEGER);
    }

    public int updateItemIds(int id, int item_ids) {
        return update(id, "item_ids", item_ids, Types.INTEGER);
    }

    public int getMaxId() {
        return maxId;
    }

    private int update(int id, String field, Object value, int type) {
        return runUpdate("UPDATE Product SET " + field + "=? WHERE id=?", ps -> ps.setObject(1, value, type), ps -> ps.setInt(2, id));
    }

    private ProductRecord readOne(Integer id, ResultSet query) throws SQLException {
        return new ProductRecord(
                query.getInt("item_ids"),
                id,
                query.getString("name"),
                query.getFloat("customerPrice"),
                query.getInt("minQty"),
                query.getInt("storageQty"),
                query.getInt("shelfQty"),
                query.getString("category"),
                query.getString("subcategory"),
                query.getString("subSubcategory"));
    }

}

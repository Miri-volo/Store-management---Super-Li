package groupk.inventory_suppliers.dataLayer.dao;

import groupk.inventory_suppliers.dataLayer.dao.records.ProductItemRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ProductItemData;


import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class ProductItemDAO extends BaseDAO<ProductItemRecord.ProductItemKey, ProductItemRecord> {
    public static final String TABLE_NAME = "ProductItem";
    public static final String PRODUCT_ID = "ProductId";
    public static final String ID = "Id";
    public static final String STORE = "Store";
    public static final String LOCATION = "Location";
    public static final String SUPPLIER = "Supplier";
    public static final String EXPIRATION_DATE = "ExpirationDate";
    public static final String IS_DEFECT = "IsDefect";
    public static final String ON_SHELF = "OnShelf";
    public static final String DEFECT_REPORTER = "DefectReporter";

    public ProductItemDAO(Connection conn) {
        super(conn);
    }

    @Override
    ProductItemRecord fetch(ProductItemRecord.ProductItemKey key) throws SQLException, NoSuchElementException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + PRODUCT_ID + " = ? AND " + ID + " =?"
        );
        ps.setInt(1, key.product_id);
        ps.setInt(2, key.id);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next())
            return readOne(key, resultSet);
        else
            throw new NoSuchElementException("no product item with product id: " + key.product_id + " and id " + key.id);
    }

    @Override
    Stream<ProductItemRecord> fetchAll() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TABLE_NAME);
        ResultSet query = stmt.executeQuery();
        ArrayList<ProductItemRecord> res = new ArrayList<>();
        while (query.next()) {
            res.add(readOne(new ProductItemRecord.ProductItemKey(query.getInt(PRODUCT_ID), query.getInt(ID)), query));
        }
        return res.stream();
    }

    public ProductItemData create(int product_id, int id, String store, String location, int supplier, Date expiration_date, boolean is_defect, boolean on_shelf, String defect_reporter) {
        ProductItemRecord response = create(
                () -> new ProductItemRecord(product_id, id, store, location, supplier, expiration_date, is_defect, on_shelf, defect_reporter),
                "INSERT INTO " + TABLE_NAME + " (" +
                        PRODUCT_ID + "," +
                        ID + "," +
                        STORE + "," +
                        LOCATION + "," +
                        SUPPLIER + "," +
                        EXPIRATION_DATE + "," +
                        IS_DEFECT + "," +
                        ON_SHELF + "," +
                        DEFECT_REPORTER +
                        ") VALUES(?,?,?,?,?,?,?,?,?)",
                ps -> ps.setInt(1, product_id),
                ps -> ps.setInt(2, id),
                ps -> ps.setString(3, store),
                ps -> ps.setString(4, location),
                ps -> ps.setInt(5, supplier),
                ps -> ps.setDate(6, expiration_date),
                ps -> ps.setBoolean(7, is_defect),
                ps -> ps.setBoolean(8, on_shelf),
                ps -> ps.setString(9, defect_reporter)
        );
        return response;
    }

    public int updateIsDefect(int product_id, int id, boolean is_defect) {
        return update(IS_DEFECT, is_defect, product_id, id, Types.BOOLEAN);
    }

    public int updateOnShelf(int product_id, int id, boolean on_shelf) {
        return update(ON_SHELF, on_shelf, product_id, id, Types.BOOLEAN);
    }

    public int updateDiscountIds(int product_id, int id, int discount_ids) {
        return update(ON_SHELF, discount_ids, product_id, id, Types.INTEGER);
    }

    public int updateDefectReporter(int product_id, int id, String defect_reporter) {
        return update(DEFECT_REPORTER, defect_reporter, product_id, id, Types.VARCHAR);
    }

    public int updateItemLocation(int product_id, int id, String location) {
        return update(LOCATION, location, product_id, id, Types.VARCHAR);
    }

    private int update(String field, Object value, int product_id, int id, int type) {
        return runUpdate(
                "UPDATE " + TABLE_NAME + " SET " + field + "=? WHERE " + PRODUCT_ID + " = ? AND " + ID + " = ?",
                ps -> ps.setObject(1, value, type),
                ps -> ps.setInt(2, product_id),
                ps -> ps.setInt(3, id));
    }

    @Override
    protected int runDeleteQuery(ProductItemRecord.ProductItemKey productItemKey) {
        return runUpdate(
                "DELETE FROM " + TABLE_NAME + " WHERE " + PRODUCT_ID + " = ? AND " + ID + " = ?",
                ps -> ps.setInt(1, productItemKey.product_id),
                ps -> ps.setInt(2, productItemKey.id)
        );
    }

    private ProductItemRecord readOne(ProductItemRecord.ProductItemKey key, ResultSet query) throws SQLException {
        return new ProductItemRecord(
                key.product_id,
                key.id,
                query.getString(STORE),
                query.getString(LOCATION),
                query.getInt(SUPPLIER),
                query.getDate(EXPIRATION_DATE),
                query.getBoolean(IS_DEFECT),
                query.getBoolean(ON_SHELF),
                query.getString(DEFECT_REPORTER));
    }
}

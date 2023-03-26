package groupk.inventory_suppliers.dataLayer.dao;

import groupk.inventory_suppliers.dataLayer.dao.records.ProductItemInReportRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ProductItemInReportData;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class ProductItemInReportDAO extends BaseDAO<Integer, ProductItemInReportRecord> {
    public static final String TABLE_NAME = "ProductItemInReport";
    public static final String REPORT_ID = "reportId";
    public static final String PRODUCT_ID = "productId";
    public static final String PRODUCT_ITEM_ID = "productItemId";

    public ProductItemInReportDAO(Connection conn) {
        super(conn);
    }

    @Override
    ProductItemInReportRecord fetch(Integer key) throws SQLException, NoSuchElementException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + REPORT_ID + " = ?"
        );
        ps.setInt(1, key);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next())
            return readOne(key, resultSet);
        else
            throw new NoSuchElementException("no product with report id: " + key);
    }

    @Override
    Stream<ProductItemInReportRecord> fetchAll() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TABLE_NAME);
        ResultSet query = stmt.executeQuery();
        ArrayList<ProductItemInReportRecord> res = new ArrayList<>();
        while (query.next()) {
            res.add(readOne(query.getInt(REPORT_ID), query));
        }
        return res.stream();
    }

    public ProductItemInReportData create(int report_id, int product_id, int productItem_id) {
        ProductItemInReportRecord response = create(
                () -> new ProductItemInReportRecord(report_id, product_id, productItem_id),
                "INSERT INTO " + TABLE_NAME + " (" +
                        REPORT_ID + "," +
                        PRODUCT_ID + "," +
                        PRODUCT_ITEM_ID +
                        ") VALUES(?, ?, ?)",
                ps -> ps.setInt(1, report_id),
                ps -> ps.setInt(2, product_id),
                ps -> ps.setInt(3, productItem_id)
        );
        return response;
    }

    @Override
    public int runDeleteQuery(Integer integer) {
        return runUpdate(
                "DELETE FROM " + TABLE_NAME + " WHERE " + REPORT_ID + " = ?",
                ps -> ps.setInt(1, integer)
        );
    }

    private ProductItemInReportRecord readOne(int key, ResultSet query) throws SQLException {
        return new ProductItemInReportRecord(
                key,
                query.getInt(PRODUCT_ID),
                query.getInt(PRODUCT_ITEM_ID));
    }
}

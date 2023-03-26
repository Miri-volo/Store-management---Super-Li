package groupk.inventory_suppliers.dataLayer.dao;

import groupk.inventory_suppliers.dataLayer.dao.records.ProductInReportRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ProductInReportData;


import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class ProductInReportDAO extends BaseDAO<Integer, ProductInReportRecord> {
    public static final String TABLE_NAME = "ProductInReport";
    public static final String REPORT_ID = "reportId";
    public static final String PRODUCT_ID = "productId";

    public ProductInReportDAO(Connection conn) {
        super(conn);
    }

    @Override
    ProductInReportRecord fetch(Integer key) throws SQLException, NoSuchElementException {
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
    Stream<ProductInReportRecord> fetchAll() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TABLE_NAME);
        ResultSet query = stmt.executeQuery();
        ArrayList<ProductInReportRecord> res = new ArrayList<>();
        while (query.next()) {
            res.add(readOne(query.getInt(REPORT_ID), query));
        }
        return res.stream();
    }

    public ProductInReportData create(int report_id, int product_id) {
        ProductInReportRecord response = create(
                () -> new ProductInReportRecord(report_id, product_id),
                "INSERT INTO " + TABLE_NAME + " (" +
                        REPORT_ID + "," +
                        PRODUCT_ID +
                        ") Values(?, ?)",
                ps -> ps.setInt(1, report_id),
                ps -> ps.setInt(2, product_id)
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

    private ProductInReportRecord readOne(int key, ResultSet query) throws SQLException {
        return new ProductInReportRecord(
                key,
                query.getInt(PRODUCT_ID));
    }
}

package groupk.inventory_suppliers.dataLayer.dao;

import groupk.inventory_suppliers.dataLayer.dao.records.OrderMapRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.OrderMapData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class OrderMapDAO extends BaseDAO<OrderMapRecord.OrderMapKey, OrderMapRecord> {
    public static final String TABLE_NAME = "OrderMap";
    public static final String ORDER_ID = "OrderId";
    public static final String PRODUCT_ID = "ProductId";
    public static final String AMOUNT = "Amount";

    public OrderMapDAO(Connection conn) {
        super(conn);
    }

    public OrderMapData create(int order_id, int product_id, int amount) {
        OrderMapRecord response = create(
                () -> new OrderMapRecord(order_id, product_id, amount),
                "INSERT INTO " + TABLE_NAME + " (" +
                        ORDER_ID + "," +
                        PRODUCT_ID + "," +
                        AMOUNT +
                        ") VALUES(?, ?, ?)",
                ps -> ps.setInt(1, order_id),
                ps -> ps.setInt(2, product_id),
                ps -> ps.setInt(3, amount)
        );
        return response;
    }

    @Override
    OrderMapRecord fetch(OrderMapRecord.OrderMapKey key) throws SQLException, NoSuchElementException {
        return null;
    }

    Stream<OrderMapRecord> fetch(int order_id) throws SQLException, NoSuchElementException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE "+ORDER_ID+" = ? ");
        ResultSet query = stmt.executeQuery();
        ArrayList<OrderMapRecord> res = new ArrayList<>();
        while (query.next()) {
            res.add(readOne(new OrderMapRecord.OrderMapKey(
                            query.getInt(ORDER_ID),
                            query.getInt(PRODUCT_ID)
                    ),
                    query));
        }
        return res.stream();
    }

    @Override
    Stream<OrderMapRecord> fetchAll() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TABLE_NAME);
        ResultSet query = stmt.executeQuery();
        ArrayList<OrderMapRecord> res = new ArrayList<>();
        while (query.next()) {
            res.add(readOne(new OrderMapRecord.OrderMapKey(
                            query.getInt(ORDER_ID),
                            query.getInt(PRODUCT_ID)
                    ),
                    query));
        }
        return res.stream();
    }

    public int update(OrderMapRecord.OrderMapKey key, int amount){
        return runUpdate(
                "UPDATE " + TABLE_NAME + " SET " + AMOUNT + "=? WHERE " + ORDER_ID + " = ? AND " + PRODUCT_ID + " = ?",
                ps -> ps.setInt(1, amount),
                ps -> ps.setInt(2, key.order_id),
                ps -> ps.setInt(3, key.product_id));
    }



    private OrderMapRecord readOne(OrderMapRecord.OrderMapKey key, ResultSet resultSet) throws SQLException {
        return new OrderMapRecord(
                key.order_id,
                key.product_id,
                resultSet.getInt(AMOUNT)
        );
    }

    @Override
    public int runDeleteQuery(OrderMapRecord.OrderMapKey orderMapKey) {
        return runUpdate(
                "DELETE FROM " + TABLE_NAME + " WHERE " + ORDER_ID + " = ? AND " + PRODUCT_ID + " = ?",
                ps -> ps.setInt(1, orderMapKey.order_id),
                ps -> ps.setInt(2, orderMapKey.product_id)
        );
    }
}

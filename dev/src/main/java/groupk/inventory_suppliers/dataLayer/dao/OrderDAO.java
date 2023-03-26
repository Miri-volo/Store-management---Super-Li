package groupk.inventory_suppliers.dataLayer.dao;

import groupk.inventory_suppliers.dataLayer.dao.records.OrderType;
import groupk.inventory_suppliers.dataLayer.dao.records.OrderRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.OrderData;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class OrderDAO extends BaseDAO<Integer, OrderRecord> {

    private int maxId;

    public OrderDAO(Connection conn) {
        super(conn);
        Integer maxIdQuery = oneResultQuery("SELECT MAX(id) FROM `Order`", rs -> rs.getInt(1));
        maxId = maxIdQuery == null ? 0 : 1 + maxIdQuery;
    }

    @Override
    OrderRecord fetch(Integer id) throws SQLException, NoSuchElementException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM `Order` WHERE id=?");
        ps.setInt(1, id);
        ResultSet qu = ps.executeQuery();
        if (!qu.next()) {
            throw new NoSuchElementException("No order " + id);
        }
        return readOneFromQuery(id, qu);
    }
    @Override
    Stream<OrderRecord> fetchAll() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM `Order`");
        ResultSet qu = ps.executeQuery();
        ArrayList<OrderRecord> list = new ArrayList<>();
        while (qu.next()) {
            list.add(readOneFromQuery(qu.getInt("id"), qu));
        }
        return list.stream();
    }

    public void updateProvided(int id, LocalDate provided) {
        runUpdate(
                "UPDATE `Order` SET provided=? WHERE id=?",
                ps -> ps.setDate(1, Date.valueOf(provided)),
                ps -> ps.setInt(2, id)
        );
        get(id).setProvided(provided);
    }

    public void updateOrdered(int id, LocalDate ordered) {
        runUpdate(
                "UPDATE `Order` SET ordered=? WHERE id=?",
                ps -> ps.setDate(1, Date.valueOf(ordered)),
                ps -> ps.setInt(2, id)
        );
        get(id).setOrdered(ordered);
    }

    @Override
    protected int runDeleteQuery(Integer integer) {
        return runUpdate("DELETE FROM \"Order\" WHERE id=?", ps->ps.setInt(1, integer));
    }


    private OrderRecord readOneFromQuery(Integer id, ResultSet qu) throws SQLException {
        return new OrderRecord(
                id,
                qu.getInt("ppn"),
                qu.getFloat("price"),
                qu.getDate("ordered").toLocalDate(),
                qu.getDate("provided").toLocalDate()
        );
    }

    public OrderData createOrder(int ppn, OrderType type, LocalDate ordered, LocalDate delivered) {
        int id = maxId + 1;
        maxId++;
        return create(
                () -> new OrderRecord(id, ppn, 0, ordered, delivered),
                "INSERT INTO `Order`(id, orderType, price, ordered, provided, ppn)" +
                        " VALUES(?, ?, ?, ?, ?, ?)",
                ps -> ps.setInt(1, id),
                ps -> ps.setInt(2, type.value),
                ps -> ps.setFloat(3, 0f),
                ps -> ps.setDate(4, Date.valueOf(ordered)),
                ps -> ps.setDate(5, Date.valueOf(delivered)),
                ps -> ps.setInt(6, ppn)
        );
    }

    public void setPrice(int id, float price) {
        int rowsAffects = runUpdate("UPDATE `Order` SET price=? WHERE id=?",
              ps -> ps.setFloat(1, price),
              ps -> ps.setInt(2, id)
            );
        if(rowsAffects > 0) {
            get(id).setPrice(price);
        }
    }
}
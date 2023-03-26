package groupk.inventory_suppliers.dataLayer.dao;

import groupk.inventory_suppliers.dataLayer.dao.records.ReportRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.ReportData;


import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class ReportDAO extends BaseDAO<Integer, ReportRecord> {
    public static final String TABLE_NAME = "Report";
    public static final String ID = "id";
    public static final String REPORT_PRODUCER = "reportProducer";
    public static final String NAME = "name";
    public static final String DATE = "date";
    public static final String TYPE = "type";
    public static final String QUERY = "query";

    private int maxId;

    public ReportDAO(Connection conn) {
        super(conn);
        maxId = oneResultQuery("SELECT MAX(" + ID + ") FROM " + TABLE_NAME, rs -> rs.getInt(1));
    }

    @Override
    ReportRecord fetch(Integer key) throws SQLException, NoSuchElementException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = ?"
        );
        ps.setInt(1, key);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next())
            return readOne(key, resultSet);
        else
            throw new NoSuchElementException("no report with id: " + key);
    }

    @Override
    Stream<ReportRecord> fetchAll() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TABLE_NAME);
        ResultSet query = stmt.executeQuery();
        ArrayList<ReportRecord> res = new ArrayList<>();
        while (query.next()) {
            res.add(readOne(query.getInt(ID), query));
        }
        return res.stream();
    }

    public ReportData create(int id, String report_producer, String name, Date date, int type, String query) {
        ReportRecord response = create(
                () -> new ReportRecord(id, report_producer, name, date, type, query),
                "INSERT INTO " + TABLE_NAME + " (" +
                        ID + "," +
                        REPORT_PRODUCER + "," +
                        NAME + "," +
                        DATE + "," +
                        TYPE + "," +
                        QUERY +
                        ") VALUES(?, ?, ?, ?, ?, ?)",
                ps -> ps.setInt(1, id),
                ps -> ps.setString(2, report_producer),
                ps -> ps.setString(3, name),
                ps -> ps.setDate(4, date),
                ps -> ps.setInt(5, type),
                ps -> ps.setString(6, query)
        );
        if (id > maxId)
            maxId = id;
        return response;
    }

    @Override
    public int runDeleteQuery(Integer integer) {
        return runUpdate(
                "DELETE FROM " + TABLE_NAME + " WHERE " + ID + " = ?",
                ps -> ps.setInt(1, integer)
        );
    }

    private ReportRecord readOne(int key, ResultSet query) throws SQLException {
        return new ReportRecord(
                key,
                query.getString(REPORT_PRODUCER),
                query.getString(NAME),
                query.getDate(DATE),
                query.getInt(TYPE),
                query.getString(QUERY));
    }

    public int getMaxId() {
        return maxId;
    }
}

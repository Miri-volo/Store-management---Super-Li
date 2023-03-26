package groupk.inventory_suppliers.dataLayer.dao;

import groupk.inventory_suppliers.dataLayer.dao.records.CategoryRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.CategoryData;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class CategoryDAO extends BaseDAO<String, CategoryRecord> {

    public CategoryDAO(Connection conn) {
        super(conn);
    }

    public CategoryData create(String name) {
        return create(
                () -> new CategoryRecord(name),
                "INSERT INTO Category(name) VALUES(?)",
                ps -> ps.setString(1, name)
        );
    }

    @Override
    CategoryRecord fetch(String key) throws SQLException, NoSuchElementException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM Category WHERE name=?");
        statement.setString(1, key);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
            return new CategoryRecord(key);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    Stream<CategoryRecord> fetchAll() throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT name FROM Category");
        ResultSet results = statement.executeQuery();

        ArrayList<CategoryRecord> list = new ArrayList<>();

        while(results.next()) {
            list.add(new CategoryRecord(results.getString(1)));
        }
        return list.stream();
    }

    @Override
    public int runDeleteQuery(String name) {
        return runUpdate("DELETE FROM Category WHERE name = ?",
                ps -> ps.setString(1, name));
    }

}

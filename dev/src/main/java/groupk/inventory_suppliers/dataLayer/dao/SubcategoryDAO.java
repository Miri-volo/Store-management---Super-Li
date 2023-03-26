package groupk.inventory_suppliers.dataLayer.dao;

import groupk.inventory_suppliers.dataLayer.dao.records.SubcategoryRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.SubcategoryData;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;


public class SubcategoryDAO extends BaseDAO<SubcategoryRecord.SubcategoryKey, SubcategoryRecord> {

    public SubcategoryDAO(Connection conn) {
        super(conn);
    }

    public SubcategoryData create(String category, String name) {
        return create(
                () -> new SubcategoryRecord(category, name) ,
                "INSERT INTO Subcategory(category, name) VALUES(?, ?)",
                ps -> ps.setString(1, category),
                ps -> ps.setString(2, name)
        );
    }

    @Override
    SubcategoryRecord fetch(SubcategoryRecord.SubcategoryKey key) throws SQLException, NoSuchElementException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT name, category FROM Subcategory WHERE name=? AND category=?"
        );
        ps.setString(1, key.name);
        ps.setString(2, key.category);
        ResultSet resultSet = ps.executeQuery();
        if(resultSet.next()) {
            return (new SubcategoryRecord(key.category, key.name));
        } else {
            throw new NoSuchElementException("no such subcategory: " + key.category + " > " + key.name);
        }
    }

    @Override
    Stream<SubcategoryRecord> fetchAll() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT category, name FROM Subcategory");
        ResultSet resultSet = ps.executeQuery();
        ArrayList<SubcategoryRecord> list = new ArrayList<>();
        while(resultSet.next()) {
            list.add(new SubcategoryRecord(resultSet.getString(1), resultSet.getString(2)));
        }
        return list.stream();
    }

    @Override
    protected int runDeleteQuery(SubcategoryRecord.SubcategoryKey key) {
        return runUpdate(
                "DELETE FROM Subcategory WHERE name=? AND category=?",
                ps -> ps.setString(1, key.name),
                ps -> ps.setString(2, key.category)
        );
    }

    public int runDeleteQuery(String category, String sub_category) {
        return runUpdate(
                "DELETE FROM Subcategory WHERE name=? AND category=?",
                ps -> ps.setString(1, sub_category),
                ps -> ps.setString(2, category)
        );
    }
}

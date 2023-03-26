package groupk.inventory_suppliers.dataLayer.dao;

import groupk.shared.business.Inventory.DiscountPair;
import groupk.inventory_suppliers.dataLayer.dao.records.DiscountPairRecord;
import groupk.inventory_suppliers.dataLayer.dao.records.readonly.DiscountPairData;


import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class DiscountPairDAO extends BaseDAO<DiscountPairRecord.DiscountPairKey, DiscountPairRecord> {

    public static final String TABLE_NAME = "DiscountPair";
    public static final String PRODUCT_ID = "ProductId";
    public static final String PRODUCT_ITEM_ID = "ProductItemId";
    public static final String DISCOUNT_PAIR_ID = "DiscountPairId";
    public static final String DISCOUNT = "Discount";
    public static final String START_DATE = "StartDate";
    public static final String END_DATE = "EndDate";

    public DiscountPairDAO(Connection conn) {
        super(conn);
    }

    public DiscountPairData create(int product_id, int productItem_id, int id, Date start_date, Date end_date, float discount){
        DiscountPairRecord response = create(
                () -> new DiscountPairRecord(product_id, productItem_id, id, start_date, end_date, discount),
                "INSERT INTO " + TABLE_NAME + " (" +
                        PRODUCT_ID + "," +
                        PRODUCT_ITEM_ID + "," +
                        DISCOUNT_PAIR_ID + "," +
                        START_DATE + "," +
                        END_DATE + "," +
                        DISCOUNT +
                        ") VALUES(?, ?, ?, ?, ?, ?)",
                ps -> ps.setInt(1, product_id),
                ps -> ps.setInt(2, productItem_id),
                ps -> ps.setInt(3, id),
                ps -> ps.setDate(4, start_date),
                ps -> ps.setDate(5, end_date),
                ps -> ps.setFloat(6, discount)
        );
        return response;
    }

    @Override
    DiscountPairRecord fetch(DiscountPairRecord.DiscountPairKey key) throws SQLException, NoSuchElementException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + PRODUCT_ID + " = ? AND " + PRODUCT_ITEM_ID + " = ? AND " + DISCOUNT_PAIR_ID + " = ?"
        );
        ps.setInt(1, key.product_id);
        ps.setInt(2, key.productItem_id);
        ps.setInt(3, key.id);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next())
            return readOne(key, resultSet);
        else
            throw new NoSuchElementException("no discount pair with product id: " + key.product_id + " and product item id " + key.productItem_id + " and id " + key.id);
    }

    @Override
    Stream<DiscountPairRecord> fetchAll() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TABLE_NAME);
        ResultSet query = stmt.executeQuery();
        ArrayList<DiscountPairRecord> res = new ArrayList<>();
        while (query.next()) {
            res.add(readOne(new DiscountPairRecord.DiscountPairKey(
                            query.getInt(PRODUCT_ID),
                            query.getInt(PRODUCT_ITEM_ID),
                            query.getInt(DISCOUNT_PAIR_ID)
                    ),
                    query));
        }
        return res.stream();
    }

    private DiscountPairRecord readOne(DiscountPairRecord.DiscountPairKey key, ResultSet resultSet) throws SQLException {
        return new DiscountPairRecord(
                key.product_id,
                key.productItem_id,
                key.id,
                resultSet.getDate(START_DATE),
                resultSet.getDate(END_DATE),
                resultSet.getFloat(DISCOUNT)
        );
    }

    @Override
    protected int runDeleteQuery(DiscountPairRecord.DiscountPairKey discountPairKey) {
        return runUpdate(
                "DELETE FROM " + TABLE_NAME + " WHERE " + PRODUCT_ID + " = ? AND " + PRODUCT_ITEM_ID + " = ? AND " + DISCOUNT_PAIR_ID + " = ?",
                ps -> ps.setInt(1, discountPairKey.product_id),
                ps -> ps.setInt(2, discountPairKey.productItem_id),
                ps -> ps.setInt(3, discountPairKey.id)
        );
    }
}

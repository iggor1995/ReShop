package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.entity.Characteristic;
import com.epam.igor.electronicsshop.entity.ItemCharacteristic;
import com.epam.igor.electronicsshop.entity.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 11.08.2017.
 */
public class JDBCItemCharacteristicDao extends JDBCAbstractDao<ItemCharacteristic> {
    private static final String INSERT_ITEM_CHARACTERISTIC = "INSERT INTO electronics.item_characteristic" +
            "(price, characteristic_id, product_id) VALUES(?, ?, ?)";
    private static final String UPDATE_ITEM_CHARACTERISTIC_BY_ID = " UPDATE electronics.item_characteristic" +
            "SET price = ?, SET characteristic_id = ?, SET product_id  = ?";
    public static final String ITEM_CHARACTERISTIC = "item_characteristic";
    public static final String ID = "id";
    public static final String CHARACTERISTIC_ID = "characteristic_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRICE = "price";
    public static final String CANNOT_GET_ITEM_CHARACTERISTIC_FROM_RESULT_SET = "Cannot get item characteristic from result set";

    @Override
    protected String getTableName() {
        return ITEM_CHARACTERISTIC;
    }

    @Override
    protected String getQueryForInsert() {
        return INSERT_ITEM_CHARACTERISTIC;
    }

    @Override
    protected String getQueryForUpdate() {
        return UPDATE_ITEM_CHARACTERISTIC_BY_ID;
    }

    @Override
    protected ItemCharacteristic getObjectFromResultSet(ResultSet rs) throws DaoException {
        ItemCharacteristic itemCharacteristic = new ItemCharacteristic();
        try {
            itemCharacteristic.setId(rs.getInt(ID));
            Characteristic characteristic = new Characteristic(rs.getInt(CHARACTERISTIC_ID));
            itemCharacteristic.setCharacteristic(characteristic);
            Product product = new Product(rs.getInt(PRODUCT_ID));
            itemCharacteristic.setProduct(product);
            itemCharacteristic.setPrice(rs.getString(PRICE));
        } catch (SQLException e) {
            throw new DaoException(CANNOT_GET_ITEM_CHARACTERISTIC_FROM_RESULT_SET, e);
        }
        return itemCharacteristic;
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(ItemCharacteristic itemCharacteristic, PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, itemCharacteristic.getPrice());
            ps.setInt(2 , itemCharacteristic.getCharacteristic().getId());
            ps.setInt(3, itemCharacteristic.getProduct().getId());
        } catch (SQLException e) {
            throw new DaoException("Coulnd't set item characteristic variables for prepared statement");
        }
    }
}

package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.entity.Order;
import com.epam.igor.electronicsshop.entity.OrderingItem;
import com.epam.igor.electronicsshop.entity.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 02.08.2017.
 */
public class JDBCOrderingItemDao extends JDBCAbstractDao<OrderingItem> {
    private static final String
            ORDERING_ITEM = "ordering_item";
    private static final String INSERT_ORDERING_ITEM = "INSERT INTO electronics.ordering_item(order_id, product_id, " +
            "amount) VALUES(?, ?, ?)";
    private static final String UPDATE_ORDERING_ITEM_BY_ID = "UPDATE electronics.ordering_item" +
            "SET order_id = ?, product_id = ?, amount = ? WHERE id = ?";
    private static final String ID = "id";
    private static final String ORDER_ID = "order_id";
    private static final String PRODUCT_ID = "product_id";
    private static final String AMOUNT = "amount";
    private static final String DELETED = "deleted";
    private static final String CANNOT_GET_ORDERING_ITEM = "Cannot get ordering item from result set";
    private static final String CANNOT_SET_ORDERING_ITEM = "Couldn't set ordering item variables for prepared statement";

    @Override
    protected OrderingItem getObjectFromResultSet(ResultSet rs) throws DaoException {
        OrderingItem orderingItem = new OrderingItem();
        try {
            orderingItem.setId(rs.getInt(ID));
            Order order = new Order(rs.getInt(ORDER_ID));
            orderingItem.setOrder(order);
            Product product = new Product(rs.getInt(PRODUCT_ID));
            orderingItem.setProduct(product);
            orderingItem.setAmount(rs.getInt(AMOUNT));
            orderingItem.setDeleted(rs.getBoolean(DELETED));
        } catch (SQLException e) {
            throw new DaoException(CANNOT_GET_ORDERING_ITEM);
        }
        return orderingItem;
    }

    @Override
    protected String getQueryForInsert() {
        return INSERT_ORDERING_ITEM;
    }

    @Override
    protected String getQueryForUpdate() {
        return UPDATE_ORDERING_ITEM_BY_ID;
    }

    @Override
    protected String getTableName() {
        return ORDERING_ITEM;
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(OrderingItem orderingItem, PreparedStatement ps) throws DaoException {
        try {
            ps.setInt(1, orderingItem.getOrder().getId());
            ps.setInt(2, orderingItem.getProduct().getId());
            ps.setInt(3, orderingItem.getAmount());
        } catch (SQLException e) {
            throw new DaoException(CANNOT_SET_ORDERING_ITEM);
        }
    }
}

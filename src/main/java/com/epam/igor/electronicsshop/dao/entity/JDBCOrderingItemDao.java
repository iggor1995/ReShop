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
    private static final String INSERT_ORDERING_ITEM = "INSERT INTO electronics.ordering_item(order_id, product_id, " +
            "amount) VALUES(?, ?, ?)";
    private static final String UPDATE_ORDERING_ITEM_BY_ID = "UPDATE electronics.ordering_item" +
            "SET order_id = ?, SET product_id = ?, SET amount = ? WHERE id = ?";
    @Override
    protected OrderingItem getObjectFromResultSet(ResultSet rs) throws DaoException {
        OrderingItem orderingItem = new OrderingItem();
        try {
            orderingItem.setId(rs.getInt("id"));
            Order order = new Order(rs.getInt("order_id"));
            orderingItem.setOrder(order);
            Product product = new Product(rs.getInt("product_id"));
            orderingItem.setProduct(product);
            orderingItem.setAmount(rs.getInt("amount"));
            orderingItem.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException e) {
            throw new DaoException("Cannot get ordering item from result set");
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
        return "ordering_item";
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(OrderingItem orderingItem, PreparedStatement ps) throws DaoException {
        try {
            ps.setInt(1, orderingItem.getOrder().getId());
            ps.setInt(2, orderingItem.getProduct().getId());
            ps.setInt(3, orderingItem.getAmount());
        } catch (SQLException e) {
            throw new DaoException("Couldn't set ordering item variables for prepared statement");
        }
    }
}

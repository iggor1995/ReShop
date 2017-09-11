package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.entity.JDBCAbstractDao;
import com.epam.igor.electronicsshop.entity.Order;
import com.epam.igor.electronicsshop.entity.OrderStatus;
import com.epam.igor.electronicsshop.entity.User;
import org.joda.time.DateTime;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by User on 02.08.2017.
 */
public class JDBCOrderDao extends JDBCAbstractDao<Order> {
    private static final String INSERT_ORDER = "INSERT INTO electronics.order(user_id, created, description," +
            " status_id) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_ORDER_BY_ID = "UPDATE electronics.order SET user_id = ?," +
            "SET created = ?, description = ?, status = ? WHERE id = ?";
    @Override
    protected Order getObjectFromResultSet(ResultSet rs) throws DaoException {
        Order order = new Order();
        try {
            order.setId(rs.getInt("id"));
            User user = new User(rs.getInt("user_id"));
            order.setUser(user);
            order.setCreationTime(new DateTime(rs.getTimestamp("created")));
            order.setDescription(rs.getString("description"));
            OrderStatus status = new OrderStatus(rs.getInt("status_id"));
            order.setStatus(status);
            order.setDeleted(rs.getBoolean("deleted"));

        } catch (SQLException e) {
            throw new DaoException("Cannot get order from result set");
        }
        return order;
    }

    @Override
    protected String getQueryForInsert() {
        return INSERT_ORDER;
    }

    @Override
    protected String getQueryForUpdate() {
        return UPDATE_ORDER_BY_ID;
    }

    @Override
    protected String getTableName() {
        return "electronics.order";
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(Order order, PreparedStatement ps) throws DaoException {
        try {
            ps.setInt(1, order.getUser().getId());
            ps.setTimestamp(2, new Timestamp(order.getCreationTime().getMillis()));
            ps.setString(3, order.getDescription());
            ps.setInt(4, order.getStatus().getId());
        } catch (SQLException e) {
            throw new DaoException("Couldn't set order variables for prepared statement", e);
        }
    }
}

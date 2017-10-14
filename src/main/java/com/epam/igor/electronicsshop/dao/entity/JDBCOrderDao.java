package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.entity.Order;
import com.epam.igor.electronicsshop.entity.OrderStatus;
import com.epam.igor.electronicsshop.entity.User;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JDBCOrderDao extends JDBCAbstractDao<Order> {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCOrderDao.class);
    private static final String INSERT_ORDER = "INSERT INTO electronics.order(user_id, created, description," +
            " status_id) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_ORDER_BY_ID = "UPDATE electronics.order SET user_id = ?," +
            " created = ?, description = ?, status_id = ? WHERE id = ?";
    private static final String ID = "id";
    private static final String USER_ID = "user_id";
    private static final String CREATED = "created";
    private static final String DESCRIPTION = "description";
    private static final String STATUS_ID = "status_id";
    private static final String DELETED = "deleted";
    private static final String CANNOT_GET_ORDER = "Cannot get order from result set";
    private static final String ELECTRONICS_ORDER = "electronics.order";
    private static final String COULDN_T_SET_ORDER = "Couldn't set order variables for prepared statement";

    @Override
    protected Order getObjectFromResultSet(ResultSet rs) throws DaoException {
        Order order = new Order();
        try {
            order.setId(rs.getInt(ID));
            User user = new User(rs.getInt(USER_ID));
            order.setUser(user);
            order.setCreationTime(new DateTime(rs.getTimestamp(CREATED)));
            order.setDescription(rs.getString(DESCRIPTION));
            OrderStatus status = new OrderStatus(rs.getInt(STATUS_ID));
            order.setStatus(status);
            order.setDeleted(rs.getBoolean(DELETED));

        } catch (SQLException e) {
            LOG.info(CANNOT_GET_ORDER, e);
            throw new DaoException(CANNOT_GET_ORDER);
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
        return ELECTRONICS_ORDER;
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(Order order, PreparedStatement ps) throws DaoException {
        try {
            ps.setInt(1, order.getUser().getId());
            ps.setTimestamp(2, new Timestamp(order.getCreationTime().getMillis()));
            ps.setString(3, order.getDescription());
            ps.setInt(4, order.getStatus().getId());
        } catch (SQLException e) {
            LOG.info(COULDN_T_SET_ORDER, e);
            throw new DaoException(COULDN_T_SET_ORDER, e);
        }
    }
}

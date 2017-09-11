package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.entity.JDBCAbstractDao;
import com.epam.igor.electronicsshop.entity.OrderStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 02.08.2017.
 */
public class JDBCOrderStatusDao extends JDBCAbstractDao<OrderStatus> {
    private static final String INSERT_ODER_STATUS = "INSERT INTO electronics.order_status" +
            "(name_ru, name_en) VALUES(?, ?)";
    private static final String UPDATE_ORDER_STATUS_BY_ID = "UPDATE electronics.order_status" +
            "SET name_ru = ?, name_en = ? WHERE id = ?";
    @Override
    protected OrderStatus getObjectFromResultSet(ResultSet rs) throws DaoException {
        OrderStatus orderStatus = new OrderStatus();
        try {
            orderStatus.setId(rs.getInt("id"));
            orderStatus.setRuName(rs.getString("name_ru"));
            orderStatus.setEnName(rs.getString("name_en"));
            orderStatus.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException e) {
            throw new DaoException("Cannot get order status from result set", e);
        }
        return orderStatus;
    }

    @Override
    protected String getQueryForInsert() {
        return INSERT_ODER_STATUS;
    }

    @Override
    protected String getQueryForUpdate() {
        return UPDATE_ORDER_STATUS_BY_ID;
    }

    @Override
    protected String getTableName() {
        return "order_status";
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(OrderStatus orderStatus, PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, orderStatus.getRuName());
            ps.setString(2, orderStatus.getEnName());

        } catch (SQLException e) {
            throw  new DaoException("Couldn't set order status variables for prepared statement", e);
        }
    }
}

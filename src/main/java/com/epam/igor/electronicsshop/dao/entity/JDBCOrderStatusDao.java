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
    private static final String ID = "id";
    private static final String NAME_RU = "name_ru";
    private static final String NAME_EN = "name_en";
    private static final String DELETED = "deleted";
    private static final String CANNOT_GET_ORDER_STATUS = "Cannot get order status from result set";
    private static final String ORDER_STATUS = "order_status";
    private static final String COULDN_T_SET_ORDER_STATUS = "Couldn't set order status variables for prepared statement";

    @Override
    protected OrderStatus getObjectFromResultSet(ResultSet rs) throws DaoException {
        OrderStatus orderStatus = new OrderStatus();
        try {
            orderStatus.setId(rs.getInt(ID));
            orderStatus.setRuName(rs.getString(NAME_RU));
            orderStatus.setEnName(rs.getString(NAME_EN));
            orderStatus.setDeleted(rs.getBoolean(DELETED));
        } catch (SQLException e) {
            throw new DaoException(CANNOT_GET_ORDER_STATUS, e);
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
        return ORDER_STATUS;
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(OrderStatus orderStatus, PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, orderStatus.getRuName());
            ps.setString(2, orderStatus.getEnName());

        } catch (SQLException e) {
            throw  new DaoException(COULDN_T_SET_ORDER_STATUS, e);
        }
    }
}

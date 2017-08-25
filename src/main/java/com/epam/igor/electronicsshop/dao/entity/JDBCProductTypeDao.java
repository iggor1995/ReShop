package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.Dao;
import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.entity.JDBCAbstractDao;
import com.epam.igor.electronicsshop.entity.ProductType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 02.08.2017.
 */
public class JDBCProductTypeDao extends JDBCAbstractDao<ProductType> {
    private static final String INSERT_PRODUCT_TYPE = "INSERT INTO electronics.product_type(name_ru, name_en)" +
            "VALUES(?, ?)";
    private static final String UPDATE_PRODUCT_TYPE_BY_ID = "UPDATE electronics.product_type" +
            "SET name_ru = ?, SET name_en = ?";

    @Override
    protected ProductType getObjectFromResultSet(ResultSet rs) throws DaoException {
        ProductType productType = new ProductType();

        try {
            productType.setId(rs.getInt("id"));
            productType.setRuName(rs.getString("name_ru"));
            productType.setEnName(rs.getString("name_en"));
            productType.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException e) {
            throw new DaoException("Cannot create productType from result set");
        }
        return productType;
    }

    @Override
    protected String getQueryForInsert() {
        return INSERT_PRODUCT_TYPE;
    }

    @Override
    protected String getQueryForUpdate() {
        return UPDATE_PRODUCT_TYPE_BY_ID;
    }

    @Override
    protected String getTableName() {
        return "product_type";
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(ProductType productType, PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, productType.getRuName());
            ps.setString(2, productType.getEnName());
        } catch (SQLException e) {
            throw new DaoException("Couldn't set product type variables for prepared statement", e);
        }
    }
}

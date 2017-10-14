package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.entity.ProductType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCProductTypeDao extends JDBCAbstractDao<ProductType> {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCProductTypeDao.class);
    private static final String INSERT_PRODUCT_TYPE = "INSERT INTO electronics.product_type(name_ru, name_en)" +
            "VALUES(?, ?)";
    private static final String UPDATE_PRODUCT_TYPE_BY_ID = "UPDATE electronics.product_type" +
            "SET name_ru = ?, name_en = ?";
    private static final String ID = "id";
    private static final String NAME_RU = "name_ru";
    private static final String NAME_EN = "name_en";
    private static final String DELETED = "deleted";
    private static final String CANNOT_CREATE_PRODUCT_TYPE = "Cannot create productType from result set";
    private static final String PRODUCT_TYPE = "product_type";
    private static final String COULDN_T_SET_PRODUCT_TYPE = "Couldn't set product type variables for prepared statement";

    @Override
    protected ProductType getObjectFromResultSet(ResultSet rs) throws DaoException {
        ProductType productType = new ProductType();

        try {
            productType.setId(rs.getInt(ID));
            productType.setRuName(rs.getString(NAME_RU));
            productType.setEnName(rs.getString(NAME_EN));
            productType.setDeleted(rs.getBoolean(DELETED));
        } catch (SQLException e) {
            LOG.info(CANNOT_CREATE_PRODUCT_TYPE, e);
            throw new DaoException(CANNOT_CREATE_PRODUCT_TYPE);
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
        return PRODUCT_TYPE;
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(ProductType productType, PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, productType.getRuName());
            ps.setString(2, productType.getEnName());
        } catch (SQLException e) {
            LOG.info(COULDN_T_SET_PRODUCT_TYPE, e);
            throw new DaoException(COULDN_T_SET_PRODUCT_TYPE, e);
        }
    }
}

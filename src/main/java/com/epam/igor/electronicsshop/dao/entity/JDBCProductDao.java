package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.entity.JDBCAbstractDao;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.entity.ProductType;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 02.08.2017.
 */
public class JDBCProductDao extends JDBCAbstractDao<Product> {
    private static final String INSERT_PRODUCT = "INSERT INTO electronics.product(name, price, type_id, " +
            "description_RU, description_EN) VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE_PRODUCT_BY_ID = "UPDATE electronics.product" +
            "SET name = ?, price = ?, type_id = ?, description_RU = ?, description_EN" +
            "WHERE id = ?";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String KZT = "KZT";
    private static final String PRICE = "price";
    private static final String TYPE_ID = "type_id";
    private static final String DESCRIPTION_RU = "description_RU";
    private static final String DESCRIPTION_EN = "description_EN";
    private static final String DELETED = "deleted";
    private static final String CANNOT_GET_PRODUCT= "Cannot get product from result set";
    private static final String PRODUCT = "product";
    private static final String COULDN_T_SET_PRODUCT = "Couldn't set product variables for prepared statement";

    @Override
    protected Product getObjectFromResultSet(ResultSet rs) throws DaoException {
        Product product = new Product();
        try {
            product.setId(rs.getInt(ID));
            product.setName(rs.getString(NAME));
            product.setPrice(Money.of(CurrencyUnit.getInstance(KZT), rs.getBigDecimal(PRICE)));
            ProductType type = new ProductType(rs.getInt(TYPE_ID));
            product.setType(type);
            product.setRuDescription(rs.getString(DESCRIPTION_RU));
            product.setEnDescription(rs.getString(DESCRIPTION_EN));
            product.setDeleted(rs.getBoolean(DELETED));
        } catch (SQLException e) {
            throw new DaoException(CANNOT_GET_PRODUCT);
        }
        return product;
    }

    @Override
    protected String getQueryForInsert() {
        return INSERT_PRODUCT;
    }

    @Override
    protected String getQueryForUpdate() {
        return UPDATE_PRODUCT_BY_ID;
    }

    @Override
    protected String getTableName() {
        return PRODUCT;
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(Product product, PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, product.getName());
            ps.setBigDecimal(2, product.getPrice().getAmount());
            ps.setInt(3, product.getType().getId());
            ps.setString(4, product.getRuDescription());
            ps.setString(5, product.getEnDescription());
        } catch (SQLException e) {
            throw new DaoException(COULDN_T_SET_PRODUCT);
        }
    }
}

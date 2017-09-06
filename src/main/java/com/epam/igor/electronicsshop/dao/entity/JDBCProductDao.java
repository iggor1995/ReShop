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
            "SET name = ?, SET price = ?, SET type_id = ?, SET description_RU = ?, SET description_EN" +
            "WHERE id = ?";
    @Override
    protected Product getObjectFromResultSet(ResultSet rs) throws DaoException {
        Product product = new Product();
        try {
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setPrice(Money.of(CurrencyUnit.getInstance("KZT"), rs.getBigDecimal("price")));
            ProductType type = new ProductType();
            type.setId(rs.getInt("type_id"));
            product.setType(type);
            product.setRuDescription(rs.getString("description_RU"));
            product.setEnDescription(rs.getString("description_EN"));
            product.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException e) {
            throw new DaoException("Cannot get product from result set");
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
        return "product";
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
            throw new DaoException("Couldn't set product variables for prepared statement");
        }
    }
}

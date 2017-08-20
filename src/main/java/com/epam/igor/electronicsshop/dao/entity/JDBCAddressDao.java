package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.entity.Address;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 02.08.2017.
 */
public class JDBCAddressDao extends JDBCAbstractDao<Address> {
    private static final String INSERT_ADDRESS = "INSERT INTO reshop.address" +
            "(country, city, street, building_number, apartment_number) VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE_ADDRESS_BY_ID = "UPDATE reshop.address" +
            "SET country = ?, SET city = ?, SET street = ?, SET building_number = ?, " +
            "SET apartment_number = ? WHERE id = ?";
    @Override
    protected Address getObjectFromResultSet(ResultSet rs) throws DaoException {
        Address address = new Address();
        try {
            address.setId(rs.getInt("id"));
            address.setCountry(rs.getString("country"));
            address.setCity(rs.getString("city"));
            address.setStreet(rs.getString("street"));
            address.setBuildingNumber(rs.getInt("building_number"));
            address.setApartmentNumber(rs.getInt("apartment_number"));
            address.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException e) {
            throw  new DaoException("Cannot get address from result set", e);
        }
        return address;
    }

    @Override
    protected String getQueryForInsert() {
        return INSERT_ADDRESS;
    }

    @Override
    protected String getQueryForUpdate() {
        return UPDATE_ADDRESS_BY_ID;
    }

    @Override
    protected String getTableName() {
        return "address";
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(Address address, PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, address.getCountry());
            ps.setString(2, address.getCity());
            ps.setString(3, address.getStreet());
            ps.setInt(4, address.getBuildingNumber());
            ps.setInt(5, address.getApartmentNumber());
        } catch (SQLException e) {
            throw  new DaoException("Couldn't set address variables for prepared statement", e);
        }
    }
}

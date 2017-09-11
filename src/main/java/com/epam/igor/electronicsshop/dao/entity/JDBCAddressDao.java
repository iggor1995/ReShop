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
    private static final String INSERT_ADDRESS = "INSERT INTO electronics.address" +
            "(country, city, street, building_number, apartment_number) VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE_ADDRESS_BY_ID = "UPDATE electronics.address " +
            "SET country = ?, city = ?, street = ?, building_number = ?, " +
            "apartment_number = ? WHERE id = ?";
    private static final String ID = "id";
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String STREET = "street";
    private static final String BUILDING_NUMBER = "building_number";
    private static final String APARTMENT_NUMBER = "apartment_number";
    private static final String DELETED = "deleted";
    private static final String CANNOT_GET_ADDRESS= "Cannot get address from result set";
    private static final String ADDRESS = "address";
    private static final String COULDN_T_SET_ADDRESS = "Couldn't set address variables for prepared statement";

    @Override
    protected Address getObjectFromResultSet(ResultSet rs) throws DaoException {
        Address address = new Address();
        try {
            address.setId(rs.getInt(ID));
            address.setCountry(rs.getString(COUNTRY));
            address.setCity(rs.getString(CITY));
            address.setStreet(rs.getString(STREET));
            address.setBuildingNumber(rs.getString(BUILDING_NUMBER));
            address.setApartmentNumber(rs.getString(APARTMENT_NUMBER));
            address.setDeleted(rs.getBoolean(DELETED));
        } catch (SQLException e) {
            throw  new DaoException(CANNOT_GET_ADDRESS, e);
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
        return ADDRESS;
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(Address address, PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, address.getCountry());
            ps.setString(2, address.getCity());
            ps.setString(3, address.getStreet());
            ps.setString(4, address.getBuildingNumber());
            ps.setString(5, address.getApartmentNumber());
        } catch (SQLException e) {
            throw  new DaoException(COULDN_T_SET_ADDRESS, e);
        }
    }
}

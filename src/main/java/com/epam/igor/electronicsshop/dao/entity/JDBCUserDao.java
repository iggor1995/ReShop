package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.entity.JDBCAbstractDao;
import com.epam.igor.electronicsshop.entity.Address;
import com.epam.igor.electronicsshop.entity.Gender;
import com.epam.igor.electronicsshop.entity.User;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 01.08.2017.
 */
public class JDBCUserDao extends JDBCAbstractDao<User> {
    private static final String INSERT_USER = "INSERT INTO electronics.user(email, password, " +
            "firstname, lastname, address_id, phonenumber, role, cash, gender_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_BY_ID = "UPDATE electronics.user SET email = ?, SET password = ?," +
            "SET role = ?, SET first_name = ?, SET last_name = ?, SET address_id = ?, SET cash = ?, " +
            "SET phone_number = ? WHERE id = ?";
    @Override
    protected User getObjectFromResultSet(ResultSet rs) throws DaoException {
        User user = new User();
        try {
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setRole(User.Role.valueOf(rs.getString("role")));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            Address address = new Address(rs.getInt("address_id"));
            user.setAddress(address);
            user.setCash(Money.of(CurrencyUnit.getInstance("KZT"), rs.getBigDecimal("cash")));
            user.setPhoneNumber(rs.getString("phone"));
            Gender gender = new Gender();
            gender.setId(rs.getInt("gender_id"));
            user.setGender(gender);
            user.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException e) {
            throw new DaoException("Cannot get user from result set", e);
        }
        return user;
    }

    @Override
    protected String getQueryForInsert() {
        return INSERT_USER;
    }

    @Override
    protected String getQueryForUpdate() {
        return UPDATE_USER_BY_ID;
    }

    @Override
    protected String getTableName() {
        return "user";
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(User user, PreparedStatement ps) throws DaoException {

        try {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setInt(5, user.getAddress().getId());
            ps.setString(6, user.getPhoneNumber());
            ps.setString(7, String.valueOf(user.getRole()));
            ps.setBigDecimal(8, user.getCash().getAmount());
            ps.setInt(9, user.getGender().getId());
        } catch (SQLException e) {
            throw new DaoException("Couldn't set user variables for prepared statement", e);
        }
    }
}

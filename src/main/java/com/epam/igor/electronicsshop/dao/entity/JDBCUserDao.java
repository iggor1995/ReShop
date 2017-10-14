package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.entity.Address;
import com.epam.igor.electronicsshop.entity.Gender;
import com.epam.igor.electronicsshop.entity.User;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JDBCUserDao extends JDBCAbstractDao<User> {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCUserDao.class);
    private static final String INSERT_USER = "INSERT INTO electronics.user(email, password, " +
            "firstname, lastname, address_id, phonenumber, role, cash, gender_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_BY_ID = "UPDATE electronics.user SET email = ?, password = ?, " +
            "firstname = ?, lastname = ?, address_id = ?, phonenumber = ?, role = ?, " +
            "cash = ?, gender_id = ? WHERE id = ?";
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PASS_WORD = "password";
    private static final String ROLE = "role";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String ADDRESS_ID = "address_id";
    private static final String KZT = "KZT";
    private static final String CASH = "cash";
    private static final String PHONENUMBER = "phonenumber";
    private static final String GENDER_ID = "gender_id";
    private static final String DELETED = "deleted";
    private static final String CANNOT_GET_USER_FROM_RESULT_SET = "Cannot get user from result set";
    private static final String USER = "user";
    private static final String COULDN_T_SET_USER = "Couldn't set user variables for prepared statement";

    @Override
    protected User getObjectFromResultSet(ResultSet rs) throws DaoException {
        User user;
        try {
            Gender gender = new Gender();
            gender.setId(rs.getInt(GENDER_ID));
            user = new User.UserBuilder(rs.getString(FIRSTNAME), rs.getString(LASTNAME))
                    .password(rs.getString(PASS_WORD))
                    .gender(gender)
                    .phoneNumber(rs.getString(PHONENUMBER))
                    .email(rs.getString(EMAIL))
                    .build();
            user.setId(rs.getInt(ID));
            user.setRole(User.Role.valueOf(rs.getString(ROLE)));
            Address address = new Address(rs.getInt(ADDRESS_ID));
            user.setAddress(address);
            user.setCash(Money.of(CurrencyUnit.getInstance(KZT), rs.getBigDecimal(CASH)));
            user.setDeleted(rs.getBoolean(DELETED));


        } catch (SQLException e) {
            LOG.info(CANNOT_GET_USER_FROM_RESULT_SET, e);
            throw new DaoException(CANNOT_GET_USER_FROM_RESULT_SET, e);
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
        return USER;
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
            LOG.info(COULDN_T_SET_USER, e);
            throw new DaoException(COULDN_T_SET_USER, e);
        }
    }
}

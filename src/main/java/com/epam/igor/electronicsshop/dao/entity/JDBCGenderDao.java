package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.entity.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 02.08.2017.
 */
public class JDBCGenderDao extends JDBCAbstractDao<Gender> {
    private static final String INSERT_GENDER = "INSERT INTO electronics.gender(name_ru, name_en)" +
            "VALUES(?, ?)";
    private static final String UPDATE_GENDER_BY_ID = "UPDATE electronics.gender" +
            "SET name_ru = ?, name_en = ? WHERE id = ?";
    private static final Logger LOG = LoggerFactory.getLogger(JDBCGenderDao.class);
    public static final String ID = "id";
    private static final String NAME_RU = "name_ru";
    private static final String NAME_EN = "name_en";
    private static final String COULD_NOT_GET_GENDER = "Could not get gender from result set";
    private static final String GENDER = "gender";
    private static final String COULDN_T_SET_GENDER = "Couldn't set gender variables for prepared statement";

    @Override
    protected Gender getObjectFromResultSet(ResultSet rs) throws DaoException {
        Gender gender = new Gender();
        LOG.info("123");
        try {
            gender.setId(rs.getInt(ID));
            gender.setRuName(rs.getString(NAME_RU));
            gender.setEnName(rs.getString(NAME_EN));
        } catch (SQLException e) {
            throw new DaoException(COULD_NOT_GET_GENDER, e);
        }
        return gender;
    }

    @Override
    protected String getQueryForInsert() {
        return INSERT_GENDER;
    }

    @Override
    protected String getQueryForUpdate() {
        return UPDATE_GENDER_BY_ID;
    }

    @Override
    protected String getTableName() {
        return GENDER;
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(Gender gender, PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, gender.getRuName());
            ps.setString(2, gender.getEnName());
        } catch (SQLException e) {
            throw new DaoException(COULDN_T_SET_GENDER);
        }
    }
}

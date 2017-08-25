package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.action.RegisterAction;
import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.entity.JDBCAbstractDao;
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
            "SET name_ru = ?, SET name_en = ? WHERE id = ?";
    private static final Logger LOG = LoggerFactory.getLogger(JDBCGenderDao.class);

    @Override
    protected Gender getObjectFromResultSet(ResultSet rs) throws DaoException {
        Gender gender = new Gender();
        LOG.info("123");
        try {
            gender.setId(rs.getInt("id"));
            gender.setEnName(rs.getString("name_ru"));
            gender.setRuName(rs.getString("name_en"));
        } catch (SQLException e) {
            throw new DaoException("Could not get object from result set", e);
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
        return "gender";
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(Gender gender, PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, gender.getRuName());
            ps.setString(2, gender.getEnName());
        } catch (SQLException e) {
            throw new DaoException("Couldn't set gender variables for prepared statement");
        }
    }
}

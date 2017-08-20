package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.entity.Characteristic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 11.08.2017.
 */
public class JDBCCharacteristicDao extends JDBCAbstractDao<Characteristic> {
    private static final String INSERT_CHARACTERISTIC = "INSERT INTO reshop.characteristic(name_ru, name_en)" +
            "VALUES(?, ?)";
    private static final String UPDATE_CHARACTERISTIC_BY_ID = "UPDATE reshop.characteristic" +
            "SET name_ru = ?, SET name_en = ? WHERE id = ?";
    @Override
    protected String getTableName() {
        return "characteristic";
    }

    @Override
    protected String getQueryForInsert() {
        return INSERT_CHARACTERISTIC;
    }

    @Override
    protected String getQueryForUpdate() {
        return UPDATE_CHARACTERISTIC_BY_ID;
    }

    @Override
    protected Characteristic getObjectFromResultSet(ResultSet rs) throws DaoException {
        Characteristic characteristic = new Characteristic();
        try {
            characteristic.setId(rs.getInt("id"));
            characteristic.setRuName(rs.getString("ru_name"));
            characteristic.setEnName(rs.getString("en_name"));
            characteristic.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException e) {
            throw new DaoException("Cannot get characteristic from result set", e);
        }
        return characteristic;
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(Characteristic characteristic,
                                                            PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, characteristic.getRuName());
            ps.setString(2, characteristic.getEnName());
        } catch (SQLException e) {
            throw new DaoException("Coulnd't set characteristic variables for prepared statement");
        }
    }
}

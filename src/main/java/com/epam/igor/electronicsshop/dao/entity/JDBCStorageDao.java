package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.entity.JDBCAbstractDao;
import com.epam.igor.electronicsshop.entity.Storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 02.08.2017.
 */
public class JDBCStorageDao extends JDBCAbstractDao<Storage> {

    private static final String INSERT_STORAGE = "INSERT INTO electronics.user(name, description_RU," +
            "description_EN) VALUES (?, ?, ?)";
    private static final String UPDATE_STORAGE_BY_ID = "UPDATE electronics.user SET name = ?," +
            "SET description_RU = ?, SET description_EN = ? WHERE id = ?";
    @Override
    protected Storage getObjectFromResultSet(ResultSet rs) throws DaoException {
        Storage storage = new Storage();
        try {
            storage.setName(rs.getString("name"));
            storage.setRuDescription(rs.getString("description_RU"));
            storage.setEnDescription(rs.getString("description_EN"));
            storage.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException e) {
            throw new DaoException("Cannot set storage variables for prepared statement", e);
        }
        return storage;
    }

    @Override
    protected String getQueryForInsert() {
        return INSERT_STORAGE;
    }

    @Override
    protected String getQueryForUpdate() {
        return UPDATE_STORAGE_BY_ID;
    }

    @Override
    protected String getTableName() {
        return "electronics.storage";
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(Storage storage, PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, storage.getName());
            ps.setString(2, storage.getRuDescription());
            ps.setString(3, storage.getEnDescription());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

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
            "SET description_RU = ?, description_EN = ? WHERE id = ?";
    private static final String NAME = "name";
    private static final String DESCRIPTION_RU = "description_RU";
    private static final String DESCRIPTION_EN = "description_EN";
    private static final String DELETED = "deleted";
    private static final String CANNOT_SET_STORAGE = "Cannot set storage variables for prepared statement";
    private static final String ELECTRONICS_STORAGE = "electronics.storage";

    @Override
    protected Storage getObjectFromResultSet(ResultSet rs) throws DaoException {
        Storage storage = new Storage();
        try {
            storage.setName(rs.getString(NAME));
            storage.setRuDescription(rs.getString(DESCRIPTION_RU));
            storage.setEnDescription(rs.getString(DESCRIPTION_EN));
            storage.setDeleted(rs.getBoolean(DELETED));
        } catch (SQLException e) {
            throw new DaoException(CANNOT_SET_STORAGE, e);
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
        return ELECTRONICS_STORAGE;
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

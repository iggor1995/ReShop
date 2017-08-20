package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.entity.JDBCAbstractDao;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.entity.Storage;
import com.epam.igor.electronicsshop.entity.StorageItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 02.08.2017.
 */
public class JDBCStorageItemDao extends JDBCAbstractDao<StorageItem> {
    private static final String INSERT_STORAGE_ITEM = "INSERT INTO reshop.storage_item(storage_id," +
            "product_id, amount) VALUES(?, ?, ?)";
    private static final String UPDATE_STORAGE_ITEM_BY_ID = "UPDATE reshop.storage_item" +
            "SET storage_id = ?, SET rpoduct_id = ?, SET amount = ? WHERE id = ?";
    @Override
    protected StorageItem getObjectFromResultSet(ResultSet rs) throws DaoException {
        StorageItem storageItem = new StorageItem();
        try {
            storageItem.setId(rs.getInt("id"));
            Product product = new Product(rs.getInt("product_id"));
            storageItem.setProduct(product);
            Storage storage = new Storage(rs.getInt("storage_id"));
            storageItem.setStorage(storage);
            storageItem.setAmount(rs.getInt("amount"));
            storageItem.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storageItem;
    }

    @Override
    protected String getQueryForInsert() {
        return INSERT_STORAGE_ITEM;
    }

    @Override
    protected String getQueryForUpdate() {
        return UPDATE_STORAGE_ITEM_BY_ID;
    }

    @Override
    protected String getTableName() {
        return "storage item";
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(StorageItem storageItem, PreparedStatement ps) throws DaoException {
        try {
            ps.setInt(1, storageItem.getStorage().getId());
            ps.setInt(2, storageItem.getProduct().getId());
            ps.setInt(3, storageItem.getAmount());
        } catch (SQLException e) {
            throw new DaoException("Couldn't set storage variables for prepared statement");
        }
    }
}

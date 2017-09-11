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
    private static final String INSERT_STORAGE_ITEM = "INSERT INTO electronics.storage_item(storage_id, " +
            "product_id, amount) VALUES(?, ?, ?)";
    private static final String UPDATE_STORAGE_ITEM_BY_ID = "UPDATE electronics.storage_item " +
            "SET storage_id = ?, product_id = ?, amount = ? WHERE id = ?";
    private static final String ID = "id";
    private static final String PRODUCT_ID = "product_id";
    private static final String STORAGE_ID = "storage_id";
    private static final String AMOUNT = "amount";
    private static final String DELETED = "deleted";
    private static final String ELECTRONICS_STORAGE_ITEM = "electronics.storage_item";
    private static final String COULDN_T_SET_STORAGE_ITEM= "Couldn't set storage item variables for prepared statement";

    @Override
    protected StorageItem getObjectFromResultSet(ResultSet rs) throws DaoException {
        StorageItem storageItem = new StorageItem();
        try {
            storageItem.setId(rs.getInt(ID));
            Product product = new Product(rs.getInt(PRODUCT_ID));
            storageItem.setProduct(product);
            Storage storage = new Storage(rs.getInt(STORAGE_ID));
            storageItem.setStorage(storage);
            storageItem.setAmount(rs.getInt(AMOUNT));
            storageItem.setDeleted(rs.getBoolean(DELETED));
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
        return ELECTRONICS_STORAGE_ITEM;
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(StorageItem storageItem, PreparedStatement ps) throws DaoException {
        try {
            ps.setInt(1, storageItem.getStorage().getId());
            ps.setInt(2, storageItem.getProduct().getId());
            ps.setInt(3, storageItem.getAmount());
        } catch (SQLException e) {
            throw new DaoException(COULDN_T_SET_STORAGE_ITEM);
        }
    }
}

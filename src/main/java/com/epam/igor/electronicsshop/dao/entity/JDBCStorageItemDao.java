package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.entity.Storage;
import com.epam.igor.electronicsshop.entity.StorageItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCStorageItemDao extends JDBCAbstractDao<StorageItem> {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCOrderingItemDao.class);
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
    private static final String COULDN_T_SET_STORAGE_ITEM = "Couldn't set storage item variables for prepared statement";
    private static final String COULDN_T_GET_STORAGE_ITEM_FROM_RESULT_SET = "Couldn't get storage item from result set";

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
            LOG.info(COULDN_T_GET_STORAGE_ITEM_FROM_RESULT_SET, e);
            throw new DaoException(COULDN_T_GET_STORAGE_ITEM_FROM_RESULT_SET, e);
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
            LOG.info(COULDN_T_SET_STORAGE_ITEM, e);
            throw new DaoException(COULDN_T_SET_STORAGE_ITEM);
        }
    }
}

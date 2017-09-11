package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.entity.JDBCAbstractDao;
import com.epam.igor.electronicsshop.entity.Image;
import com.epam.igor.electronicsshop.entity.Product;
import org.joda.time.DateTime;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by User on 02.08.2017.
 */
public class JDBCImageDao extends JDBCAbstractDao<Image> {
    private static final String CONTENT = "content";
    private static final String INSERT_IMAGE = "INSERT INTO electronics.image(name, product_id, " +
            "content, date_modified) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_IMAGE_BY_ID = "UPDATE electronics.image " +
            "SET name = ?, product_id = ?, content = ?, date_modified";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PRODUCT_ID = "product_id";
    private static final String DATE_MODIFIED = "date_modified";
    private static final String DELETED = "deleted";
    private static final String CANNOT_GET_IMAGE = "Cannot get image from result set";
    private static final String IMAGE = "image";
    private static final String COULDN_T_SET_IMAGE = "Couldn't set image variables from prepared statement";

    @Override
    protected Image getObjectFromResultSet(ResultSet rs) throws DaoException {
        Image image = new Image();
        try {
            image.setId(rs.getInt(ID));
            image.setName(rs.getString(NAME));
            Product product = new Product(rs.getInt(PRODUCT_ID));
            image.setProduct(product);
            image.setImageStream(rs.getBinaryStream(CONTENT));
            image.setModifiedTime(new DateTime(rs.getTimestamp(DATE_MODIFIED)));
            image.setDeleted(rs.getBoolean(DELETED));
        } catch (SQLException e) {
            throw new DaoException(CANNOT_GET_IMAGE, e);
        }
        return image;
    }

    @Override
    protected String getQueryForInsert() {
        return INSERT_IMAGE;
    }

    @Override
    protected String getQueryForUpdate() {
        return UPDATE_IMAGE_BY_ID;
    }

    @Override
    protected String getTableName() {
        return IMAGE;
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(Image image, PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, image.getName());
            ps.setInt(2, image.getProduct().getId());
            ps.setBinaryStream(3, image.getImageStream());
            ps.setTimestamp(4, new Timestamp(image.getTimeModified().getMillis()));
        } catch (SQLException e) {
            throw new DaoException(COULDN_T_SET_IMAGE);
        }
    }
}

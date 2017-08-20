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
    private static final String INSERT_IMAGE = "INSERT INTO reshop.image(name, product_id, " +
            "content, date_modified) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_IMAGE_BY_ID = "UPDATE reshop.image " +
            "SET name = ?, ST product_id = ?, SET content = ?, SET date_modified";
    @Override
    protected Image getObjectFromResultSet(ResultSet rs) throws DaoException {
        Image image = new Image();
        try {
            image.setId(rs.getInt("id"));
            image.setName(rs.getString("name"));
            Product product = new Product(rs.getInt("product_id"));
            image.setProduct(product);
            image.setImageStream(rs.getBinaryStream("content"));
            image.setModifiedTime(new DateTime(rs.getTimestamp("date_modified")));
            image.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException e) {
            throw new DaoException("Cannot get image from result set", e);
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
        return "image";
    }

    @Override
    protected void setVariablesForPreparedStatementExceptId(Image image, PreparedStatement ps) throws DaoException {
        try {
            ps.setString(1, image.getName());
            ps.setInt(2, image.getProduct().getId());
            ps.setBinaryStream(3, image.getImageStream());
            ps.setTimestamp(4, new Timestamp(image.getTimeModified().getMillis()));
        } catch (SQLException e) {
            throw new DaoException("Couldn't set image variables from prepared statement");
        }
    }
}

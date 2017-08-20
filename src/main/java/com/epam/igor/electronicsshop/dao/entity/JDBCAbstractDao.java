package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.Dao;
import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.GenericDaoInterface;
import com.epam.igor.electronicsshop.entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 01.08.2017.
 */
public abstract class JDBCAbstractDao<T extends BaseEntity> implements GenericDaoInterface<T> {

    private static final String SELECT_FROM = "SELECT * FROM ";
    private static final String WHERE_ID = "WHERE id = ";
    private static final String WHERE = " WHERE ";
    private static final String WHERE_NOT_DELETED = "WHERE deleted = 0 ";
    private static final String UPDATE = "UPDATE ";
    private static final String ORDER_BY_ID = " ORDER BY id ";
    private static final String SET_DELETED = "SET deleted = 1 ";
    private static final String LIMIT_OFFSET = "LIMIT = ? OFFSET = ? ";
    private static final Logger LOG = LoggerFactory.getLogger(JDBCAbstractDao.class);
    private Connection connection;

    public JDBCAbstractDao() {
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    protected abstract String getTableName();

    protected abstract String getQueryForInsert();

    protected abstract String getQueryForUpdate();

    protected abstract T getObjectFromResultSet(ResultSet rs) throws DaoException;

    protected abstract void setVariablesForPreparedStatementExceptId(T t, PreparedStatement ps) throws DaoException;

    public void setVariablesForPreparedStatement(T t, PreparedStatement ps) throws DaoException{
        setVariablesForPreparedStatementExceptId(t, ps);
        int lastParameter;
        try {
            lastParameter = ps.getParameterMetaData().getParameterCount();
            ps.setInt(lastParameter, t.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T insert(T t) throws DaoException {
        try {
            PreparedStatement ps = connection.prepareStatement(getQueryForInsert(), Statement.RETURN_GENERATED_KEYS);
            setVariablesForPreparedStatementExceptId(t, ps);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            t.setId(rs.getInt(1));
            LOG.debug("{} inserted ", t);
        } catch (SQLException e) {
            throw new DaoException("Couldn't insert Object to DB", e);
        }
        return t;
    }

    @Override
    public T findByPK(Integer id) throws DaoException {
        Statement st = null;
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(SELECT_FROM + getTableName() + WHERE_ID + id);
            rs.next();
            T object = getObjectFromResultSet(rs);
            LOG.debug("Getting object {} with id = {}",object, id);
            return object;
        } catch (SQLException e) {
            throw new DaoException("Couldn't find object by current id", e);
        }
    }

    @Override
    public List<T> findAllByParams(Map<String, String> params) throws DaoException {
        List<T> objects = new ArrayList<>();
        try(Statement st = connection.createStatement();) {
            ResultSet rs = st.executeQuery(createQueryForFindAllByParams(params));
            while(rs.next()){
                objects.add(getObjectFromResultSet(rs));
                LOG.debug("Get object list - {}", objects);
            }
        } catch (SQLException e) {
            throw  new DaoException("couldn't get objects list", e);
        }
        return objects;
    }

    @Override
    public List<T> findAll() throws DaoException {
        List<T> objects = new ArrayList();
        try (Statement st = connection.createStatement();){
            ResultSet rs = st.executeQuery(SELECT_FROM + getTableName() + ORDER_BY_ID);
            while(rs.next()){
                objects.add(getObjectFromResultSet(rs));
            }
            LOG.debug("Get objects list - {}", objects);
        } catch (SQLException e) {
            throw new DaoException("Could't get object list", e);
        }
        return null;
    }

    @Override
    public List<T> findAll(int pageNumber, int pageSize) throws DaoException {
        List<T> objects = new ArrayList<>();
        try(PreparedStatement st = connection.prepareStatement(SELECT_FROM + getTableName() + WHERE_NOT_DELETED + LIMIT_OFFSET)) {
            st.setInt(1, pageSize);
            st.setInt(2, (pageNumber - 1) * pageSize);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                objects.add(getObjectFromResultSet(rs));
                LOG.debug("Get object list from page number - {} and page siae - {} - {}", pageNumber, pageSize, objects);
            }
        } catch (SQLException e) {
            throw new DaoException("Couldn't get object list", e);
        }
        return objects;
    }

    @Override
    public void update(T t) throws DaoException {
        try {
            PreparedStatement ps = connection.prepareStatement(getQueryForUpdate());
            setVariablesForPreparedStatement(t, ps);
            ps.executeUpdate();
            LOG.debug("Updating ", t);
        } catch (SQLException e) {
            throw new DaoException("Couldn't update Object in DB");
        }
    }

    @Override
    public void delete(Integer id) throws DaoException {
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(UPDATE + getTableName() + SET_DELETED + WHERE_ID + id);
            LOG.debug("Object with id - {} deleted from {} table", id, getTableName());
        } catch (SQLException e) {
            throw new DaoException("Couldn't delete object", e);
        }
    }

    @Override
    public int getNotDeletedCount() throws DaoException {
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(SELECT_FROM + getTableName() + WHERE_NOT_DELETED);
            rs.next();
            int count = rs.getInt(1);
            LOG.debug("{} table has {} not deleted rows", getTableName(), count);
            return count;
        } catch (SQLException e) {
            throw new DaoException("Couldn't get count", e);
        }
    }

    public String createQueryForFindAllByParams(Map<String, String> params){
        String resultQuery = SELECT_FROM + getTableName() + WHERE;
        for(Map.Entry<String, String> param : params.entrySet()){
            if(params.size() == 1){
                resultQuery += param.getKey() + " = '" + param.getValue() + "'";
                return resultQuery;
            }
            else{
                resultQuery += param.getKey() + " = '" + param.getValue() + "' AND ";
            }
        }
        return resultQuery.substring(0, resultQuery.length() - 5); //if no params then select all
    }
}

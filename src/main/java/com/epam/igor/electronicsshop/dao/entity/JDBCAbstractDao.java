package com.epam.igor.electronicsshop.dao.entity;

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
 * Class for common dao methods
 * @author Igor Lapin
 */
public abstract class JDBCAbstractDao<T extends BaseEntity> implements GenericDaoInterface<T> {

    private static final String SELECT_FROM = "SELECT * FROM ";
    private static final String SELECT_COUNT_FROM = "SELECT count(*) FROM ";
    private static final String WHERE_ID = " WHERE id = ";
    private static final String WHERE = " WHERE ";
    private static final String WHERE_NOT_DELETED = " WHERE deleted = 0 ";
    private static final String UPDATE = "UPDATE ";
    private static final String ORDER_BY_ID = " ORDER BY id ";
    private static final String SET_DELETED = " SET deleted = 1 ";
    private static final Logger LOG = LoggerFactory.getLogger(JDBCAbstractDao.class);
    private static final String OBJECT_WITH_ID_DELETED_FROM_TABLE = "Object with id - {} deleted from {} table";
    private static final String INSERTED = "{} inserted ";
    private static final String COULDN_T_INSERT_OBJECT_TO_DB = "Couldn't insert Object to DB";
    private static final String GETTING_OBJECT_WITH_ID = "Getting object {} with id = {}";
    private static final String COULDN_T_FIND_OBJECT_BY_CURRENT_ID = "Couldn't find object by current id";
    private static final String GET_ENTITY_LIST_BY_CURRENT_PARAMS = "Get entity list by current params: {} - {}";
    private static final String GET_ENTITY_LIST = "Get entity list - {}";
    private static final String COULD_NOT_FIND_OBJECT_BY_CURRENT_ID = "Could not find object by current id";
    private static final String UPDATING = "Updating ";
    private static final String COULDN_T_UPDATE_OBJECT_IN_DB = "Couldn't update Object in DB";
    private static final String COULDN_T_DELETE_OBJECT = "Couldn't delete object";
    private static final String COULDN_T_GET_COUNT = "Couldn't get count";
    private static final String TABLE_HAS_NOT_DELETED_ROWS = "{} table has {} not deleted rows";
    private static final String RESULT_QUERY = "result query - {}";
    private static final String AND = "' AND ";
    private static final String COULD_NOT_FIND_OBJECT_WITH_THIS_PARAMS = "Could not find object with this params";
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

    /**
     * sets id
     * @param t Entity
     * @param ps prepared statement except id
     * @throws DaoException
     */
    private void setVariablesForPreparedStatement(T t, PreparedStatement ps) throws DaoException{
        setVariablesForPreparedStatementExceptId(t, ps);
        int lastParameter;
        try {
            lastParameter = ps.getParameterMetaData().getParameterCount();
            ps.setInt(lastParameter, t.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * inserts entity
     * @param t entity
     * @return Entity
     * @throws DaoException
     */
    @Override
    public T insert(T t) throws DaoException {
        try {
            PreparedStatement ps = connection.prepareStatement(getQueryForInsert(), Statement.RETURN_GENERATED_KEYS);
            setVariablesForPreparedStatementExceptId(t, ps);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            t.setId(rs.getInt(1));
            LOG.debug(INSERTED, t);
        } catch (SQLException e) {
            throw new DaoException(COULDN_T_INSERT_OBJECT_TO_DB, e);
        }
        return t;
    }

    /**
     * gets entity with such id
     * @param id or primary key
     * @return Entity
     * @throws DaoException
     */
    @Override
    public T findByPK(Integer id) throws DaoException {
        try(Statement st = connection.createStatement();) {
            LOG.info(SELECT_FROM + getTableName() + WHERE_ID + id);
            ResultSet rs = st.executeQuery(SELECT_FROM + getTableName() + WHERE_ID + id);
            LOG.info(SELECT_FROM + getTableName() + WHERE_ID + id);
            rs.next();
            T object = getObjectFromResultSet(rs);
            LOG.debug(GETTING_OBJECT_WITH_ID, object, id);
            return object;
        } catch (SQLException e) {
            throw new DaoException(COULDN_T_FIND_OBJECT_BY_CURRENT_ID, e);
        }
    }

    /**
     * get list of objects with such params
     * @param params
     * @return objects list
     * @throws DaoException
     */
    @Override
    public List<T> findAllByParams(Map<String, String> params) throws DaoException {
        List<T> objects = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(createQueryForFindAllByParams(params))) {
            while (rs.next()) {
                objects.add(getObjectFromResultSet(rs));
            }
            LOG.debug(GET_ENTITY_LIST_BY_CURRENT_PARAMS, params, objects);
        } catch (SQLException e) {
            throw new DaoException(COULD_NOT_FIND_OBJECT_WITH_THIS_PARAMS, e);
        }
        return objects;
    }

    /**
     * gets all objects from table
     * @return objects list
     * @throws DaoException
     */
    @Override
    public List<T> findAll() throws DaoException {
        List<T> objects = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(SELECT_FROM + getTableName() + ORDER_BY_ID)) {
            while (rs.next()) {
                objects.add(getObjectFromResultSet(rs));
            }
            LOG.info(SELECT_FROM + getTableName() + ORDER_BY_ID);
            LOG.debug(GET_ENTITY_LIST, objects);
        } catch (SQLException e) {
            throw new DaoException(COULD_NOT_FIND_OBJECT_BY_CURRENT_ID, e);
        }
        return objects;
    }

    /**
     * gets objects for page with certain size and number
     * @param pageNumber
     * @param pageSize
     * @return objects list
     * @throws DaoException
     */
    @Override
    public List<T> findAll(int pageNumber, int pageSize) throws DaoException {
        List<T> objects = new ArrayList<>();

        try (PreparedStatement st = connection.prepareStatement(SELECT_FROM + getTableName() + " WHERE deleted=0 LIMIT ? OFFSET ?")) {
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

    /**
     * updates object in table
     * @param t entity
     * @throws DaoException
     */
    @Override
    public void update(T t) throws DaoException {
        try(PreparedStatement ps = connection.prepareStatement(getQueryForUpdate());) {
            setVariablesForPreparedStatement(t, ps);
            ps.executeUpdate();
            LOG.debug(UPDATING, t);
        } catch (SQLException e) {
            throw new DaoException(COULDN_T_UPDATE_OBJECT_IN_DB);
        }
    }

    /**
     * sets deleted column as true
     * @param id
     * @throws DaoException
     */
    @Override
    public void delete(Integer id) throws DaoException {
        try(Statement st = connection.createStatement();) {
            st.executeUpdate(UPDATE + getTableName() + SET_DELETED + WHERE_ID + id);
            LOG.debug(OBJECT_WITH_ID_DELETED_FROM_TABLE, id, getTableName());
        } catch (SQLException e) {
            throw new DaoException(COULDN_T_DELETE_OBJECT, e);
        }
    }

    /**
     * get all objects from table if deleted column is false
     * @return objects list
     * @throws DaoException
     */
    @Override
    public int getNotDeletedCount() throws DaoException {
        try(Statement st = connection.createStatement();) {
            ResultSet rs = st.executeQuery(SELECT_COUNT_FROM + getTableName() + WHERE_NOT_DELETED);
            rs.next();
            int count = rs.getInt(1);
            LOG.debug(TABLE_HAS_NOT_DELETED_ROWS, getTableName(), count);
            return count;
        } catch (SQLException e) {
            throw new DaoException(COULDN_T_GET_COUNT, e);
        }
    }

    /**creates query for getting objects with certain params
     * @param params
     * @return query(String)
     */
    private String createQueryForFindAllByParams(Map<String, String> params){
        String resultQuery = SELECT_FROM + getTableName() + WHERE;
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (params.size() == 1) {
                resultQuery += param.getKey() + " = '" + param.getValue() + "'";
                return resultQuery;
            } else {
                resultQuery += param.getKey() + " = '" + param.getValue() + AND;
            }
        }
        LOG.info(RESULT_QUERY, resultQuery);
        return resultQuery.substring(0, resultQuery.length() - 5);
    }
}

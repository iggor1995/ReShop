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
 *
 * @author Igor Lapin
 */
public abstract class JDBCAbstractDao<T extends BaseEntity> implements GenericDaoInterface<T> {
    private static final String SELECT_FROM = "SELECT * FROM ";
    private static final String SELECT_COUNT_FROM = "SELECT count(*) FROM ";
    private static final String WHERE_ID = " WHERE id = ";
    private static final String WHERE = " WHERE ";
    private static final String WHERE_NOT_DELETED = " WHERE deleted = 0 ";
    private static final String ORDER_BY_ID = " ORDER BY id ";
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
    private static final String COULDN_T_SET_VARIABLES_FOR_PREPARED_STATEMENT = "Couldn't set variables for prepared statement";
    private static final String COULDN_T_GET_OBJECT_LIST = "Couldn't get object list";
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
     *
     * @param t  Entity
     * @param ps prepared statement except id
     * @throws DaoException
     */
    private void setVariablesForPreparedStatement(T t, PreparedStatement ps) throws DaoException {
        setVariablesForPreparedStatementExceptId(t, ps);
        int lastParameter;
        try {
            lastParameter = ps.getParameterMetaData().getParameterCount();
            ps.setInt(lastParameter, t.getId());
        } catch (SQLException e) {
            LOG.info(COULDN_T_SET_VARIABLES_FOR_PREPARED_STATEMENT, e);
            throw new DaoException(COULDN_T_SET_VARIABLES_FOR_PREPARED_STATEMENT, e);
        }
    }

    /**
     * inserts entity
     *
     * @param t entity
     * @return Entity
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
            LOG.info(COULDN_T_INSERT_OBJECT_TO_DB, e);
            throw new DaoException(COULDN_T_INSERT_OBJECT_TO_DB, e);
        }
        return t;
    }

    /**
     * gets entity with such id
     *
     * @param id or primary key
     * @return Entity
     */
    @Override
    public T findByPK(Integer id) throws DaoException {
        StringBuilder builder = new StringBuilder();
        builder.append(SELECT_FROM).append(getTableName()).append(WHERE_ID).append(id);
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(builder.toString())) {
            LOG.info(builder.toString());
            rs.next();
            T object = getObjectFromResultSet(rs);
            LOG.debug(GETTING_OBJECT_WITH_ID, object, id);
            return object;
        } catch (SQLException e) {
            LOG.info(COULDN_T_FIND_OBJECT_BY_CURRENT_ID, e);
            throw new DaoException(COULDN_T_FIND_OBJECT_BY_CURRENT_ID, e);
        }
    }

    /**
     * get list of objects with such params
     *
     * @param params
     * @return objects list
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
            LOG.info(COULD_NOT_FIND_OBJECT_WITH_THIS_PARAMS, e);
            throw new DaoException(COULD_NOT_FIND_OBJECT_WITH_THIS_PARAMS, e);
        }
        return objects;
    }

    /**
     * gets all objects from table
     *
     * @return objects list
     */
    @Override
    public List<T> findAll() throws DaoException {
        List<T> objects = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(SELECT_FROM).append(getTableName()).append(ORDER_BY_ID);
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(builder.toString())) {
            while (rs.next()) {
                objects.add(getObjectFromResultSet(rs));
            }
            LOG.info(builder.toString());
            LOG.debug(GET_ENTITY_LIST, objects);
        } catch (SQLException e) {
            LOG.info(COULDN_T_FIND_OBJECT_BY_CURRENT_ID, e);
            throw new DaoException(COULD_NOT_FIND_OBJECT_BY_CURRENT_ID, e);
        }
        return objects;
    }

    /**
     * gets objects for page with certain size and number
     *
     * @param pageNumber
     * @param pageSize
     * @return objects list
     */
    @Override
    public List<T> findAll(int pageNumber, int pageSize) throws DaoException {
        List<T> objects = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(SELECT_FROM).append(getTableName()).append(" WHERE deleted=0 LIMIT ? OFFSET ?");
        try (PreparedStatement st = connection.prepareStatement(builder.toString())) {
            st.setInt(1, pageSize);
            st.setInt(2, (pageNumber - 1) * pageSize);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                objects.add(getObjectFromResultSet(rs));
                LOG.debug("Get object list from page number - {} and page siae - {} - {}", pageNumber, pageSize, objects);
            }
        } catch (SQLException e) {
            LOG.info(COULDN_T_GET_OBJECT_LIST, e);
            throw new DaoException(COULDN_T_GET_OBJECT_LIST, e);
        }
        return objects;
    }

    /**
     * updates object in table
     *
     * @param t entity
     */
    @Override
    public void update(T t) throws DaoException {
        try (PreparedStatement ps = connection.prepareStatement(getQueryForUpdate())) {
            setVariablesForPreparedStatement(t, ps);
            ps.executeUpdate();
            LOG.debug(UPDATING, t);
        } catch (SQLException e) {
            LOG.info(COULDN_T_UPDATE_OBJECT_IN_DB, e);
            throw new DaoException(COULDN_T_UPDATE_OBJECT_IN_DB);
        }
    }

    /**
     * sets deleted column as true
     *
     * @param id
     * @throws DaoException
     */
    @Override
    public void delete(Integer id) throws DaoException {
        try (Statement st = connection.createStatement()) {
            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE ").append(getTableName()).append(" SET deleted=1").append(WHERE_ID).append(id);
            st.executeUpdate(builder.toString());

            LOG.debug(OBJECT_WITH_ID_DELETED_FROM_TABLE, id, getTableName());
        } catch (SQLException e) {
            LOG.info(COULDN_T_DELETE_OBJECT, e);
            throw new DaoException(COULDN_T_DELETE_OBJECT, e);
        }
    }

    /**
     * get all objects from table if deleted column is false
     *
     * @return objects list
     * @throws DaoException
     */
    @Override
    public int getNotDeletedCount() throws DaoException {
        StringBuilder builder = new StringBuilder();
        builder.append(SELECT_COUNT_FROM).append(getTableName()).append(WHERE_NOT_DELETED);
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(builder.toString())) {
            rs.next();
            int count = rs.getInt(1);
            LOG.debug(TABLE_HAS_NOT_DELETED_ROWS, getTableName(), count);
            return count;
        } catch (SQLException e) {
            LOG.info(COULDN_T_GET_COUNT, e);
            throw new DaoException(COULDN_T_GET_COUNT, e);
        }
    }

    /**
     * creates query for getting objects with certain params
     *
     * @param params
     * @return query(String)
     */
    private String createQueryForFindAllByParams(Map<String, String> params) {
        StringBuilder resultQuery = new StringBuilder();
        resultQuery.append(SELECT_FROM).append(getTableName()).append(WHERE);
        //String resultQuery = SELECT_FROM + getTableName() + WHERE;
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (params.size() == 1) {
                resultQuery.append(param.getKey()).append(" = '").append(param.getValue()).append("'");
                //resultQuery += param.getKey() + " = '" + param.getValue() + "'";
                return resultQuery.toString();
            } else {
                resultQuery.append(param.getKey()).append(" = '").append(param.getValue()).append(AND);
                //resultQuery += param.getKey() + " = '" + param.getValue() + AND;
            }
        }
        LOG.info(RESULT_QUERY, resultQuery);
        return resultQuery.substring(0, resultQuery.length() - 5);
    }
}

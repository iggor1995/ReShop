package com.epam.igor.electronicsshop.service;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.DaoFactory;
import com.epam.igor.electronicsshop.dao.GenericDaoInterface;
import com.epam.igor.electronicsshop.entity.*;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.epam.igor.electronicsshop.dao.DaoFactory.getDaoFactory;

/**
 * Set methods for working with user
 *
 * @author Igor Lapin
 */

public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private static final String EMAIL = "email";
    private static final String COULD_NOT_CHECK_EMAIL = "Could not check email";
    private static final String COULDN_T_REGISTER_USER = "Couldn't register user";
    private static final String COULDN_T_GET_USER_ADDRESS = "Couldn't get user address";
    private static final String COULDN_T_UPDATE_USER = "Couldn't update user";
    private static final String KZT = "KZT";
    private static final String COULDN_T_REFILL_USER_S_MONEY = "Couldn't refill user's money";
    private static final String COULDN_T_INITIALIZE_FACTORY = "Couldn't initialize factory";
    private static final String USER_ID = "user_id";
    private static final String ORDER_ID = "order_id";
    private static final String COULDN_T_GET_USER_S_ORDERS_LIST = "Couldn't get user's orders list";
    private static final String COULDN_T_UPDATE_USER_S_ADDRESS = "Couldn't update user's address";
    private static final String COULDN_T_GET_FILLED_USER_BY_ID = "Couldn't get filled user by id";
    private static final String COULDN_T_GET_USER = "Couldn't get user";
    private static final String PASSWORD = "password";
    private static final String COULDN_T_GET_USER_S_GENDER = "Couldn't get user's gender";
    private static final String USER_UPDATED = "User updated";

    /**
     * Performing user login. If user's params don't match, return null
     *
     * @param email
     * @param password
     * @return user
     * @throws ServiceException
     */
    public User performUserLogin(String email, String password) throws ServiceException {
        try (DaoFactory jdbcDaofactory = getDaoFactory();) {
            Map<String, String> params = new HashMap<>();
            params.put(EMAIL, email);
            params.put(PASSWORD, password);
            GenericDaoInterface<User> userDao = jdbcDaofactory.getDao(User.class);
            List<User> users = userDao.findAllByParams(params);
            if (!users.isEmpty() && !users.get(0).isDeleted()) {
                return users.get(0);
            }
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_USER, e);
            throw new ServiceException(e, COULDN_T_GET_USER);
        }
        return null;
    }

    /**
     * Email has to be unique in user's database, so before storing it into database
     * email has to be checked if it already exists
     *
     * @param email
     * @return false, if no same email found, or return true otherwise
     * @throws ServiceException
     */
    public boolean checkEmail(String email) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            List<User> usersWithCurrentEmail = userDao.findAllByParams(Collections.singletonMap(EMAIL, email));
            return usersWithCurrentEmail.isEmpty();
        } catch (DaoException e) {
            LOG.info(COULD_NOT_CHECK_EMAIL, e);
            throw new ServiceException(e, COULD_NOT_CHECK_EMAIL);
        }
    }

    /**
     * Registering user. Inserting new user and new user's address in database
     *
     * @param user
     * @param address
     * @return new registered user
     * @throws ServiceException if dao exception caught throw Service exception adn do rollback transaction
     */
    public User registerUser(User user, Address address) throws ServiceException {
        User registeredUser;
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            registeredUser = registerUserToDB(jdbcDaoFactory, user, address);
        } catch (DaoException e) {
            throw new ServiceException(e, COULDN_T_INITIALIZE_FACTORY);
        }
        return registeredUser;
    }

    /**method adds user and address to db
     * @param jdbcDaoFactory
     * @param user
     * @param address
     */
    private User registerUserToDB(DaoFactory jdbcDaoFactory, User user, Address address) throws DaoException, ServiceException {
        User registeredUser;
        try {
            jdbcDaoFactory.startTransaction();
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            GenericDaoInterface<Address> addressDao = jdbcDaoFactory.getDao(Address.class);
            Address registeredAddress = addressDao.insert(address);
            user.setAddress(registeredAddress);
            registeredUser = userDao.insert(user);
            jdbcDaoFactory.commitTransaction();
        } catch (DaoException e) {
            LOG.info(COULDN_T_REGISTER_USER, e);
            jdbcDaoFactory.rollbackTransaction();
            throw new ServiceException(e, COULDN_T_REGISTER_USER);
        }
        return registeredUser;
    }

    /**
     * get user's address by user
     *
     * @param user
     * @return address
     */
    public Address getUserAddress(User user) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            GenericDaoInterface<Address> addressDao = jdbcDaoFactory.getDao(Address.class);
            return addressDao.findByPK(user.getAddress().getId());
        } catch (DaoException e) {
            LOG.info(COULDN_T_UPDATE_USER_S_ADDRESS, e);
            throw new ServiceException(e, COULDN_T_GET_USER_ADDRESS);
        }
    }

    /**
     * get user's gender by user
     *
     * @param user
     * @return gender
     */
    public Gender getUserGender(User user) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            GenericDaoInterface<Gender> genderDao = jdbcDaoFactory.getDao(Gender.class);
            return genderDao.findByPK(user.getGender().getId());
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_USER_S_GENDER, e);
            throw new ServiceException(e, COULDN_T_GET_USER_S_GENDER);
        }
    }

    /**
     * if user has been changed, it has to be updated in database
     *
     * @param user
     */
    public User updateUser(User user) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            userDao.update(user);
            LOG.info(USER_UPDATED);
        } catch (DaoException e) {
            LOG.info(COULDN_T_UPDATE_USER, e);
            throw new ServiceException(e, COULDN_T_UPDATE_USER);
        }
        return user;
    }

    /**
     * refilling users cash
     *
     * @param id
     * @param cash
     * @return updated user
     */
    public User refillCash(int id, String cash) throws ServiceException {
        User user;
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            user = updateUserCash(jdbcDaoFactory, id, cash);
        } catch (DaoException e) {
            LOG.info(COULDN_T_INITIALIZE_FACTORY, e);
            throw new ServiceException(e, COULDN_T_INITIALIZE_FACTORY);
        }
        return user;
    }

    /** Method updates users money
     * @param jdbcDaoFactory
     * @param id
     * @param cash
     */
    private User updateUserCash(DaoFactory jdbcDaoFactory, int id, String cash) throws DaoException, ServiceException {
        User user;
        try {
            jdbcDaoFactory.startTransaction();
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            user = userDao.findByPK(id);
            Money totalCash = user.getCash().plus(Money.parse(KZT + cash));
            user.setCash(totalCash);
            userDao.update(user);
            jdbcDaoFactory.commitTransaction();
        } catch (DaoException e) {
            LOG.info(COULDN_T_REFILL_USER_S_MONEY, e);
            jdbcDaoFactory.rollbackTransaction();
            throw new ServiceException(e, COULDN_T_REFILL_USER_S_MONEY);
        }
        return user;
    }

    /**
     * get user's orders. Set order's status, items. Set item's product
     *
     * @param id
     */
    public List<Order> getUserOrders(int id) throws ServiceException {
        List<Order> orders;
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
            GenericDaoInterface<OrderStatus> orderStatusDao = jdbcDaoFactory.getDao(OrderStatus.class);
            GenericDaoInterface<OrderingItem> orderItemDao = jdbcDaoFactory.getDao(OrderingItem.class);
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            orders = orderDao.findAllByParams(Collections.singletonMap(USER_ID, String.valueOf(id)));
            for (Order order : orders) {
                order.setStatus(orderStatusDao.findByPK(order.getStatus().getId()));
                List<OrderingItem> orderingItems = orderItemDao.
                        findAllByParams(Collections.singletonMap(ORDER_ID, String.valueOf(order.getId())));
                for (OrderingItem item : orderingItems) {
                    item.setProduct(productDao.findByPK(item.getProduct().getId()));
                }
                order.setOrderingItems(orderingItems);
            }
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_USER_S_ORDERS_LIST, e);
            throw new ServiceException(e, COULDN_T_GET_USER_S_ORDERS_LIST);
        }
        return orders;
    }

    /**
     * if address has been changed, it has to be updated in database
     *
     * @param address
     * @return updated address
     * @throws ServiceException
     */
    public Address updateUserAddress(Address address) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            GenericDaoInterface<Address> addressDao = jdbcDaoFactory.getDao(Address.class);
            addressDao.update(address);
        } catch (DaoException e) {
            LOG.info(COULDN_T_UPDATE_USER_S_ADDRESS, e);
            throw new ServiceException(e, COULDN_T_UPDATE_USER_S_ADDRESS);
        }
        return address;
    }

    /**
     * get user by user's ID filled with address, gender
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public User getFilledUserById(int id) throws ServiceException {
        User user;
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            GenericDaoInterface<Address> addressDao = jdbcDaoFactory.getDao(Address.class);
            GenericDaoInterface<Gender> genderDao = jdbcDaoFactory.getDao(Gender.class);
            user = userDao.findByPK(id);
            user.setGender(genderDao.findByPK(user.getGender().getId()));
            user.setAddress(addressDao.findByPK(user.getAddress().getId()));
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_FILLED_USER_BY_ID, e);
            throw new ServiceException(e, COULDN_T_GET_FILLED_USER_BY_ID);
        }
        return user;
    }
}

package com.epam.igor.electronicsshop.service;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.DaoFactory;
import com.epam.igor.electronicsshop.dao.GenericDaoInterface;
import com.epam.igor.electronicsshop.dao.entity.JDBCDaoFactory;
import com.epam.igor.electronicsshop.entity.*;
import org.joda.money.Money;

import java.util.*;

import static com.epam.igor.electronicsshop.dao.DaoFactory.JDBC;
import static com.epam.igor.electronicsshop.dao.DaoFactory.getDaoFactory;

/**
 * Created by User on 03.08.2017.
 */
public class UserService {
    public UserService(){}

    public User performUserLogin(String email, String password) throws ServiceException{

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        DaoFactory jdbcDaofactory = new JDBCDaoFactory();
        try {
            GenericDaoInterface<User> userDao = jdbcDaofactory.getDao(User.class);
        } catch (DaoException e) {
            throw new ServiceException("");
        }
        return null;
    }
    public boolean checkEmail(String email) throws ServiceException{
        List<User> usersWithThisEmail = new ArrayList<>();
        try(DaoFactory jdbcDaoFactory = new JDBCDaoFactory()) {
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            usersWithThisEmail = userDao.findAllByParams(Collections.singletonMap("email", email));
            return usersWithThisEmail.isEmpty();
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't check email");
        }
    }
    public User registerUser(User user, Address address) throws ServiceException{
        User registeredUser;
        try(DaoFactory jdbcDaoFactory = new JDBCDaoFactory()) {
            try {
                jdbcDaoFactory.startTransaction();
                GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
                GenericDaoInterface<Address> addressDao = jdbcDaoFactory.getDao(Address.class);
                Address registeredAddress = addressDao.insert(address);
                user.setAddress(registeredAddress);
                registeredUser = userDao.insert(user);
                jdbcDaoFactory.commitTransaction();
            }
            catch (DaoException e){
                jdbcDaoFactory.rollbackTransaction();
                throw new ServiceException(e, "Couldn't register user");
            }
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't initialize factory");
        }
        return registeredUser;
    }
    public Address getUserAddress(User user) throws ServiceException{
        try(DaoFactory jdbcDaoFactory = new JDBCDaoFactory()) {
            GenericDaoInterface<Address> addressDao = jdbcDaoFactory.getDao(Address.class);
            Address address = addressDao.findByPK(user.getAddress().getId());
            return address;
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get user address");
        }
    }
    public Gender getUserGender(User user) throws ServiceException{
        try(DaoFactory jdbcDaoFactory = new JDBCDaoFactory()) {
            GenericDaoInterface<Gender> genderDao = jdbcDaoFactory.getDao(Gender.class);
            Gender gender = genderDao.findByPK(user.getGender().getId());
            return gender;
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get user address");
        }
    }
    public User getUserById(int id)throws ServiceException{
        User user;
        try(DaoFactory jdbcDaoFactory = new JDBCDaoFactory()) {
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            GenericDaoInterface<Address> addressDao = jdbcDaoFactory.getDao(Address.class);
            GenericDaoInterface<Gender> genderDao = jdbcDaoFactory.getDao(Gender.class);
            user = userDao.findByPK(id);
            user.setAddress(addressDao.findByPK(user.getAddress().getId()));
            user.setGender(genderDao.findByPK(user.getGender().getId()));
        } catch (DaoException e) {
            throw new ServiceException(e, "Coulldn't get user");
        }
        return user;
    }
    public User updateUser(User user) throws ServiceException{
        try(DaoFactory jdbcDaoFactory = new JDBCDaoFactory()) {
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            userDao.update(user);
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't update user");
        }
        return user;
    }
    public User refillCash(int id, String cash) throws ServiceException {
        User user;
        try (DaoFactory jdbcDaoFactory = new JDBCDaoFactory()) {
            try {
                jdbcDaoFactory.startTransaction();
                GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
                user = userDao.findByPK(id);
                Money totalCash = user.getCash().plus(Money.parse("KZT" + cash));
                user.setCash(totalCash);
                userDao.update(user);
                jdbcDaoFactory.commitTransaction();
            }
            catch (DaoException e){
                jdbcDaoFactory.rollbackTransaction();
                throw new ServiceException(e, "Couldn't refill user's money");
            }
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't initialize factory");
        }
        return user;
    }
    public List<Order> getUserOrders (int id) throws ServiceException{
        List<Order> orders;
        try(DaoFactory jdbcDaoFactory = new JDBCDaoFactory()) {
            GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
            GenericDaoInterface<OrderStatus> orderStatusDao = jdbcDaoFactory.getDao(OrderStatus.class);
            GenericDaoInterface<OrderingItem> orderItemDao = jdbcDaoFactory.getDao(OrderingItem.class);
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            orders = orderDao.findAllByParams(Collections.singletonMap("user_id", String.valueOf(id)));
            for(Order order : orders){
                order.setStatus(orderStatusDao.findByPK(order.getStatus().getId()));
                List<OrderingItem> orderingItems = orderItemDao.findAllByParams(Collections.singletonMap("order_id", String.valueOf(order.getId())));
                for(OrderingItem item : orderingItems){
                    item.setProduct(productDao.findByPK(item.getProduct().getId()));
                }
                order.setOrderingItems(orderingItems);
            }
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get user's orders list");
        }
        return orders;
    }
    public Address updateUserAddress(Address address) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Address> addressDao = jdbcDaoFactory.getDao(Address.class);
            addressDao.update(address);
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't update user's address");
        }
        return address;
    }
}

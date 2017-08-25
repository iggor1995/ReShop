package com.epam.igor.electronicsshop.service;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.DaoFactory;
import com.epam.igor.electronicsshop.dao.GenericDaoInterface;
import com.epam.igor.electronicsshop.dao.entity.JDBCDaoFactory;
import com.epam.igor.electronicsshop.entity.*;

import java.util.Collections;
import java.util.List;

import static com.epam.igor.electronicsshop.dao.DaoFactory.JDBC;
import static com.epam.igor.electronicsshop.dao.DaoFactory.getDaoFactory;

/**
 * Created by User on 12.08.2017.
 */
public class ShopService {
    public ShopService() {
    }
    public Order getOrder(int id) throws ServiceException{
        Order order;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            GenericDaoInterface<OrderingItem> orderingItemDao = jdbcDaoFactory.getDao(OrderingItem.class);
            GenericDaoInterface<OrderStatus> orderStatusDao = jdbcDaoFactory.getDao(OrderStatus.class);
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            order = orderDao.findByPK(id);
            order.setUser(userDao.findByPK(order.getUser().getId()));
            order.setStatus(orderStatusDao.findByPK(order.getStatus().getId()));
            List<OrderingItem> orderingItems = orderingItemDao.findAllByParams(Collections.singletonMap("order_id",
                    String.valueOf(order.getId())));
            for(OrderingItem item : orderingItems){
                item.setProduct(productDao.findByPK(item.getProduct().getId()));
            }
            order.setOrderingItems(orderingItems);
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get order");
        }
        return order;
    }
    public List<Gender> getAllGenders() throws ServiceException{
        List<Gender> genders;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Gender> genderDao = jdbcDaoFactory.getDao(Gender.class);
            genders = genderDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get gender list");
        }
        return genders;
    }
}

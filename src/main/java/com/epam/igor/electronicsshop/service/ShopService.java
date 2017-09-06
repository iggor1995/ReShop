package com.epam.igor.electronicsshop.service;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.DaoFactory;
import com.epam.igor.electronicsshop.dao.GenericDaoInterface;
import com.epam.igor.electronicsshop.dao.entity.JDBCDaoFactory;
import com.epam.igor.electronicsshop.entity.*;

import javax.servlet.ServletException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<ProductType> getAllProductTypes() throws ServiceException{
        List<ProductType> productTypes;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<ProductType> productTypeDao = jdbcDaoFactory.getDao(ProductType.class);
            productTypes = productTypeDao.findAll();
            productTypes = productTypes.stream().filter(productType ->
                    !productType.isDeleted()).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException(e, "Coulnd't get product types list");
        }
        return productTypes;
    }
    public List<Product> getAllProductsOnPage(int pageSize, int pageNumber) throws ServiceException{
        List<Product> products;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            GenericDaoInterface<ProductType> productTypeDao = jdbcDaoFactory.getDao(ProductType.class);
            products = productDao.findAll(pageNumber, pageSize);
            for(Product product : products){
                product.setType(productTypeDao.findByPK(product.getType().getId()));
            }
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get products on page");
        }
        return products;
    }
    public int getProductsCount() throws ServiceException{
        int productsCount;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            return productDao.getNotDeletedCount();
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get products count");
        }
    }
    public User buyCart(Order order) throws ServiceException{
        User user;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            try{
                jdbcDaoFactory.startTransaction();
                GenericDaoInterface<OrderStatus> orderStatusDao = jdbcDaoFactory.getDao(OrderStatus.class);
                GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
                GenericDaoInterface<OrderingItem> orderingItemDao = jdbcDaoFactory.getDao(OrderingItem.class);
                GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
                user = userDao.findByPK(order.getUser().getId());
                user.spendCash(order.getPrice());
                userDao.update(user);
                order.setStatus(orderStatusDao.findByPK(1));
                Order newOrder = orderDao.insert(order);
                for(OrderingItem orderingItem : order.getOrderingItems()){
                    orderingItem.setOrder(newOrder);
                    orderingItemDao.insert(orderingItem);
                }
                jdbcDaoFactory.commitTransaction();
            }catch (DaoException e){
                jdbcDaoFactory.rollbackTransaction();
                throw new ServiceException(e, "Couldn't place order");
            }
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't init factory");
        }
        return user;
    }
}

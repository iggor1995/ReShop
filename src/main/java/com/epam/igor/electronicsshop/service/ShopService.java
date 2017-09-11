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
    public List<User> getAllUsersOnPage(int pageNumber, int pageSize) throws ServiceException{
        List<User> users;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            GenericDaoInterface<Gender> genderDao = jdbcDaoFactory.getDao(Gender.class);
            users = userDao.findAll(pageNumber, pageSize);
            for(User user : users){
                user.setGender(genderDao.findByPK(user.getGender().getId()));
            }
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get users");
        }
        return users;
    }
    public int getUsersCount() throws ServiceException{
        int usersCount;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            usersCount = userDao.getNotDeletedCount();
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get users count");
        }
        return usersCount;
    }
    public List<Order> getAllOrdersOnPage(int pageSize, int pageNumber) throws ServiceException{
        List<Order> orders;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
            GenericDaoInterface<OrderingItem> orderingItemDao = jdbcDaoFactory.getDao(OrderingItem.class);
            GenericDaoInterface<OrderStatus> orderStatusDao = jdbcDaoFactory.getDao(OrderStatus.class);
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            orders = orderDao.findAll(pageNumber, pageSize);
            for(Order order : orders){
                order.setUser(userDao.findByPK(order.getUser().getId()));
                order.setStatus(orderStatusDao.findByPK(order.getStatus().getId()));
                List<OrderingItem> orderingItems = orderingItemDao.findAllByParams(Collections.singletonMap("order_id",
                        String.valueOf(order.getId())));
                for(OrderingItem orderingItem : orderingItems){
                    orderingItem.setProduct(productDao.findByPK(orderingItem.getProduct().getId()));
                }
                order.setOrderingItems(orderingItems);
            }
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get orders on page");
        }
        return orders;
    }
    public List<StorageItem> getAllStorageItemsOnPage(int pageSize, int pageNumber) throws ServiceException{
        List<StorageItem> storageItems;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<StorageItem> storageItemDao = jdbcDaoFactory.getDao(StorageItem.class);
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            GenericDaoInterface<Storage> storageDao = jdbcDaoFactory.getDao(Storage.class);
            storageItems = storageItemDao.findAll(pageNumber, pageSize);
            for(StorageItem storageItem : storageItems){
                storageItem.setStorage(storageDao.findByPK(storageItem.getStorage().getId()));
                storageItem.setProduct(productDao.findByPK(storageItem.getProduct().getId()));
            }
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get storage items on page");
        }
        return storageItems;
    }
    public int getOrdersCount() throws ServiceException{
        int ordersCount;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
            ordersCount = orderDao.getNotDeletedCount();
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get orders count");
        }
        return ordersCount;
    }
    public int getStorageItemsCount() throws ServiceException{
        int storageItemsCount;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<StorageItem> storageItemDao = jdbcDaoFactory.getDao(StorageItem.class);
            storageItemsCount = storageItemDao.getNotDeletedCount();
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get orders count");
        }
        return storageItemsCount;
    }
    public List<OrderStatus> getAllOrderStatuses() throws ServiceException{
        List<OrderStatus> orderStatuses;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<OrderStatus> orderStatusDao = jdbcDaoFactory.getDao(OrderStatus.class);
            orderStatuses = orderStatusDao.findAll();
            orderStatuses = orderStatuses.stream().filter(status -> !status.isDeleted()).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get order statuses");
        }
        return orderStatuses;
    }
    public void updateOrderStatus(String orderId, String statusId) throws ServiceException{
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
            Order order = orderDao.findByPK(Integer.valueOf(orderId));
            order.getStatus().setId(Integer.valueOf(statusId));
            orderDao.update(order);
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't update Order status");
        }
    }
    public void deleteOrderById(String id) throws ServiceException{
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)){
            GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
            orderDao.delete(Integer.valueOf(id));
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't delete order");
        }
    }
    public void deleteProductById(String id) throws ServiceException{
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)){
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            productDao.delete(Integer.valueOf(id));
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't delete product");
        }
    }
    public void deleteUserById(String id) throws ServiceException{
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)){
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            userDao.delete(Integer.valueOf(id));
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't delete user");
        }
    }
    public void deleteStorageItemById(String id) throws ServiceException{
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)){
            GenericDaoInterface<StorageItem> storageItemDao = jdbcDaoFactory.getDao(StorageItem.class);
            storageItemDao.delete(Integer.valueOf(id));
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't delete storage item");
        }
    }
}

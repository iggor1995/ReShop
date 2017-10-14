package com.epam.igor.electronicsshop.service;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.DaoFactory;
import com.epam.igor.electronicsshop.dao.GenericDaoInterface;
import com.epam.igor.electronicsshop.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.igor.electronicsshop.dao.DaoFactory.JDBC;
import static com.epam.igor.electronicsshop.dao.DaoFactory.getDaoFactory;

/**
 * Set methods for working with shop
 *
 * @author Igor Lapin
 */

public class ShopService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopService.class);
    private static final String ORDER_ID = "order_id";
    private static final String COULDN_T_GET_ORDER = "Couldn't get order";
    private static final String COULDN_T_GET_GENDER_LIST = "Couldn't get gender list";
    private static final String COULDN_T_GET_PRODUCT_TYPES_LIST = "Couldn't get product types list";
    private static final String COULDN_T_GET_PRODUCTS_ON_PAGE = "Couldn't get products on page";
    private static final String COULDN_T_GET_PRODUCTS_COUNT = "Couldn't get products count";
    private static final String COULDN_T_PLACE_ORDER = "Couldn't place order";
    private static final int ID_ONE = 1;
    private static final String COULDN_T_INIT_FACTORY = "Couldn't init factory";
    private static final String COULDN_T_GET_USERS = "Couldn't get users";
    private static final String COULDN_T_GET_USERS_COUNT = "Couldn't get users count";
    private static final String COULDN_T_GET_ORDERS_ON_PAGE = "Couldn't get orders on page";
    private static final String COULDN_T_GET_STORAGE_ITEMS_ON_PAGE = "Couldn't get storage items on page";
    private static final String COULDN_T_GET_ORDERS_COUNT = "Couldn't get orders count";
    private static final String COULDN_T_GET_ITEMS_COUNT = "Couldn't get items count";
    private static final String COULDN_T_GET_ORDER_STATUSES = "Couldn't get order statuses";
    private static final String COULDN_T_UPDATE_ORDER_STATUS = "Couldn't update Order status";
    private static final String COULDN_T_DELETE_ORDER = "Couldn't delete order";
    private static final String COULDN_T_DELETE_PRODUCT = "Couldn't delete product";
    private static final String COULDN_T_DELETE_USER = "Couldn't delete user";
    private static final String COULDN_T_DELETE_STORAGE_ITEM = "Couldn't delete storage item";
    private static final String COULDN_T_UPDATE_STORAGE_ITEM_AMOUNT = "Couldn't update storage item amount";

    /**
     * getting order by id, setting orders user, status, items
     *
     * @param id order id
     * @return found order
     * @throws ServiceException
     */
    public Order getOrder(int id) throws ServiceException {
        Order order;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            GenericDaoInterface<OrderingItem> orderingItemDao = jdbcDaoFactory.getDao(OrderingItem.class);
            GenericDaoInterface<OrderStatus> orderStatusDao = jdbcDaoFactory.getDao(OrderStatus.class);
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            order = orderDao.findByPK(id);
            order.setUser(userDao.findByPK(order.getUser().getId()));
            order.setStatus(orderStatusDao.findByPK(order.getStatus().getId()));
            List<OrderingItem> orderingItems = orderingItemDao.findAllByParams(Collections.singletonMap(ORDER_ID,
                    String.valueOf(order.getId())));
            for (OrderingItem item : orderingItems) {
                item.setProduct(productDao.findByPK(item.getProduct().getId()));
            }
            order.setOrderingItems(orderingItems);
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_ORDER, e);
            throw new ServiceException(e, COULDN_T_GET_ORDER);
        }
        return order;
    }

    /**
     * getting all genders list
     *
     * @return gender List
     * @throws ServiceException
     */
    public List<Gender> getAllGenders() throws ServiceException {
        List<Gender> genders;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Gender> genderDao = jdbcDaoFactory.getDao(Gender.class);
            genders = genderDao.findAll();
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_GENDER_LIST, e);
            throw new ServiceException(e, COULDN_T_GET_GENDER_LIST);
        }
        return genders;
    }

    /**
     * getting all products types
     *
     * @return product types List
     * @throws ServiceException
     */
    public List<ProductType> getAllProductTypes() throws ServiceException {
        List<ProductType> productTypes;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<ProductType> productTypeDao = jdbcDaoFactory.getDao(ProductType.class);
            productTypes = productTypeDao.findAll();
            productTypes = productTypes.stream().filter(productType ->
                    !productType.isDeleted()).collect(Collectors.toList());
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_PRODUCT_TYPES_LIST, e);
            throw new ServiceException(e, COULDN_T_GET_PRODUCT_TYPES_LIST);
        }
        return productTypes;
    }

    /**
     * Page has limited size. Depending on page size and page number get products list
     *
     * @param pageSize
     * @param pageNumber
     * @return products list
     * @throws ServiceException
     */
    public List<Product> getAllProductsOnPage(int pageSize, int pageNumber) throws ServiceException {
        List<Product> products;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            GenericDaoInterface<ProductType> productTypeDao = jdbcDaoFactory.getDao(ProductType.class);
            products = productDao.findAll(pageNumber, pageSize);
            for (Product product : products) {
                product.setType(productTypeDao.findByPK(product.getType().getId()));
            }
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_PRODUCTS_ON_PAGE, e);
            throw new ServiceException(e, COULDN_T_GET_PRODUCTS_ON_PAGE);
        }
        return products;
    }

    /**
     * getting products quantity.
     *
     * @return products quantity
     * @throws ServiceException
     */
    public int getProductsCount() throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            return productDao.getNotDeletedCount();
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_PRODUCTS_COUNT, e);
            throw new ServiceException(e, COULDN_T_GET_PRODUCTS_COUNT);
        }
    }

    /**
     * Buy cart: order status has to be changed, user has to spend money
     * items has to be added in order database
     *
     * @param order
     * @return user which has bought cart
     * @throws ServiceException in case Dao exception throw service exception and do rollback transaction
     */
    public User buyCart(Order order) throws ServiceException {
        User user;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            try {
                jdbcDaoFactory.startTransaction();
                GenericDaoInterface<OrderStatus> orderStatusDao = jdbcDaoFactory.getDao(OrderStatus.class);
                GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
                GenericDaoInterface<OrderingItem> orderingItemDao = jdbcDaoFactory.getDao(OrderingItem.class);
                GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
                user = userDao.findByPK(order.getUser().getId());
                user.spendCash(order.getPrice());
                userDao.update(user);
                order.setStatus(orderStatusDao.findByPK(ID_ONE));
                Order newOrder = orderDao.insert(order);
                for (OrderingItem orderingItem : order.getOrderingItems()) {
                    orderingItem.setOrder(newOrder);
                    orderingItemDao.insert(orderingItem);
                }
                jdbcDaoFactory.commitTransaction();
            } catch (DaoException e) {
                LOG.info(COULDN_T_PLACE_ORDER, e);
                jdbcDaoFactory.rollbackTransaction();
                throw new ServiceException(e, COULDN_T_PLACE_ORDER);
            }
        } catch (DaoException e) {
            LOG.info(COULDN_T_INIT_FACTORY, e);
            throw new ServiceException(e, COULDN_T_INIT_FACTORY);
        }
        return user;
    }

    /**
     * Page has limited size. Based on page number and page size, get users list
     *
     * @param pageNumber
     * @param pageSize
     * @return users list
     * @throws ServiceException
     */
    public List<User> getAllUsersOnPage(int pageNumber, int pageSize) throws ServiceException {
        List<User> users;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            GenericDaoInterface<Gender> genderDao = jdbcDaoFactory.getDao(Gender.class);
            users = userDao.findAll(pageNumber, pageSize);
            for (User user : users) {
                user.setGender(genderDao.findByPK(user.getGender().getId()));
            }
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_USERS, e);
            throw new ServiceException(e, COULDN_T_GET_USERS);
        }
        return users;
    }

    /**
     * get users quantity
     *
     * @return users quantity
     * @throws ServiceException
     */
    public int getUsersCount() throws ServiceException {
        int usersCount;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            usersCount = userDao.getNotDeletedCount();
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_USERS_COUNT, e);
            throw new ServiceException(e, COULDN_T_GET_USERS_COUNT);
        }
        return usersCount;
    }

    /**
     * Page has limited size. Based on page number and page size, get orders list
     * Orders has to be filled with users, and items
     *
     * @param pageSize
     * @param pageNumber
     * @return orders list
     * @throws ServiceException
     */
    public List<Order> getAllOrdersOnPage(int pageSize, int pageNumber) throws ServiceException {
        List<Order> orders;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
            GenericDaoInterface<OrderingItem> orderingItemDao = jdbcDaoFactory.getDao(OrderingItem.class);
            GenericDaoInterface<OrderStatus> orderStatusDao = jdbcDaoFactory.getDao(OrderStatus.class);
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            orders = orderDao.findAll(pageNumber, pageSize);
            for (Order order : orders) {
                order.setUser(userDao.findByPK(order.getUser().getId()));
                order.setStatus(orderStatusDao.findByPK(order.getStatus().getId()));
                List<OrderingItem> orderingItems = orderingItemDao.findAllByParams(Collections.singletonMap(ORDER_ID,
                        String.valueOf(order.getId())));
                for (OrderingItem orderingItem : orderingItems) {
                    orderingItem.setProduct(productDao.findByPK(orderingItem.getProduct().getId()));
                }
                order.setOrderingItems(orderingItems);
            }
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_ORDERS_ON_PAGE, e);
            throw new ServiceException(e, COULDN_T_GET_ORDERS_ON_PAGE);
        }
        return orders;
    }

    /**
     * Page has limited size. Based on page number and page size, get storage items list
     * Orders has to be filled with storage
     *
     * @param pageSize
     * @param pageNumber
     * @return storage items list
     * @throws ServiceException
     */
    public List<StorageItem> getAllStorageItemsOnPage(int pageSize, int pageNumber) throws ServiceException {
        List<StorageItem> storageItems;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<StorageItem> storageItemDao = jdbcDaoFactory.getDao(StorageItem.class);
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            GenericDaoInterface<Storage> storageDao = jdbcDaoFactory.getDao(Storage.class);
            storageItems = storageItemDao.findAll(pageNumber, pageSize);
            for (StorageItem storageItem : storageItems) {
                storageItem.setStorage(storageDao.findByPK(storageItem.getStorage().getId()));
                storageItem.setProduct(productDao.findByPK(storageItem.getProduct().getId()));
            }
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_STORAGE_ITEMS_ON_PAGE, e);
            throw new ServiceException(e, COULDN_T_GET_STORAGE_ITEMS_ON_PAGE);
        }
        return storageItems;
    }

    /**
     * get orders quantity
     *
     * @return orders quantity
     * @throws ServiceException
     */
    public int getOrdersCount() throws ServiceException {
        int ordersCount;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
            ordersCount = orderDao.getNotDeletedCount();
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_ORDERS_COUNT, e);
            throw new ServiceException(e, COULDN_T_GET_ORDERS_COUNT);
        }
        return ordersCount;
    }

    /**
     * get storage items quantity
     *
     * @return storage items quantity
     * @throws ServiceException
     */
    public int getStorageItemsCount() throws ServiceException {
        int storageItemsCount;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<StorageItem> storageItemDao = jdbcDaoFactory.getDao(StorageItem.class);
            storageItemsCount = storageItemDao.getNotDeletedCount();
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_ITEMS_COUNT, e);
            throw new ServiceException(e, COULDN_T_GET_ITEMS_COUNT);
        }
        return storageItemsCount;
    }

    /**
     * get all orders statuses
     * orders has to be not deleted
     *
     * @return order statuses list
     * @throws ServiceException
     */
    public List<OrderStatus> getAllOrderStatuses() throws ServiceException {
        List<OrderStatus> orderStatuses;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<OrderStatus> orderStatusDao = jdbcDaoFactory.getDao(OrderStatus.class);
            orderStatuses = orderStatusDao.findAll();
            orderStatuses = orderStatuses.stream().filter(status -> !status.isDeleted()).collect(Collectors.toList());
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_ORDER_STATUSES, e);
            throw new ServiceException(e, COULDN_T_GET_ORDER_STATUSES);
        }
        return orderStatuses;
    }

    /**
     * after changes order status has to be updated in database
     *
     * @param orderId
     * @param statusId
     * @throws ServiceException
     */
    public void updateOrderStatus(String orderId, String statusId) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
            Order order = orderDao.findByPK(Integer.valueOf(orderId));
            order.getStatus().setId(Integer.valueOf(statusId));
            orderDao.update(order);
        } catch (DaoException e) {
            LOG.info(COULDN_T_UPDATE_ORDER_STATUS, e);
            throw new ServiceException(e, COULDN_T_UPDATE_ORDER_STATUS);
        }
    }

    /**
     * deleting order by order's ID
     *
     * @param id
     * @throws ServiceException
     */
    public void deleteOrderById(String id) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Order> orderDao = jdbcDaoFactory.getDao(Order.class);
            orderDao.delete(Integer.valueOf(id));
        } catch (DaoException e) {
            LOG.info(COULDN_T_DELETE_ORDER, e);
            throw new ServiceException(e, COULDN_T_DELETE_ORDER);
        }
    }

    /**
     * deleting product by product's ID
     *
     * @param id
     * @throws ServiceException
     */
    public void deleteProductById(String id) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            productDao.delete(Integer.valueOf(id));
        } catch (DaoException e) {
            LOG.info(COULDN_T_DELETE_PRODUCT, e);
            throw new ServiceException(e, COULDN_T_DELETE_PRODUCT);
        }
    }

    /**
     * Deleting user by user's ID
     *
     * @param id
     * @throws ServiceException
     */
    public void deleteUserById(String id) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<User> userDao = jdbcDaoFactory.getDao(User.class);
            userDao.delete(Integer.valueOf(id));
        } catch (DaoException e) {
            LOG.info(COULDN_T_DELETE_USER, e);
            throw new ServiceException(e, COULDN_T_DELETE_USER);
        }
    }

    /**
     * Deleting storage item by storage item's ID
     *
     * @param id
     * @throws ServiceException
     */
    public void deleteStorageItemById(String id) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<StorageItem> storageItemDao = jdbcDaoFactory.getDao(StorageItem.class);
            storageItemDao.delete(Integer.valueOf(id));
        } catch (DaoException e) {
            LOG.info(COULDN_T_DELETE_STORAGE_ITEM, e);
            throw new ServiceException(e, COULDN_T_DELETE_STORAGE_ITEM);
        }
    }

    /**
     * After changes storage item has to be updated in database
     *
     * @param itemId
     * @param amount
     * @throws ServiceException
     */
    public void updateStorageItem(String itemId, String amount) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<StorageItem> storageItemDao = jdbcDaoFactory.getDao(StorageItem.class);
            StorageItem item = storageItemDao.findByPK(Integer.valueOf(itemId));
            item.setAmount(Integer.valueOf(amount));
            storageItemDao.update(item);
        } catch (DaoException e) {
            LOG.info(COULDN_T_UPDATE_STORAGE_ITEM_AMOUNT, e);
            throw new ServiceException(e, COULDN_T_UPDATE_STORAGE_ITEM_AMOUNT);
        }
    }
}

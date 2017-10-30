package com.epam.igor.electronicsshop.service;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.DaoFactory;
import com.epam.igor.electronicsshop.dao.GenericDaoInterface;
import com.epam.igor.electronicsshop.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.igor.electronicsshop.dao.DaoFactory.getDaoFactory;

/**
 * Set methods for working with products
 *
 * @author Igor Lapin
 */
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);
    private static final String COULDN_T_FIND_PRODUCT_BY_ID = "Couldn't find product by ID";
    private static final String COULDN_T_ADD_PRODUCT = "Couldn't add product";
    private static final String COULDN_T_INITIALIZE_JDBC_FACTORY = "Couldn't initialize jdbc factory";
    private static final String COULDN_T_GET_FEATURED_PRODUCTS_LIST = "Couldn't get featured products list";
    private static final String TYPE_ID = "type_id";
    private static final String COULDN_T_GET_PRODUCTS_BY_TYPE = "Couldn't get products by type";
    private static final String PRODUCT_ID = "product_id";
    private static final String COULDN_T_GET_PREVIEW_IMAGE = "Couldn't get preview image";
    private static final String COULDN_T_GET_FILLED_PRODUCT = "Couldn't get filled product";
    private static final String COULDN_T_UPDATE_PRODUCT = "Couldn't update product";

    /**
     * Using productDao returns Product found by id
     *
     * @param id - product id
     * @return returns product, found by id
     * @throws ServiceException in case there is DAO Exception
     */
    public Product getProductById(String id) throws ServiceException {
        Product product;
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            product = productDao.findByPK(Integer.parseInt(id));

        } catch (DaoException e) {
            LOG.info(COULDN_T_FIND_PRODUCT_BY_ID, e);
            throw new ServiceException(e, COULDN_T_FIND_PRODUCT_BY_ID);
        }
        return product;
    }

    /**
     * Initialize DaoFactory, call method addProductToDB
     *
     * @param product - inserting product
     * @param image   - inserting image
     * @return return inserted product
     * @throws ServiceException in case Dao exception caught does rollback transaction
     */
    public Product addProduct(Product product, Image image) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
           addProductToDB(product, image, jdbcDaoFactory);
        } catch (DaoException e) {
            LOG.info(COULDN_T_INITIALIZE_JDBC_FACTORY, e);
            throw new ServiceException(e, COULDN_T_INITIALIZE_JDBC_FACTORY);
        }
        return product;
    }

    /**
     * Method adds product to DB
     * @param product
     * @param image
     * @param jdbcDaoFactory
     * @return product
     */
    private Product addProductToDB(Product product, Image image, DaoFactory jdbcDaoFactory) throws ServiceException, DaoException {
        try {
            jdbcDaoFactory.startTransaction();
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            GenericDaoInterface<Image> imageDao = jdbcDaoFactory.getDao(Image.class);
            Product addedProduct = productDao.insert(product);
            image.setProduct(addedProduct);
            imageDao.insert(image);
            jdbcDaoFactory.commitTransaction();
        } catch (DaoException e) {
            LOG.info(COULDN_T_ADD_PRODUCT, e);
            jdbcDaoFactory.rollbackTransaction();
            throw new ServiceException(e, COULDN_T_ADD_PRODUCT);
        }
        return product;
    }

    /**
     * after adding product to database, product has to be set in storage
     *
     * @param product - inserting product
     */
    public void addProductToStorage(Product product) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            GenericDaoInterface<StorageItem> storageDao = jdbcDaoFactory.getDao(StorageItem.class);
            StorageItem storageItem = new StorageItem();
            storageItem.setProduct(product);
            storageItem.setStorage(new Storage(1));
            storageItem.setAmount(0);
            storageDao.insert(storageItem);
        } catch (DaoException e) {
            LOG.info(COULDN_T_ADD_PRODUCT, e);
            throw new ServiceException(e, COULDN_T_ADD_PRODUCT);
        }
    }

    public List<Product> getFeaturedProducts() throws ServiceException {
        List<Product> featuredProducts;
        try (DaoFactory jdbcDaoFactory = getDaoFactory();) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            List<Product> products = productDao.findAll();
            featuredProducts = products.subList(products.size() - 9, products.size());
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_FEATURED_PRODUCTS_LIST, e);
            throw new ServiceException(e, COULDN_T_GET_FEATURED_PRODUCTS_LIST);
        }
        return featuredProducts;
    }

    /**
     * getting all products by type
     *
     * @param typeId Type ID
     * @return products List
     */
    public List<Product> getAllProductsByType(String typeId) throws ServiceException {
        List<Product> productsList;
        try (DaoFactory jdbcDaoFactory = getDaoFactory();) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            productsList = productDao.findAllByParams(Collections.singletonMap(TYPE_ID, typeId));
            productsList = productsList.stream().filter(product -> !product.isDeleted()).collect(Collectors.toList());
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_PRODUCTS_BY_TYPE, e);
            throw new ServiceException(e, COULDN_T_GET_PRODUCTS_BY_TYPE);
        }
        return productsList;
    }

    /**
     * gettting image by product ID
     *
     * @param id product id
     * @return first found image
     */
    public Image getProductPreviewImage(String id) throws ServiceException {
        List<Image> images;
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            GenericDaoInterface<Image> imageDao = jdbcDaoFactory.getDao(Image.class);
            images = imageDao.findAllByParams(Collections.singletonMap(PRODUCT_ID, id));
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_PREVIEW_IMAGE, e);
            throw new ServiceException(e, COULDN_T_GET_PREVIEW_IMAGE);
        }
        return images.get(0);
    }

    /**
     * getting product object filled with image and type
     *
     * @param id product id
     * @return filled product
     * @throws ServiceException in case Dao exception throw service exception and do rollback transaction
     */
    public Product getFilledProduct(String id) throws ServiceException {
        Product product = null;
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            product = fillProduct(id, jdbcDaoFactory);
        } catch (DaoException e) {
            LOG.info(COULDN_T_INITIALIZE_JDBC_FACTORY, e);
            throw new ServiceException(e, COULDN_T_INITIALIZE_JDBC_FACTORY);
        }
        return product;
    }

    /** Method fills product with params
     * @param id
     * @param jdbcDaoFactory
     * @return product
     */
    private Product fillProduct(String id, DaoFactory jdbcDaoFactory) throws ServiceException, DaoException {
        Product product;
        try {
            jdbcDaoFactory.startTransaction();
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            GenericDaoInterface<Image> imageDao = jdbcDaoFactory.getDao(Image.class);
            GenericDaoInterface<ProductType> productTypeDao = jdbcDaoFactory.getDao(ProductType.class);
            product = productDao.findByPK(Integer.parseInt(id));
            ProductType productType = productTypeDao.findByPK(product.getType().getId());
            product.setType(productType);
            Map<String, String> productParam = Collections.singletonMap(PRODUCT_ID, id);
            List<Image> images = imageDao.findAllByParams(productParam);
            product.setImages(images);
            jdbcDaoFactory.commitTransaction();
        } catch (DaoException e) {
            LOG.info(COULDN_T_GET_FILLED_PRODUCT, e);
            jdbcDaoFactory.rollbackTransaction();
            throw new ServiceException(e, COULDN_T_GET_FILLED_PRODUCT);
        }
        return product;
    }

    /**
     * if product has been changed, it has to be changed in database
     *
     * @param product
     */
    public void updateProduct(Product product) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            productDao.update(product);
        } catch (DaoException e) {
            LOG.info(COULDN_T_UPDATE_PRODUCT, e);
            throw new ServiceException(e, COULDN_T_UPDATE_PRODUCT);
        }
    }

    /**
     * if product image has been changed, it has to be changed in database
     *
     * @param image
     * @throws ServiceException
     */
    public void updateProductImage(Image image) throws ServiceException {
        try (DaoFactory jdbcDaoFactory = getDaoFactory()) {
            GenericDaoInterface<Image> imageDao = jdbcDaoFactory.getDao(Image.class);
            imageDao.update(image);
        } catch (DaoException e) {
            LOG.info(COULDN_T_UPDATE_PRODUCT, e);
            throw new ServiceException(e, COULDN_T_UPDATE_PRODUCT);
        }
    }
}

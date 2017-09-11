package com.epam.igor.electronicsshop.service;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.DaoFactory;
import com.epam.igor.electronicsshop.dao.GenericDaoInterface;
import com.epam.igor.electronicsshop.dao.entity.JDBCDaoFactory;
import com.epam.igor.electronicsshop.entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.igor.electronicsshop.action.SelectLocaleAction.LOG;
import static com.epam.igor.electronicsshop.dao.DaoFactory.JDBC;
import static com.epam.igor.electronicsshop.dao.DaoFactory.getDaoFactory;

/**
 * Created by User on 10.08.2017.
 */
public class ProductService {

    private static final String COULDN_T_FIND_PRODUCT_BY_ID = "Couldn't find product by ID";
    private static final String COULDN_T_ADD_PRODUCT = "Couldn't add product";
    private static final String COULDN_T_INITIALIZE_JDBC_FACTORY = "Couldn't initialize jdbc factory";
    private static final String COULDN_T_GET_FEATURED_PRODUCTS_LIST = "Couldn't get featured products list";
    private static final String TYPE_ID = "type_id";
    private static final String COULDN_T_GET_PRODUCTS_BY_TYPE = "Couldn't get products by type";
    private static final String PRODUCT_ID = "product_id";
    private static final String COULDN_T_GET_PREVIEW_IMAGE = "Couldn't get preview image";
    private static final String COULDN_T_GET_FILLED_PRODUCT = "Couldn't get filled product";

    public ProductService() {
    }
    public Product getProductById(String id) throws ServiceException {
        Product product;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            product = productDao.findByPK(Integer.parseInt(id));

        } catch (DaoException e) {
            throw new ServiceException(e, COULDN_T_FIND_PRODUCT_BY_ID);
        }
        return product;
    }
    public Product addProduct(Product product, Image image) throws ServiceException {
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            try {
                jdbcDaoFactory.startTransaction();
                GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
                GenericDaoInterface<Image> imageDao = jdbcDaoFactory.getDao(Image.class);
                Product addedProduct = productDao.insert(product);
                image.setProduct(addedProduct);
                imageDao.insert(image);
                jdbcDaoFactory.commitTransaction();
            }
            catch (DaoException e){
                jdbcDaoFactory.rollbackTransaction();
                throw  new ServiceException(e, COULDN_T_ADD_PRODUCT);
            }
        } catch (DaoException e) {
            throw new ServiceException(e, COULDN_T_INITIALIZE_JDBC_FACTORY);
        }
        return product;
    }
    public void addProductToStorage(Product product) throws ServiceException{
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<StorageItem> storageDao = jdbcDaoFactory.getDao(StorageItem.class);
            StorageItem storageItem = new StorageItem();
            storageItem.setProduct(product);
            storageItem.setStorage(new Storage(1));
            storageItem.setAmount(0);
            storageDao.insert(storageItem);
        } catch (DaoException e) {
            throw new ServiceException(e, COULDN_T_INITIALIZE_JDBC_FACTORY);
        }
    }
    public List<Product> getFeaturedProducts() throws ServiceException{
        List<Product> featuredProducts = new ArrayList<>();
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC);) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            List<Product> products = productDao.findAll();
            featuredProducts = products.subList(products.size() - 9, products.size());
        } catch (DaoException e) {
            throw new ServiceException(e, COULDN_T_GET_FEATURED_PRODUCTS_LIST);
        }
        return featuredProducts;
    }
    public List<Product> getAllProductsByType(String typeid) throws ServiceException {
        List<Product> productsList;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC);) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            productsList = productDao.findAllByParams(Collections.singletonMap(TYPE_ID, typeid));
            productsList = productsList.stream().filter(product -> !product.isDeleted()).collect(Collectors.toList());
        } catch (DaoException e) {
            throw  new ServiceException(e, COULDN_T_GET_PRODUCTS_BY_TYPE);
        }
        return productsList;
    }
    public Image getProductPreviewImage(String id) throws ServiceException{
        List<Image> images;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Image> imageDao = jdbcDaoFactory.getDao(Image.class);
            images = imageDao.findAllByParams(Collections.singletonMap(PRODUCT_ID, id));
        } catch (DaoException e) {
            throw new ServiceException(e, COULDN_T_GET_PREVIEW_IMAGE);
        }
        return images.get(0);
    }
    public Product getFilledProduct(String id) throws ServiceException{
        Product product;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
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
            }catch (DaoException e){
                jdbcDaoFactory.rollbackTransaction();
                throw new ServiceException(e, COULDN_T_GET_FILLED_PRODUCT);
            }

        } catch (DaoException e) {
            throw new ServiceException(e, COULDN_T_INITIALIZE_JDBC_FACTORY);
        }
        return product;
    }
}

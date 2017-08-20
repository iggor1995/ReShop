package com.epam.igor.electronicsshop.service;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.DaoFactory;
import com.epam.igor.electronicsshop.dao.GenericDaoInterface;
import com.epam.igor.electronicsshop.dao.entity.JDBCDaoFactory;
import com.epam.igor.electronicsshop.entity.Image;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.entity.Storage;
import com.epam.igor.electronicsshop.entity.StorageItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.igor.electronicsshop.dao.DaoFactory.JDBC;
import static com.epam.igor.electronicsshop.dao.DaoFactory.getDaoFactory;

/**
 * Created by User on 10.08.2017.
 */
public class ProductService {
    public ProductService() {
    }
    public Product getProductById(String id) throws ServiceException {
        Product product;
        try (DaoFactory jdbcDaoFactory = getDaoFactory(JDBC)) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            product = productDao.findByPK(Integer.parseInt(id));

        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't find product by ID");
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
                throw  new ServiceException(e, "Couldn't add product");
            }
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't initialize jdbc factory");
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
            throw new ServiceException(e, "Couldn't initialize jdbc factory");
        }
    }
    public List<Product> getFeaturedProducts() throws ServiceException{
        List<Product> featuredProducts = new ArrayList<>();
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC);) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            List<Product> products = productDao.findAll();
            featuredProducts = products.subList(products.size() - 9, products.size());
        } catch (DaoException e) {
            throw new ServiceException(e, "Couldn't get featured products list");
        }
        return featuredProducts;
    }
    public List<Product> getAllProductsByType(String typeid) throws ServiceException {
        List<Product> productsList;
        try(DaoFactory jdbcDaoFactory = getDaoFactory(JDBC);) {
            GenericDaoInterface<Product> productDao = jdbcDaoFactory.getDao(Product.class);
            productsList = productDao.findAllByParams(Collections.singletonMap("product_type_id", typeid));
            productsList = productsList.stream().filter(product -> !product.isDeleted()).collect(Collectors.toList());
        } catch (DaoException e) {
            throw  new ServiceException(e, "Couldn't get products by type");
        }
        return productsList;
    }
}

package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.constants.ProductConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.Image;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.entity.ProductType;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * For adding new product to database
 *
 * @author Igor Lapin
 */

public class AddProductAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(AddProductAction.class);
    private static final String COULDN_T_ADD_PRODUCT = "Couldn't add product";
    private static final String TRUE = "true";
    private static final String PRODUCT = "product";
    private static final String INVALID_CONTENT_TYPE = "Invalid content type - {}";
    private boolean invalid;
    private static final String MONEY = "money";
    private static final String MONEY_REGEX = "money.regex";
    private static final String ERROR_MONEY = "moneyError";
    private static final String ERROR_IMAGE = "imageError";
    private static final String ERROR_PROPERTIES = "Couldn't get validation.properties";
    private static final String ADD_PRODUCT_PAGE = "product-add";
    private static final String MANAGE_PRODUCTS_PAGE = "manage/products";
    private static final String VALIDATION_PROPERTIES = "validation.properties";
    private static final String ADDED_PRODUCT = "{} inserted in db and added on central storage by {}";
    private Properties properties = new Properties();
    private Validation validation = new Validation();
    private String name;
    private String type;
    private String descriptionRU;
    private String descriptionEN;
    private String price;

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            properties.load(AddProductAction.class.getClassLoader().getResourceAsStream(VALIDATION_PROPERTIES));
        } catch (IOException e) {
            LOG.info(ERROR_PROPERTIES, e);
            throw new ActionException(ERROR_PROPERTIES, e);
        }
        name = req.getParameter(ProductConstants.NAME);
        type = req.getParameter(ProductConstants.TYPE_ID);
        descriptionEN = req.getParameter(ProductConstants.DESCRIPTION_EN);
        descriptionRU = req.getParameter(ProductConstants.DESCRIPTION_RU);
        price = req.getParameter(ProductConstants.PRICE);

        try {
            Product product = filledProduct();
            invalid = validation.checkParameterByRegex(invalid, price, MONEY, properties.getProperty(MONEY_REGEX), req);
            if (invalid) {
                invalid = false;
                req.setAttribute(ERROR_MONEY, TRUE);
                req.setAttribute(PRODUCT, product);
                return new ActionResult(ADD_PRODUCT_PAGE);
            }
            product.setPrice(Money.parse(ProductConstants.KZT + price));
            if (fillImage(req, product)) {
                return new ActionResult(ADD_PRODUCT_PAGE);
            }
        } catch (ServiceException | IOException | ServletException e) {
            LOG.info(COULDN_T_ADD_PRODUCT, e);
            throw new ActionException(COULDN_T_ADD_PRODUCT, e);
        }
        return new ActionResult(MANAGE_PRODUCTS_PAGE, true);
    }

    private Product filledProduct() {
        Product filledProduct = new Product();
        filledProduct.setName(name);
        filledProduct.setType(new ProductType(Integer.valueOf(type)));
        filledProduct.setEnDescription(descriptionEN);
        filledProduct.setRuDescription(descriptionRU);
        return filledProduct;
    }

    private boolean fillImage(HttpServletRequest req, Product product) throws IOException, ServletException, ServiceException {
        ProductService productService = new ProductService();
        Part imagePart = req.getPart(ProductConstants.IMAGE);
        if (!imagePart.getContentType().startsWith(ProductConstants.IMAGE)) {
            req.setAttribute(ERROR_IMAGE, TRUE);
            req.setAttribute(PRODUCT, product);
            LOG.error(INVALID_CONTENT_TYPE, imagePart.getContentType());
            return true;
        }

        Image image = new Image();
        image.setName(name.replaceAll("\\s", "").toLowerCase());
        image.setModifiedTime(DateTime.now());
        image.setContentType(imagePart.getContentType());
        image.setImageStream(imagePart.getInputStream());
        Product newProduct = productService.addProduct(product, image);
        productService.addProductToStorage(newProduct);
        LOG.info(ADDED_PRODUCT, newProduct, req.getSession(false).getAttribute(UserConstants.LOGGED_USER));
        return false;
    }


}

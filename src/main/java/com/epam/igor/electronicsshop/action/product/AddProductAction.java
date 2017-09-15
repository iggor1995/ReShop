package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
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
 * Created by User on 27.08.2017.
 */

public class AddProductAction implements Action {
    public static final String FLASH = "flash.";
    public static final String ERROR = "Error";
    private static Logger LOG = LoggerFactory.getLogger(AddProductAction.class);
    private static final String CHECK_PARAMETR = "Check parameter '{}' with value '{}' by regex '{}'";
    private boolean INVALID;
    private static final String WRONG_PARAMETR = "Parameter '{}' with value '{}' is unsuitable.";
    private static final String MONEY = "money";
    private static final String MONEY_REGEX = "money.regex";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_TYPE_ID = "typeId";
    private static final String PARAMETER_DESCRIPTION_RU = "descriptionRU";
    private static final String PARAMETER_DESCRIPTION_EN = "descriptionEN";
    private static final String PARAMETER_PRICE = "price";
    private static final String ERROR_MONEY = "moneyError";
    private static final String ERROR_IMAGE = "imageError";
    private static final String ERROR_PROPERTIES = "Couldn't get validation.properties";
    private static final String ADD_PRODUCT_PAGE = "add-product";
    private static final String MANAGE_PRODUCTS_PAGE = "manage/products";
    private static final String CURRENCY_KZT = "KZT";
    private static final String IMAGE_PART = "image";
    private static final String PROPERTIES = "validation.properties";
    private static final String LOGGED_USER = "loggedUser";
    private static final String ADDED_PRODUCT = "{} inserted in db and added on central storage by {}";
    private Properties properties = new Properties();
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            properties.load(AddProductAction.class.getClassLoader().getResourceAsStream(PROPERTIES));
        } catch (IOException e) {
            throw new ActionException(ERROR_PROPERTIES , e);
        }

        String name = req.getParameter(PARAMETER_NAME);
        String type = req.getParameter(PARAMETER_TYPE_ID);
        String descriptionEN = req.getParameter(PARAMETER_DESCRIPTION_EN);
        String descriptionRU = req.getParameter(PARAMETER_DESCRIPTION_RU);
        String price = req.getParameter(PARAMETER_PRICE);

        ProductService productService = new ProductService();
        try{
            Product product = new Product();
            product.setName(name);
            product.setType(new ProductType(Integer.valueOf(type)));
            product.setEnDescription(descriptionEN);
            product.setRuDescription(descriptionRU);
            checkParameterByRegex(price, MONEY, properties.getProperty(MONEY_REGEX), req);
            if(INVALID){
                INVALID = false;
                req.setAttribute(ERROR_MONEY, "true");
                req.setAttribute("product", product);
                return new ActionResult(ADD_PRODUCT_PAGE);
            }
            product.setPrice(Money.parse(CURRENCY_KZT + price));
            Part imagePart = req.getPart(IMAGE_PART);
            if(!imagePart.getContentType().startsWith(IMAGE_PART)){
                req.setAttribute(ERROR_IMAGE, "true");
                req.setAttribute("product", product);
                LOG.error("Invalid content type - {}", imagePart.getContentType());
                return new ActionResult(ADD_PRODUCT_PAGE);
            }

            Image image = new Image();
            image.setName(name.replaceAll("\\s", "").toLowerCase());
            image.setModifiedTime(DateTime.now());
            image.setContentType(imagePart.getContentType());
            image.setImageStream(imagePart.getInputStream());
            Product newProduct = productService.addProduct(product, image);
            productService.addProductToStorage(newProduct);
            LOG.info(ADDED_PRODUCT, newProduct, req.getSession(false).getAttribute(LOGGED_USER));
        } catch (ServiceException | IOException | ServletException e) {
            throw new ActionException("Couldn't add product", e);
        }
        return new ActionResult(MANAGE_PRODUCTS_PAGE, true);
    }
    private void checkParameterByRegex(String parameter, String parameterName, String regex, HttpServletRequest req) {
        LOG.debug(CHECK_PARAMETR, parameterName, parameter, regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        if (!matcher.matches()) {
            LOG.debug(WRONG_PARAMETR, parameterName, parameter);
            req.setAttribute(FLASH + parameterName + ERROR, "true");
            INVALID = true;
        }
    }
}

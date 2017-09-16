package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.user.RegisterAction;
import com.epam.igor.electronicsshop.entity.Image;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import org.joda.money.Money;
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
 *  For saving product changes to database.
 * @author Igor Lapin
 *  */
public class EditProductAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(EditProductAction.class);
    private static final String CHECK_PARAMETR = "Check parameter '{}' with value '{}' by regex '{}'";
    private static final String WRONG_PARAMETR = "Parameter '{}' with value '{}' is unsuitable.";
    private static final String MONEY_REGEX = "money.regex";
    private static final String PRICE = "price";
    private static final String REFERER = "referer";
    private static final String INVALID_MONEY_FORMAT = "Invalid money format - {}";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String TYPE_ID = "typeId";
    private static final String DESCRIPTION_EN = "descriptionEN";
    private static final String DESCRIPTION_RU = "descriptionRU";
    private static final String KZT = "KZT";
    private static final String IMAGE = "image";
    private static final String IMAGE_ERROR = "imageError";
    private static final String INVALID_CONTENT_TYPE = "Invalid content type - {}";
    private static final String UPDATED_BY = "{} updated by {}";
    private static final String LOGGED_USER = "loggedUser";
    private static final String MANAGE_PRODUTS = "manage/products";
    private static final String FLASH = "flash.";
    public static final String ERROR = "Error";
    private static final String COULDN_T_EDIT_PRODUCT = "Couldn't edit product";
    private static final String TRUE = "true";
    private boolean invalid;
    private static final String PROPERTIES_ERROR= "Cannot load properties";
    private static final String MONEY= "money";
    private static final String VALIDATION_PROPERTIES= "validation.properties";
    private Properties properties = new Properties();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        String price = req.getParameter(PRICE);
        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream(VALIDATION_PROPERTIES));
        } catch (IOException e) {
            throw new ActionException(PROPERTIES_ERROR, e);
        }
        checkParameterByRegex(price, MONEY, properties.getProperty(MONEY_REGEX), req);
        if(invalid){
            invalid = false;
            LOG.info(INVALID_MONEY_FORMAT, price);
            return new ActionResult(req.getHeader(REFERER), true);
        }
        try {
            String id = req.getParameter(ID);
            String name = req.getParameter(NAME);
            String typeId = req.getParameter(TYPE_ID);
            String descriptionRu = req.getParameter(DESCRIPTION_RU);
            String descriptionEn = req.getParameter(DESCRIPTION_EN);
            ProductService productService = new ProductService();
            Product product = productService.getProductById(id);
            product.setName(name);
            product.getType().setId(Integer.valueOf(typeId));
            product.setPrice(Money.parse(KZT + price));
            product.setRuDescription(descriptionRu);
            product.setEnDescription(descriptionEn);
            productService.updateProduct(product);
            Part imagePart = req.getPart(IMAGE);
            if(imagePart.getSize() != 0){
                if(!imagePart.getContentType().startsWith(IMAGE)){
                    req.setAttribute(IMAGE_ERROR, true);
                    LOG.info(INVALID_CONTENT_TYPE, imagePart.getContentType());
                    return new ActionResult(req.getHeader(REFERER), true);
                } else{
                    Image image = productService.getProductPreviewImage(id);
                    image.setName(name.replaceAll("\\s", "").toLowerCase());
                    image.setContentType(imagePart.getContentType());
                    image.setImageStream(imagePart.getInputStream());
                    productService.updateProductImage(image);
                }
                LOG.info(UPDATED_BY, product, req.getSession(false).getAttribute(LOGGED_USER));
            }
        } catch (IOException | ServletException | ServiceException e) {
            throw new ActionException(COULDN_T_EDIT_PRODUCT, e);
        }
        return new ActionResult(MANAGE_PRODUTS, true);
    }

    private void checkParameterByRegex(String parameter, String parameterName, String regex, HttpServletRequest req) {
        LOG.debug(CHECK_PARAMETR, parameterName, parameter, regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        if (!matcher.matches()) {
            LOG.debug(WRONG_PARAMETR, parameterName, parameter);
            req.setAttribute(FLASH + parameterName + ERROR, TRUE);
            invalid = true;
        }
    }
}

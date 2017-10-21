package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.action.user.RegisterAction;
import com.epam.igor.electronicsshop.constants.ErrorConstants;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.ProductConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
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

/**
 * For saving product changes to database.
 *
 * @author Igor Lapin
 */
public class EditProductAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(EditProductAction.class);
    private static final String MONEY_REGEX = "money.regex";
    private static final String INVALID_MONEY_FORMAT = "Invalid money format - {}";
    private static final String INVALID_CONTENT_TYPE = "Invalid content type - {}";
    private static final String UPDATED_BY = "{} updated by {}";
    private static final String COULDN_T_EDIT_PRODUCT = "Couldn't edit product";
    private static final String PROPERTIES_ERROR = "Cannot load properties";
    private static final String MONEY = "money";
    private static final String VALIDATION_PROPERTIES = "validation.properties";
    private Properties properties = new Properties();
    private boolean invalid;
    private String price;
    private String id;
    private String name;
    private Product product;

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {

        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream(VALIDATION_PROPERTIES));
        } catch (IOException e) {
            LOG.info(PROPERTIES_ERROR, e);
            throw new ActionException(PROPERTIES_ERROR, e);
        }
        if (checkPrice(req)) {
            invalid = false;
            return new ActionResult(req.getHeader(PageConstants.REFERER_PAGE), true);
        }
        try {
            updateProductData(req);
        } catch (ServiceException e) {
            LOG.info(COULDN_T_EDIT_PRODUCT, e);
            throw new ActionException(COULDN_T_EDIT_PRODUCT, e);
        }
        if (updateImageData(req)) {
            return new ActionResult(req.getHeader(PageConstants.REFERER_PAGE), true);
        }
        return new ActionResult(PageConstants.MANAGE_PRODUCTS_REDIRECT, true);

    }

    private boolean checkPrice(HttpServletRequest req) {
        Validation validation = new Validation();
        price = req.getParameter(ProductConstants.PRICE);
        invalid = validation.checkParameterByRegex(invalid, price, MONEY, properties.getProperty(MONEY_REGEX), req);
        if (invalid) {
            LOG.info(INVALID_MONEY_FORMAT, price);
        }
        return invalid;
    }

    private void updateProductData(HttpServletRequest req) throws ServiceException {
        id = req.getParameter(ProductConstants.ID);
        name = req.getParameter(ProductConstants.NAME);
        String typeId = req.getParameter(ProductConstants.TYPE_ID);
        String descriptionRu = req.getParameter(ProductConstants.DESCRIPTION_RU);
        String descriptionEn = req.getParameter(ProductConstants.DESCRIPTION_EN);
        ProductService productService = new ProductService();
        product = productService.getProductById(id);
        product.setName(name);
        product.getType().setId(Integer.valueOf(typeId));
        product.setPrice(Money.parse(ProductConstants.KZT + price));
        product.setRuDescription(descriptionRu);
        product.setEnDescription(descriptionEn);
        productService.updateProduct(product);
    }

    private boolean updateImageData(HttpServletRequest req) throws ActionException {
        try {
            ProductService productService = new ProductService();
            Part imagePart = req.getPart(ProductConstants.IMAGE);
            if (imagePart.getSize() != 0) {
                if (!imagePart.getContentType().startsWith(ProductConstants.IMAGE)) {
                    req.setAttribute(ErrorConstants.IMAGE_ERROR, true);
                    LOG.info(INVALID_CONTENT_TYPE, imagePart.getContentType());
                    return true;                                                // go to referer page
                } else {
                    Image image = productService.getProductPreviewImage(id);
                    image.setName(name.replaceAll("\\s", "").toLowerCase());
                    image.setContentType(imagePart.getContentType());
                    image.setImageStream(imagePart.getInputStream());
                    productService.updateProductImage(image);
                }
                LOG.info(UPDATED_BY, product, req.getSession(false).getAttribute(UserConstants.LOGGED_USER));
            }
        } catch (IOException | ServletException | ServiceException e) {
            LOG.info(COULDN_T_EDIT_PRODUCT, e);
            throw new ActionException(COULDN_T_EDIT_PRODUCT, e);
        }
        return false;
    }
}

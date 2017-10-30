package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.constants.ErrorConstants;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.ProductConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.Image;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.entity.ProductType;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.util.ProductUtil;
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

/**
 * For adding new product to database
 *
 * @author Igor Lapin
 */

public class AddProductAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(AddProductAction.class);
    private static final String COULDN_T_ADD_PRODUCT = "Couldn't add product";
    private static final String ADDED_PRODUCT = "{} inserted in db and added on central storage by {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            ProductUtil productUtil = new ProductUtil();
            ProductService productService = new ProductService();
            if(productUtil.validateMoneyAndImage(req)) {
                return new ActionResult(PageConstants.ADD_PRODUCT);
            }
            Product product = productUtil.getFilledProduct(req);
            Image image = new Image();
            Product newProduct = productService.addProduct(product, productUtil.getFilledImage(image));
            productService.addProductToStorage(newProduct);
            LOG.info(ADDED_PRODUCT, newProduct, req.getSession(false).getAttribute(UserConstants.LOGGED_USER));
        } catch (ServiceException | IOException | ServletException e) {
            LOG.info(COULDN_T_ADD_PRODUCT, e);
            throw new ActionException(COULDN_T_ADD_PRODUCT, e);
        }
        return new ActionResult(PageConstants.MANAGE_PRODUCTS_REDIRECT, true);
    }

}

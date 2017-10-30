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
import com.epam.igor.electronicsshop.util.ProductUtil;
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
    private static final String INVALID_MONEY_FORMAT = "Invalid money format - {}";
    private static final String COULDN_T_EDIT_PRODUCT = "Couldn't edit product";
    private  ProductUtil productUtil = new ProductUtil();
    private String id;

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {

        try {
            ProductService productService = new ProductService();
            if (checkPrice(req) || productUtil.checkImagePart(req)) {
                return new ActionResult(req.getHeader(PageConstants.REFERER_PAGE), true);
            }
            productService.updateProduct(getFilledProduct(req));
            Image image = productService.getProductPreviewImage(id);
            image = productUtil.getFilledImage(image);
            productService.updateProductImage(image);
        } catch (IOException | ServletException | ServiceException e) {
            LOG.info(COULDN_T_EDIT_PRODUCT, e);
            throw new ActionException(COULDN_T_EDIT_PRODUCT, e);
        }
        return new ActionResult(PageConstants.MANAGE_PRODUCTS_REDIRECT, true);

    }

    private Product getFilledProduct(HttpServletRequest req){
        id = req.getParameter(ProductConstants.ID);
        Product product = productUtil.getFilledProduct(req);
        product.setId(Integer.valueOf(id));
        return product;
    }
    private boolean checkPrice(HttpServletRequest req) throws ActionException {
        Validation validation = new Validation();
        String price = req.getParameter(ProductConstants.PRICE);
        if (validation.checkMoney(req, price)) {
            LOG.info(INVALID_MONEY_FORMAT, price);
            return true;
        }
        return false;
    }
}

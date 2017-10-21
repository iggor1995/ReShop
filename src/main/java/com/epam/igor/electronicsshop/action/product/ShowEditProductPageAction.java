package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.ProductConstants;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.entity.ProductType;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Class sets necessary attributes for displaying edit product page.
 *
 * @author Igor Lapin
 */
public class ShowEditProductPageAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(ShowEditProductPageAction.class);
    private static final String COULDN_T_SHOW_EDIT_PRODUCT_PAGE = "Couldn't show edit product page";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        ShopService shopService = new ShopService();
        ProductService productService = new ProductService();

        try {
            Product product = productService.getFilledProduct(req.getParameter(ProductConstants.ID));
            req.setAttribute(ProductConstants.PRODUCT, product);
            List<ProductType> productTypes = shopService.getAllProductTypes();
            req.setAttribute(ProductConstants.TYPES, productTypes);
            return new ActionResult(PageConstants.EDIT_PRODUCT);
        } catch (ServiceException e) {
            LOG.info(COULDN_T_SHOW_EDIT_PRODUCT_PAGE, e);
            throw new ActionException(COULDN_T_SHOW_EDIT_PRODUCT_PAGE, e);
        }
    }
}

package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.entity.ProductType;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *  Class sets necessary attributes for displaying add product page.
 * @author Igor Lapin
 *  */
public class ShowAddProductPageAction implements Action {

    private static final String TYPES = "types";
    private static final String ERROR = "Couldn't show product-add page";
    private static final String PRODUCT_ADD_PAGE = "product-add";
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {

        List<ProductType> productTypes;
        ShopService shopService = new ShopService();
        try {
            productTypes = shopService.getAllProductTypes();
        } catch (ServiceException e) {
            throw new ActionException(ERROR, e);
        }
        req.getSession(false).setAttribute(TYPES, productTypes);
        return new ActionResult(PRODUCT_ADD_PAGE);
    }
}

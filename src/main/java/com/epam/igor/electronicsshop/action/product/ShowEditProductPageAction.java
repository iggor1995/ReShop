package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
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
 * Created by User on 11.09.2017.
 */
public class ShowEditProductPageAction implements Action {

    private static final String ID = "id";
    private static final String PRODUCT = "product";
    private static final String TYPES = "types";
    private static final String EDIT_PRODUCT = "edit-product";
    private static final String COULDN_T_SHOW_EDIT_PRODUCT_PAGE = "Couldn't show edit product page";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        ShopService shopService = new ShopService();
        ProductService productService = new ProductService();

        try {
            Product product = productService.getFilledProduct(req.getParameter(ID));
            req.setAttribute(PRODUCT, product);
            List<ProductType> productTypes = shopService.getAllProductTypes();
            req.setAttribute(TYPES, productTypes);
            return new ActionResult(EDIT_PRODUCT);
        } catch (ServiceException e) {
            throw new ActionException(COULDN_T_SHOW_EDIT_PRODUCT_PAGE, e);
        }
    }
}

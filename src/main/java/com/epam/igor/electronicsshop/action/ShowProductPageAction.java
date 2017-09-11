package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 03.09.2017.
 */
public class ShowProductPageAction implements Action {
    private static final String ID = "id";
    private static final String PRODUCT = "product";
    private static final String PRODUCT_PAGE = "product-page";
    private static final String ERROR = "Could't show product page action";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Product product;
        try {
            ProductService productService = new ProductService();
            product = productService.getFilledProduct(req.getParameter(ID));
        } catch (ServiceException e) {
            throw new ActionException(ERROR);
        }
        req.setAttribute(PRODUCT, product);
        return new ActionResult(PRODUCT_PAGE);
    }
}

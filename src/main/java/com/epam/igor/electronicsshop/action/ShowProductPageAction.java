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

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Product product;
        try {
            ProductService productService = new ProductService();
            product = productService.getFilledProduct(req.getParameter("id"));
        } catch (ServiceException e) {
            throw new ActionException("Could't show product page action");
        }
        req.setAttribute("product", product);
        return new ActionResult("product-page");
    }
}

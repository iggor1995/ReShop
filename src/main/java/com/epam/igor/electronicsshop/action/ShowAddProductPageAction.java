package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.ProductType;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by User on 27.08.2017.
 */
public class ShowAddProductPageAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        List<ProductType> productTypes;
        ShopService shopService = new ShopService();
        try {
            productTypes = shopService.getAllProductTypes();
        } catch (ServiceException e) {
            throw new ActionException("Couldn't show product-add page", e);
        }
        req.getSession(false).setAttribute("types", productTypes);
        return new ActionResult("product-add");
    }
}

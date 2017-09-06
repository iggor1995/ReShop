package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.entity.ProductType;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class ShowHomePageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowHomePageAction.class);
    public final String FIRST_PAGE = "1";
    public final String DEFAULT_PAGE_SIZE = "4";
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        List<Product> products;
        List<ProductType> productTypes;
        String page = req.getParameter("page");
        if (page == null) {
            page = FIRST_PAGE;
        }
        String pageSize = req.getParameter("pageSize");
        if (req.getParameter("pageSize") == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        int productsCount;
        try {
            ShopService shopService = new ShopService();
            products = shopService.getAllProductsOnPage(Integer.parseInt(page), Integer.parseInt(pageSize));
            productsCount = shopService.getProductsCount();
            productTypes = shopService.getAllProductTypes();
        } catch (ServiceException e) {
            throw new ActionException("Could not fill content at homepage", e);
        }
        int pageCount;
        if (productsCount % Integer.parseInt(pageSize) == 0) {
            pageCount = productsCount / Integer.parseInt(pageSize);
        } else {
            pageCount = productsCount / Integer.parseInt(pageSize) + 1;
        }
        req.setAttribute("products", products);
        req.getSession().setAttribute("productTypes", productTypes);
        req.setAttribute("page", page);
        req.setAttribute("pagesCount", pageCount);
        req.setAttribute("pageSize", pageSize);
        LOG.info("Page number: {}. Page size: {}. Pages count: {}", page, pageSize, pageCount);
        return new ActionResult("home");
    }
}

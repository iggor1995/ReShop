package com.epam.igor.electronicsshop.action.util;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowWelcomePageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowHomePageAction.class);
    private static final String FIRST_PAGE = "1";
    private static final String DEFAULT_PAGE_SIZE = "2";
    private static final String PRODUCTS = "products";
    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGE = "page";
    private static final String PAGES_COUNT = "pagesCount";
    private static final String ERROR = "Couldn't fill content at home page";
    private static final String INFO = "Page number: {}. Page size: {}. Pages count: {}";
    private static final String WELCOME = "welcome";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        List<Product> products;
        String page = req.getParameter(PAGE);
        if (page == null) {
            page = FIRST_PAGE;
        }
        String pageSize = req.getParameter(PAGE_SIZE);
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        List<Product> productsOnPage;
        try {
            ProductService productService = new ProductService();
            products = productService.getFeaturedProducts();
            if (products.size() < pageInt * pageSizeInt) {
                productsOnPage = products.subList(((pageInt - 1) * pageSizeInt), products.size());
            } else {
                productsOnPage = products.subList(((pageInt - 1) * pageSizeInt), pageInt * pageSizeInt);
            }
        } catch (ServiceException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR, e);
        }
        int pageCount;
        if (products.size() % Integer.parseInt(pageSize) == 0) {
            pageCount = products.size() / Integer.parseInt(pageSize);
        } else {
            pageCount = products.size() / Integer.parseInt(pageSize) + 1;
        }
        req.setAttribute(PRODUCTS, productsOnPage);
        req.setAttribute(PAGE, page);
        req.setAttribute(PAGES_COUNT, pageCount);
        req.setAttribute(PAGE_SIZE, pageSize);
        LOG.info(INFO, page, pageSize, pageCount);
        return new ActionResult(WELCOME);
    }
}

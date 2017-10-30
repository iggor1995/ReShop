package com.epam.igor.electronicsshop.action.common;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.ProductConstants;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.util.PageUtil;
import com.epam.igor.electronicsshop.util.ProductUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowWelcomePageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowHomePageAction.class);
    private static final String ERROR = "Couldn't fill content at home page";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        List<Product> products;
        PageUtil<Product> pageUtil = new PageUtil<>();
        String page = pageUtil.getPage(req);
        String pageSize = pageUtil.getPageSize(req);
        List<Product> productsOnPage;
        try {
            ProductService productService = new ProductService();
            products = productService.getFeaturedProducts();
            productsOnPage = pageUtil.getEntitiesOnPage(products, Integer.parseInt(page), Integer.parseInt(pageSize));
        } catch (ServiceException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR, e);
        }
        int pageCount = pageUtil.getPageCount(products.size(), pageSize);
        req.setAttribute(ProductConstants.PRODUCTS, productsOnPage);
        req.setAttribute(PageConstants.PAGE, page);
        req.setAttribute(PageConstants.PAGES_COUNT, pageCount);
        req.setAttribute(PageConstants.PAGE_SIZE, pageSize);
        LOG.info(PageConstants.INFO, page, pageSize, pageCount);
        return new ActionResult(PageConstants.WELCOME);
    }
}

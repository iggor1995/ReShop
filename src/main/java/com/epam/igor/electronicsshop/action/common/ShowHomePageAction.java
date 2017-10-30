package com.epam.igor.electronicsshop.action.common;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.ProductConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.entity.ProductType;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import com.epam.igor.electronicsshop.util.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Class sets necessary attributes for displaying home page
 *
 * @author Igor Lapin
 */

public class ShowHomePageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowHomePageAction.class);
    private static final String ERROR = "Couldn't fill content at home page";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        PageUtil pageUtil = new PageUtil();
        if (checkUser(req)) {
            return new ActionResult(PageConstants.WELCOME, true);
        }
        List<Product> products;
        List<ProductType> productTypes;
        String page = pageUtil.getPage(req);
        String pageSize = pageUtil.getPageSize(req);
        int productsCount;
        try {
            ShopService shopService = new ShopService();
            products = shopService.getAllProductsOnPage(Integer.parseInt(pageSize), Integer.parseInt(page));
            productsCount = shopService.getProductsCount();
            productTypes = shopService.getAllProductTypes();
        } catch (ServiceException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR, e);
        }
        int pageCount = pageUtil.getPageCount(productsCount, pageSize);
        req.setAttribute(ProductConstants.PRODUCTS, products);
        req.getSession().setAttribute(ProductConstants.PRODUCT_TYPES, productTypes);
        req.setAttribute(PageConstants.PAGE, page);
        req.setAttribute(PageConstants.PAGES_COUNT, pageCount);
        req.setAttribute(PageConstants.PAGE_SIZE, pageSize);
        LOG.info(PageConstants.INFO, page, pageSize, pageCount);
        return new ActionResult(PageConstants.HOME);
    }

    private boolean checkUser(HttpServletRequest req){
        User loggedUser = (User) req.getSession().getAttribute(UserConstants.LOGGED_USER);
        return  (loggedUser == null);
    }
}

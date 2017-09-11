package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.entity.ProductType;
import com.epam.igor.electronicsshop.entity.User;
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
    private static final String FIRST_PAGE = "1";
    private static final String DEFAULT_PAGE_SIZE = "4";
    private static final String PRODUCTS = "products";
    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGE = "page";
    private static final String PAGES_COUNT = "pagesCount";
    private static final String HOME_PAGE = "home";
    private static final String PRODUCT_TYPES = "productTypes";
    private static final String ERROR = "Couldn't fill content at home page";
    private static final String INFO = "Page number: {}. Page size: {}. Pages count: {}";
    public static final String WELCOME = "welcome";
    public static final String LOGGED_USER = "loggedUser";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        User loggedUser = (User)req.getSession().getAttribute(LOGGED_USER);
        if(loggedUser == null){
            return new ActionResult(WELCOME);
        }
        List<Product> products;
        List<ProductType> productTypes;
        String page = req.getParameter(PAGE);
        if (page == null) {
            page = FIRST_PAGE;
        }
        String pageSize = req.getParameter(PAGE_SIZE);
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        int productsCount;
        try {
            ShopService shopService = new ShopService();
            products = shopService.getAllProductsOnPage(Integer.parseInt(page), Integer.parseInt(pageSize));
            productsCount = shopService.getProductsCount();
            productTypes = shopService.getAllProductTypes();
        } catch (ServiceException e) {
            throw new ActionException(ERROR, e);
        }
        int pageCount;
        if (productsCount % Integer.parseInt(pageSize) == 0) {
            pageCount = productsCount / Integer.parseInt(pageSize);
        } else {
            pageCount = productsCount / Integer.parseInt(pageSize) + 1;
        }
        req.setAttribute(PRODUCTS, products);
        req.getSession().setAttribute(PRODUCT_TYPES, productTypes);
        req.setAttribute(PAGE, page);
        req.setAttribute(PAGES_COUNT, pageCount);
        req.setAttribute(PAGE_SIZE, pageSize);
        LOG.info(INFO, page, pageSize, pageCount);
        return new ActionResult(HOME_PAGE);
    }
}

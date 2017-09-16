package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *  Class sets necessary attributes for displaying manage products page.
 * @author Igor Lapin
 * */
public class ShowManageProductsPageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowManageProductsPageAction.class);
    private static final String FIRST_PAGE = "1";
    private static final String DEFAULT_PAGE_SIZE = "2";
    private static final String PAGE = "page";
    private static final String ENCODING = "UTF-8";
    private static final String PRODUCTS = "products";
    private static final String PAGES_COUNT = "pagesCount";
    private static final String PAGE_SIZE = "pageSize";
    private static final String ERROR = "Couldn't show manage products page";
    private static final String INFO = "Page number: {}. Page size: {}. Pages count: {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        ShopService shopService = new ShopService();
        List<Product> products;
        String pageNumber = req.getParameter(PAGE);
        if(pageNumber == null){
            pageNumber = FIRST_PAGE;
        }
        String pageSize = req.getParameter(PAGE_SIZE);
        if(pageSize == null){
            pageSize = DEFAULT_PAGE_SIZE;
        }
        int productsCount;
        try {
            products = shopService.getAllProductsOnPage(Integer.parseInt(pageSize), Integer.parseInt(pageNumber));
            productsCount = shopService.getProductsCount();
        } catch (ServiceException e) {
            throw new ActionException(ERROR, e);
        }
        int pageCount;
        if(productsCount % Integer.parseInt(pageSize) == 0){
            pageCount = productsCount / Integer.parseInt(pageSize);
        }
        else {
            pageCount =  productsCount / Integer.parseInt(pageSize) + 1;
        }
        req.setAttribute(PAGE, pageNumber);
        req.setAttribute(PAGES_COUNT, pageCount);
        req.setAttribute(PAGE_SIZE, pageSize);
        req.setAttribute(PRODUCTS, products);
        try {
            req.setCharacterEncoding(ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LOG.info(INFO, pageNumber, pageSize, pageCount);
        return new ActionResult(PRODUCTS);
    }
}

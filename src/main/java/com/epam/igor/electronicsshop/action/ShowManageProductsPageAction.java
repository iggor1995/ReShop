package com.epam.igor.electronicsshop.action;

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
 * Created by User on 27.08.2017.
 */
public class ShowManageProductsPageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowManageProductsPageAction.class);
    private static final String FIRST_PAGE = "1";
    private static final String DEFAULT_PAGE_SIZE = "4";
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        ShopService shopService = new ShopService();
        List<Product> products;
        String pageNumber = req.getParameter("page");
        if(pageNumber == null){
            pageNumber = FIRST_PAGE;
        }
        String pageSize = req.getParameter("pageSize");
        if(pageSize == null){
            pageSize = DEFAULT_PAGE_SIZE;
        }
        int productsCount;
        try {
            products = shopService.getAllProductsOnPage(Integer.parseInt(pageSize), Integer.parseInt(pageNumber));
            productsCount = shopService.getProductsCount();
            LOG.info("products count - {}", productsCount);
        } catch (ServiceException e) {
            throw new ActionException("Couldn't show manage products page", e);
        }
        int pageCount;
        if(productsCount % Integer.parseInt(pageSize) == 0){
            pageCount = productsCount / Integer.parseInt(pageSize);
        }
        else {
            pageCount =  productsCount / Integer.parseInt(pageSize) + 1;
        }
        req.setAttribute("page", pageNumber);
        req.setAttribute("pagesCount", pageCount);
        req.setAttribute("pageSize", pageSize);
        req.setAttribute("products", products);
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LOG.info("Page number - {}. Page size - {}. Pages count - {}", pageNumber, pageSize, pageCount);
        return new ActionResult("products");
    }
}

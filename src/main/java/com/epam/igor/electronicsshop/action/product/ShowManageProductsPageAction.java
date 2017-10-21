package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.ProductConstants;
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
 * Class sets necessary attributes for displaying manage products page.
 *
 * @author Igor Lapin
 */
public class ShowManageProductsPageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowManageProductsPageAction.class);
    private static final String ENCODING = "UTF-8";
    private static final String ERROR = "Couldn't show manage products page";
    private static final String COULDN_T_SET_ENCODING = "Couldn't set encoding";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        ShopService shopService = new ShopService();
        List<Product> products;
        String pageNumber = req.getParameter(PageConstants.PAGE);
        if (pageNumber == null) {
            pageNumber = PageConstants.FIRST_PAGE;
        }
        String pageSize = req.getParameter(PageConstants.PAGE_SIZE);
        if (pageSize == null) {
            pageSize = PageConstants.DEFAULT_SIZE;
        }
        int productsCount;
        try {
            products = shopService.getAllProductsOnPage(Integer.parseInt(pageSize), Integer.parseInt(pageNumber));
            productsCount = shopService.getProductsCount();
        } catch (ServiceException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR, e);
        }
        int pageCount;
        if (productsCount % Integer.parseInt(pageSize) == 0) {
            pageCount = productsCount / Integer.parseInt(pageSize);
        } else {
            pageCount = productsCount / Integer.parseInt(pageSize) + 1;
        }
        req.setAttribute(PageConstants.PAGE, pageNumber);
        req.setAttribute(PageConstants.PAGES_COUNT, pageCount);
        req.setAttribute(PageConstants.PAGE_SIZE, pageSize);
        req.setAttribute(ProductConstants.PRODUCTS, products);
        try {
            req.setCharacterEncoding(ENCODING);
        } catch (UnsupportedEncodingException e) {
            LOG.info(COULDN_T_SET_ENCODING, e);
            throw new ActionException(COULDN_T_SET_ENCODING, e);
        }
        LOG.info(PageConstants.INFO, pageNumber, pageSize, pageCount);
        return new ActionResult(ProductConstants.PRODUCTS);
    }
}

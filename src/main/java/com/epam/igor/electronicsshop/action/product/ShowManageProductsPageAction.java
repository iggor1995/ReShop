package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.ProductConstants;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import com.epam.igor.electronicsshop.util.PageUtil;
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

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        PageUtil pageUtil = new PageUtil();
        ShopService shopService = new ShopService();
        List<Product> products;
        String pageNumber = pageUtil.getPage(req);
        String pageSize = pageUtil.getPageSize(req);
        int productsCount;
        try {
            req.setCharacterEncoding(ENCODING);
            products = shopService.getAllProductsOnPage(Integer.parseInt(pageSize), Integer.parseInt(pageNumber));
            productsCount = shopService.getProductsCount();
        } catch (ServiceException | UnsupportedEncodingException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR, e);
        }
        int pageCount = pageUtil.getPageCount(productsCount ,pageSize);
        req.setAttribute(PageConstants.PAGE, pageNumber);
        req.setAttribute(PageConstants.PAGES_COUNT, pageCount);
        req.setAttribute(PageConstants.PAGE_SIZE, pageSize);
        req.setAttribute(ProductConstants.PRODUCTS, products);
        LOG.info(PageConstants.INFO, pageNumber, pageSize, pageCount);
        return new ActionResult(ProductConstants.PRODUCTS);
    }
}

package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.ProductConstants;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Class sets necessary attributes for displaying catalog page.
 *
 * @author Igor Lapin
 */
public class ShowCatalogPageAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(ShowManageProductsPageAction.class);
    private static final String ERROR = "Couldn't show catalog page";
    private List<Product> products;

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        String page = req.getParameter(PageConstants.PAGE);
        if (page == null) {
            page = PageConstants.FIRST_PAGE;
        }
        String pageSize = req.getParameter(PageConstants.PAGE_SIZE);
        if (pageSize == null) {
            pageSize = PageConstants.DEFAULT_SIZE;
        }
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        String type = req.getParameter(ProductConstants.TYPE);
        List<Product> productsOnPage = getProductsOnPage(pageSizeInt, pageInt, type);
        int pageCount = countPage(products, pageSizeInt);
        req.setAttribute(ProductConstants.PRODUCTS, productsOnPage);
        req.setAttribute(ProductConstants.TYPE, type);
        req.setAttribute(PageConstants.PAGES_COUNT, pageCount);
        req.setAttribute(PageConstants.PAGE, page);
        req.setAttribute(PageConstants.PAGE_SIZE, pageSize);
        LOG.info(PageConstants.INFO, page, pageSize, pageCount);
        return new ActionResult(PageConstants.CATALOG_PAGE);
    }

    private int countPage(List<Product> products, int pageSizeInt) {
        int pageCount;
        if (products.size() % pageSizeInt == 0) {
            pageCount = products.size() / pageSizeInt;
        } else {
            pageCount = products.size() / pageSizeInt + 1;
        }
        return pageCount;
    }

    private List<Product> getProductsOnPage(int pageSizeInt, int pageInt, String type) throws ActionException {
        List<Product> productsOnPage;
        try {
            ProductService productService = new ProductService();
            products = productService.getAllProductsByType(type);
            if (products.size() < pageInt * pageSizeInt) {
                productsOnPage = products.subList(((pageInt - 1) * pageSizeInt), products.size());
            } else {
                productsOnPage = products.subList(((pageInt - 1) * pageSizeInt), pageInt * pageSizeInt);
            }
        } catch (ServiceException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR, e);
        }
        return productsOnPage;
    }
}

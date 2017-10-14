package com.epam.igor.electronicsshop.action.product;

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

/**
 * Class sets necessary attributes for displaying catalog page.
 *
 * @author Igor Lapin
 */
public class ShowCatalogPageAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(ShowManageProductsPageAction.class);
    private static final String FIRST_PAGE = "1";
    private static final String DEFAULT_SIZE = "2";
    private static final String PAGE = "page";
    private static final String PRODUCTS = "products";
    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGES_COUNT = "pagesCount";
    private static final String TYPE = "type";
    private static final String CATALOG_PAGE = "type-catalog";
    private static final String ERROR = "Couldn't show catalog page";
    private static final String INFO = "Page number: {}. Page size: {}. Pages count: {}";
    private List<Product> products;

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        String page = req.getParameter(PAGE);
        if (page == null) {
            page = FIRST_PAGE;
        }
        String pageSize = req.getParameter(PAGE_SIZE);
        if (pageSize == null) {
            pageSize = DEFAULT_SIZE;
        }
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        String type = req.getParameter(TYPE);
        List<Product> productsOnPage = getProductsOnPage(pageSizeInt, pageInt, type);
        int pageCount = countPage(products, pageSizeInt);
        req.setAttribute(PRODUCTS, productsOnPage);
        req.setAttribute(TYPE, type);
        req.setAttribute(PAGES_COUNT, pageCount);
        req.setAttribute(PAGE, page);
        req.setAttribute(PAGE_SIZE, pageSize);
        LOG.info(INFO, page, pageSize, pageCount);
        return new ActionResult(CATALOG_PAGE);
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

package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.ProductConstants;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.util.PageUtil;
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
        PageUtil<Product> pageUtil = new PageUtil<>();
        String page = pageUtil.getPage(req);
        String pageSize = pageUtil.getPageSize(req);
        String type = req.getParameter(ProductConstants.TYPE);
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        try {
            ProductService productService = new ProductService();
            products = productService.getAllProductsByType(type);
        } catch (ServiceException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR, e);
        }
        int pageCount = pageUtil.getPageCount(products.size(), pageSize);
        List<Product> productsOnPage = pageUtil.getEntitiesOnPage(products, pageInt, pageSizeInt);
        req.setAttribute(ProductConstants.PRODUCTS, productsOnPage);
        req.setAttribute(ProductConstants.TYPE, type);
        req.setAttribute(PageConstants.PAGES_COUNT, pageCount);
        req.setAttribute(PageConstants.PAGE, page);
        req.setAttribute(PageConstants.PAGE_SIZE, pageSize);
        LOG.info(PageConstants.INFO, page, pageSize, pageCount);
        return new ActionResult(PageConstants.CATALOG_PAGE);
    }

}

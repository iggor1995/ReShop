package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class ShowCatalogPageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowManageProductsPageAction.class);
    private final String FIRTS_PAGE = "1";
    public final String DEAULT_SIZE = "4";
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        List<Product> products;
        String page = req.getParameter("page");
        if(page == null){
            page = FIRTS_PAGE;
        }
        String pageSize = req.getParameter("pageSize");
        if(pageSize == null){
            pageSize = DEAULT_SIZE;
        }
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        List<Product> productsOnPage;
        String type = req.getParameter("type");

        try {
            ProductService productService = new ProductService();
            products = productService.getAllProductsByType(type);
            if(products.size() < pageInt * pageSizeInt){
                productsOnPage = products.subList(((pageInt - 1) * pageSizeInt), products.size());
            }
            else {
                productsOnPage = products.subList(((pageInt - 1) * pageSizeInt), pageInt * pageSizeInt);
            }
        } catch (ServiceException e) {
            throw new ActionException("Couldn't show catalog page", e);
        }
        int pageCount;
        if(products.size() % pageSizeInt == 0){
            pageCount = products.size() / pageSizeInt;
        }
        else {
            pageCount = products.size() / pageSizeInt + 1;
        }
        req.setAttribute("products", productsOnPage);
        req.setAttribute("type", type);
        req.setAttribute("pagesCount", pageCount);
        req.setAttribute("page", page);
        req.setAttribute("pageSize", pageSize);
        LOG.info("Page number: {}. Page size: {}. Pages count: {}", page, pageSize, pageCount);
        return new ActionResult("type-catalog");
    }
}

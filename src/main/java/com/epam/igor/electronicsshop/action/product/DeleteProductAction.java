package com.epam.igor.electronicsshop.action.product;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.ProductConstants;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * For deleting product.
 *
 * @author Igor Lapin
 */
public class DeleteProductAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(DeleteProductAction.class);
    private static final String REFERER_PAGE = "referer";
    private static final String ERROR_DELETE = "Couldn't delete product by id";
    private static final String DELETED_PRODUCT = "product - {} has been deleted";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        String id = req.getParameter(ProductConstants.ID);
        try {
            ShopService shopService = new ShopService();
            shopService.deleteProductById(id);
        } catch (ServiceException e) {
            LOG.info(ERROR_DELETE, e);
            throw new ActionException(ERROR_DELETE);
        }
        LOG.info(DELETED_PRODUCT, id);
        return new ActionResult(req.getHeader(REFERER_PAGE), true);
    }
}

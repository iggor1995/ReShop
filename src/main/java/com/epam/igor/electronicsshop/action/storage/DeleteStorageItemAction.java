package com.epam.igor.electronicsshop.action.storage;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * For deleting storage item
 *
 * @author Igor Lapin
 */
public class DeleteStorageItemAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(DeleteStorageItemAction.class);
    private static final String DELETED_ITEM = "storage item - {} has been deleted";
    private static final String ERROR_DELETE = "Couldn't delete sotrage item by id";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        ShopService shopService = new ShopService();
        String id = req.getParameter(UserConstants.ID);
        try {
            shopService.deleteStorageItemById(id);
            LOG.info(DELETED_ITEM, id);
        } catch (ServiceException e) {
            LOG.info(ERROR_DELETE, e);
            throw new ActionException(ERROR_DELETE, e);
        }
        return new ActionResult(req.getHeader(PageConstants.REFERER_PAGE), true);
    }
}

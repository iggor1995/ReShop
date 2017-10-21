package com.epam.igor.electronicsshop.action.storage;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.OrderConstants;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.entity.StorageItem;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Class sets necessary attributes for displaying manage storage page.
 *
 * @author Igor Lapin
 */
public class ShowManageStoragePageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowManageStoragePageAction.class);
    private static final String ERROR = "Couldn't show manage storage items page";

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
        int storageItemsCount;
        List<StorageItem> storageItems;
        try {
            ShopService shopService = new ShopService();
            storageItems = shopService.getAllStorageItemsOnPage(pageSizeInt, pageInt);
            storageItemsCount = shopService.getStorageItemsCount();
        } catch (ServiceException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR, e);
        }
        int pageCount;
        if (storageItemsCount % pageSizeInt == 0) {
            pageCount = storageItemsCount / pageSizeInt;
        } else {
            pageCount = storageItemsCount / pageSizeInt + 1;
        }
        req.setAttribute(PageConstants.PAGES_COUNT, pageCount);
        req.setAttribute(PageConstants.PAGE_SIZE, pageSize);
        req.setAttribute(PageConstants.PAGE, page);
        req.setAttribute(OrderConstants.STORAGE_ITEMS, storageItems);
        LOG.info(PageConstants.INFO, page, pageSize, pageCount);
        return new ActionResult(PageConstants.STORAGE);
    }
}

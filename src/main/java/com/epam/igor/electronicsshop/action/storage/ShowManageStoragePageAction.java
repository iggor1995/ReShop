package com.epam.igor.electronicsshop.action.storage;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.entity.StorageItem;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by User on 08.09.2017.
 */
public class ShowManageStoragePageAction implements Action {
    private final static Logger LOG = LoggerFactory.getLogger(ShowManageStoragePageAction.class);
    private static final String FIRST_PAGE = "1";
    private static final String DEFAULT_SIZE = "2";
    private static final String PAGE = "page";
    private static final String PAGES_COUNT = "pagesCount";
    private static final String PAGE_SIZE = "pageSize";
    private static final String STORAGE_ITEMS = "storageItems";
    private static final String STORAGE = "storage";
    private static final String ERROR = "Couldn't show manage storage items page";
    private static final String INFO = "Page number: {}. Page size: {}. Pages count: {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {

        String page = req.getParameter(PAGE);
        if(page == null){
            page= FIRST_PAGE;
        }
        String pageSize = req.getParameter(PAGE_SIZE);
        if(pageSize == null){
            pageSize = DEFAULT_SIZE;
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
            throw new ActionException(ERROR, e);
        }
        int pageCount;
        if(storageItemsCount % pageSizeInt == 0){
            pageCount = storageItemsCount / pageSizeInt;
        }
        else {
            pageCount = storageItemsCount / pageSizeInt + 1;
        }
        req.setAttribute(PAGES_COUNT, pageCount);
        req.setAttribute(PAGE_SIZE, pageSize);
        req.setAttribute(PAGE, page);
        req.setAttribute(STORAGE_ITEMS, storageItems);
        LOG.info(INFO, page, pageSize, pageCount);
        return  new ActionResult(STORAGE);
    }
}

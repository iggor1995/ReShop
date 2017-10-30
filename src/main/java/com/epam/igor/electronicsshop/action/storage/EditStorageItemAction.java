package com.epam.igor.electronicsshop.action.storage;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.action.user.RegisterAction;
import com.epam.igor.electronicsshop.constants.ErrorConstants;
import com.epam.igor.electronicsshop.constants.OrderConstants;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * For saving storage item changes to database
 *
 * @author Igor Lapin
 */
public class EditStorageItemAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(EditStorageItemAction.class);
    private static final String ROWS_COUNT = "rowsCount";
    private static final String INVALID_STORAGE_ITEM_AMOUNT_FORMAT = "Invalid storage item amount format - {}";
    private static final String STORAGE_ITEM_ID_AMOUNT_SET_TO_BY = "Storage item (id = {}) amount set to {} by {}";
    private static final String COULD_NOT_EDIT_STORAGE_ITEM_AMOUNT = "Could not edit storage item amount";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        int rowsCount = Integer.parseInt(req.getParameter(ROWS_COUNT));

        Map<Integer, String> errorMap = new HashMap<>();
        for (int i = 0; i < rowsCount; i++) {
            String amount = req.getParameter(OrderConstants.AMOUNT + i);
            Validation validation = new Validation();
            if (validation.checkAmount(req, amount)) {
                errorMap.put(i, ErrorConstants.TRUE);
                LOG.info(INVALID_STORAGE_ITEM_AMOUNT_FORMAT, amount);
            } else {
               updateStorageItem(req, amount, i);
            }
        }
        req.setAttribute(ErrorConstants.FLASH_ERROR_MAP, errorMap);
        return new ActionResult(req.getHeader(PageConstants.REFERER_PAGE), true);
    }
    private void updateStorageItem(HttpServletRequest req, String amount, int i) throws ActionException {
        try {
            ShopService shopService = new ShopService();
            String itemId = req.getParameter(OrderConstants.ITEM_ID + i);
            shopService.updateStorageItem(itemId, amount);
            LOG.info(STORAGE_ITEM_ID_AMOUNT_SET_TO_BY, itemId, amount, req.getSession(false)
                    .getAttribute(UserConstants.LOGGED_USER));
        } catch (ServiceException e) {
            LOG.info(COULD_NOT_EDIT_STORAGE_ITEM_AMOUNT, e);
            throw new ActionException(COULD_NOT_EDIT_STORAGE_ITEM_AMOUNT, e);
        }

    }
}

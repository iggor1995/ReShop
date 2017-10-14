package com.epam.igor.electronicsshop.action.storage;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.action.user.RegisterAction;
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
    private static final String PROPERTIES_ERROR = "Cannot load properties";
    private static final String VALIDATION_PROPERTIES = "validation.properties";
    private static final String ROWS_COUNT = "rowsCount";
    private static final String AMOUNT = "amount";
    private static final String STORAGE_AMOUNT_REGEX = "storage.amount.regexp";
    private static final String INVALID_STORAGE_ITEM_AMOUNT_FORMAT = "Invalid storage item amount format - {}";
    private static final String STORAGE_ITEM_ID_AMOUNT_SET_TO_BY = "Storage item (id = {}) amount set to {} by {}";
    private static final String ITEM_ID = "itemId";
    private static final String COULD_NOT_EDIT_STORAGE_ITEM_AMOUNT = "Could not edit storage item amount";
    private static final String ERROR_MAP = "flash.errorMap";
    private static final String REFERER = "referer";
    private static final String LOGGED_USER = "loggedUser";
    private static final String TRUE = "true";
    private boolean invalid;
    private Properties properties = new Properties();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {

        ShopService shopService = new ShopService();
        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream(VALIDATION_PROPERTIES));
        } catch (IOException e) {
            LOG.info(PROPERTIES_ERROR, e);
            throw new ActionException(PROPERTIES_ERROR, e);
        }
        int rowsCount = Integer.parseInt(req.getParameter(ROWS_COUNT));
        Map<Integer, String> errorMap = new HashMap<>();
        for (int i = 0; i < rowsCount; i++) {
            String amount = req.getParameter(AMOUNT + i);
            Validation validation = new Validation();
            invalid = validation.checkParameterByRegex(invalid, amount, AMOUNT, properties.getProperty(STORAGE_AMOUNT_REGEX), req);
            if (invalid) {
                invalid = false;
                errorMap.put(i, TRUE);
                LOG.info(INVALID_STORAGE_ITEM_AMOUNT_FORMAT, amount);
            } else {
                try {
                    String itemId = req.getParameter(ITEM_ID + i);
                    shopService.updateStorageItem(itemId, amount);
                    LOG.info(STORAGE_ITEM_ID_AMOUNT_SET_TO_BY, itemId, amount, req.getSession(false).getAttribute(LOGGED_USER));
                } catch (ServiceException e) {
                    LOG.info(COULD_NOT_EDIT_STORAGE_ITEM_AMOUNT, e);
                    throw new ActionException(COULD_NOT_EDIT_STORAGE_ITEM_AMOUNT, e);
                }
            }
        }
        req.setAttribute(ERROR_MAP, errorMap);
        return new ActionResult(req.getHeader(REFERER), true);
    }

}

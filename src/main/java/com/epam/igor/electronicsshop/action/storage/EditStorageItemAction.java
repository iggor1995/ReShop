package com.epam.igor.electronicsshop.action.storage;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  For saving storage item changes to database
 * @author Igor Lapin
 */
public class EditStorageItemAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(EditStorageItemAction.class);
    private static final String CHECK_PARAMETER = "Check parameter '{}' with value '{}' by regex '{}'";
    private static final String WRONG_PARAMETER = "Parameter '{}' with value '{}' is unsuitable.";
    public static final String ERROR = "Error";
    private static final String PROPERTIES_ERROR= "Cannot load properties";
    private static final String VALIDATION_PROPERTIES= "validation.properties";
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
    public static final String FLASH = "flash";
    private boolean invalid;
    Properties properties = new Properties();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {

        ShopService shopService = new ShopService();
        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream(VALIDATION_PROPERTIES));
        } catch (IOException e) {
            throw new ActionException(PROPERTIES_ERROR, e);
        }

        int rowsCount = Integer.parseInt(req.getParameter(ROWS_COUNT));
        Map<Integer, String> errorMap = new HashMap<>();
        for (int i = 0; i < rowsCount; i++) {
            String amount = req.getParameter(AMOUNT + i);
            checkParameterByRegex(amount, AMOUNT, properties.getProperty(STORAGE_AMOUNT_REGEX), req);
            if (invalid) {
                invalid = false;
                errorMap.put(i, "true");
                LOG.info(INVALID_STORAGE_ITEM_AMOUNT_FORMAT, amount);
            } else {
                try {
                    String itemId = req.getParameter(ITEM_ID + i);
                    shopService.updateStorageItem(itemId, amount);
                    LOG.info(STORAGE_ITEM_ID_AMOUNT_SET_TO_BY, itemId, amount, req.getSession(false).getAttribute(LOGGED_USER));
                } catch (ServiceException e) {
                    throw new ActionException(COULD_NOT_EDIT_STORAGE_ITEM_AMOUNT, e);
                }
            }
        }
        req.setAttribute(ERROR_MAP, true);
        return new ActionResult(req.getHeader(REFERER), true);
    }
    private void checkParameterByRegex(String parameter, String parameterName, String regex, HttpServletRequest req) {
        LOG.debug(CHECK_PARAMETER, parameterName, parameter, regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        if (!matcher.matches()) {
            LOG.debug(WRONG_PARAMETER, parameterName, parameter);
            req.setAttribute(FLASH + parameterName + ERROR, "true");
            invalid = true;
        }
    }
}
package com.epam.igor.electronicsshop.action.cart;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.user.RegisterAction;
import com.epam.igor.electronicsshop.entity.Order;
import com.epam.igor.electronicsshop.entity.OrderingItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * For recounting cart
 *@author Igor Lapin
 */
public class RecountCartAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(RecountCartAction.class);
    private static final String CHECK_PARAMETR = "Check parameter '{}' with value '{}' by regex '{}'";
    private static final String WRONG_PARAMETR = "Parameter '{}' with value '{}' is unsuitable.";
    private static final String VALIDATION_PROPERTIES = "validation.properties";
    private static final String AMOUNT = "amount";
    private static final String INVALID_PRODUCT_AMOUNT_FORMAT = "Invalid product amount format - {}";
    private static final String AMOUNT_SET_TO = "{} amount set to {}";
    private static final String FLASH_ERROR_MAP = "flash.errorMap";
    private static final String CART = "cart";
    private static final String CANNOT_LOAD_PROPERTIES = "Cannot load properties";
    private static final String REFERER = "referer";
    private static final String TRUE = "true";
    private static final String ITEM = "item";
    private static final String STORAGE_AMOUNT_REGEXP = "storage.amount.regexp";
    private boolean invalid;
    private Properties properties = new Properties();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Order cart = (Order) req.getSession(false).getAttribute("cart");
        List<OrderingItem> orderItems = cart.getOrderingItems();
        Map<Integer, String> errorMap = new HashMap<>();
        try {
           properties.load(AddProductToCartAction.class.getClassLoader().getResourceAsStream(VALIDATION_PROPERTIES));
        } catch (IOException e) {
            throw new ActionException(CANNOT_LOAD_PROPERTIES, e);
        }
        for (int i = 0; i < orderItems.size(); i++) {
            String amount = req.getParameter(ITEM + i);
            checkParameterByRegex(amount, AMOUNT, properties.getProperty(STORAGE_AMOUNT_REGEXP));
            if (invalid) {
                errorMap.put(i, TRUE);
                LOG.info(INVALID_PRODUCT_AMOUNT_FORMAT, amount);
            } else {
                orderItems.get(i).setAmount(Integer.parseInt(amount));
                LOG.info(AMOUNT_SET_TO, orderItems.get(i), amount);
            }
        }
        req.setAttribute(FLASH_ERROR_MAP, errorMap);
        req.getSession().setAttribute(CART, cart);
        return new ActionResult(req.getHeader(REFERER), true);
    }
    private void checkParameterByRegex(String parameter, String parameterName, String regex) {
        LOG.debug(CHECK_PARAMETR, parameterName, parameter, regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        if (!matcher.matches()) {
            LOG.debug(WRONG_PARAMETR, parameterName, parameter);
            invalid = true;
        }
    }
}

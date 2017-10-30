package com.epam.igor.electronicsshop.action.cart;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.constants.ErrorConstants;
import com.epam.igor.electronicsshop.constants.OrderConstants;
import com.epam.igor.electronicsshop.constants.PageConstants;
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

/**
 * For recounting cart
 *
 * @author Igor Lapin
 */
public class RecountCartAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(RecountCartAction.class);
    private static final String INVALID_PRODUCT_AMOUNT_FORMAT = "Invalid product amount format - {}";
    private static final String AMOUNT_SET_TO = "{} amount set to {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Order cart = (Order) req.getSession(false).getAttribute(PageConstants.CART);
        List<OrderingItem> orderItems = cart.getOrderingItems();
        Map<Integer, String> errorMap = new HashMap<>();
        for (int i = 0; i < orderItems.size(); i++) {
            String amount = req.getParameter(OrderConstants.ITEM + i);
            Validation validation = new Validation();
            if (validation.checkAmount(req, amount)) {
                errorMap.put(i, ErrorConstants.TRUE);
                LOG.info(INVALID_PRODUCT_AMOUNT_FORMAT, amount);
            } else {
                orderItems.get(i).setAmount(Integer.parseInt(amount));
                LOG.info(AMOUNT_SET_TO, orderItems.get(i), amount);
            }
        }
        req.setAttribute(ErrorConstants.FLASH_ERROR_MAP, errorMap);
        req.getSession().setAttribute(PageConstants.CART, cart);
        return new ActionResult(req.getHeader(PageConstants.REFERER_PAGE), true);
    }

}

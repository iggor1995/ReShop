package com.epam.igor.electronicsshop.action.cart;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 16.09.2017.
 */
public class AddCartDescriptionAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(AddCartDescriptionAction.class);
    private static final String CART = "cart";
    private static final String DESCRIPTION = "description";
    private static final String CART_DESCRIPTION_ADDED = "Cart description added - {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Order cart = (Order) req.getSession(false).getAttribute(CART);
        String description = req.getParameter(DESCRIPTION);
        cart.setDescription(description);
        req.getSession(false).setAttribute(CART, cart);
        LOG.info(CART_DESCRIPTION_ADDED, description);
        return new ActionResult(CART, true);
    }
}

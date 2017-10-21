package com.epam.igor.electronicsshop.action.cart;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddCartDescriptionAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(AddCartDescriptionAction.class);
    private static final String DESCRIPTION = "description";
    private static final String CART_DESCRIPTION_ADDED = "Cart description added - {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Order cart = (Order) req.getSession(false).getAttribute(PageConstants.CART);
        String description = req.getParameter(DESCRIPTION);
        cart.setDescription(description);
        req.getSession(false).setAttribute(PageConstants.CART, cart);
        LOG.info(CART_DESCRIPTION_ADDED, description);
        return new ActionResult(PageConstants.CART, true);
    }
}

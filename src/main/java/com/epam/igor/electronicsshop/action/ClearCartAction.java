package com.epam.igor.electronicsshop.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 08.09.2017.
 */
public class ClearCartAction implements Action {
    private final static Logger LOG = LoggerFactory.getLogger(ClearCartAction.class);
    private static final String LOGGED_USER = "loggedUser";
    private static final String REFERER_PAGE = "referer";
    private static final String CART = "cart";
    private static final String CART_CLEAR_MESSAGE = "cleared cart by - {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        req.getSession(false).removeAttribute(CART);
        LOG.info(CART_CLEAR_MESSAGE, req.getSession().getAttribute(LOGGED_USER));
        return new ActionResult(req.getHeader(REFERER_PAGE), true);
    }
}

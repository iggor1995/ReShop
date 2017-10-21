package com.epam.igor.electronicsshop.action.cart;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class for deleting cart from session
 *
 * @author Igor Lapin
 */
public class ClearCartAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ClearCartAction.class);
    private static final String CART_CLEAR_MESSAGE = "cleared cart by - {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        req.getSession(false).removeAttribute(PageConstants.CART);
        LOG.info(CART_CLEAR_MESSAGE, req.getSession().getAttribute(UserConstants.LOGGED_USER));
        return new ActionResult(req.getHeader(PageConstants.REFERER_PAGE), true);
    }
}

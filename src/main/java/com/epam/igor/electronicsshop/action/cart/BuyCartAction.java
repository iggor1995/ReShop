package com.epam.igor.electronicsshop.action.cart;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.ErrorConstants;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.Order;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class for buying cart and creating order
 *
 * @author Igor Lapin
 */
public class BuyCartAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(BuyCartAction.class);
    private static final String ERROR_MESSAGE = "{} - doesn't has enough money. Money needed - {}";
    private static final String NOT_ENOUGH = "notEnough";
    private static final String ERROR_PLACING = "Couldn't place order";
    private static final String BOUGHT_ORDER = "{} has been bought by - {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Order order = (Order) req.getSession().getAttribute(PageConstants.CART);
        User loggedUser = (User) req.getSession().getAttribute(UserConstants.LOGGED_USER);
        order.setUser(loggedUser);
        if (loggedUser.getCash().isLessThan(order.getPrice())) {
            req.setAttribute(ErrorConstants.BALANCE_ERROR, NOT_ENOUGH);
            Money balanceNeeded = order.getPrice().minus(loggedUser.getCash());
            req.setAttribute(ErrorConstants.BALANCE_NEEDED, balanceNeeded);
            LOG.info(ERROR_MESSAGE, loggedUser, balanceNeeded);
            return new ActionResult(PageConstants.USER_PROFILE_REDIRECT, true);
        }
        try {
            ShopService shopService = new ShopService();
            User user = shopService.buyCart(order);
            req.getSession().setAttribute(UserConstants.LOGGED_USER, user);
            req.getSession(false).removeAttribute(PageConstants.CART);
            LOG.info(BOUGHT_ORDER, order, loggedUser);
            return new ActionResult(PageConstants.USER_ORDERS_REDIRECT, true);
        } catch (ServiceException e) {
            LOG.info(ERROR_PLACING, e);
            throw new ActionException(ERROR_PLACING, e);
        }
    }

}

package com.epam.igor.electronicsshop.action;

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
 * Created by User on 05.09.2017.
 */
public class BuyCartAction implements Action {
    private final static Logger LOG = LoggerFactory.getLogger(BuyCartAction.class);
    private static final String BALANCE_ERROR = "balance.error";
    private static final String BALANCE_NEEDED = "balance.needed";
    private static final String ERROR_MESSAGE = "{} - doesn't has enough money. Money needed - {}";
    private static final String NOT_ENOUGH = "notEnough";
    private static final String LOGGED_USER = "loggedUser";
    private static final String USER_PROFILE_PAGE = "user/profile";
    private static final String USER_ORDERS_PAGE = "user/orders";
    private static final String ERROR_PLACING = "Couldn't place order";
    private static final String BOUGHT_ORDER = "{} has been bought by - {}";
    private static final String CART = "cart";
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Order order = (Order) req.getSession().getAttribute(CART);
        User loggedUser = (User) req.getSession().getAttribute(LOGGED_USER);
        order.setUser(loggedUser);
        if(loggedUser.getCash().isLessThan(order.getPrice())){
            req.setAttribute(BALANCE_ERROR, NOT_ENOUGH);
            Money balanceNeeded = order.getPrice().minus(loggedUser.getCash());
            req.setAttribute(BALANCE_NEEDED, balanceNeeded);
            LOG.info(ERROR_MESSAGE, loggedUser, balanceNeeded);
            return new ActionResult(USER_PROFILE_PAGE, true);
        }
        try {
            ShopService shopService = new ShopService();
            shopService.buyCart(order);
            req.getSession().setAttribute(LOGGED_USER, loggedUser);
            req.getSession(false).removeAttribute(CART);
            LOG.info(BOUGHT_ORDER, order, loggedUser);
            return new ActionResult(USER_ORDERS_PAGE, true);
        } catch (ServiceException e) {
            throw new ActionException(ERROR_PLACING, e);
        }
    }
}

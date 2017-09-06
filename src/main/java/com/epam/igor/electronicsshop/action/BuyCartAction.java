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
    private final String BALANCE_ERROR = "balance.error";
    private final String BALANCE_NEEDED = "balance.needed";
    private final String ERROR_MESSAGE = "{} - doesn't has enough money. Money needed - {}";
    private final String NOT_ENOUGH = "notEnough";
    private final String LOGGED_USER = "loggedUser";
    private final String CART = "cart";
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
            return new ActionResult("user/profile", true);
        }
        try {
            ShopService shopService = new ShopService();
            shopService.buyCart(order);
            req.getSession().setAttribute("loggedUser", loggedUser);
            req.getSession(false).removeAttribute("cart");
            LOG.info("{} has been bought by - {}", order, loggedUser);
            return new ActionResult("user-orders");
        } catch (ServiceException e) {
            throw new ActionException("Couldn't place order", e);
        }
    }
}

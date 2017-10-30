package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;
import com.epam.igor.electronicsshop.util.ProductUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * For refilling user's balance
 *
 * @author Igor Lapin
 */
public class RefillUserBalanceAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(RefillUserBalanceAction.class);
    private static final String REFILL = "refill";
    private static final String BALANCE = "balance";
    private static final String REFILLED_BALANCE = "{} refilled balance --> +{}";
    private static final String REFILL_ERROR = "Couldn't refill balance";
    private UserService userService = new UserService();
    private String balance;
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            String id = req.getParameter(UserConstants.USER_ID);
            balance = req.getParameter(BALANCE);
            Validation validation = new Validation();
            User user = userService.getFilledUserById(Integer.valueOf(id));
            if (validation.checkMoney(req, balance)) {
                req.setAttribute(UserConstants.USER, user);
                return new ActionResult(REFILL);
            }
            updateUserBalance(req, user);
            return new ActionResult(PageConstants.MANAGE_USERS_REDIRECT, true);
        } catch (ServiceException e) {
            LOG.info(REFILL_ERROR, e);
            throw new ActionException(REFILL_ERROR, e);
        }
    }

    private void updateUserBalance(HttpServletRequest req, User user) throws ServiceException {
        User updatedUser;
        User loggedUser = (User) req.getSession().getAttribute(UserConstants.LOGGED_USER);
        if (loggedUser.equals(user)) {
            updatedUser = userService.refillCash(loggedUser.getId(), balance);
            req.getSession().setAttribute(UserConstants.LOGGED_USER, updatedUser);
        } else {
            updatedUser = userService.refillCash(user.getId(), balance);
        }
        LOG.info(REFILLED_BALANCE, updatedUser, balance);
    }
}

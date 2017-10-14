package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;
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
    private static final String USER = "user";
    private static final String REFILL = "refill";
    private static final String BALANCE = "balance";
    private static final String USER_ID = "userId";
    private static final String REFILLED_BALANCE = "{} refilled balance --> +{}";
    private static final String MANAGE_USERS_PAGE = "manage/users";
    private static final String REFILL_ERROR = "Couldn't refill balance";
    private static final String PROPERTIES_ERROR = "Cannot load properties";
    private static final String MONEY_REGEX = "money.regex";
    private static final String VALIDATION_PROPERTIES = "validation.properties";
    private boolean invalid;
    private Properties properties = new Properties();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {

        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream(VALIDATION_PROPERTIES));
        } catch (IOException e) {
            LOG.info(PROPERTIES_ERROR, e);
            throw new ActionException(PROPERTIES_ERROR, e);
        }
        String id = req.getParameter(USER_ID);
        String balance = req.getParameter(BALANCE);
        Validation validation = new Validation();
        invalid = validation.checkParameterByRegex(invalid, balance, BALANCE, properties.getProperty(MONEY_REGEX), req);

        UserService userService = new UserService();
        try {
            User loggedUser = (User) req.getSession().getAttribute(UserConstants.LOGGED_USER);
            User user = userService.getFilledUserById(Integer.valueOf(id));
            User updatedUser;
            if (invalid) {
                invalid = false;
                req.setAttribute(USER, user);
                return new ActionResult(REFILL);
            }
            if (loggedUser.equals(user)) {
                updatedUser = userService.refillCash(loggedUser.getId(), balance);
                req.getSession().setAttribute(UserConstants.LOGGED_USER, updatedUser);
            } else {
                updatedUser = userService.refillCash(user.getId(), balance);
            }
            LOG.info(REFILLED_BALANCE, updatedUser, balance);
            return new ActionResult(MANAGE_USERS_PAGE, true);
        } catch (ServiceException e) {
            LOG.info(REFILL_ERROR, e);
            throw new ActionException(REFILL_ERROR, e);
        }
    }

}

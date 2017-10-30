package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.Address;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;
import com.epam.igor.electronicsshop.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * For saving new user to database
 */

public class RegisterAction implements Action {
    private static final String UNABLE_REGISTER = "Couldn't register user";
    private static final Logger LOG = LoggerFactory.getLogger(RegisterAction.class);
    private static final String USER_HAS_BEEN_REGISTERED = "{} registered. Address - {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Validation validation = new Validation();
        try {
            if(validation.fullCheck(req)) {
                    return new ActionResult(UserConstants.REGISTER);
            }
            UserUtil userUtil = new UserUtil();
            User user = userUtil.fillUser(req);
            Address address = userUtil.fillAddress(req);
            UserService userService = new UserService();
            User registeredUser = userService.registerUser(user, address);
            req.getSession(false).setAttribute(UserConstants.LOGGED_USER, registeredUser);
            req.getSession(false).removeAttribute(UserConstants.GENDERS);
            LOG.info(USER_HAS_BEEN_REGISTERED, registeredUser, address);
            LOG.info(UserConstants.ROLE, registeredUser.getRole());
        } catch (ServiceException e) {
            LOG.error(UNABLE_REGISTER, e);
            throw new ActionException(UNABLE_REGISTER, e);
        }
        return new ActionResult(UserConstants.HOME_PAGE, true);
    }


}

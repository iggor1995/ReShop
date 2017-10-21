package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.constants.PageConstants;
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
 * For saving user's address changes to database
 *
 * @author Igor Lapin
 */
public class EditUserAddressAction implements Action {

    private static final String UPDATED_ERROR = "Could not update profile";
    private static final String UNABLE_REGISTER = "Couldn't register user";
    private static final Logger LOG = LoggerFactory.getLogger(EditUserAction.class);

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        UserService userService = new UserService();
        Address address;
        Validation validation = new Validation();
        try {
            address = userService.getUserAddress((User) req.getSession().getAttribute(UserConstants.LOGGED_USER));
            if (validation.checkUserParam(req, false, true)) {
                req.setAttribute(UserConstants.ADDRESS, address);
                return new ActionResult(PageConstants.EDIT_USER_ADDRESS);
            }
        } catch (ServiceException e) {
            LOG.info(UNABLE_REGISTER, e);
            throw new ActionException(UNABLE_REGISTER, e);
        }
        try {
            UserUtil.fillAddress(req, (User) req.getSession().getAttribute(UserConstants.LOGGED_USER));
            return new ActionResult(PageConstants.USER_PROFILE_REDIRECT, true);
        } catch (ServiceException e) {
            LOG.info(UPDATED_ERROR, e);
            throw new ActionException(UPDATED_ERROR, e);
        }
    }

}

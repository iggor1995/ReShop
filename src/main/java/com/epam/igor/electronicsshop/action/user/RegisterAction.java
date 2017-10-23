package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.service.ServiceException;
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

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Validation validation = new Validation();
        try {
                if (validation.checkUserParam(req, true, true)) {
                    return new ActionResult(UserConstants.REGISTER, true);
            }
        } catch (ServiceException e) {
            LOG.info(UNABLE_REGISTER, e);
            throw new ActionException(UNABLE_REGISTER, e);
        }
        UserUtil.fillUser(req);
        return new ActionResult(UserConstants.HOME_PAGE, true);
    }


}

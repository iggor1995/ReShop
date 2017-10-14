package com.epam.igor.electronicsshop.action.util;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.UserConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * For deleting logged user from session
 *
 * @author Igor Lapin
 */
public class LogoutAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(LogoutAction.class);
    private static final String WELCOME_PAGE = "welcome";
    private static final String LOGOUT = "{} logged out";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info(LOGOUT, req.getSession(false).getAttribute(UserConstants.LOGGED_USER));
        req.getSession(false).removeAttribute(UserConstants.LOGGED_USER);
        return new ActionResult(WELCOME_PAGE, true);
    }
}

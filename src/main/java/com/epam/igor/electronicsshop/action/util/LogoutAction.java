package com.epam.igor.electronicsshop.action.util;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 09.09.2017.
 */
public class LogoutAction implements Action {
    public static final Logger LOG = LoggerFactory.getLogger(LogoutAction.class);
    private static final String LOGGED_USER = "loggedUser";
    private static final String WELCOME_PAGE = "welcome";
    private static final String LOGOUT = "{} logged out";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info(LOGOUT, req.getSession(false).getAttribute(LOGGED_USER));
        req.getSession(false).removeAttribute(LOGGED_USER);
        return new ActionResult(WELCOME_PAGE, true);
    }
}

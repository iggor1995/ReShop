package com.epam.igor.electronicsshop.action.common;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

/**
 * For changing locale
 */
public class SelectLocaleAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(SelectLocaleAction.class);
    private static final String LOCALE = "locale";
    private static final int MAX_AGE = 24 * 60 * 60;
    private static final String CHANGED = "{} changed language to {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) {
        String language = req.getParameter(LOCALE);
        Config.set(req.getSession(), Config.FMT_LOCALE, new Locale(language));
        Cookie cookie = new Cookie(LOCALE, language);
        cookie.setMaxAge(MAX_AGE);
        cookie.setSecure(true);
        res.addCookie(cookie);
        LOG.info(CHANGED, req.getSession(false).getAttribute(UserConstants.LOGGED_USER), language);
        return new ActionResult(req.getHeader(PageConstants.REFERER_PAGE), true);
    }
}

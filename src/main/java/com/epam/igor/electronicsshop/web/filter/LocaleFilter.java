package com.epam.igor.electronicsshop.web.filter;

import javax.servlet.jsp.jstl.core.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Class-filter for work with page locale.
 *
 * @author Igor Lapin
 */

public class LocaleFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(LocaleFilter.class);
    private static final String LOCALE = "locale";
    private static final String LOCALE_ADDED_IN_SESSION_FROM_COOKIES = "{} locale added in session from cookies";
    private static final String DEFAULT_LOCALE_ADDED_IN_SESSION = "{} - default locale added in session";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //no-op
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setSecure(true);
                if (LOCALE.equals(cookie.getName())) {
                    Locale locale = new Locale(cookie.getValue());
                    req.getSession().setAttribute(LOCALE, locale);
                    Config.set(req.getSession(), Config.FMT_LOCALE, locale);
                    LOG.info(LOCALE_ADDED_IN_SESSION_FROM_COOKIES, locale);
                }
                if (req.getSession(false).getAttribute(LOCALE) == null) {
                    req.getSession().setAttribute(LOCALE, req.getLocale());
                    LOG.info(DEFAULT_LOCALE_ADDED_IN_SESSION, req.getLocale());
                }
            }
        }
        filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        //no-op
    }
}

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
 * @author Igor Lapin
 */

public class LocaleFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(LocaleFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse)servletResponse, filterChain);
    }

    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        Cookie[] cookies = req.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if("locale".equals(cookie.getName())){
                    Locale locale = new Locale(cookie.getValue());
                    req.getSession().setAttribute("locale", locale);
                    Config.set(req.getSession(), Config.FMT_LOCALE, locale);
                    LOG.info("{} locale added in session from cookies", locale);
                }
                if(req.getSession(false).getAttribute("locale") == null){
                    req.getSession().setAttribute("locale", req.getLocale());
                    LOG.info("{} - default locale added in session", req.getLocale());
                }
            }
        }
            filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}

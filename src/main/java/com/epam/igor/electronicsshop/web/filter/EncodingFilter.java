package com.epam.igor.electronicsshop.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;


/**
 * Class-filter for work with view's character encoding.
 *
 * @author Igor Lapin
 */
public class EncodingFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(EncodingFilter.class);
    private static final String ENCODING = "UTF-8";
    private static final String ENCODING_SET = "Character encoding set to UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //no-op
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        req.setCharacterEncoding(ENCODING);
        LOG.trace(ENCODING_SET);
        filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        //no-op
    }

}

package com.epam.igor.electronicsshop.web.filter;

import com.epam.igor.electronicsshop.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecurityFilter implements Filter {

    private final static Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);
    private List<String> guestAccessList;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        guestAccessList = new ArrayList<>();
        guestAccessList.add("/welcome");
        guestAccessList.add("/product");
        guestAccessList.add("/catalog");
        guestAccessList.add("/register");
        guestAccessList.add("/login");
        guestAccessList.add("/locale");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        User user = (User) request.getSession(false).getAttribute("loggedUser");
        String pathInfo = request.getPathInfo();
        if(user == null){
            if (!guestAccessList.contains(pathInfo) && !pathInfo.startsWith("/cart") || pathInfo.endsWith("buy")) {
                response.sendRedirect(request.getContextPath() + "/do/login");
                LOG.info("not logged user redirected to login page");
                return;
             }
        } else if (pathInfo.startsWith("/login")){
            response.sendError(403, "already logged in");
            return;
        }
        else if(user.getRole().equals(User.Role.user)) {
            if (pathInfo.startsWith("/manage") || pathInfo.startsWith("/refill") || pathInfo.startsWith("/add") || pathInfo.startsWith("/delete") || pathInfo.startsWith("edit")) {
                response.sendError(403, "Access denied");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}

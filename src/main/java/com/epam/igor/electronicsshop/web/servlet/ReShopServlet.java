package com.epam.igor.electronicsshop.web.servlet;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionFactory;
import com.epam.igor.electronicsshop.action.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class handles and does necessary work with all request and response except those which connected
 * with images
 *
 * @author Igor Lapin
 */
@MultipartConfig(maxFileSize = 104_857_600)
public class ReShopServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ReShopServlet.class);
    private static final String NOT_FOUND = "Not found";
    private static final String ACTION_NAME = "Action name - ";
    private static final String ACTION_RESULT_VIEW_REDIRECT = "Action result view: {}. Redirect: {}";
    private static final String CANNOT_EXECUTE_ACTION = "Cannot execute action";
    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String NO_CACHE_NO_STORE_MUST_REVALIDATE = "no-cache, no-store, must-revalidate";
    private static final String NO_CACHE = "no-cache";
    private static final String PRAGMA = "Pragma";
    private static final String EXPIRES = "Expires";
    private static final String DO = "/do/";
    private static final String HTTP = "http://";
    private static final String LOCATION_FOR_REDIRECT = "Location for 'redirect' - ";
    private static final String WEB_INF_JSP = "/WEB-INF/jsp/";
    private static final String JSP = ".jsp";
    private static final String PATH_FOR_FORWARD = "Path for 'forward' - ";
    private static final String INIT_BY_KEY = "{} init by key: '{}'";

    @Override
    public void init() throws ServletException {
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionName = req.getMethod() + req.getPathInfo();
        LOG.info(ACTION_NAME + actionName);
        Action action = ActionFactory.getAction(actionName);
        if (action == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, NOT_FOUND);
            return;
        }
        LOG.debug(INIT_BY_KEY, action.getClass().getSimpleName(), actionName);
        ActionResult result;
        try {
            result = action.execute(req, resp);
            if (result == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, NOT_FOUND);
                return;
            }
            LOG.debug(ACTION_RESULT_VIEW_REDIRECT, result.getView(), result.isRedirect());
        } catch (ActionException e) {
            LOG.error(CANNOT_EXECUTE_ACTION, e);
            throw new ServletException(CANNOT_EXECUTE_ACTION, e);
        }
        resp.setHeader(CACHE_CONTROL, NO_CACHE_NO_STORE_MUST_REVALIDATE);
        resp.setHeader(PRAGMA, NO_CACHE);
        resp.setDateHeader(EXPIRES, 0);
        doForwardOrRedirect(result, req, resp);

    }

    private void doForwardOrRedirect(ActionResult result, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (result.isRedirect()) {
            String location = req.getContextPath() + DO + result.getView();
            if (result.getView().startsWith(HTTP)) {
                location = result.getView();
            }
            LOG.info(LOCATION_FOR_REDIRECT + location);
            resp.sendRedirect(location);
        } else {
            String path = String.format(WEB_INF_JSP + result.getView() + JSP);
            LOG.info(PATH_FOR_FORWARD + path);
            req.getRequestDispatcher(path).forward(req, resp);
        }
    }
}



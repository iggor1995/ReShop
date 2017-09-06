package com.epam.igor.electronicsshop.servlet;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionFactory;
import com.epam.igor.electronicsshop.action.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by User on 14.08.2017.
 */
@WebServlet(name = "ReShopServlet", urlPatterns = "/do/*")
@MultipartConfig(maxFileSize = 104_857_600)
public class ReShopServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ReShopServlet.class);

    @Override
    public void init() throws ServletException {
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionName = req.getMethod() + req.getPathInfo();
        LOG.info("Action name - " + actionName);
        Action action = ActionFactory.getAction(actionName);
        if (action == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
            return;
        }
        LOG.debug("{} init by key: '{}'", action.getClass().getSimpleName(), actionName);
        ActionResult result;
        try {
            result = action.execute(req, resp);
            if (result == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
                return;
            }
            LOG.debug("Action result view: {}. Redirect: {}", result.getView(), result.isRedirect());
        } catch (ActionException e) {
            LOG.error("Cannot execute action", e);
            throw new ServletException("Cannot execute action", e);
        }
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
        doForwardOrRedirect(result, req, resp);

    }

    private void doForwardOrRedirect(ActionResult result, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (result.isRedirect()) {
            String location = req.getContextPath() + "/do/" + result.getView();
            if (result.getView().startsWith("http://")) {
                location = result.getView();
            }
            LOG.info("Location for 'redirect' - " + location);
            resp.sendRedirect(location);
        } else {
            String path = String.format("/WEB-INF/jsp/" + result.getView() + ".jsp");
            LOG.info("Path for 'forward' - " + path);
            req.getRequestDispatcher(path).forward(req, resp);
        }
    }
}


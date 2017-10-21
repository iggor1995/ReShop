package com.epam.igor.electronicsshop.action.common;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class sets necessary attributes for displaying refill page
 *
 * @author Igor Lapin
 */

public class ShowRefillPageAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(ShowRefillPageAction.class);
    private static final String ERROR = "Couldn't show user  profile page";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            String id = req.getParameter(UserConstants.ID);
            UserService userService = new UserService();
            User user = userService.getFilledUserById(Integer.valueOf(id));
            req.setAttribute(UserConstants.USER, user);
        } catch (ServiceException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR);
        }
        return new ActionResult(PageConstants.REFILL);
    }
}

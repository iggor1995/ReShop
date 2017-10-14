package com.epam.igor.electronicsshop.action.util;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.Address;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class sets necessary attributes for displaying edit address page
 *
 * @author Igor Lapin
 */
public class ShowEditAddressAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(ShowEditAddressAction.class);
    private static final String ADDRESS = "address";
    private static final String ADDRESS_INFO = "{} - address";
    private static final String ERROR = "Couldn't show edit user page";
    private static final String EDIT_USER_ADDRESS_PAGE = "edit-user-address";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            UserService userService = new UserService();
            Address address = userService.getUserAddress((User) req.getSession().getAttribute(UserConstants.LOGGED_USER));
            LOG.info(ADDRESS_INFO, address);
            req.setAttribute(ADDRESS, address);
        } catch (ServiceException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR, e);
        }
        return new ActionResult(EDIT_USER_ADDRESS_PAGE);
    }
}

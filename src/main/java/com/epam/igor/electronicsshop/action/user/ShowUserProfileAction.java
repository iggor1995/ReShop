package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.entity.Address;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class sets necessary attributes for displaying user profile page
 * @author Igor Lapin
 */

public class ShowUserProfileAction implements Action {

    private static final String LOGGED_USER = "loggedUser";
    private static final String ADDRESS = "address";
    private static final String USER_PROFILE_PAGE = "user-profile";
    private static final String ERROR = "Couldn't show user profile page";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Address address;
        User user = (User)req.getSession().getAttribute(LOGGED_USER);
        try {
            UserService userService = new UserService();
            address = userService.getUserAddress(user);
            user.setGender(userService.getUserGender(user));
            req.setAttribute(ADDRESS, address);
        } catch (ServiceException e) {
            throw new ActionException(ERROR);
        }
        return new ActionResult(USER_PROFILE_PAGE);
    }
}

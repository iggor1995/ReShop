package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.Gender;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import com.epam.igor.electronicsshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Class sets necessary attributes for displaying edit user data page
 *
 * @author Igor Lapin
 */
public class ShowEditUserDataAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(ShowEditUserDataAction.class);
    private static final String USER = "user";
    private static final String USER_INFO = "{} - user";
    private static final String EDIT_USER_DATA_PAGE = "edit-user-data";
    private static final String ERROR = "Couldn't show edit user page";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            UserService userService = new UserService();
            User currentUser = (User) req.getSession().getAttribute(UserConstants.LOGGED_USER);
            User user = userService.getFilledUserById(currentUser.getId());
            ShopService shopService = new ShopService();
            List<Gender> genders = shopService.getAllGenders();
            LOG.info(USER_INFO, user);
            req.setAttribute(USER, user);
            req.getSession(false).setAttribute(UserConstants.GENDERS, genders);
        } catch (ServiceException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR, e);
        }
        return new ActionResult(EDIT_USER_DATA_PAGE);
    }
}

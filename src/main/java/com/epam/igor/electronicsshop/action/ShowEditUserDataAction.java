package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.Address;
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
 * Created by User on 09.09.2017.
 */
public class ShowEditUserDataAction implements Action {

    private final static Logger LOG = LoggerFactory.getLogger(ShowEditUserDataAction.class);
    private static final String LOGGED_USER = "loggedUser";
    private static final String GENDERS = "genders";
    private static final String USER = "user";
    private static final String USER_INFO = "{} - user";
    private static final String EDIT_USER_DATA_PAGE = "edit-user-data-page";
    private static final String ERROR = "Couldn't show edit user page";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            UserService userService = new UserService();
            User currentUser = (User)req.getSession().getAttribute(LOGGED_USER);
            User user = userService.getFilledUserById(currentUser.getId());
            ShopService shopService = new ShopService();
            List<Gender> genders = shopService.getAllGenders();
            LOG.info(USER_INFO, user);
            req.setAttribute(USER, user);
            req.getSession(false).setAttribute(GENDERS, genders);
        } catch (ServiceException e) {
            throw new ActionException(ERROR, e);
        }
        return new ActionResult(EDIT_USER_DATA_PAGE);
    }
}
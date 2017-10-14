package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
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
 * Class sets necessary attributes for displaying edit user page
 *
 * @author Igor Lapin
 */
public class ShowEditUserPageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowEditUserPageAction.class);
    private static final String ID = "id";
    private static final String USER = "user";
    private static final String USER_INFO = "{} - user";
    private static final String ADDRESS = "address";
    private static final String GENDERS = "genders";
    private static final String ERROR = "Couldn't show edit user page";
    private static final String EDIT_USER_PAGE = "edit-user";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            UserService userService = new UserService();
            User user = userService.getFilledUserById(Integer.valueOf(req.getParameter(ID)));
            Address address = userService.getUserAddress(user);
            ShopService shopService = new ShopService();
            List<Gender> genders = shopService.getAllGenders();
            LOG.info(USER_INFO, user);
            req.setAttribute(USER, user);
            req.setAttribute(ADDRESS, address);
            req.getSession(false).setAttribute(GENDERS, genders);
        } catch (ServiceException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR, e);
        }
        return new ActionResult(EDIT_USER_PAGE);
    }
}

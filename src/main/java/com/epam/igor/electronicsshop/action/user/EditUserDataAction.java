package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.*;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;
import com.epam.igor.electronicsshop.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.epam.igor.electronicsshop.constants.UserConstants.FIRST_NAME;

/**
 * For saving user's data changes to database
 * @author Igor Lapin
 */
public class EditUserDataAction implements Action {
    private static final String UPDATED_ERROR = "Could not update profile";
    private static final Logger LOG = LoggerFactory.getLogger(EditUserAction.class);

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            UserService userService = new UserService();
            User user;
            UserUtil userUtil = new UserUtil();
            Validation validation = new Validation();
            user = userService.getFilledUserById(Integer.valueOf(req.getParameter(UserConstants.USER_ID)));
            if(validation.validateUser(req)){
                req.setAttribute(UserConstants.USER, user);
                return new ActionResult(PageConstants.EDIT_USER_DATA);
            }
            userUtil.fillUser(req, user);
            req.getSession().setAttribute(UserConstants.LOGGED_USER, user);
            String path = PageConstants.USER_PROFILE_REDIRECT;
            return new ActionResult(path, true);
        } catch (ServiceException e) {
            LOG.info(UPDATED_ERROR, e);
            throw new ActionException(UPDATED_ERROR, e);
        }
    }

}

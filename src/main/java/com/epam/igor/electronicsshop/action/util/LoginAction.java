package com.epam.igor.electronicsshop.action.util;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * For checking logging user parameters
 * @author Igor Lapin
 */
public class LoginAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(LoginAction.class);
    private static final String EMAIL = "email";
    private static final String PASS_WORD = "password";
    private static final String LOGGED_USER = "loggedUser";
    private static final String HOME_PAGE = "home";
    private static final String LOGIN_PAGE = "login";
    private static final String SERVICE_ERROR = "Couldn't login";
    private static final String LOGIN_ERROR = "loginError";
    private static final String LOGGED = "{} logged";
    private static final String INPUT_ERROR = "wrong email {} or password {}";
    private static final String INVALID_DATA = "Invalid login or password";


    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        String email = req.getParameter(EMAIL);
        String password = req.getParameter(PASS_WORD);
        String md5HexPassword = DigestUtils.md5Hex(password);
        User user;

        try {
            UserService userService = new UserService();
            user = userService.performUserLogin(email, md5HexPassword);
        } catch (ServiceException e) {
            throw new ActionException(SERVICE_ERROR, e);
        }
        if(user != null){
            req.getSession().setAttribute(LOGGED_USER, user);
            LOG.info(LOGGED, user);
            return new ActionResult(HOME_PAGE, true);
        }
        else{
            LOG.info(INPUT_ERROR, email, password);
            req.setAttribute(LOGIN_ERROR, INVALID_DATA);
            return new ActionResult(LOGIN_PAGE);
        }
    }
}

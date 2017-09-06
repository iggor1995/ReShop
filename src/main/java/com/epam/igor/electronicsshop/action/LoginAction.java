package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 25.08.2017.
 */
public class LoginAction implements Action {

    private final static Logger LOG = LoggerFactory.getLogger(LoginAction.class);
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user;

        try {
            UserService userService = new UserService();
            user = userService.performUserLogin(email, password);
        } catch (ServiceException e) {
            throw new ActionException("Couldn't login", e);
        }
        if(user != null){
            req.getSession().setAttribute("loggedUser", user);
            LOG.info("{} logged", user);
            return new ActionResult("home", true);
        }
        else{
            LOG.info("wrong email {} or password {]", email, password);
            req.setAttribute("loginError", "Invalid login or password");
            return new ActionResult("login");
        }
    }
}

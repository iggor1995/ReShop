package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.Address;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 10.09.2017.
 */
public class ShowRefillPageAction implements Action {

    private static final String ID = "id";
    private static final String USER = "user";
    private static final String REFILL_PAGE = "refill";
    private static final String ERROR = "Couldn't show user  profile page";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            String id = req.getParameter(ID);
            UserService userService = new UserService();
            User user = userService.getFilledUserById(Integer.valueOf(id));
            req.setAttribute(USER, user);
        } catch (ServiceException e) {
            throw new ActionException(ERROR);
        }
        return new ActionResult(REFILL_PAGE);
    }
}

package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.Address;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 26.08.2017.
 */
public class ShowUserProfileAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Address address;
        User user = (User)req.getSession().getAttribute("loggedUser");
        try {
            UserService userService = new UserService();
            address = userService.getUserAddress(user);
            user.setGender(userService.getUserGender(user));
            req.setAttribute("address", address);
        } catch (ServiceException e) {
            throw new ActionException("Couldn't show user  profile page");
        }
        return new ActionResult("user-profile");
    }
}

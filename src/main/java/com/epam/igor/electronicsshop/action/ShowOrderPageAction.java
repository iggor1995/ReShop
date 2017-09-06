package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.Order;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import com.epam.igor.electronicsshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 03.09.2017.
 */
public class ShowOrderPageAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        String orderId = req.getParameter("id");
        Order order;
        try {
            ShopService shopService = new ShopService();
            order = shopService.getOrder(Integer.parseInt(orderId));
            UserService userService = new UserService();
            User user = userService.getUserById(order.getUser().getId());
            req.setAttribute("order", order);
            req.setAttribute("user", user);
            return new ActionResult("order");
        } catch (ServiceException e) {
            throw new ActionException("couldn't show order page");
        }

    }
}

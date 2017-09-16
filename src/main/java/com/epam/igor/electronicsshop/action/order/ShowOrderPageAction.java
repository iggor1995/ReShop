package com.epam.igor.electronicsshop.action.order;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
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
 * Class sets necessary attributes for displaying order page
 * @author Igor Lapin
 * */

public class ShowOrderPageAction implements Action {
    private static final String ID = "id";
    private static final String ORDER = "order";
    private static final String USER = "user";
    private static final String ERROR = "Couldn't show order page";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        String orderId = req.getParameter(ID);
        Order order;
        try {
            ShopService shopService = new ShopService();
            order = shopService.getOrder(Integer.parseInt(orderId));
            UserService userService = new UserService();
            User user = userService.getFilledUserById(order.getUser().getId());
            req.setAttribute(ORDER, order);
            req.setAttribute(USER, user);
            return new ActionResult(ORDER);
        } catch (ServiceException e) {
            throw new ActionException(ERROR);
        }

    }
}

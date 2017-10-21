package com.epam.igor.electronicsshop.action.order;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.OrderConstants;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.Order;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Class sets necessary attributes for displaying user page
 *
 * @author Igor Lapin
 */
public class ShowUserOrdersAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowUserOrdersAction.class);
    private static final String ERROR = "Couldn't get orders";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        User user = (User) req.getSession().getAttribute(UserConstants.LOGGED_USER);
        List<Order> orders;
        String page = req.getParameter(PageConstants.PAGE);
        if (page == null) {
            page = PageConstants.FIRST_PAGE;
        }
        String pageSize = req.getParameter(PageConstants.PAGE_SIZE);
        if (pageSize == null) {
            pageSize = PageConstants.DEFAULT_SIZE;
        }
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        List<Order> ordersOnPage;
        try {
            UserService userService = new UserService();
            orders = userService.getUserOrders(user.getId());
            if (orders.size() < pageInt * pageSizeInt) {
                ordersOnPage = orders.subList(((pageInt - 1) * pageSizeInt), orders.size());
            } else {
                ordersOnPage = orders.subList(((pageInt - 1) * pageSizeInt), pageSizeInt * pageInt);
            }
        } catch (ServiceException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR, e);
        }
        int pageCount;
        if (orders.size() % pageSizeInt == 0) {
            pageCount = orders.size() / pageSizeInt;
        } else {
            pageCount = orders.size() / pageSizeInt + 1;
        }
        req.setAttribute(PageConstants.PAGE, page);
        req.setAttribute(PageConstants.PAGE_SIZE, pageSize);
        req.setAttribute(PageConstants.PAGES_COUNT, pageCount);
        req.setAttribute(OrderConstants.ORDERS, ordersOnPage);
        LOG.info(PageConstants.INFO, page, pageSize, pageCount);
        String path = PageConstants.USER_ORDERS;
        return new ActionResult(path);
    }
}

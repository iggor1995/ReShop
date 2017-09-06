package com.epam.igor.electronicsshop.action;

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
 * Created by User on 04.09.2017.
 */
public class ShowUserOrdersAction implements Action {
    private final static Logger LOG = LoggerFactory.getLogger(ShowUserOrdersAction.class);
    private final String FIRST_PAGE = "1";
    private final String DEFAULT_SIZE = "3";
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        User user = (User)req.getSession().getAttribute("loggedUser");
        List<Order> orders;
        String page = req.getParameter("page");
        if (page == null) {
            page = FIRST_PAGE;
        }
        String pageSize = req.getParameter("pageSize");
        if(pageSize == null){
            pageSize = DEFAULT_SIZE;
        }
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        List<Order> oredersOnPage;
        try {
            UserService userService = new UserService();
            orders = userService.getUserOrders(user.getId());
            if(orders.size() < pageInt * pageSizeInt){
                oredersOnPage = orders.subList(((pageInt - 1) * pageSizeInt), orders.size());
            }
            else {
                oredersOnPage = orders.subList(((pageInt - 1) * pageSizeInt), pageSizeInt * pageInt);
            }
        } catch (ServiceException e) {
            throw new ActionException("Couldn't get orders", e);
        }
        int pageCount;
        if(orders.size() % pageSizeInt == 0){
            pageCount = orders.size() / pageSizeInt;
        }
        else {
            pageCount = orders.size() / pageSizeInt + 1;
        }
        req.setAttribute("page", page);
        req.setAttribute("pageSize", pageSize);
        req.setAttribute("pageCount", pageCount);
        req.setAttribute("orders", oredersOnPage);
        LOG.info("Page number - {}. Page size - {}. Pages count - {}", page, pageSize, pageCount);
        return new ActionResult("user-orders");
    }
}

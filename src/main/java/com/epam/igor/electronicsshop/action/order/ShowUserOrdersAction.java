package com.epam.igor.electronicsshop.action.order;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
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
    private static final String FIRST_PAGE = "1";
    private static final String DEFAULT_SIZE = "2";
    private static final String LOGGED_USER = "loggedUser";
    private static final String ERROR = "Couldn't get orders";
    private static final String PAGE = "page";
    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGES_COUNT = "pagesCount";
    private static final String ORDERS = "orders";
    private static final String USER_ORDERS_PAGE = "user-orders";
    private static final String INFO = "Page number - {}. Page size - {}. Pages count - {}";
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        User user = (User)req.getSession().getAttribute(LOGGED_USER);
        List<Order> orders;
        String page = req.getParameter(PAGE);
        if (page == null) {
            page = FIRST_PAGE;
        }
        String pageSize = req.getParameter(PAGE_SIZE);
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
            throw new ActionException(ERROR, e);
        }
        int pageCount;
        if(orders.size() % pageSizeInt == 0){
            pageCount = orders.size() / pageSizeInt;
        }
        else {
            pageCount = orders.size() / pageSizeInt + 1;
        }
        req.setAttribute(PAGE, page);
        req.setAttribute(PAGE_SIZE, pageSize);
        req.setAttribute(PAGES_COUNT, pageCount);
        req.setAttribute(ORDERS, oredersOnPage);
        LOG.info(INFO, page, pageSize, pageCount);
        return new ActionResult(USER_ORDERS_PAGE);
    }
}

package com.epam.igor.electronicsshop.action.order;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.entity.Order;
import com.epam.igor.electronicsshop.entity.OrderStatus;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by User on 06.09.2017.
 */
public class ShowManageOrdersPageAction implements Action {
    private final static Logger LOG = LoggerFactory.getLogger(ShowManageOrdersPageAction.class);
    private final static String DEFAULT_SIZE = "2";
    private final static String FIRST_PAGE = "1";
    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGE = "page";
    private static final String ORDERS = "orders";
    private static final String STATUSES = "statuses";
    private static final String PAGES_COUNT = "pagesCount";
    private static final String MANAGE_ORDERS_PAGE = "manage-orders";
    private static final String PRODUCT_TYPES = "productTypes";
    private static final String ERROR = "Couldn't show manage orders page";
    private static final String INFO = "Page number: {}. Page size: {}. Pages count: {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        String page = req.getParameter(PAGE);
        if(page == null){
            page= FIRST_PAGE;
        }
        String pageSize = req.getParameter(PAGE_SIZE);
        if(pageSize == null){
            pageSize = DEFAULT_SIZE;
        }
        List<Order> orders;
        List<OrderStatus> orderStatuses;
        int ordersCount;
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        try {
            ShopService shopService = new ShopService();
            orders = shopService.getAllOrdersOnPage(pageSizeInt, pageInt);
            ordersCount = shopService.getOrdersCount();
            orderStatuses = shopService.getAllOrderStatuses();
        } catch (ServiceException e) {
            throw new ActionException(ERROR);
        }
        int pageCount;
        if(ordersCount % pageSizeInt == 0){
            pageCount = ordersCount / pageSizeInt;
        }
        else {
            pageCount = ordersCount / pageSizeInt + 1;
        }
        req.setAttribute(ORDERS, orders);
        req.setAttribute(STATUSES, orderStatuses);
        req.setAttribute(PAGES_COUNT, pageCount);
        req.setAttribute(PAGE_SIZE, pageSize);
        req.setAttribute(PAGE, page);
        LOG.info(INFO, page, pageSize, pageCount);
        return new ActionResult(MANAGE_ORDERS_PAGE);
    }
}

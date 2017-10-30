package com.epam.igor.electronicsshop.action.order;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.OrderConstants;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.entity.Order;
import com.epam.igor.electronicsshop.entity.OrderStatus;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import com.epam.igor.electronicsshop.util.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Class sets necessary attributes for displaying manage orders page
 *
 * @author Igor Lapin
 */
public class ShowManageOrdersPageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowManageOrdersPageAction.class);
    private static final String ERROR = "Couldn't show manage orders page";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        PageUtil pageUtil = new PageUtil();
        String page = pageUtil.getPage(req);
        String pageSize = pageUtil.getPageSize(req);
        List<Order> orders;
        List<OrderStatus> orderStatuses;
        int ordersCount;
        try {
            ShopService shopService = new ShopService();
            orders = shopService.getAllOrdersOnPage(Integer.parseInt(pageSize), Integer.parseInt(page));
            ordersCount = shopService.getOrdersCount();
            orderStatuses = shopService.getAllOrderStatuses();
        } catch (ServiceException e) {
            LOG.info(ERROR, e);
            throw new ActionException(ERROR);
        }
        int pageCount = pageUtil.getPageCount(ordersCount, pageSize);
        req.setAttribute(OrderConstants.ORDERS, orders);
        req.setAttribute(OrderConstants.STATUSES, orderStatuses);
        req.setAttribute(PageConstants.PAGES_COUNT, pageCount);
        req.setAttribute(PageConstants.PAGE_SIZE, pageSize);
        req.setAttribute(PageConstants.PAGE, page);
        LOG.info(PageConstants.INFO, page, pageSize, pageCount);
        return new ActionResult(PageConstants.MANAGE_ORDERS);
    }
}

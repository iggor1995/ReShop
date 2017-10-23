package com.epam.igor.electronicsshop.action.order;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.OrderConstants;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * For deleting item from order
 *
 * @author Igor Lapin
 */
public class DeleteOrderingItemAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteOrderingItemAction.class);
    private static final String ITEM_DELETED = "item - {} deleted from cart";
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Order order = (Order) req.getSession().getAttribute(PageConstants.CART);
        String rowNumber = req.getParameter(OrderConstants.ITEM);
        int rowNumberInt = Integer.parseInt(rowNumber);
        LOG.info(ITEM_DELETED, order.getOrderingItems().get(rowNumberInt));
        order.getOrderingItems().remove(rowNumberInt);
        req.getSession().setAttribute(PageConstants.CART, order);
        return new ActionResult(req.getHeader(PageConstants.REFERER_PAGE), true);
    }

}

package com.epam.igor.electronicsshop.action.order;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * For deleting item from order
 * @author Igor Lapin
 */
public class DeleteOrderingItemAction implements Action {

    private final static Logger LOG = LoggerFactory.getLogger(DeleteOrderingItemAction.class);
    private static final String CART = "cart";
    private static final String ITEM = "item";
    private static final String REFERER_PAGE = "referer";
    private static final String ITEM_DELETED = "item - {} deleted from cart";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        Order order = (Order)req.getSession().getAttribute(CART);
        String rowNumber = req.getParameter(ITEM);
        int rowNumberInt = Integer.parseInt(rowNumber);
        order.getOrderingItems().remove(rowNumberInt);
        req.getSession().setAttribute(CART, order);
        LOG.info(ITEM_DELETED, order.getOrderingItems().get(rowNumberInt));
        return new ActionResult(req.getHeader(REFERER_PAGE), true);
    }
}
package com.epam.igor.electronicsshop.action.order;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.constants.OrderConstants;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * For saving order status changes in database
 *
 * @author Igor Lapin
 */
public class EditOrderStatusAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(EditOrderStatusAction.class);
    private static final String EDIT_ERROR = "Couldn't edit order status";
    private static final String UPDATED = "{} order has been upadted - status: {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        ShopService shopService = new ShopService();
        try {
            String statusId = req.getParameter(OrderConstants.STATUS_ID);
            String orderId = req.getParameter(OrderConstants.ORDER_ID);
            shopService.updateOrderStatus(orderId, statusId);
            LOG.info(UPDATED, orderId, statusId);
        } catch (ServiceException e) {
            LOG.info(EDIT_ERROR, e);
            throw new ActionException(EDIT_ERROR, e);
        }
        return new ActionResult(req.getHeader(PageConstants.REFERER_PAGE), true);
    }
}

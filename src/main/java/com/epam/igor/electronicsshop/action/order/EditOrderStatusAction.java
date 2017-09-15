package com.epam.igor.electronicsshop.action.order;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 07.09.2017.
 */
public class EditOrderStatusAction implements Action {
    private final static Logger LOG = LoggerFactory.getLogger(EditOrderStatusAction.class);
    private static final String PARAMETER_STATUS_ID = "statusId";
    private static final String PARAMETER_ORDER_ID = "orderId";
    private static final String EDIT_ERROR = "Couldn't edit order status";
    private static final String REFERER_PAGE = "referer";
    private static final String UPDATED = "{} order has been upadted - status: {}";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        ShopService shopService = new ShopService();
        try{
            String statusId = req.getParameter(PARAMETER_STATUS_ID);
            String orderId = req.getParameter(PARAMETER_ORDER_ID);
            shopService.updateOrderStatus(orderId, statusId);
            LOG.info(UPDATED, orderId, statusId);
        } catch (ServiceException e) {
            throw new ActionException(EDIT_ERROR, e);
        }
        return new ActionResult(req.getHeader(REFERER_PAGE), true);
    }
}

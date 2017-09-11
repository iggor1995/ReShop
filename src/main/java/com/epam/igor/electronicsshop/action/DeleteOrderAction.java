package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 08.09.2017.
 */
public class DeleteOrderAction implements Action {

    private final static Logger LOG = LoggerFactory.getLogger(DeleteOrderAction.class);
    private static final String PARAMETER_ID = "id";
    private static final String ERROR_DELETE = "Couldn't delete order by id";
    private static final String REFERER_PAGE = "referer";
    private static final String ORDER_DELETED = "order - {} has been deleted";

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        String id = req.getParameter(PARAMETER_ID);
        try {
            ShopService shopService = new ShopService();
            shopService.deleteOrderById(id);
        } catch (ServiceException e) {
            throw new ActionException(ERROR_DELETE);
        }
        LOG.info(ORDER_DELETED, id);
        return new ActionResult(req.getHeader(REFERER_PAGE), true);
    }
}

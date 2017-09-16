package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * For deleting user
 * @author Igor Lapin
 * */
public class DeleteUserAction implements Action {

    private final static Logger LOG = LoggerFactory.getLogger(DeleteUserAction.class);
    private static final String LOGGED_USER = "loggedUser";
    private static final String REFERER_PAGE = "referer";
    private static final String PARAMETER_ID = "id";
    private static final String SELF_DELETE_ERROR = "user.deleteError";
    private static final String DELETE_ERROR = "Couldn't delete user by id";
    private static final String DELETE_LOG_ERROR = "{} tried to delete himself when logged in";
    private static final String LOG_DELETED = "{} - has been deleted";


    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        User user = (User) req.getSession().getAttribute(LOGGED_USER);
        String id = req.getParameter(PARAMETER_ID);
        if(id == String.valueOf(user.getId())){
            req.setAttribute(DELETE_ERROR, "true");
            LOG.info(DELETE_LOG_ERROR, user);
            return new ActionResult(req.getHeader(REFERER_PAGE));
        }
        try{
            ShopService shopService = new ShopService();
            shopService.deleteUserById(id);
            LOG.info(LOG_DELETED, user);
            return new ActionResult(req.getHeader(REFERER_PAGE), true);
        } catch (ServiceException e) {
            throw new ActionException(DELETE_ERROR, e) ;
        }
    }
}

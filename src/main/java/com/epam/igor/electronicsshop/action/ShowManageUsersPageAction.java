package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.User;
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
public class ShowManageUsersPageAction implements Action {
    private final static Logger LOG = LoggerFactory.getLogger(ShowManageProductsPageAction.class);
    private final static String DEFAULT_SIZE = "3";
    private final static String FIRST_PAGE = "1";
    private static final String PAGE = "page";
    private static final String PAGES_COUNT = "pagesCount";
    private static final String PAGE_SIZE = "pageSize";
    private static final String MANAGE_USERS_PAGE = "manage-users";
    private static final String USERS = "users";
    private static final String ERROR = "Couldn't show manage users page";
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
        ShopService shopService = new ShopService();
        List<User> users;
        int usersCount;
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        try {
            users = shopService.getAllUsersOnPage(pageInt, pageSizeInt);
            usersCount = shopService.getUsersCount();
        } catch (ServiceException e) {
            throw new ActionException(ERROR);
        }
        int pageCount;
        if(usersCount % pageSizeInt == 0){
            pageCount = usersCount / pageSizeInt;
        }
        else {
            pageCount = usersCount / pageSizeInt + 1;
        }
        req.setAttribute(USERS, users);
        req.setAttribute(PAGES_COUNT, pageCount);
        req.setAttribute(PAGE_SIZE, pageSize);
        req.setAttribute(PAGE, page);
        LOG.info(INFO, page, pageSize, pageCount);
        return new ActionResult(MANAGE_USERS_PAGE);
    }
}
package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.Gender;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by User on 23.08.2017.
 */
public class ShowRegisterPageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterAction.class);

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        List<Gender> genders;
        try {
            ShopService shopService = new ShopService();
            genders = shopService.getAllGenders();
        } catch (ServiceException e) {
            throw new ActionException("genders list unavailable", e);
        }
        req.getSession(false).setAttribute("genders", genders);
        return new ActionResult("register");
    }
}

package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 10.09.2017.
 */
public class RefillUserBalanceAction implements Action {

    private final static Logger LOG = LoggerFactory.getLogger(RefillUserBalanceAction.class);
    private static final String WRONG_PARAMETER = "Parameter '{}' with value '{}' is unsuitable.";
    private static final String USER = "user";
    private static final String REFILL = "refill";
    private static final String BALANCE = "balance";
    private static final String USER_ID = "userId";
    private static final String REFILLED_BALANCE = "{} refilled balance --> +{}";
    private static final String MANAGE_USERS_PAGE = "manage/users";
    private static final String REFILL_ERROR = "Couldn't refill balance";
    private static final String PROPERTIES_ERROR = "Cannot load properties";
    private static final String MONEY_REGEX = "money.regex";
    private static final String VALIDATION_PROPERTIES = "money.regex";
    private boolean INVALID;
    private Properties properties = new Properties();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {

        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream(VALIDATION_PROPERTIES));
        } catch (IOException e) {
            throw new ActionException(PROPERTIES_ERROR, e);
        }
        String id = req.getParameter(USER_ID);
        String balance = req.getParameter(BALANCE);
        checkParameterByRegex(balance, BALANCE, properties.getProperty(MONEY_REGEX), req);

        UserService userService = new UserService();
        try {
            User user = userService.getFilledUserById(Integer.valueOf(id));
            User updatedUser;
            if (INVALID){
                INVALID = false;
                req.setAttribute(USER, user);
                return new ActionResult(REFILL);
            }
            updatedUser = userService.refillCash(user.getId(), balance);
            LOG.info(REFILLED_BALANCE, updatedUser, balance);
            return new ActionResult(MANAGE_USERS_PAGE, true);
        } catch (ServiceException e) {
            throw new ActionException(REFILL_ERROR, e);
        }
    }
    private void checkParameterByRegex(String parameter, String parameterName, String regex, HttpServletRequest req) {
        String CHECK_PARAMETER = "Check parameter '{}' with value '{}' by regex '{}'";
        LOG.debug(CHECK_PARAMETER, parameterName, parameter, regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        if (!matcher.matches()) {
            LOG.debug(WRONG_PARAMETER, parameterName, parameter);
            req.setAttribute(parameterName + "Error", "true");
            INVALID = true;
        }
    }
}

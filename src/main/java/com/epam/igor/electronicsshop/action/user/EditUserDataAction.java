package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.*;
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

public class EditUserDataAction implements Action {
    private static final String PASS_WORD_REGEX = "password.regex";
    private static final String PASS_WORD = "password";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String UNABLE_UPDATE= "Couldn't update user data";
    private static final String NOT_EMPTY_TEXT = "not_empty_string.regex";
    private static final String NOT_EMPTY_NUMBER = "not_empty_digits.regex";
    private static final String CHECK_PARAMETR = "Check parameter '{}' with value '{}' by regex '{}'";
    private static final String WRONG_PARAMETR = "Parameter '{}' with value '{}' is unsuitable.";
    private static final String GENDERS = "genders";
    private static final String LOGGED_USER = "loggedUser";
    private static final String USER_PROFILE_PAGE = "user/profile";
    private static final String UPDATED = "{} updated data to {}";
    private static final String UPDATED_ERROR = "Could not update profile";
    private static final String PROPERTIES_ERROR= "Cannot load properties";
    private static final String VALIDATION_PROPERTIES= "validation.properties";
    private static final String USER_ID = "userId";
    private static final String USER = "user";
    private static final String GENDER = "gender";
    private static final String EDIT_USER_DATA_PAGE= "edit-user-data";
    private static final Logger LOG = LoggerFactory.getLogger(EditUserAction.class);
    private boolean invalid;
    private Properties properties = new Properties();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        UserService userService = new UserService();
        User user;
        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream(VALIDATION_PROPERTIES));
        } catch (IOException e) {
            throw new ActionException(PROPERTIES_ERROR, e);
        }
        try {
            user = userService.getFilledUserById(Integer.valueOf(req.getParameter(USER_ID)));
        } catch (ServiceException e) {
            throw new ActionException(UNABLE_UPDATE, e);
        }
        String password = req.getParameter(PASS_WORD);
        String firstName = req.getParameter(FIRST_NAME);
        String lastName = req.getParameter(LAST_NAME);
        String phoneNumber = req.getParameter(PHONE_NUMBER);
        checkParameterByRegex(password, PASS_WORD, properties.getProperty(PASS_WORD_REGEX), req);
        checkParameterByRegex(firstName, FIRST_NAME, properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(lastName, LAST_NAME, properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(phoneNumber, PHONE_NUMBER, properties.getProperty(NOT_EMPTY_NUMBER), req);

        if(invalid){
            invalid = false;
            req.setAttribute(USER, user);
            return new ActionResult(EDIT_USER_DATA_PAGE);
        }
        try {
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNumber);
            user.getGender().setId(Integer.valueOf(req.getParameter(GENDER)));
            userService.updateUser(user);
            req.getSession(false).removeAttribute(GENDERS);
            LOG.info(UPDATED, req.getSession(false).getAttribute(LOGGED_USER), user);
            return new ActionResult(USER_PROFILE_PAGE, true);
        } catch (ServiceException e) {
            throw new ActionException(UPDATED_ERROR, e);
        }
    }
    private void checkParameterByRegex(String parameter, String parameterName, String regex, HttpServletRequest req) {
        LOG.debug(CHECK_PARAMETR, parameterName, parameter, regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        if (!matcher.matches()) {
            LOG.debug(WRONG_PARAMETR, parameterName, parameter);
            req.setAttribute("flash." + parameterName + "Error", "true");
            invalid = true;
        }
    }
}

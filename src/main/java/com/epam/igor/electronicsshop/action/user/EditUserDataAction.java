package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.*;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

import static com.epam.igor.electronicsshop.constants.UserConstants.FIRST_NAME;

/**
 * For saving user's data changes to database
 * @author Igor Lapin
 */
public class EditUserDataAction implements Action {
    private static final String PASS_WORD_REGEX = "password.regex";
    private static final String UNABLE_UPDATE= "Couldn't update user data";
    private static final String NOT_EMPTY_TEXT = "not_empty_string.regex";
    private static final String NOT_EMPTY_NUMBER = "not_empty_digits.regex";
    private static final String USER_PROFILE_PAGE = "user/profile";
    private static final String UPDATED = "{} updated data to {}";
    private static final String UPDATED_ERROR = "Could not update profile";
    private static final String PROPERTIES_ERROR= "Cannot load properties";
    private static final String VALIDATION_PROPERTIES= "validation.properties";
    private static final String USER_ID = "userId";
    private static final String EDIT_USER_DATA_PAGE= "edit-user-data";
    private static final Logger LOG = LoggerFactory.getLogger(EditUserAction.class);
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
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
            LOG.info(UNABLE_UPDATE, e);
            throw new ActionException(UNABLE_UPDATE, e);
        }
        checkData(req);
        String md5HexPassword = DigestUtils.md5Hex(password);
        if(invalid){
            invalid = false;
            req.setAttribute(UserConstants.USER, user);
            return new ActionResult(EDIT_USER_DATA_PAGE);
        }
        try {
            user.setPassword(md5HexPassword);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNumber);
            user.getGender().setId(Integer.valueOf(req.getParameter(UserConstants.GENDER)));
            userService.updateUser(user);
            req.getSession(false).removeAttribute(UserConstants.GENDERS);
            LOG.info(UPDATED, req.getSession(false).getAttribute(UserConstants.LOGGED_USER), user);
            return new ActionResult(USER_PROFILE_PAGE, true);
        } catch (ServiceException e) {
            LOG.info(UPDATED_ERROR, e);
            throw new ActionException(UPDATED_ERROR, e);
        }
    }
    private void checkData(HttpServletRequest req){
        Validation validation = new Validation();
        password = req.getParameter(UserConstants.PASS_WORD);
        firstName = req.getParameter(UserConstants.FIRST_NAME);
        lastName = req.getParameter(UserConstants.LAST_NAME);
        phoneNumber = req.getParameter(UserConstants.PHONE_NUMBER);
        invalid = validation.checkParameterByRegex(invalid, password, UserConstants.PASS_WORD, properties.getProperty(PASS_WORD_REGEX), req);
        invalid = validation.checkParameterByRegex(invalid,firstName, UserConstants.FIRST_NAME, properties.getProperty(NOT_EMPTY_TEXT), req);
        invalid = validation.checkParameterByRegex(invalid,lastName, UserConstants.LAST_NAME, properties.getProperty(NOT_EMPTY_TEXT), req);
        invalid = validation.checkParameterByRegex(invalid,phoneNumber, UserConstants.PHONE_NUMBER, properties.getProperty(NOT_EMPTY_NUMBER), req);

    }

}

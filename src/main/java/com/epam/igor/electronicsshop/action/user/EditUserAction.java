package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.Address;
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

/**
 * For saving user changes to database
 *
 * @author Igor Lapin
 */
public class EditUserAction implements Action {
    private static final String NOT_EMPTY_TEXT = "not_empty_string.regex";
    private static final String NOT_EMPTY_NUMBER = "not_empty_digits.regex";
    private static final String PASS_WORD_REGEX = "password.regex";
    private static final String USER_ID = "userId";
    private static final String EDIT_USER_PAGE = "edit-user";
    private static final String MANAGE_USERS_PAGE = "manage/users";
    private static final String ADDRESS = "address";
    private static final String UPDATED = "{} updated data to {}, {}";
    private static final String UPDATED_ERROR = "Could not update profile";
    private static final String EMAIL_ERROR = "emailError";
    private static final String EMAIL_TAKEN = "email {} has already been taken!";
    private static final String UNABLE_REGISTER = "Couldn't register user";
    private static final String PROPERTIES_ERROR = "Cannot load properties";
    private static final String VALIDATION_PROPERTIES = "validation.properties";
    private static final Logger LOG = LoggerFactory.getLogger(EditUserAction.class);
    private static final String TRUE = "true";
    private boolean invalid;
    private Properties properties = new Properties();
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String country;
    private String city;
    private String street;
    private String buildingNumber;
    private String apartmentNumber;
    private User user;
    private String md5HexPassword;

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {

        UserService userService = new UserService();
        email = req.getParameter(UserConstants.EMAIL);
        try {
            user = userService.getFilledUserById(Integer.valueOf(req.getParameter(USER_ID)));
            if (!userService.checkEmail(email)) {
                req.setAttribute(EMAIL_ERROR, TRUE);
                invalid = true;
                LOG.error(EMAIL_TAKEN, email);
            }
            if (checkUserParam(req)) {
                invalid = false;
                return new ActionResult(EDIT_USER_PAGE);
            }
        } catch (ServiceException e) {
            LOG.info(UNABLE_REGISTER, e);
            throw new ActionException(UNABLE_REGISTER, e);
        }

        try {
            md5HexPassword = DigestUtils.md5Hex(password);
            fillUserAndAddress(req, userService);
            return new ActionResult(MANAGE_USERS_PAGE, true);
        } catch (ServiceException e) {
            LOG.info(UPDATED_ERROR, e);
            throw new ActionException(UPDATED_ERROR, e);
        }
    }

    private boolean checkUserParam(HttpServletRequest req) throws ActionException {

        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream(VALIDATION_PROPERTIES));
        } catch (IOException e) {
            LOG.info(PROPERTIES_ERROR, e);
            throw new ActionException(PROPERTIES_ERROR, e);
        }

        Validation validation = new Validation();
        password = req.getParameter(UserConstants.PASS_WORD);
        firstName = req.getParameter(UserConstants.FIRST_NAME);
        lastName = req.getParameter(UserConstants.LAST_NAME);
        phoneNumber = req.getParameter(UserConstants.PHONE_NUMBER);
        country = req.getParameter(UserConstants.COUNTRY);
        city = req.getParameter(UserConstants.CITY);
        street = req.getParameter(UserConstants.STREET);
        buildingNumber = req.getParameter(UserConstants.BUILDING_NUMBER);
        apartmentNumber = req.getParameter(UserConstants.APARTMENT_NUMBER);

        invalid = validation.checkParameterByRegex(invalid, email, UserConstants.EMAIL,
                properties.getProperty(UserConstants.EMAIL_REGEX), req);
        invalid = validation.checkParameterByRegex(invalid, password, UserConstants.PASS_WORD,
                properties.getProperty(PASS_WORD_REGEX), req);
        invalid = validation.checkParameterByRegex(invalid, firstName, UserConstants.FIRST_NAME,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        invalid = validation.checkParameterByRegex(invalid, lastName, UserConstants.LAST_NAME,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        invalid = validation.checkParameterByRegex(invalid, phoneNumber, UserConstants.PHONE_NUMBER,
                properties.getProperty(NOT_EMPTY_NUMBER), req);
        invalid = validation.checkParameterByRegex(invalid, country, UserConstants.COUNTRY,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        invalid = validation.checkParameterByRegex(invalid, city, UserConstants.CITY,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        invalid = validation.checkParameterByRegex(invalid, street, UserConstants.STREET,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        invalid = validation.checkParameterByRegex(invalid, buildingNumber, UserConstants.BUILDING_NUMBER,
                properties.getProperty(NOT_EMPTY_NUMBER), req);
        invalid = validation.checkParameterByRegex(invalid, apartmentNumber, UserConstants.APARTMENT_NUMBER,
                properties.getProperty(NOT_EMPTY_NUMBER), req);

        if (invalid) {
            req.setAttribute(UserConstants.USER, user);
            req.setAttribute(ADDRESS, user.getAddress());
        }
        return invalid;
    }

    private void fillUserAndAddress(HttpServletRequest req, UserService userService) throws ServiceException {
        user.setEmail(email);
        user.setPassword(md5HexPassword);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.getGender().setId(Integer.valueOf(req.getParameter(UserConstants.GENDER)));
        Address userAddress = user.getAddress();
        userAddress.setCountry(country);
        userAddress.setCity(city);
        userAddress.setStreet(street);
        userAddress.setBuildingNumber(buildingNumber);
        userAddress.setApartmentNumber(apartmentNumber);
        if (!req.getParameter(UserConstants.ROLE).equals(user.getRole().name())) {
            user.setRole(User.Role.valueOf(req.getParameter(UserConstants.ROLE)));
        }
        userService.updateUserAddress(userAddress);
        userService.updateUser(user);
        req.getSession(false).removeAttribute(UserConstants.GENDERS);
        LOG.info(UPDATED, req.getSession(false).getAttribute(UserConstants.LOGGED_USER), user, userAddress);
    }

}

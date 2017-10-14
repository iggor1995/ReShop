package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.Address;
import com.epam.igor.electronicsshop.entity.Gender;
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
 * For saving new user to database
 */

public class RegisterAction implements Action {
    private static final String USED = "used";
    private static final String NOT_EMPTY_TEXT = "not_empty_string.regex";
    private static final String NOT_EMPTY_NUMBER = "not_empty_digits.regex";
    private static final String EMAIL_ERROR = "emailError";
    private static final String EMAIL_TAKEN = "email {} has already been taken!";
    private static final String UNABLE_REGISTER = "Couldn't register user";
    private static final String USER_HAS_BEEN_REGISTERED = "{} registered. Address - {}";
    private static final String ROLE = "ROLE - {}";

    private static final Logger LOG = LoggerFactory.getLogger(RegisterAction.class);
    private Properties properties = new Properties();
    private boolean invalid;
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

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        UserService userService = new UserService();
        email = req.getParameter(UserConstants.EMAIL);
        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream(UserConstants.VALIDATION_PROPERTIES));
        } catch (IOException e) {
            LOG.info(UserConstants.PROPERTIES_ERROR, e);
            throw new ActionException(UserConstants.PROPERTIES_ERROR, e);
        }

        try {
            if (!userService.checkEmail(email)) {
                req.setAttribute(EMAIL_ERROR, USED);
                LOG.error(EMAIL_TAKEN, email);
            } else {
                if (checkUserParam(req)) {
                    invalid = false;
                    return new ActionResult(UserConstants.REGISTER);
                }
            }
        } catch (ServiceException e) {
            LOG.info(UNABLE_REGISTER, e);
            throw new ActionException(UNABLE_REGISTER, e);
        }
        fillUser(req);
        return new ActionResult(UserConstants.HOME_PAGE, true);
    }

    private boolean checkUserParam(HttpServletRequest req) {

        Validation validation = new Validation();
        email = req.getParameter(UserConstants.EMAIL);
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
                properties.getProperty(UserConstants.PASS_WORD_REGEX), req);
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
            req.setAttribute(UserConstants.EMAIL, email);
            req.setAttribute(UserConstants.PASS_WORD, password);
            req.setAttribute(UserConstants.FIRST_NAME, firstName);
            req.setAttribute(UserConstants.LAST_NAME, lastName);
            req.setAttribute(UserConstants.PHONE_NUMBER, phoneNumber);
            req.setAttribute(UserConstants.COUNTRY, city);
            req.setAttribute(UserConstants.STREET, street);
            req.setAttribute(UserConstants.BUILDING_NUMBER, buildingNumber);
            req.setAttribute(UserConstants.APARTMENT_NUMBER, apartmentNumber);
            req.setAttribute(UserConstants.GENDER, req.getParameter(UserConstants.GENDER));
        }
        return invalid;
    }

    private void fillUser(HttpServletRequest req) throws ActionException {

        String md5HexPassword = DigestUtils.md5Hex(password);
        Gender gender = new Gender();
        gender.setId(Integer.valueOf(req.getParameter(UserConstants.GENDER)));
        User user = new User.UserBuilder(firstName, lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .password(md5HexPassword)
                .build();
        Address address = new Address.AddressBuilder()
                .country(country)
                .city(city)
                .street(street)
                .buildingNumber(buildingNumber)
                .apartmentNumber(apartmentNumber)
                .build();
        try {
            UserService userService = new UserService();
            User registeredUser = userService.registerUser(user, address);
            req.getSession(false).setAttribute(UserConstants.LOGGED_USER, registeredUser);
            req.getSession(false).removeAttribute(UserConstants.GENDERS);
            LOG.info(USER_HAS_BEEN_REGISTERED, registeredUser, address);
            LOG.info(ROLE, registeredUser.getRole());
        } catch (ServiceException e) {
            throw new ActionException(UNABLE_REGISTER, e);
        }
    }

}

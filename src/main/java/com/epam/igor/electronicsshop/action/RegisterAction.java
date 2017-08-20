package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.Address;
import com.epam.igor.electronicsshop.entity.Gender;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 20.08.2017.
 */
public class RegisterAction implements Action {
    private static final String NOT_EMPTY_TEXT = "not_empty_string.regex";
    private static final String NOT_EMPTY_NUMBER = "not_empty_digits.regex";
    private static final String EMAIL_REGEX = "email.regex";
    private static final String PASSWORD_REGEX = "password.regex";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "firstName";
    private static final String REGISTER = "register";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String EMAIL = "email";
    private static final String GENDER = "gender";
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String STREET = "street";
    private static final String BUILDING_NUMBER = "buildingNumber";
    private static final String APARTMENT_NUMBER = "apartmentNumber";
    private static final String EMAIL_ERROR = "emailError";
    private static final String EMAIL_TAKEN = "email {} has already been taken!";
    private static final String CHECK_PARAMETR = "Check parameter '{}' with value '{}' by regex '{}'";
    private static final String WRONG_PARAMETR = "Parameter '{}' with value '{}' is unsuitable.";
    private static final String UNABLE_REGISTER= "Couldn't register user";
    private static final String USER_HAS_BEEN_REGISTERED= "{} registered. Address - {}";
    private static final Logger LOG = LoggerFactory.getLogger(RegisterAction.class);
    private boolean invalid;
    Properties properties = new Properties();
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        UserService userService = new UserService();
        String email = req.getParameter(EMAIL);
        try {
            if(!userService.checkEmail(email)){
                req.setAttribute(EMAIL_ERROR, "used");
                invalid = true;
                LOG.error(EMAIL_TAKEN, email);
            }
            else {
                checkParameterByRegex(email, EMAIL, properties.getProperty(EMAIL_REGEX), req);
            }
        } catch (ServiceException e) {
            throw new ActionException(UNABLE_REGISTER, e);
        }
        String password = req.getParameter(PASSWORD);
        String firstName = req.getParameter(FIRST_NAME);
        String lastName = req.getParameter(LAST_NAME);
        String phoneNumber = req.getParameter(PHONE_NUMBER);
        String country = req.getParameter(COUNTRY);
        String city = req.getParameter(CITY);
        String street = req.getParameter(STREET);
        String buildingNumber = req.getParameter(BUILDING_NUMBER);
        String apartmentNumber = req.getParameter(APARTMENT_NUMBER);
        checkParameterByRegex(password, PASSWORD, properties.getProperty(PASSWORD_REGEX), req);
        checkParameterByRegex(firstName, FIRST_NAME, properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(lastName, LAST_NAME, properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(phoneNumber, PHONE_NUMBER, properties.getProperty(NOT_EMPTY_NUMBER), req);
        checkParameterByRegex(country, COUNTRY, NOT_EMPTY_TEXT, req);
        checkParameterByRegex(city, CITY, NOT_EMPTY_TEXT, req);
        checkParameterByRegex(street, STREET, NOT_EMPTY_TEXT, req);
        checkParameterByRegex(buildingNumber, BUILDING_NUMBER, NOT_EMPTY_NUMBER, req);
        checkParameterByRegex(apartmentNumber, APARTMENT_NUMBER, NOT_EMPTY_NUMBER, req);

        if(invalid){
            invalid = false;
            req.setAttribute(EMAIL, email);
            req.setAttribute(PASSWORD, password);
            req.setAttribute(FIRST_NAME, false);
            req.setAttribute(LAST_NAME, lastName);
            req.setAttribute(PHONE_NUMBER, phoneNumber);
            req.setAttribute(COUNTRY, city);
            req.setAttribute(STREET, street);
            req.setAttribute(BUILDING_NUMBER, buildingNumber);
            req.setAttribute(APARTMENT_NUMBER, apartmentNumber);
            req.setAttribute(GENDER, req.getParameter(GENDER));
            return new ActionResult(REGISTER);
        }
        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        Address address = new Address();
        address.setCountry(country);
        address.setStreet(street);
        address.setBuildingNumber(Integer.parseInt(buildingNumber));
        address.setApartmentNumber(Integer.parseInt(apartmentNumber));
        user.setAddress(address);
        Gender gender = new Gender();
        gender.setId(Integer.valueOf(req.getParameter("gender_id")));
        user.setGender(gender);

        try {
            User registeredUser = userService.registerUser(user, address);
            req.getSession(false).setAttribute("loggedUser", registeredUser);
            req.getSession(false).removeAttribute("genders");
            LOG.info(USER_HAS_BEEN_REGISTERED, registeredUser, address);
        } catch (ServiceException e) {
            throw new ActionException(UNABLE_REGISTER, e);
        }
        return new ActionResult("home", true);
    }
    private void checkParameterByRegex(String parameter, String parameterName, String regex, HttpServletRequest req) {
        LOG.debug(CHECK_PARAMETR, parameterName, parameter, regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        if (!matcher.matches()) {
            LOG.debug(WRONG_PARAMETR, parameterName, parameter);
            req.setAttribute(parameterName + "Error", "true");
            invalid = true;
        }
    }
}

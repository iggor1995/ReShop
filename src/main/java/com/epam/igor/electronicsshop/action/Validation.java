package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.action.user.RegisterAction;
import com.epam.igor.electronicsshop.constants.OrderConstants;
import com.epam.igor.electronicsshop.constants.UserConstants;
import com.epam.igor.electronicsshop.entity.User;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.epam.igor.electronicsshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private static final Logger LOG = LoggerFactory.getLogger(Validation.class);
    private static final String CHECK_PARAMETER = "Check parameter '{}' with value '{}' by regex '{}'";
    private static final String WRONG_PARAMETER = "Parameter '{}' with value '{}' is unsuitable.";
    private static final String ERROR = "Error";
    private static final String TRUE = "true";
    private static final String NOT_EMPTY_TEXT = "not_empty_string.regex";
    private static final String NOT_EMPTY_NUMBER = "not_empty_digits.regex";
    private static final String EMAIL_ERROR = "emailError";
    private static final String EMAIL_TAKEN = "email {} has already been taken!";
    private static final String USED = "used";
    private static final String MONEY = "money";
    private static final String MONEY_REGEX = "money.regex";
    private static final String PROPERTY_PRODUCT_AMOUNT = "product.amount";
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


    public boolean checkParameterByRegex(String parameter, String parameterName, String regex, HttpServletRequest req) {
        LOG.debug(CHECK_PARAMETER, parameterName, parameter, regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        if (!matcher.matches()) {
            LOG.debug(WRONG_PARAMETER, parameterName, parameter);
            req.setAttribute(parameterName + ERROR, TRUE);
            invalid = true;
            return true;
        }
        return false;
    }

    public boolean checkMoney(HttpServletRequest req, String price) throws ActionException {
        loadProperties();
        return checkParameterByRegex(price, MONEY, properties.getProperty(MONEY_REGEX), req);
    }

    public boolean checkAmount(HttpServletRequest req, String amount) throws ActionException {
        loadProperties();
        return checkParameterByRegex(amount, OrderConstants.AMOUNT,
                properties.getProperty(PROPERTY_PRODUCT_AMOUNT), req);
    }
    private void loadProperties() throws ActionException {
        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream(UserConstants.VALIDATION_PROPERTIES));
        } catch (IOException e) {
            LOG.info(UserConstants.PROPERTIES_ERROR, e);
            throw new ActionException(UserConstants.PROPERTIES_ERROR, e);
        }
    }

    public boolean validateUserAddress(HttpServletRequest req) throws ActionException {
            loadProperties();
            setAddressData(req);
            checkAddressData(req);
            setAddressErrorsAttributes(req);
        return invalid;
    }

    public boolean fullCheck(HttpServletRequest req) throws ServiceException, ActionException {
        loadProperties();
        emailCheck(req);
        emailExistsCheck(req);
        validateUser(req);
        validateUserAddress(req);
        return invalid;
    }

    public boolean userAndEmailCheck(HttpServletRequest req) throws ActionException, ServiceException {
        validateUser(req);
        emailCheck(req);
        emailExistsCheck(req);
        return invalid;
    }

    public boolean validateUser(HttpServletRequest req) throws ActionException {
        loadProperties();
        setProfileData(req);
        checkProfileData(req);
        setProfileErrorsAttributes(req);
        return invalid;
    }

    private boolean emailExistsCheck(HttpServletRequest req) throws ServiceException {

            UserService userService = new UserService();
            if (!userService.checkEmail(email)) {
                req.setAttribute(EMAIL_ERROR, USED);
                LOG.error(EMAIL_TAKEN, email);
                invalid = true;
            }
        return invalid;
    }

    private boolean emailCheck(HttpServletRequest req) throws ActionException {
        loadProperties();
        email = req.getParameter(UserConstants.EMAIL);
            checkParameterByRegex(email, UserConstants.EMAIL,
                    properties.getProperty(UserConstants.EMAIL_REGEX), req);
            if(invalid){
                req.setAttribute(EMAIL_ERROR, TRUE);
            }
            req.setAttribute(UserConstants.EMAIL, email);
            return invalid;
    }

    private void setProfileData(HttpServletRequest req) {
        password = req.getParameter(UserConstants.PASS_WORD);
        firstName = req.getParameter(UserConstants.FIRST_NAME);
        lastName = req.getParameter(UserConstants.LAST_NAME);
        phoneNumber = req.getParameter(UserConstants.PHONE_NUMBER);
        email = req.getParameter(UserConstants.EMAIL);

    }

    private void checkProfileData(HttpServletRequest req) throws ActionException {
        loadProperties();
        checkParameterByRegex(password, UserConstants.PASS_WORD,
                properties.getProperty(UserConstants.PASS_WORD_REGEX), req);
        checkParameterByRegex(firstName, UserConstants.FIRST_NAME,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(lastName, UserConstants.LAST_NAME,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(phoneNumber, UserConstants.PHONE_NUMBER,
                properties.getProperty(NOT_EMPTY_NUMBER), req);

    }

    private void setProfileErrorsAttributes(HttpServletRequest req){
        if (invalid) {
            req.setAttribute(UserConstants.PASS_WORD, password);
            req.setAttribute(UserConstants.FIRST_NAME, firstName);
            req.setAttribute(UserConstants.LAST_NAME, lastName);
            req.setAttribute(UserConstants.PHONE_NUMBER, phoneNumber);
            req.setAttribute(UserConstants.GENDER, req.getParameter(UserConstants.GENDER));
        }
    }

    private void setAddressData(HttpServletRequest req){
        country = req.getParameter(UserConstants.COUNTRY);
        city = req.getParameter(UserConstants.CITY);
        street = req.getParameter(UserConstants.STREET);
        buildingNumber = req.getParameter(UserConstants.BUILDING_NUMBER);
        apartmentNumber = req.getParameter(UserConstants.APARTMENT_NUMBER);
    }

    private void checkAddressData(HttpServletRequest req) throws ActionException {

        loadProperties();
        checkParameterByRegex(country, UserConstants.COUNTRY,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(city, UserConstants.CITY,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(street, UserConstants.STREET,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(buildingNumber, UserConstants.BUILDING_NUMBER,
                properties.getProperty(NOT_EMPTY_NUMBER), req);
        checkParameterByRegex(apartmentNumber, UserConstants.APARTMENT_NUMBER,
                properties.getProperty(NOT_EMPTY_NUMBER), req);


    }

    private void setAddressErrorsAttributes(HttpServletRequest req){
        if (invalid) {
            req.setAttribute(UserConstants.COUNTRY, country);
            req.setAttribute(UserConstants.CITY, city);
            req.setAttribute(UserConstants.STREET, street);
            req.setAttribute(UserConstants.BUILDING_NUMBER, buildingNumber);
            req.setAttribute(UserConstants.APARTMENT_NUMBER, apartmentNumber);
            req.setAttribute(UserConstants.GENDER, req.getParameter(UserConstants.GENDER));
        }
    }

}

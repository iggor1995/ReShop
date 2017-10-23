package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.action.user.RegisterAction;
import com.epam.igor.electronicsshop.constants.UserConstants;
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
    private static final String FLASH = "flash.";
    private boolean invalid;
    private Properties properties = new Properties();


    public boolean checkParameterByRegex(boolean invalid, String parameter, String parameterName, String regex, HttpServletRequest req) {

        boolean invalidPar = false;
        LOG.debug(CHECK_PARAMETER, parameterName, parameter, regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        if (!matcher.matches()) {
            LOG.debug(WRONG_PARAMETER, parameterName, parameter);
            req.setAttribute(FLASH + parameterName + ERROR, TRUE);
            invalidPar = true;
        }
        if (!invalid) {
            return invalidPar;
        } else return true;
    }
    public boolean checkUserParam(HttpServletRequest req, boolean checkData, boolean checkAddress) throws ActionException, ServiceException {
        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream(UserConstants.VALIDATION_PROPERTIES));
        } catch (IOException e) {
            LOG.info(UserConstants.PROPERTIES_ERROR, e);
            throw new ActionException(UserConstants.PROPERTIES_ERROR, e);
        }
        if(checkData) {
            checkProfileData(req);
        }
        if(checkAddress){
            checkAddressData(req);
        }
        if(checkAddress && checkData){
            emailCheck(req);
        }
        return invalid;
    }

    private void emailCheck(HttpServletRequest req) throws ServiceException {

        String email = req.getParameter(UserConstants.EMAIL);
        UserService userService = new UserService();
            if (!userService.checkEmail(email)) {
                req.setAttribute(FLASH + EMAIL_ERROR, USED);
                LOG.error(EMAIL_TAKEN, email);
                invalid = true;
            }
            invalid = checkParameterByRegex(invalid, email, UserConstants.EMAIL,
                    properties.getProperty(UserConstants.EMAIL_REGEX), req);
            if(invalid){
                req.setAttribute(UserConstants.EMAIL, email);
            }
    }

    private void checkProfileData(HttpServletRequest req){

        String password = req.getParameter(UserConstants.PASS_WORD);
        String firstName = req.getParameter(UserConstants.FIRST_NAME);
        String lastName = req.getParameter(UserConstants.LAST_NAME);
        String phoneNumber = req.getParameter(UserConstants.PHONE_NUMBER);

        invalid = checkParameterByRegex(invalid, password, UserConstants.PASS_WORD,
                properties.getProperty(UserConstants.PASS_WORD_REGEX), req);
        invalid = checkParameterByRegex(invalid, firstName, UserConstants.FIRST_NAME,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        invalid = checkParameterByRegex(invalid, lastName, UserConstants.LAST_NAME,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        invalid = checkParameterByRegex(invalid, phoneNumber, UserConstants.PHONE_NUMBER,
                properties.getProperty(NOT_EMPTY_NUMBER), req);
        if (invalid) {
            req.setAttribute(UserConstants.PASS_WORD, password);
            req.setAttribute(UserConstants.FIRST_NAME, firstName);
            req.setAttribute(UserConstants.LAST_NAME, lastName);
            req.setAttribute(UserConstants.PHONE_NUMBER, phoneNumber);
            req.setAttribute(UserConstants.GENDER, req.getParameter(UserConstants.GENDER));
        }
    }

    private void checkAddressData(HttpServletRequest req){
        String country = req.getParameter(UserConstants.COUNTRY);
        String city = req.getParameter(UserConstants.CITY);
        String street = req.getParameter(UserConstants.STREET);
        String buildingNumber = req.getParameter(UserConstants.BUILDING_NUMBER);
        String apartmentNumber = req.getParameter(UserConstants.APARTMENT_NUMBER);

        invalid = checkParameterByRegex(invalid, country, UserConstants.COUNTRY,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        invalid = checkParameterByRegex(invalid, city, UserConstants.CITY,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        invalid = checkParameterByRegex(invalid, street, UserConstants.STREET,
                properties.getProperty(NOT_EMPTY_TEXT), req);
        invalid = checkParameterByRegex(invalid, buildingNumber, UserConstants.BUILDING_NUMBER,
                properties.getProperty(NOT_EMPTY_NUMBER), req);
        invalid = checkParameterByRegex(invalid, apartmentNumber, UserConstants.APARTMENT_NUMBER,
                properties.getProperty(NOT_EMPTY_NUMBER), req);

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

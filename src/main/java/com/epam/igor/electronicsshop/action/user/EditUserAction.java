package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * For saving user changes to database
 * @author Igor Lapin
 * */
public class EditUserAction implements Action {
    private static final String NOT_EMPTY_TEXT = "not_empty_string.regex";
    private static final String NOT_EMPTY_NUMBER = "not_empty_digits.regex";
    private static final String EMAIL_REGEX = "email.regex";
    private static final String PASS_WORD_REGEX = "password.regex";
    private static final String PASS_WORD = "password";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String EMAIL = "email";
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String STREET = "street";
    private static final String BUILDING_NUMBER = "buildingNumber";
    private static final String APARTMENT_NUMBER = "apartmentNumber";
    private static final String USER_ID = "userId";
    private static final String USER = "user";
    private static final String ROLE = "role";
    private static final String ERROR = "Error";
    private static final String GENDER = "gender";
    private static final String GENDERS = "genders";
    private static final String LOGGED_USER = "loggedUser";
    private static final String EDIT_USER_PAGE = "edit-user";
    private static final String MANAGE_USERS_PAGE = "manage/users";
    private static final String ADDRESS = "address";
    private static final String UPDATED = "{} updated data to {}, {}";
    private static final String UPDATED_ERROR = "Could not update profile";
    private static final String EMAIL_ERROR = "emailError";
    private static final String EMAIL_TAKEN = "email {} has already been taken!";
    private static final String CHECK_PARAMETER = "Check parameter '{}' with value '{}' by regex '{}'";
    private static final String WRONG_PARAMETER = "Parameter '{}' with value '{}' is unsuitable.";
    private static final String UNABLE_REGISTER= "Couldn't register user";
    private static final String PROPERTIES_ERROR= "Cannot load properties";
    private static final String VALIDATION_PROPERTIES= "validation.properties";
    private static final Logger LOG = LoggerFactory.getLogger(EditUserAction.class);
    private static final String FLASH = "flash.";
    private static final String TRUE = "true";
    private boolean invalid;
    private Properties properties = new Properties();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        UserService userService = new UserService();
        User user;
        String email = req.getParameter(EMAIL);
        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream(VALIDATION_PROPERTIES));
        } catch (IOException e) {
            throw new ActionException(PROPERTIES_ERROR, e);
        }
        try {
            user = userService.getFilledUserById(Integer.valueOf(req.getParameter(USER_ID)));
            if(!userService.checkEmail(email)){
                req.setAttribute(EMAIL_ERROR, TRUE);
                invalid = true;
                LOG.error(EMAIL_TAKEN, email);
            }
            else {
                checkParameterByRegex(email, EMAIL, properties.getProperty(EMAIL_REGEX), req);
            }
        } catch (ServiceException e) {
            throw new ActionException(UNABLE_REGISTER, e);
        }
        String password = req.getParameter(PASS_WORD);
        String firstName = req.getParameter(FIRST_NAME);
        String lastName = req.getParameter(LAST_NAME);
        String phoneNumber = req.getParameter(PHONE_NUMBER);
        String country = req.getParameter(COUNTRY);
        String city = req.getParameter(CITY);
        String street = req.getParameter(STREET);
        String buildingNumber = req.getParameter(BUILDING_NUMBER);
        String apartmentNumber = req.getParameter(APARTMENT_NUMBER);
        checkParameterByRegex(password, PASS_WORD, properties.getProperty(PASS_WORD_REGEX), req);
        checkParameterByRegex(firstName, FIRST_NAME, properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(lastName, LAST_NAME, properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(phoneNumber, PHONE_NUMBER, properties.getProperty(NOT_EMPTY_NUMBER), req);
        checkParameterByRegex(country, COUNTRY, properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(city, CITY, properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(street, STREET, properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(buildingNumber, BUILDING_NUMBER, properties.getProperty(NOT_EMPTY_NUMBER), req);
        checkParameterByRegex(apartmentNumber, APARTMENT_NUMBER, properties.getProperty(NOT_EMPTY_NUMBER), req);
        String md5HexPassword = DigestUtils.md5Hex(password);

        if(invalid){
            invalid = false;
            req.setAttribute(USER, user);
            req.setAttribute(ADDRESS, user.getAddress());
            return new ActionResult(EDIT_USER_PAGE);
        }

        try {
            user.setEmail(email);
            user.setPassword(md5HexPassword);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNumber);
            user.getGender().setId(Integer.valueOf(req.getParameter(GENDER)));
            Address userAddress = user.getAddress();
            userAddress.setCountry(country);
            userAddress.setCity(city);
            userAddress.setStreet(street);
            userAddress.setBuildingNumber(buildingNumber);
            userAddress.setApartmentNumber(apartmentNumber);
            if (!req.getParameter(ROLE).equals(user.getRole().name())) {
                user.setRole(User.Role.valueOf(req.getParameter(ROLE)));
            }
            userService.updateUserAddress(userAddress);
            userService.updateUser(user);
            req.getSession(false).removeAttribute(GENDERS);
            LOG.info(UPDATED, req.getSession(false).getAttribute(LOGGED_USER), user, userAddress);
            return new ActionResult(MANAGE_USERS_PAGE, true);
        } catch (ServiceException e) {
            throw new ActionException(UPDATED_ERROR, e);
        }
    }
    private void checkParameterByRegex(String parameter, String parameterName, String regex, HttpServletRequest req) {
        LOG.debug(CHECK_PARAMETER, parameterName, parameter, regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        if (!matcher.matches()) {
            LOG.debug(WRONG_PARAMETER, parameterName, parameter);
            req.setAttribute(FLASH + parameterName + ERROR, TRUE);
            invalid = true;
        }
    }
}
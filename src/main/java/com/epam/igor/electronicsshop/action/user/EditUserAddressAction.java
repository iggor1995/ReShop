package com.epam.igor.electronicsshop.action.user;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.entity.Address;
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
 *  For saving user's address changes to database
 * @author Igor Lapin
 */
public class EditUserAddressAction implements Action {

    private static final String NOT_EMPTY_TEXT = "not_empty_string.regex";
    private static final String NOT_EMPTY_NUMBER = "not_empty_digits.regex";
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String STREET = "street";
    private static final String BUILDING_NUMBER = "buildingNumber";
    private static final String APARTMENT_NUMBER = "apartmentNumber";
    private static final String CHECK_PARAMETR = "Check parameter '{}' with value '{}' by regex '{}'";
    private static final String WRONG_PARAMETR = "Parameter '{}' with value '{}' is unsuitable.";
    private static final String GENDERS = "genders";
    private static final String LOGGED_USER = "loggedUser";
    private static final String EDIT_USER_PAGE = "edit-user";
    private static final String USER_PROFILE_PAGE = "user/profile";
    private static final String ADDRESS = "address";
    private static final String UPDATED = "{} updated data to {}";
    private static final String UPDATED_ERROR = "Could not update profile";
    private static final String UNABLE_REGISTER= "Couldn't register user";
    private static final String PROPERTIES_ERROR= "Cannot load properties";
    private static final String VALIDATION_PROPERTIES= "validation.properties";
    private static final Logger LOG = LoggerFactory.getLogger(EditUserAction.class);
    private boolean INVALID;
    private Properties properties = new Properties();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        UserService userService = new UserService();
        Address address;
        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream(VALIDATION_PROPERTIES));
        } catch (IOException e) {
            throw new ActionException(PROPERTIES_ERROR, e);
        }
        try {
            address = userService.getUserAddress((User) req.getSession().getAttribute(LOGGED_USER));
        } catch (ServiceException e) {
            throw new ActionException(UNABLE_REGISTER, e);
        }
        String country = req.getParameter(COUNTRY);
        String city = req.getParameter(CITY);
        String street = req.getParameter(STREET);
        String buildingNumber = req.getParameter(BUILDING_NUMBER);
        String apartmentNumber = req.getParameter(APARTMENT_NUMBER);
        checkParameterByRegex(country, COUNTRY, properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(city, CITY, properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(street, STREET, properties.getProperty(NOT_EMPTY_TEXT), req);
        checkParameterByRegex(buildingNumber, BUILDING_NUMBER, properties.getProperty(NOT_EMPTY_NUMBER), req);
        checkParameterByRegex(apartmentNumber, APARTMENT_NUMBER, properties.getProperty(NOT_EMPTY_NUMBER), req);

        if(INVALID){
            INVALID = false;
            req.setAttribute(ADDRESS, address);
            return new ActionResult(EDIT_USER_PAGE);
        }

        try {
            address.setCountry(country);
            address.setCity(city);
            address.setStreet(street);
            address.setBuildingNumber(buildingNumber);
            address.setApartmentNumber(apartmentNumber);
            userService.updateUserAddress(address);
            req.getSession(false).removeAttribute(GENDERS);
            LOG.info(UPDATED, req.getSession(false).getAttribute(LOGGED_USER), address);
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
            INVALID = true;
        }
    }
}

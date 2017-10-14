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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * For saving user's address changes to database
 *
 * @author Igor Lapin
 */
public class EditUserAddressAction implements Action {

    private static final String NOT_EMPTY_TEXT = "not_empty_string.regex";
    private static final String NOT_EMPTY_NUMBER = "not_empty_digits.regex";
    private static final String EDIT_USER_ADDRESS_PAGE = "edit-user-address";
    private static final String USER_PROFILE_PAGE = "user/profile";
    private static final String ADDRESS = "address";
    private static final String UPDATED = "{} updated data to {}";
    private static final String UPDATED_ERROR = "Could not update profile";
    private static final String UNABLE_REGISTER = "Couldn't register user";
    private static final String PROPERTIES_ERROR = "Cannot load properties";
    private static final String VALIDATION_PROPERTIES = "validation.properties";
    private static final Logger LOG = LoggerFactory.getLogger(EditUserAction.class);
    private boolean invalid;
    private Properties properties = new Properties();
    private String country;
    private String city;
    private String street;
    private String buildingNumber;
    private String apartmentNumber;

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
            address = userService.getUserAddress((User) req.getSession().getAttribute(UserConstants.LOGGED_USER));
        } catch (ServiceException e) {
            LOG.info(UNABLE_REGISTER, e);
            throw new ActionException(UNABLE_REGISTER, e);
        }
        checkAddress(req);
        if (invalid) {
            invalid = false;
            req.setAttribute(ADDRESS, address);
            return new ActionResult(EDIT_USER_ADDRESS_PAGE);
        }

        try {
            address.setCountry(country);
            address.setCity(city);
            address.setStreet(street);
            address.setBuildingNumber(buildingNumber);
            address.setApartmentNumber(apartmentNumber);
            userService.updateUserAddress(address);
            req.getSession(false).removeAttribute(UserConstants.GENDERS);
            LOG.info(UPDATED, req.getSession(false).getAttribute(UserConstants.LOGGED_USER), address);
            return new ActionResult(USER_PROFILE_PAGE, true);
        } catch (ServiceException e) {
            LOG.info(UPDATED_ERROR, e);
            throw new ActionException(UPDATED_ERROR, e);
        }
    }

    private void checkAddress(HttpServletRequest req) {
        country = req.getParameter(UserConstants.COUNTRY);
        city = req.getParameter(UserConstants.CITY);
        street = req.getParameter(UserConstants.STREET);
        buildingNumber = req.getParameter(UserConstants.BUILDING_NUMBER);
        apartmentNumber = req.getParameter(UserConstants.APARTMENT_NUMBER);
        Validation validation = new Validation();
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
    }

}

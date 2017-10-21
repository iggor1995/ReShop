package com.epam.igor.electronicsshop.util;

import com.epam.igor.electronicsshop.action.ActionException;
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

public class UserUtil {

    private static final Logger LOG = LoggerFactory.getLogger(UserUtil.class);
    private static final String UNABLE_REGISTER = "Couldn't register user";
    private static final String USER_HAS_BEEN_REGISTERED = "{} registered. Address - {}";
    private static final String ROLE = "ROLE - {}";
    private static final String UPDATED = "{} updated data to {}, {}";
    private static String email;
    private static String password;
    private static String firstName;
    private static String lastName;
    private static String phoneNumber;
    private static String country;
    private static String city;
    private static String street;
    private static String buildingNumber;
    private static String apartmentNumber;

    private UserUtil(){}

    private static void setUserParamsFromRequest(HttpServletRequest req){
        email = req.getParameter(UserConstants.EMAIL);
        password = req.getParameter(UserConstants.PASS_WORD);
        firstName = req.getParameter(UserConstants.FIRST_NAME);
        lastName = req.getParameter(UserConstants.LAST_NAME);
        phoneNumber = req.getParameter(UserConstants.PHONE_NUMBER);

    }
    private static void setAddressParamsFromRequest(HttpServletRequest req){
        country = req.getParameter(UserConstants.COUNTRY);
        city = req.getParameter(UserConstants.CITY);
        street = req.getParameter(UserConstants.STREET);
        buildingNumber = req.getParameter(UserConstants.BUILDING_NUMBER);
        apartmentNumber = req.getParameter(UserConstants.APARTMENT_NUMBER);
    }

    public static void fillUser(HttpServletRequest req) throws ActionException {
        setUserParamsFromRequest(req);
        setAddressParamsFromRequest(req);
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

    public static void fillUser(HttpServletRequest req, User user) throws ServiceException {
        setUserParamsFromRequest(req);
        String md5HexPassword = DigestUtils.md5Hex(password);
        UserService userService = new UserService();
        if(email != null) {
            user.setEmail(email);
        }
        user.setPassword(md5HexPassword);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.getGender().setId(Integer.valueOf(req.getParameter(UserConstants.GENDER)));
        userService.updateUser(user);
        req.getSession(false).removeAttribute(UserConstants.GENDERS);
        LOG.info(UPDATED, req.getSession(false).getAttribute(UserConstants.LOGGED_USER), user);
    }

    public static void fillAddress(HttpServletRequest req, User user) throws ServiceException {
        setAddressParamsFromRequest(req);
        UserService userService = new UserService();
        Address userAddress = user.getAddress();
        userAddress.setCountry(country);
        userAddress.setCity(city);
        userAddress.setStreet(street);
        userAddress.setBuildingNumber(buildingNumber);
        userAddress.setApartmentNumber(apartmentNumber);
        userService.updateUserAddress(userAddress);
        LOG.info(UPDATED, req.getSession(false).getAttribute(UserConstants.LOGGED_USER), userAddress);
    }
}

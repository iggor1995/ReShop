package com.epam.igor.electronicsshop.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private static final Logger LOG = LoggerFactory.getLogger(Validation.class);
    private static final String CHECK_PARAMETER = "Check parameter '{}' with value '{}' by regex '{}'";
    private static final String WRONG_PARAMETER = "Parameter '{}' with value '{}' is unsuitable.";
    private static final String ERROR = "Error";
    private static final String FLASH = "flash.";
    private static final String TRUE = "true";

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
}

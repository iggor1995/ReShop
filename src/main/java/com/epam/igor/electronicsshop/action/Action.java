package com.epam.igor.electronicsshop.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for Actions
 *
 * @author Igor Lapin
 */
public interface Action {
    ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException;
}

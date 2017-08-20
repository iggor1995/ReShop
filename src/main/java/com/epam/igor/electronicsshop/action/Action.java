package com.epam.igor.electronicsshop.action;

/**
 * Created by User on 03.08.2017.
 */
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
public interface Action {
    ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException;
}

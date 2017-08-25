package com.epam.igor.electronicsshop.action;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 12.08.2017.
 */
public class ActionFactory {
    private static Map<String, Action> actions;
    static {
        actions = new HashMap<>();
        actions.put("GET/welcome", new ShowPageAction("welcome"));
        actions.put("GET/register", new ShowRegisterPageAction());
        actions.put("GET/home", new ShowPageAction("home"));
        actions.put("GET/locale", new SelectLocaleAction());
        actions.put("POST/register", new RegisterAction());


    }

    public static Action getAction(String actionName) {
        return actions.get(actionName);
    }
}

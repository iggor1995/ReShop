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
        actions.put("GET/home", new ShowPageAction("home"));
        actions.put("GET/register", new ShowPageAction("register"));
        actions.put("GET/locale", new SelectLocaleAction());


    }

    public static Action getAction(String actionName) {
        return actions.get(actionName);
    }
}

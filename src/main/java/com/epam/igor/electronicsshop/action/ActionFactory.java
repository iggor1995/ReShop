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
        actions.put("GET/login", new ShowPageAction("login"));
        actions.put("GET/home", new ShowHomePageAction());
        actions.put("GET/locale", new SelectLocaleAction());
        actions.put("GET/catalog", new ShowCatalogPageAction());
        actions.put("GET/product", new ShowProductPageAction());
        actions.put("GET/user/profile", new ShowUserProfileAction());
        actions.put("GET/user/orders", new ShowUserOrdersAction());
        actions.put("GET/add/product", new ShowAddProductPageAction());
        actions.put("GET/cart", new ShowPageAction("cart"));
        actions.put("GET/manage/products", new ShowManageProductsPageAction());
        actions.put("GET/cart/buy", new BuyCartAction());
        actions.put("GET/cart/buy", new BuyCartAction());
        actions.put("GET/order", new ShowOrderPageAction());

        actions.put("POST/register", new RegisterAction());
        actions.put("POST/login", new LoginAction());
        actions.put("POST/add/product", new AddProductAction());
        actions.put("POST/cart/add", new AddProductToCartAction());

    }

    public static Action getAction(String actionName) {
        return actions.get(actionName);
    }
}

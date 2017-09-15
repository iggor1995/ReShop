package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.action.cart.AddProductToCartAction;
import com.epam.igor.electronicsshop.action.cart.BuyCartAction;
import com.epam.igor.electronicsshop.action.cart.ClearCartAction;
import com.epam.igor.electronicsshop.action.order.*;
import com.epam.igor.electronicsshop.action.product.*;
import com.epam.igor.electronicsshop.action.storage.DeleteStorageItemAction;
import com.epam.igor.electronicsshop.action.storage.EditStorageItemAction;
import com.epam.igor.electronicsshop.action.storage.ShowManageStoragePageAction;
import com.epam.igor.electronicsshop.action.user.*;
import com.epam.igor.electronicsshop.action.util.*;

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
        actions.put("GET/cart/clear", new ClearCartAction());
        actions.put("GET/order", new ShowOrderPageAction());
        actions.put("GET/manage/users", new ShowManageUsersPageAction());
        actions.put("GET/manage/orders", new ShowManageOrdersPageAction());
        actions.put("GET/manage/storage", new ShowManageStoragePageAction());
        actions.put("GET/delete/order", new DeleteOrderAction());
        actions.put("GET/delete/user", new DeleteUserAction());
        actions.put("GET/delete/product", new DeleteProductAction());
        actions.put("GET/delete/orderingItem", new DeleteOrderingItemAction());
        actions.put("GET/delete/storageItem", new DeleteStorageItemAction());
        actions.put("GET/edit/user", new ShowEditUserPageAction());
        actions.put("GET/edit/product", new ShowEditProductPageAction());
        actions.put("GET/edit/userData", new ShowEditUserDataAction());
        actions.put("GET/edit/userAddress", new ShowEditAddressAction());
        actions.put("GET/refill/user", new ShowRefillPageAction());
        actions.put("GET/logout", new LogoutAction());

        actions.put("POST/register", new RegisterAction());
        actions.put("POST/edit/storage/item", new EditStorageItemAction());
        actions.put("POST/refill/user", new RefillUserBalanceAction());
        actions.put("POST/edit/user", new EditUserAction());
        actions.put("POST/edit/product", new EditProductAction());
        actions.put("POST/edit/userData", new EditUserDataAction());
        actions.put("POST/edit/userAddress", new EditUserAddressAction());
        actions.put("POST/login", new LoginAction());
        actions.put("POST/add/product", new AddProductAction());
        actions.put("POST/cart/add", new AddProductToCartAction());
        actions.put("POST/edit/orderStatus", new EditOrderStatusAction());

    }

    public static Action getAction(String actionName) {
        return actions.get(actionName);
    }
}

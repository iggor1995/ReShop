package com.epam.igor.electronicsshop.action.cart;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.entity.Order;
import com.epam.igor.electronicsshop.entity.OrderingItem;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * Class for adding product to cart
 *
 * @author Igor Lapin
 */
public class AddProductToCartAction implements Action {
    private static final String AMOUNT = "amount";
    private static final String ERROR_AMOUNT = "amountError";
    private static final String ERROR_ADD = "Couldn't add product to cart";
    private static final String INVALID_AMOUNT = "Invalid product amount format - {}";
    private static final String PARAMETER_PRODUCT = "product";
    private static final String REFERER_PAGE = "referer";
    private static final String ATTRIBUTE_CART = "cart";
    private static final String AMOUNT_INCREASED = "Product amount in cart increased by - {}";
    private static final String PRODUCT_ADDED = "product - {} added in cart. Amount - {}";
    private static final String PROPERTY_PRODUCT_AMOUNT = "product.amount";
    private static final String TRUE = "true";
    private static final String CANNOT_LOAD_PROPERTIES = "Cannot load properties";
    private static final String VALIDATION_PROPERTIES = "validation.properties";
    private boolean invalid;
    private static final Logger LOG = LoggerFactory.getLogger(AddProductToCartAction.class);
    private Properties properties = new Properties();
    private String amount;
    private Order cart;

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            properties.load(AddProductToCartAction.class.getClassLoader().getResourceAsStream(VALIDATION_PROPERTIES));
        } catch (IOException e) {
            LOG.info(CANNOT_LOAD_PROPERTIES, e);
            throw new ActionException(CANNOT_LOAD_PROPERTIES, e);
        }

        if (checkAmount(req)) {
            invalid = false;
            req.setAttribute(ERROR_AMOUNT, TRUE);
            LOG.info(INVALID_AMOUNT, amount);
            return new ActionResult(req.getHeader(REFERER_PAGE), true);
        }

        cart = (Order) req.getSession(false).getAttribute(ATTRIBUTE_CART);
        if (cart == null) {
            cart = new Order();
        }
        setOrderingItem(req);
        return new ActionResult(req.getHeader(REFERER_PAGE), true);
    }

    private boolean checkAmount(HttpServletRequest req) {
        amount = req.getParameter(AMOUNT);
        Validation validation = new Validation();
        invalid = validation.checkParameterByRegex(invalid, amount, AMOUNT,
                properties.getProperty(PROPERTY_PRODUCT_AMOUNT), req);
        if (invalid) {
            req.setAttribute(ERROR_AMOUNT, TRUE);
            LOG.info(INVALID_AMOUNT, amount);
        }
        return invalid;
    }

    private void setOrderingItem(HttpServletRequest req) throws ActionException {

        String productId = req.getParameter(PARAMETER_PRODUCT);
        Integer amountInt = Integer.parseInt(amount);

        for (OrderingItem orderingItem : cart.getOrderingItems()) {                   //if already in cart
            if (orderingItem.getProduct().getId() == Integer.parseInt(productId)) {
                orderingItem.setAmount(orderingItem.getAmount() + amountInt);
                req.getSession().setAttribute(ATTRIBUTE_CART, cart);
                LOG.info(AMOUNT_INCREASED, amount);
            }
        }
        OrderingItem orderingItem = new OrderingItem();                 //else set new one
        orderingItem.setAmount(amountInt);

        try {
            ProductService productService = new ProductService();
            Product product = productService.getProductById(productId);
            orderingItem.setProduct(product);
            cart.addProduct(orderingItem);
            req.getSession().setAttribute(ATTRIBUTE_CART, cart);
            LOG.info(PRODUCT_ADDED, product, amount);
        } catch (ServiceException e) {
            LOG.info(ERROR_ADD, e);
            throw new ActionException(ERROR_ADD, e);
        }

    }
}

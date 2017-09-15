package com.epam.igor.electronicsshop.action.cart;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.user.RegisterAction;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 03.09.2017.
 */
public class AddProductToCartAction implements Action {
    private static final String CHECK_PARAMETR = "Check parameter '{}' with value '{}' by regex '{}'";
    private static final String WRONG_PARAMETR = "Parameter '{}' with value '{}' is unsuitable.";
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
    public static final String FLASH = "flash.";
    public static final String ERROR = "Error";
    private boolean INVALID;
    private final static Logger LOG = LoggerFactory.getLogger(AddProductToCartAction.class);
    private Properties properties = new Properties();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            properties.load(RegisterAction.class.getClassLoader().getResourceAsStream("validation.properties"));
        } catch (IOException e) {
            throw new ActionException("Cannot load properties", e);
        }
        String amount = req.getParameter(AMOUNT);
        checkParameterByRegex(amount, AMOUNT, properties.getProperty(PROPERTY_PRODUCT_AMOUNT), req);
        if(INVALID){
            INVALID = false;
            req.setAttribute(ERROR_AMOUNT, "true");
            LOG.info(INVALID_AMOUNT, amount);
            return new ActionResult(req.getHeader(REFERER_PAGE), true);
        }
        Order cart = (Order)req.getSession(false).getAttribute(ATTRIBUTE_CART);
        if(cart == null){
            cart = new Order();
        }
        String productId = req.getParameter(PARAMETER_PRODUCT);
        Integer amountInt = Integer.parseInt(amount);

        for(OrderingItem orderingItem : cart.getOrderingItems()){
            if(orderingItem.getProduct().getId() == Integer.parseInt(productId)){
                orderingItem.setAmount(orderingItem.getAmount() + amountInt);
                req.getSession().setAttribute(ATTRIBUTE_CART, cart);
                LOG.info(AMOUNT_INCREASED, amount);
                return new ActionResult(req.getHeader(REFERER_PAGE), true);
            }
        }
        OrderingItem orderingItem = new OrderingItem();
        orderingItem.setAmount(amountInt);
        try {
            ProductService productService = new ProductService();
            Product product = productService.getProductById(productId);
            orderingItem.setProduct(product);
            cart.addProduct(orderingItem);
            req.getSession().setAttribute(ATTRIBUTE_CART, cart);
            LOG.info(PRODUCT_ADDED, product, amount);
            return new ActionResult(req.getHeader(REFERER_PAGE), true);
        } catch (ServiceException e) {
            throw  new ActionException(ERROR_ADD, e);
        }
    }
    private void checkParameterByRegex(String parameter, String parameterName, String regex, HttpServletRequest req) {
        LOG.debug(CHECK_PARAMETR, parameterName, parameter, regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        if (!matcher.matches()) {
            LOG.debug(WRONG_PARAMETR, parameterName, parameter);
            req.setAttribute(FLASH + parameterName + ERROR, "true");
            INVALID = true;
        }
    }
}

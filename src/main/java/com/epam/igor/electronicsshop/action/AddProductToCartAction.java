package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.Order;
import com.epam.igor.electronicsshop.entity.OrderingItem;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import com.oracle.webservices.internal.api.message.PropertySet;
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
        String amount = req.getParameter("amount");
        checkParameterByRegex(amount, AMOUNT, properties.getProperty("product.amount"), req);
        if(INVALID){
            INVALID = false;
            req.setAttribute("amountError", "true");
            LOG.info("Invalid product amount format - {}", amount);
            return new ActionResult(req.getHeader("referer"), true);
        }
        LOG.info("here");
        Order cart = (Order)req.getSession(false).getAttribute("cart");
        if(cart == null){
            cart = new Order();
        }
        String productId = req.getParameter("product");
        Integer amountInt = Integer.parseInt(amount);

        for(OrderingItem orderingItem : cart.getOrderingItems()){
            if(orderingItem.getProduct().getId() == Integer.parseInt(productId)){
                orderingItem.setAmount(orderingItem.getAmount() + amountInt);
                req.getSession().setAttribute("cart", cart);
                LOG.info("Product amount in cart increaser by - {}", amount);
                return new ActionResult(req.getHeader("referer"), true);
            }
        }
        OrderingItem orderingItem = new OrderingItem();
        orderingItem.setAmount(amountInt);
        try {
            ProductService productService = new ProductService();
            Product product = productService.getProductById(productId);
            orderingItem.setProduct(product);
            cart.addProduct(orderingItem);
            req.getSession().setAttribute("cart", cart);
            LOG.info("product - {} added in cart. Amount - {}", product, amount);
            return new ActionResult(req.getHeader("referer"), true);
        } catch (ServiceException e) {
            throw  new ActionException("Couldn't add product to cart", e);
        }
    }
    private void checkParameterByRegex(String parameter, String parameterName, String regex, HttpServletRequest req) {
        LOG.debug(CHECK_PARAMETR, parameterName, parameter, regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        if (!matcher.matches()) {
            LOG.debug(WRONG_PARAMETR, parameterName, parameter);
            req.setAttribute(parameterName + "Error", "true");
            INVALID = true;
        }
    }
}

package com.epam.igor.electronicsshop.action.cart;

import com.epam.igor.electronicsshop.action.Action;
import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.ActionResult;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.constants.ErrorConstants;
import com.epam.igor.electronicsshop.constants.OrderConstants;
import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.constants.ProductConstants;
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
    private static final String ERROR_ADD = "Couldn't add product to cart";
    private static final String INVALID_AMOUNT = "Invalid product amount format - {}";
    private static final String AMOUNT_INCREASED = "Product amount in cart increased by - {}";
    private static final String PRODUCT_ADDED = "product - {} added in cart. Amount - {}";
    private static final Logger LOG = LoggerFactory.getLogger(AddProductToCartAction.class);
    private String amount;
    private Order cart;

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {

        if (checkAmount(req)) {
            return new ActionResult(req.getHeader(PageConstants.REFERER_PAGE), true);
        }
        cart = getCart(req);
        String productId = req.getParameter(ProductConstants.PRODUCT);
        Integer amountInt = Integer.parseInt(amount);
        if(!increaseAmountIfAlreadyExists(amountInt ,productId, req)) {
            setOrderingItem(req, amountInt ,productId);
        }

        return new ActionResult(req.getHeader(PageConstants.REFERER_PAGE), true);
    }

    private boolean checkAmount(HttpServletRequest req) throws ActionException {
        amount = req.getParameter(OrderConstants.AMOUNT);
        Validation validation = new Validation();
        if (validation.checkAmount(req, amount)) {
            req.setAttribute(ErrorConstants.ERROR_AMOUNT, ErrorConstants.TRUE);
            LOG.info(INVALID_AMOUNT, amount);
            return true;
        }
        return false;
    }

    private void setOrderingItem(HttpServletRequest req, Integer amountInt, String productId) throws ActionException {
            try {
                OrderingItem orderingItem = new OrderingItem();                 //else set new one
                orderingItem.setAmount(amountInt);
                ProductService productService = new ProductService();
                Product product = productService.getProductById(productId);
                orderingItem.setProduct(product);
                cart.addProduct(orderingItem);
                req.getSession().setAttribute(PageConstants.CART, cart);
                LOG.info(PRODUCT_ADDED, product, amount);
            } catch (ServiceException e) {
                LOG.info(ERROR_ADD, e);
                throw new ActionException(ERROR_ADD, e);
            }
    }

    private boolean increaseAmountIfAlreadyExists(Integer amountInt, String productId, HttpServletRequest req){
        for (OrderingItem orderingItem : cart.getOrderingItems()) {                   //if already in cart
            if (orderingItem.getProduct().getId() == Integer.parseInt(productId)) {
                orderingItem.setAmount(orderingItem.getAmount() + amountInt);
                req.getSession().setAttribute(PageConstants.CART, cart);
                LOG.info(AMOUNT_INCREASED, amount);
                return true;
            }
        }
        return false;
    }

    private Order getCart(HttpServletRequest req){
        Order cart = (Order) req.getSession(false).getAttribute(PageConstants.CART);
        if (cart == null) {
            cart = new Order();
        }
        return cart;
    }
}

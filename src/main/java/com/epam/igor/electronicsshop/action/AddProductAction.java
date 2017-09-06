package com.epam.igor.electronicsshop.action;

import com.epam.igor.electronicsshop.entity.Image;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.entity.ProductType;
import com.epam.igor.electronicsshop.service.ProductService;
import com.epam.igor.electronicsshop.service.ServiceException;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 27.08.2017.
 */

public class AddProductAction implements Action {
    private static Logger LOG = LoggerFactory.getLogger(AddProductAction.class);
    private static final String CHECK_PARAMETR = "Check parameter '{}' with value '{}' by regex '{}'";
    private boolean INVALID;
    private static final String WRONG_PARAMETR = "Parameter '{}' with value '{}' is unsuitable.";
    private static final String MONEY = "money";
    private static final String MONEY_REGEX = "money.regex";
    Properties properties = new Properties();
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        try {
            properties.load(AddProductAction.class.getClassLoader().getResourceAsStream("validation.properties"));
        } catch (IOException e) {
            throw new ActionException("Couldn't get validation.properties", e);
        }

        String name = req.getParameter("name");
        String type = req.getParameter("typeId");
        String descriptionEN = req.getParameter("descriptionEN");
        String descriptionRU = req.getParameter("descriptionRU");
        String price = req.getParameter("price");

        ProductService productService = new ProductService();
        try{
            Product product = new Product();
            product.setName(name);
            product.setType(new ProductType(Integer.valueOf(type)));
            product.setEnDescription(descriptionEN);
            product.setRuDescription(descriptionRU);
            checkParameterByRegex(price, MONEY, properties.getProperty(MONEY_REGEX), req);
            if(INVALID){
                INVALID = false;
                req.setAttribute("moneyError", "true");
                req.setAttribute("product", product);
                return new ActionResult("add-product");
            }
            product.setPrice(Money.parse("KZT" + price));
            Part imagePart = req.getPart("image");
            if(!imagePart.getContentType().startsWith("image")){
                req.setAttribute("imageError", "true");
                req.setAttribute("product", product);
                LOG.error("Invalid content type - {}", imagePart.getContentType());
                return new ActionResult("add-product");
            }

            Image image = new Image();
            image.setName(name.replaceAll("\\s", "").toLowerCase());
            image.setModifiedTime(DateTime.now());
            image.setContentType(imagePart.getContentType());
            image.setImageStream(imagePart.getInputStream());
            Product newProduct = productService.addProduct(product, image);
            productService.addProductToStorage(newProduct);
            LOG.info("{} inserted in db and added on central storage by {}", newProduct, req.getSession(false).getAttribute("loggedUser"));
        } catch (ServiceException | IOException | ServletException e) {
            throw new ActionException("Couldn't add product", e);
        }
        return new ActionResult("manage/products", true);
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

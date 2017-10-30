package com.epam.igor.electronicsshop.util;

import com.epam.igor.electronicsshop.action.ActionException;
import com.epam.igor.electronicsshop.action.Validation;
import com.epam.igor.electronicsshop.constants.ErrorConstants;
import com.epam.igor.electronicsshop.constants.ProductConstants;
import com.epam.igor.electronicsshop.entity.Image;
import com.epam.igor.electronicsshop.entity.Product;
import com.epam.igor.electronicsshop.entity.ProductType;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;

public class ProductUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ProductUtil.class);
    private static final String INVALID_CONTENT_TYPE = "Invalid content type - {}";
    private Validation validation = new Validation();
    private String name;
    private String type;
    private String descriptionRU;
    private String descriptionEN;
    private String price;
    private Part imagePart;

    public boolean validateMoneyAndImage(HttpServletRequest req) throws IOException, ServletException, ActionException {
        setProductParams(req);
        boolean invalid = false;
        if(validation.checkMoney(req, price)) {
            req.setAttribute(ErrorConstants.ERROR_MONEY, ErrorConstants.TRUE);
            invalid = true;
        }
        if(checkImagePart(req)){
            req.setAttribute(ErrorConstants.ERROR_IMAGE, ErrorConstants.TRUE);
            LOG.error(INVALID_CONTENT_TYPE);
            invalid = true;
        }
        return invalid;
    }

    private void setProductParams(HttpServletRequest req){
        name = req.getParameter(ProductConstants.NAME);
        type = req.getParameter(ProductConstants.TYPE_ID);
        descriptionEN = req.getParameter(ProductConstants.DESCRIPTION_EN);
        descriptionRU = req.getParameter(ProductConstants.DESCRIPTION_RU);
        price = req.getParameter(ProductConstants.PRICE);
    }

    public Product getFilledProduct(HttpServletRequest req) {
        setProductParams(req);
        Product filledProduct = new Product();
        filledProduct.setName(name);
        filledProduct.setType(new ProductType(Integer.valueOf(type)));
        filledProduct.setEnDescription(descriptionEN);
        filledProduct.setRuDescription(descriptionRU);
        filledProduct.setPrice(Money.parse(ProductConstants.KZT + price));

        return filledProduct;
    }

    public boolean checkImagePart(HttpServletRequest req) throws IOException, ServletException {
        imagePart = req.getPart(ProductConstants.IMAGE);
        if(imagePart.getSize() != 0) {
            return !imagePart.getContentType().startsWith(ProductConstants.IMAGE);
        }
        LOG.info(INVALID_CONTENT_TYPE);
        return true;
    }

    public Image getFilledImage(Image image) throws IOException {
        image.setName(name.replaceAll("\\s", "").toLowerCase());
        image.setModifiedTime(DateTime.now());
        image.setContentType(imagePart.getContentType());
        image.setImageStream(imagePart.getInputStream());
        return image;
    }


}

package com.epam.igor.electronicsshop.util;

import com.epam.igor.electronicsshop.constants.PageConstants;
import com.epam.igor.electronicsshop.entity.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class PageUtil<T> {

    public String getPage(HttpServletRequest req){
        String page = req.getParameter(PageConstants.PAGE);
        if (page == null) {
            page = PageConstants.FIRST_PAGE;
        }
        return page;
    }
    public String getPageSize(HttpServletRequest req){
        String pageSize = req.getParameter(PageConstants.PAGE_SIZE);
        if (pageSize == null) {
            pageSize = PageConstants.DEFAULT_SIZE;
        }
        return pageSize;
    }
    public int getPageCount(int entitiesCount, String pageSize){
        int pageCount;
        if (entitiesCount % Integer.parseInt(pageSize) == 0) {
            pageCount = entitiesCount / Integer.parseInt(pageSize);
        } else {
            pageCount = entitiesCount / Integer.parseInt(pageSize) + 1;
        }
        return pageCount;
    }
    public List<T> getEntitiesOnPage(List<T> products, int page, int pageSize){
        List<T> entitiesOnPage;
        if (products.size() < page * pageSize) {
            entitiesOnPage = products.subList(((page - 1) * pageSize), products.size());
        } else {
            entitiesOnPage = products.subList(((page - 1) * pageSize), page * pageSize);
        }
        return entitiesOnPage;
    }
}

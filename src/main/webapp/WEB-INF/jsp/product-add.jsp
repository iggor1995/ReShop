<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="i18n">
    <fmt:message key="product.pagetitle" var="pagetitle"/>
</fmt:bundle>
<%--@elvariable id="types" type="java.util.List"--%>
<%--@elvariable id="type" type="com.epam.igor.electronicsshop.entity.ProductType"--%>
<%--@elvariable id="product" type="com.epam.igor.electronicsshop.entity.Product"--%>
<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>

<my:page-pattern pagetitle="${pagetitle}">
    <div class="row row-offcanvas row-offcanvas-right" style="width: 1200px; margin: auto;">
        <my:user user="${loggedUser}"/>
        <div class="col-lg-10" align="center" style="padding-left: 100px">
            <my:product-add-form action="/do/add/product" product="${product}" types="${types}"/>
        </div>
    </div>
</my:page-pattern>
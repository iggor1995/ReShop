<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="i18n">
    <fmt:message key="common.price" var="price"/>
    <fmt:message key="common.quantity" var="quantity"/>
    <fmt:message key="error.amount" var="amountErrorMessage"/>
    <fmt:message key="common.addToCart" var="addToCart"/>
    <fmt:message key="common.characteristics" var="characteristicsLabel"/>
    <fmt:message key="common.description" var="descriptionLabel"/>
</fmt:bundle>

<%--@elvariable id="product" type="com.epam.igor.electronicsshop.entity.Product"--%>
<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>

<my:page-pattern pagetitle="${product.name}">
    <div class="row" style="width: 1200px; margin: auto">
        <my:user user="${loggedUser}"/>
        <div class="col-lg-10" align="center">
            <h4 align="center"><b>${product.name}</b></h4>
            <div class="col-lg-6">
                <img class="pull-left" style="width: 200px"; alt="image" src="<c:url value="/img/${product.id}"/>"/>
            </div>
            <div class="col-lg-6">
                <fmt:formatNumber var="formattedPrice" type="currency" currencyCode="KZT" maxFractionDigits="0"
                value="${product.price.amount}"/>
                <p>${price}: ${formattedPrice}</p>
                <form action="<c:url value="/do/cart/add"/>" method="post">
                    <input type="hidden" name="product" value="${product.id}">
                    <div class="form-group input-group">
                        ${quantity}: <input type="number" value="1" min="1" max="9999" name="amount" style="width: 100px;">
                        <c:if test="${not empty amountError}">
                            <p class="text-danger">${amountErrorMessage}</p>
                        </c:if>
                    </div>
                    <div class="form-group input-group">
                        <input type="submit" class="btn btn-default" value="${addToCart}">
                    </div>
                </form>
            </div>
            <div class="col-lg-12">
                <c:if test="${not empty product.characteristics}">
                    <hr>
                    <h4>${characteristicsLabel}</h4>
                </c:if>
                <c:forEach items="${product.characteristics}" var="itemCharacteristic">
                    <p>${itemCharacteristic.characteristic.getName(locale)}: ${itemCharacteristic.price}</p>
                </c:forEach>
                <hr>
                <h4>${descriptionLabel}</h4>
                <p align="center">${product.getDescription(locale)}</p>
            </div>
        </div>
    </div>
</my:page-pattern>
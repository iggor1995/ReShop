<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="i18n">
    <fmt:message key="order.ordername" var="orderLabel"/>
    <fmt:message key="common.customerData" var="customerData"/>
    <fmt:message key="common.apartmentNumber" var="apartmentNumber"/>
    <fmt:message key="common.buildingNumber" var="buildingNumber"/>
    <fmt:message key="common.street" var="street"/>
    <fmt:message key="common.firstName" var="firstName"/>
    <fmt:message key="common.lastName" var="lastName"/>
    <fmt:message key="common.gender_title" var="gender_title"/>
    <fmt:message key="common.phoneNumber" var="phoneNumber"/>
    <fmt:message key="common.email" var="email"/>
    <fmt:message key="common.city" var="city"/>
    <fmt:message key="common.country" var="country"/>
    <fmt:message key="common.role" var="role"/>
    <fmt:message key="common.address.title" var="addressTitle"/>
    <fmt:message key="common.product" var="product"/>
    <fmt:message key="common.quantity" var="quantity"/>
    <fmt:message key="common.price" var="price"/>
    <fmt:message key="common.total" var="total"/>
    <fmt:message key="button.back" var="back"/>
</fmt:bundle>

<%--@elvariable id="order" type="com.epam.igor.electronicsshop.entity.Order"--%>
<%--@elvariable id="user" type="com.epam.igor.electronicsshop.entity.User"--%>
<%--@elvariable id="locale" type="java.util.locale"--%>
<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>

<my:page-pattern pagetitle="${orderLabel} ${order.id}">
    <div class="row row-offcanvas row-offcanvas-right" style="width: 1200px;">
        <my:user user="${loggedUser}"/>
        <div class="col-lg-10">
            <div class="col-lg-4">
                <h4>${customerData}</h4>
                    <hr>
                    <p><b>${firstName}:</b> ${user.firstName}</p>
                    <p><b>${lastName}:</b> ${user.lastName}</p>
                    <p><b>${email}:</b> ${user.email}</p>
                    <p><b>${phoneNumber}:</b> ${user.phoneNumber}</p>
                    <p><b>${gender_title}:</b> ${user.gender.getName(locale)}</p>
                    <p><b>${role}:</b> ${user.role}</p>
                </div>
                <div class="col-lg-4">
                    <h4>${addressTitle}</h4>
                    <hr>
                    <p><b>${country}:</b> ${user.address.country}</p>
                    <p><b>${city}:</b> ${user.address.city}</p>
                    <p><b>${street}:</b> ${user.address.street}</p>
                    <p><b>${buildingNumber}:</b> ${user.address.buildingNumber}</p>
                    <p><b>${apartmentNumber}:</b> ${user.address.apartmentNumber}</p>
                </div>
                <div class="col-lg-12">
                    <h4>${orderLabel}</h4>
                    <table class="table table-bordered">
                        <thead>
                            <tr>${product}</tr>
                            <tr>${price}</tr>
                            <tr>${quantity}</tr>
                            <tr>${total}</tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${order.orderingItems}" var="item">
                                <tr>
                                    <td>${item.product.name}</td>
                                    <td>${item.amount}</td>
                                    <fmt:formatNumber var="formattedProductPrice" type="currency" currencyCode="KZT"
                                                      maxFractionDigits="0"
                                                      value="${item.product.price.amount}"/>
                                    <td>${formattedProductPrice}</td>
                                    <fmt:formatNumber var="formattedItemPrice" type="currency" currencyCode="KZT"
                                                      maxFractionDigits="0"
                                                      value="${item.price.amount}"/>
                                    <td>${formattedItemPrice}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <fmt:formatNumber var="formattedOrderPrice" type="currency" currencyCode="KZT"
                                      maxFractionDigits="0"
                                      value="${order.price.amount}"/>
                    <p class="pull-right"><b>${total}: ${formattedOrderPrice}</b></p><span class="pull-left">
                    <button type="button" class="btn btn-default" onclick="history.back()">${back}</button>
                </span>
            </div>
        </div>
    </div>
</my:page-pattern>
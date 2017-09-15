<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:bundle basename="i18n">
    <fmt:message key="user.edit" var="pagetitle"/>
    <fmt:message key="common.apartmentNumber" var="apartmentnumber"/>
    <fmt:message key="common.buildingNumber" var="buildingnumber"/>
    <fmt:message key="register.password" var="password"/>
    <fmt:message key="common.street" var="street"/>
    <fmt:message key="common.firstName" var="firstname"/>
    <fmt:message key="common.lastName" var="lastname"/>
    <fmt:message key="error.country" var="countryErrorMessage"/>
    <fmt:message key="error.city" var="cityErrorMessage"/>
    <fmt:message key="error.street" var="streetErrorMessage"/>
    <fmt:message key="error.buildingNumber" var="buildingNumberErrorMessage"/>
    <fmt:message key="error.apartmentNumber" var="apartmentNumberErrorMessage"/>
    <fmt:message key="button.save" var="save"/>
    <fmt:message key="button.cancel" var="cancel"/>
</fmt:bundle>

<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>

<my:page-pattern  pagetitle="${pagetitle}">
   <my:user user="${loggedUser}"/>
    <div class="col-lg-10" align="right" style="width: 1200px;">
        <my:product-add-form action="/do/edit/product" product="${product}" types="${types}"/>
    </div>
</my:page-pattern>

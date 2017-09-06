<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="i18n">
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
    <fmt:message key="profile.pagetitle" var="pagetitle"/>
</fmt:bundle>

<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>
<%--@elvariable id="address" type="com.epam.igor.electronicsshop.entity.Address"--%>
<my:page-pattern pagetitle="My profile">
    <div class="row row-offcanvas row-offcanvas-right" style="width: 1200px; margin: auto;">
        <my:user user="${loggedUser}"/>

        <div class="col-lg-10">
            <div class="col-lg-4">
                <p><b>${pagetitle}</b></p>
                <hr>
                <p><b>${firstName}: </b>${loggedUser.firstName}</p>
                <p><b>${lastName}: </b>${loggedUser.lastName}</p>
                <p><b>${gender_title}: </b>${loggedUser.gender.getName(locale)}</p>
                <p><b>${email}: </b>${loggedUser.email}</p>
                <p><b>${phoneNumber}: </b>${loggedUser.phoneNumber}</p>
                <p><b>${role}: </b>${loggedUser.role}</p>
            </div>
            <div class="col-lg-4">
                <p><b>${addressTitle}</b></p>
                <hr>
                <p><b>${country}: </b>${address.country}</p>
                <p><b>${city}: </b>${address.city}</p>
                <p><b>${street}: </b>${address.street}</p>
                <p><b>${buildingNumber}: </b>${address.buildingNumber}</p>
                <p><b>${apartmentNumber}: </b>${address.apartmentNumber}</p>
            </div>
        </div>
    </div>
</my:page-pattern>
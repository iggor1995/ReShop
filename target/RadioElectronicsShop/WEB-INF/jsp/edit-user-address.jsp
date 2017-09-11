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

<%--@elvariable id="locale" type="java.util.Locale"--%>
<%--@elvariable id="address" type="com.epam.igor.electronicsshop.entity.Address"--%>

<my:page-pattern  pagetitle="${pagetitle}">
    <div class="row row-offcanvas row-offcanvas-right" style="width: 1200px;">
        <div class="col-lg-10" align="center">
            <form role="form" action="<c:url value="/do/edit/userAddress"/>" method="post" style="width: 500px;">
                <div class="col-lg-6">
                    <input hidden name="addressId" value="${address.id}">
                    <div class="form-control input-group">
                        <label for="country">${country}</label>
                        <input type="text" class="form-control" id="country" name="country"
                               value="${address.country}">
                        <c:if test="${not empty countryError}">
                            <p class="text-danger"style="height: 10px;font-size: 12px;">
                                    ${countryErrorMessage}</p>
                        </c:if>
                    </div>
                    <div class="form-control input-group">
                        <label for="city">${city}</label>
                        <input type="text" class="form-control" id="city" name="city"
                               value="${address.city}">
                        <c:if test="${not empty cityError}">
                            <p class="text-danger"style="height: 10px;font-size: 12px;">
                                    ${cityErrorMessage}</p>
                        </c:if>
                    </div>
                    <div class="form-control input-group">
                        <label for="street">${street}</label>
                        <input type="text" class="form-control" id="street" name="street"
                               value="${address.street}">
                        <c:if test="${not empty streetError}">
                            <p class="text-danger"style="height: 10px;font-size: 12px;">
                                    ${streetErrorMessage}</p>
                        </c:if>
                    </div>
                    <div class="form-control input-group">
                        <label for="buildingNumber">${buildingnumber}</label>
                        <input type="text" class="form-control" id="buildingNumber" name="buildingNumber"
                               value="${address.buildingNumber}">
                        <c:if test="${not empty buildingNumberError}">
                            <p align="right" class="text-danger"style="height: 10px;font-size: 12px;">
                                    ${buildingNumberErrorMessage}</p>
                        </c:if>
                    </div>
                    <div class="form-control input-group">
                        <label for="apartmentNumber">${apartmentnumber}</label>
                        <input type="text" class="form-control" id="apartmentNumber" name="apartmentNumber"
                               value="${address.apartmentNumber}">
                        <c:if test="${not empty apartmentNumberError}">
                            <p align="right" class="text-danger"style="height: 10px;font-size: 12px;">
                                    ${apartmentNumberErrorMessage}</p>
                        </c:if>
                    </div>
                </div>
                <div class="col-lg-12">
                    <button type="submit" class="btn btn-default" style="width: 120px">${save}</button>
                    <a href="<c:url value="/do/user/profile"/>" class="btn btn-default">${cancel}</a>
                </div>
            </form>
        </div>
    </div>
</my:page-pattern>

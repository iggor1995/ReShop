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
    <fmt:message key="common.gender_title" var="gender_title"/>
    <fmt:message key="common.phoneNumber" var="phonenumber"/>
    <fmt:message key="common.email" var="email"/>
    <fmt:message key="common.city" var="city"/>
    <fmt:message key="common.country" var="country"/>
    <fmt:message key="common.button" var="button"/>
    <fmt:message key="common.role" var="role"/>
    <fmt:message key="error.email" var="emailErrorMessage"/>
    <fmt:message key="role.admin" var="adminRole"/>
    <fmt:message key="role.user" var="userRole"/>
    <fmt:message key="error.password" var="passwordErrorMessage"/>
    <fmt:message key="error.firstName" var="firstNameErrorMessage"/>
    <fmt:message key="error.lastName" var="lastNameErrorMessage"/>
    <fmt:message key="error.phoneNumber" var="phoneNumberErrorMessage"/>
    <fmt:message key="error.country" var="countryErrorMessage"/>
    <fmt:message key="error.city" var="cityErrorMessage"/>
    <fmt:message key="error.street" var="streetErrorMessage"/>
    <fmt:message key="error.buildingNumber" var="buildingNumberErrorMessage"/>
    <fmt:message key="error.apartmentNumber" var="apartmentNumberErrorMessage"/>
    <fmt:message key="button.save" var="save"/>
    <fmt:message key="button.cancel" var="cancel"/>
</fmt:bundle>

<%--@elvariable id="user" type="com.epam.igor.electronicsshop.entity.User"--%>
<%--@elvariable id="genders" type="java.util.List"--%>
<%--@elvariable id="gender" type="com.epam.igor.electronicsshop.entity.Gender"--%>
<%--@elvariable id="locale" type="java.util.Locale"--%>
<%--@elvariable id="address" type="com.epam.igor.electronicsshop.entity.Address"--%>

<my:page-pattern  pagetitle="${pagetitle}">
    <div class="row row-offcanvas row-offcanvas-right" style="width: 1200px;">
        <div class="col-lg-10" align="center">
            <form role="form" action="<c:url value="/do/edit/user"/>" method="post" style="width: 500px;">
                <div class="6" align="center">
                    <input hidden name="userId" value="${user.id}">
                    <div class="form-control input-group">
                        <label for="email">${email}</label>
                        <input type="email" class="form-control" id="email" name="email"
                               value="${user.email}">
                        <c:if test="${not empty emailError}">
                            <p align="right" class="text-danger"style="height: 10px;font-size: 12px;">
                            ${emailErrorMessage}</p>
                        </c:if>
                    </div>
                    <div class="form-control input-group">
                        <label for="password">${password}</label>
                        <input type="password" class="form-control" id="password" name="password"
                               value="${user.password}">
                        <c:if test="${not empty passwordError}">
                            <p class="text-danger"style="height: 10px;font-size: 12px;">${passwordErrorMessage}</p>
                        </c:if>
                    </div>
                    <div class="form-control input-group">
                        <label for="firstName">${firstname}</label>
                        <input type="text" class="form-control" id="firstName" name="firstName"
                               value="${user.firstName}">
                        <c:if test="${not empty firstNameError}">
                            <p class="text-danger"style="height: 10px;font-size: 12px;">
                            ${firstNameErrorMessage}</p>
                        </c:if>
                    </div>
                    <div class="form-control input-group">
                        <label for="lastName">${lastname}</label>
                        <input type="text" class="form-control" id="lastName" name="lastName"
                               value="${user.lastName}">
                        <c:if test="${not empty lastNameError}">
                            <p class="text-danger"style="height: 10px;font-size: 12px;">
                            ${lastNameErrorMessage}</p>
                        </c:if>
                    </div>
                    <div class="form-control input-group">
                        <label for="phoneNumber">${phonenumber}</label>
                        <input type="text" class="form-control" id="phoneNumber" name="phoneNumber"
                               value="${user.phoneNumber}">
                        <c:if test="${not empty phoneNumberError}">
                            <p class="text-danger"style="height: 10px;font-size: 12px;">
                            ${phoneNumberErrorMessage}</p>
                        </c:if>
                    </div>
                    <div class="form-control input-group">
                        <label for="gender">${gender_title}:</label>
                        <select  class="form-control" id="gender" name="gender">
                            <c:forEach items="${genders}" var="gender">
                                <option value="${gender.id}" <c:if test="${user.gender.equals(gender)}">
                                selected
                            </c:if>>${gender.getName(locale)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
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
                    <div class="form-group input-group">
                        <label for="role">${role}</label>
                        <p><input type=radio class="radio-button" id="role" name="role"
                                  value="admin" <c:if test="${user.role.name().equals('admin')}">checked</c:if>><span
                                style="margin-left: 20px">${adminRole}</span>
                            <input style="margin-left: 20px" type=radio class="radio-button" name="role"
                                   value="user" <c:if test="${user.role.name().equals('user')}">checked</c:if>><span
                                    style="margin-left: 10px">${userRole}</span></p>
                    </div>
                </div>
                <div class="col-lg-12">
                    <button type="submit" class="btn btn-default" style="width: 120px">${save}</button>
                    <a href="<c:url value="/do/manage/users"/>" class="btn btn-default">${cancel}</a>
                </div>
            </form>
        </div>
    </div>
</my:page-pattern>

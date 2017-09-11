<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:bundle basename="i18n">
    <fmt:message key="user.edit" var="pagetitle"/>
    <fmt:message key="register.password" var="password"/>
    <fmt:message key="common.firstName" var="firstname"/>
    <fmt:message key="common.lastName" var="lastname"/>
    <fmt:message key="common.gender_title" var="gender_title"/>
    <fmt:message key="common.phoneNumber" var="phonenumber"/>
    <fmt:message key="common.email" var="email"/>
    <fmt:message key="error.email" var="emailErrorMessage"/>
    <fmt:message key="error.password" var="passwordErrorMessage"/>
    <fmt:message key="error.firstName" var="firstNameErrorMessage"/>
    <fmt:message key="error.lastName" var="lastNameErrorMessage"/>
    <fmt:message key="error.phoneNumber" var="phoneNumberErrorMessage"/>
    <fmt:message key="button.save" var="save"/>
    <fmt:message key="button.cancel" var="cancel"/>
</fmt:bundle>


<%--@elvariable id="user" type="com.epam.igor.electronicsshop.entity.User"--%>
<%--@elvariable id="genders" type="java.util.List"--%>
<%--@elvariable id="gender" type="com.epam.igor.electronicsshop.entity.Gender"--%>
<%--@elvariable id="locale" type="java.util.Locale"--%>

<my:page-pattern  pagetitle="${pagetitle}">
    <div class="row row-offcanvas row-offcanvas-right" style="width: 1200px;">
        <div class="col-lg-10" align="center">
            <form role="form" action="<c:url value="/do/edit/userData"/>" method="post" style="width: 500px;">
                <div class="6" align="center">
                    <input hidden name="userId" value="${user.id}">
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
                <div class="col-lg-12">
                    <button type="submit" class="btn btn-default" style="width: 120px">${save}</button>
                    <a href="<c:url value="/do/user/profile"/>" class="btn btn-default">${cancel}</a>
                </div>
            </form>
        </div>
    </div>
</my:page-pattern>

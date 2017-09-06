<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:url value="/do/register" var="register_url"/>

<my:page-pattern  pagetitle="Register">
    <fmt:message key="register" var="register"/>
    <fmt:message key="common.apartmentNumber" var="apartmentNumber"/>
    <fmt:message key="common.buildingNumber" var="buildingNumber"/>
    <fmt:message key="register.password" var="password"/>
    <fmt:message key="common.street" var="street"/>
    <fmt:message key="common.firstName" var="firstName"/>
    <fmt:message key="common.lastName" var="lastName"/>
    <fmt:message key="common.gender_title" var="gender_title"/>
    <fmt:message key="common.phoneNumber" var="phoneNumber"/>
    <fmt:message key="common.email" var="email"/>
    <fmt:message key="common.city" var="city"/>
    <fmt:message key="common.country" var="country"/>
    <fmt:message key="common.button" var="button"/>

    <%--@elvariable id="gender" type="com.epam.igor.electronicsshop.entity.Gender"--%>
    <%--@elvariable id="genders" type="java.util.List"--%>

    <div class="container">
        ${register}<br/>
        <form role="form" action="${register_url}" method="POST">
            <div class="form-group">
                <input type="text" class="form-control" placeholder="${email}" name="email">
            </div>
            <div class="form-group">
                <input type="password" class="form-control" placeholder="${password}" name="password">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder="${firstName}" name="firstName">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder="${lastName}" name="lastName">
            </div>
            <div class="form-group">
                <input type="number" class="form-control" placeholder="${phoneNumber}" name="phoneNumber">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder="${country}" name="country">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder="${city}" name="city">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder="${street}" name="street">
            </div>
            <div class="form-group">
                <input type="number" class="form-control" placeholder="${buildingNumber}" name="buildingNumber">
            </div>
            <div class="form-group">
                <input type="number" class="form-control" placeholder="${apartmentNumber}" name="apartmentNumber">
            </div>
            <div class="col-lg-12">
                <div class="form-group input-group" style="width: 120px">
                    <label for="gender">${gender_title}</label>
                    <select class="form-control" id="gender" name="gender">
                        <c:forEach items="${genders}" var="genderItem">
                            <option value="${genderItem.id}" <c:if
                                    test="${genderItem.id==gender}"> selected </c:if>
                            >${genderItem.getName(locale)}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <input type="submit" class="btn btn-default" value=${button}><br/>
        </form>
    </div>
</my:page-pattern>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@tag description="User menu" pageEncoding="UTF-8" %>
<%@attribute name="user" required="true" type="com.epam.igor.electronicsshop.entity.User" %>

<fmt:bundle basename="i18n">
    <fmt:message key="user.greetings" var="greetings"/>
    <fmt:message key="user.balance" var="balance"/>
    <fmt:message key="profile.myprofile" var="myprofile"/>
    <fmt:message key="profile.orders" var="orders"/>
    <fmt:message key="orders.manage.pagetitle" var="manageOrders"/>
    <fmt:message key="users.manage.pagetitle" var="manageUsers"/>
    <fmt:message key="storage.manage.pagetitle" var="manageStorage"/>
    <fmt:message key="manage.products" var="manageProducts"/>
    <fmt:message key="common.button.add" var="add"/>
</fmt:bundle>
<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>

<c:if test="${loggedUser.role == 'user'}">
    <div class="col-lg-2 sidebar-offcanvas" id="sidebar">
        <div class="list-group">
            <p align="center">${greetings} ${loggedUser.firstName}</p>
            <fmt:formatNumber var="formattedBalance" type="currency" currencyCode="KZT"
            maxFractionDigits="0" value="${loggedUser.cash.amount}"/>
            <p align="center">${balance} : ${formattedBalance}</p>
            <a href="<c:url value="/do/user/profile"/>" class="list-group-item">${myprofile}</a>
            <a href="<c:url value="/do/user/orders"/>" class="list-group-item">${orders}</a>
        </div>
    </div>
</c:if>
<c:if test="${loggedUser.role == 'admin'}">
    <div class="col-lg-2 sidebar-offcanvas" id="sidebar">
        <div class="list-group">
            <p align="center">${greetings} ${loggedUser.firstName}</p>
            <fmt:formatNumber var="formattedBalance" type="currency" currencyCode="KZT"
                              maxFractionDigits="0" value="${loggedUser.cash.amount}"/>
            <p align="center">${balance} : ${formattedBalance}</p>
            <a href="<c:url value="/do/user/profile"/>" class="list-group-item">${myprofile}</a>
            <a href="<c:url value="/do/user/orders"/>" class="list-group-item">${orders}</a>
            <a href="<c:url value="/do/manage/users"/>" class="list-group-item">${manageUsers}</a>
            <a href="<c:url value="/do/manage/orders"/>" class="list-group-item">${manageOrders}</a>
            <a href="<c:url value="/do/manage/storage"/>" class="list-group-item">${manageStorage}</a>
            <a href="<c:url value="/do/manage/products"/>" class="list-group-item">${manageProducts}</a>

        </div>
    </div>
</c:if>
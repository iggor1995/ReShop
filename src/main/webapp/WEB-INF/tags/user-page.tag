<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="user" required="true" type="com.epam.igor.electronicsshop.entity.User"%>

<fmt:bundle basename="i18n">
    <fmt:message key="user.greetings" var="greetings"/>
    <fmt:message key="user.balance" var="balance"/>
</fmt:bundle>

<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>
<c:if test="${loggedUser.role == 'user'}">
    <div class="col-lg-2 sidebar-offcanvas" id="sidebar">
        <div class="list-group">
            <p align="center">${greetings} ${loggedUser.firstName}</p>
            <fmt:formatNumber var="formattedBalance" type="currency" currencyCode="KZT" maxFractionDigits="0"
                              value="${loggedUser.cash.amount}"/>
            <p align="center">${balance}: ${formattedBalance}</p>
            <a href="<c:url value="/do/user/profile"/>" class="list-group-item">${myprofile}</a>
            <a href="<c:url value="/do/user/orders"/>" class="list-group-item">${myorders}</a>
        </div>
    </div>
</c:if>
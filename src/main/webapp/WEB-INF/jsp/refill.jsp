<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:bundle basename="i18n">
    <fmt:message key="pattern.welcome" var="welcome"/>
    <fmt:message key="common.email" var="email"/>
    <fmt:message key="user.balance" var="balance"/>
    <fmt:message key="balance.error" var="error"/>
    <fmt:message key="button.save" var="save"/>
    <fmt:message key="refill.pagetitle" var="pagetitle"/>
</fmt:bundle>

<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>
<%--@elvariable id="user" type="com.epam.igor.electronicsshop.entity.User"--%>

<my:page-pattern pagetitle="${pagetitle}">
    <my:user user="${loggedUser}"/>
    <div align="center">
        <form role="form" action="<c:url value="/do/refill/user"/>" method="post">
            <input hidden name="userId" value="${user.id}"/>
            <div class="col-lg-2 sidebar-offcanvas" id="sidebar">
                <div class="list-group">
                    <p align="center">${user.firstName}</p>
                    <fmt:formatNumber var="formattedBalance" type="currency" currencyCode="KZT"
                                      maxFractionDigits="0" value="${user.cash.amount}"/>
                    <p align="center">${balance} : ${formattedBalance}</p>
                </div>
            </div>
            <div class="form-group input-group" style="width: 300px">
                <span class="input-group-addon"><span class="glyphicon glyphicon-arrow-up"></span></span>
                <input type="number" class="form-control" placeholder="${balance}" name="balance">
            </div>
            <div class="form-group">
                <c:if test="${not empty balanceError}">
                    <p class="alert-danger" style="width: 250px;height: 30px;padding: 5px">${error}</p>
                </c:if>
                <button type="submit" class="btn bg-primary btn-primary-spacing" style="width: 200px">
                        ${save}
                </button>
            </div>
        </form>
    </div>
</my:page-pattern>
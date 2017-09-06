<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="i18n">
    <fmt:message key="userOrders.pagetitle" var="pagetitle"/>
    <fmt:message key="userOrders.cart" var="cart_title"/>
    <fmt:message key="common.empty" var="empty_title"/>
    <fmt:message key="common.product" var="product"/>
    <fmt:message key="common.quantity" var="quantity"/>
    <fmt:message key="common.price" var="price"/>
    <fmt:message key="common.total" var="total"/>
    <fmt:message key="button.remove" var="remove"/>
    <fmt:message key="common.description" var="description"/>
    <fmt:message key="order.status" var="status"/>
    <fmt:message key="button.details" var="details"/>
    <fmt:message key="button.save" var="b_save"/>
    <fmt:message key="button.close" var="b_close"/>
</fmt:bundle>
<%--@elvariable id="pagesCount" type="java.lang.Integer"--%>
<%--@elvariable id="orders" type="java.util.List"--%>
<%--@elvariable id="order" type="com.epam.igor.electronicsshop.entity.Order"--%>
<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>
<%--@elvariable id="locale" type="java.util.Locale"--%>

<my:page-pattern pagetitle="${pagetitle}">
    <div class="row row-offcanvas row-offcanvas-right" style="width: 1200px">
        <my:user user="${loggedUser}"/>
        <div class="col-lg-10" align="center">
            <my:pagination url="/do/user/orders" pagesCount="${pagesCount}"/>
            <c:choose>
                <c:when test="${empty orders}">
                    <p>${empty_title}</p>
                </c:when>
                <c:otherwise>
                    <table class="table table-bordered">
                        <thead>
                            <tr>${created}</tr>
                            <tr>${description}</tr>
                            <tr>${price}</tr>
                            <tr>${status}</tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${orders}" var="order">
                                <fmt:formatNumber var="formattedPrice" type="currency" currencyCode="KZT"
                                                  maxFractionDigits="0" value="${order.price.amount}"/>
                                <tr>
                                    <td>${order.formattedCreationTime}</td>
                                    <td>${order.description}</td>
                                    <td>${formattedPrice}</td>
                                    <td>${order.status.getName(locale)}</td>
                                    <td><a class="btn btn-default" href="<c:url value="/do/order?id=${order.id}"/>"
                                    >${details}</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</my:page-pattern>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="i18n">
    <fmt:message key="orders.manage.pagetitle" var="pagetitle"/>
    <fmt:message key="common.product" var="product"/>
    <fmt:message key="common.amount" var="amount"/>
    <fmt:message key="common.price" var="price"/>
    <fmt:message key="common.created" var="created"/>
    <fmt:message key="order.customer" var="customer"/>
    <fmt:message key="common.description" var="description"/>
    <fmt:message key="order.status" var="status"/>
    <fmt:message key="button.details" var="details"/>
    <fmt:message key="common.button.delete" var="delete"/>
</fmt:bundle>

<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>
<%--@elvariable id="orders" type="java.util.List"--%>
<%--@elvariable id="order" type="com.epam.igor.electronicsshop.entity.Order"--%>
<%--@elvariable id="pagesCount" type="java.lang.Integer"--%>
<%--@elvariable id="statuses" type="java.util.List"--%>
<%--@elvariable id="status" type="com.epam.igor.electronicsshop.entity.OrderStatus"--%>
<%--@elvariable id="locale" type="java.util.Locale"--%>

<my:page-pattern pagetitle="${pagetitle}">
    <div class="row row-offcanvas row-offcanvas-right" style="width: 1200px; margin: auto">
        <my:user user="${loggedUser}"/>
        <div class="col-lg-10" align="center">
            <my:pagination url="/do/manage/orders" pagesCount="${pagesCount}"/>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>${created}</th>
                    <th>${customer}</th>
                    <th>${description}</th>
                    <th>${price}</th>
                    <th>${status}</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${orders}" var="order">
                    <tr>
                        <td>${order.formattedCreationTime}</td>
                        <td>${order.user.firstName}</td>
                        <td>${order.description}</td>
                        <fmt:formatNumber var="formattedPrice" type="currency" currencyCode="KZT"
                                          maxFractionDigits="0"
                                          value="${order.price.amount}"/>
                        <td>${formattedPrice}</td>
                        <td>
                            <div class="form-group input-group">
                                <form action="<c:url value="/do/edit/orderStatus"/>" method="post">
                                    <input hidden name="orderId" value="${order.id}">
                                    <select class="form-control" id="status" name="statusId"
                                            onchange="this.form.submit()">
                                        <c:forEach items="${statuses}" var="status">
                                            <option value="${status.id}"<c:if
                                                    test="${order.status.equals(status)}"> selected
                                            </c:if>>${status.getName(locale)}</option>
                                        </c:forEach>
                                    </select>
                                </form>
                            </div>
                        </td>
                        <td style="width: 100px"><a class="btn btn-default"
                                                    href="<c:url value="/do/order?id=${order.id}"/>"
                        >${details}</a>
                            <a class="btn btn-default"
                               href="<c:url value="/do/delete/order?id=${order.id}"/>"
                            >${delete}</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</my:page-pattern>

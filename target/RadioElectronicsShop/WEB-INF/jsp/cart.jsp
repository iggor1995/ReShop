<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="i18n">
    <fmt:message key="cart.pagetitle" var="pagetitle"/>
    <fmt:message key="common.empty" var="empty_title"/>
    <fmt:message key="common.product" var="product"/>
    <fmt:message key="common.quantity" var="quantity"/>
    <fmt:message key="common.price" var="price"/>
    <fmt:message key="common.total" var="total"/>
    <fmt:message key="common.button.delete" var="delete"/>
    <fmt:message key="common.total" var="total_price"/>
    <fmt:message key="button.placeorder" var="placeorder"/>
    <fmt:message key="button.clear" var="clearcart"/>
    <fmt:message key="button.addDescription" var="addDescription"/>
    <fmt:message key="description.title" var="modalTitle"/>
    <fmt:message key="profile.orders" var="orders_title"/>
    <fmt:message key="common.created" var="created"/>
    <fmt:message key="common.description" var="description"/>
    <fmt:message key="order.status" var="status"/>
    <fmt:message key="error.amount" var="amount_error_message"/>
    <fmt:message key="button.details" var="details"/>
    <fmt:message key="order.recount" var="recount"/>
    <fmt:message key="button.save" var="save"/>
    <fmt:message key="button.close" var="close"/>
</fmt:bundle>
<%--@elvariable id="cart" type="com.epam.igor.electronicsshop.entity.Order"--%>
<%--@elvariable id="errorMap" type="java.util.Map"--%>
<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>

<my:page-pattern pagetitle="${pagetitle}">
    <div class="row row-offcanvas row-offcanvas-right">
        <my:user user="${loggedUser}"/>
        <div class="col-lg-10" align="center">
            <c:choose>
                <c:when test="${empty cart.orderingItems}">
                    <p>${empty_title}</p>
                </c:when>
                <c:otherwise>
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>${product}</th>
                                <th>${quantity}</th>
                                <th>${price}</th>
                                <th>${total_price}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <form action="<c:url value="/do/cart/recount"/>" method="post">
                                <c:forEach items="${cart.orderingItems}" var="item" varStatus="itemRow">
                                    <tr>
                                        <td>${item.product.name}</td>
                                        <td><input type="number" min="1" max="999" value="${item.amount}"
                                                   style="width:100px">
                                            <c:if test="${errorMap.get(itemRow.index).equals('true')}">
                                                <p class="text-danger"
                                                   style="height: 20px;font-size: 12px;">${amount_error_message}</p>
                                            </c:if></td>
                                        <fmt:formatNumber var="formattedProductPrice" type="currency" currencyCode="KZT"
                                                          maxFractionDigits="0"
                                                          value="${item.product.price.amount}"/>
                                        <td>${formattedProductPrice}</td>
                                        <fmt:formatNumber var="formattedItemPrice" type="currency" currencyCode="KZT"
                                                          maxFractionDigits="0"
                                                          value="${item.price.amount}"/>
                                        <td>${formattedItemPrice}</td>
                                        <td>
                                            <a class="btn btn-default"
                                               href="<c:url value="/do/delete/orderingItem?item=${itemRow.index}"/>"
                                            >${delete}
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <tr>
                                    <td colspan="3" align="cright">${total_price}</td>
                                    <fmt:formatNumber var="formattedCartPrice" type="currency" currencyCode="KZT"
                                                      maxFractionDigits="0"
                                                      value="${cart.price.amount}"/>
                                    <td><b>${formattedCartPrice}</b></td>
                                </tr>
                            </form>
                        </tbody>
                    </table>
                    <div align="center">
                        <a class="btn btn-default" href="<c:url value="/do/cart/buy"/>">${placeorder}</a>
                        <a class="btn btn-default" href="<c:url value="/do/cart/clear"/>">${clearcart}</a>
                        <button type="button" class="btn btn-default" data-toggle="modal"
                                data-target="#Description">${addDescription}</button>
                    </div>
                </c:otherwise>
            </c:choose>
            <div class="modal fade" id="Description" role="dialog">
                <div class="modal-dialog" style="margin: 100px">
                    <div class="modal-content" style="width: 440px">
                        <div class="modal-header">
                            <h4 class="modal-title">${modalTitle}</h4>
                        </div>
                        <div class="modal-body">
                            <form action="<c:url value="/do/cart/description"/>"method="post">
                                <textarea name="description" style="width: 400px;height: 100px;">
                                ${cart.description}</textarea>
                                </textarea>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-default">${save}</button>
                                    <button type="button" class="btn btn-default" data-dismiss="modal">
                                    ${save}</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</my:page-pattern>
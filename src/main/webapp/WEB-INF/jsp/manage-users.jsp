<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="i18n">
    <fmt:message key="common.password" var="password"/>
    <fmt:message key="common.firstName" var="firstName"/>
    <fmt:message key="common.lastName" var="lastName"/>
    <fmt:message key="common.gender_title" var="gender_title"/>
    <fmt:message key="common.phoneNumber" var="phoneNumber"/>
    <fmt:message key="user.balance" var="balance_label"/>
    <fmt:message key="common.email" var="email"/>
    <fmt:message key="common.role" var="role"/>
    <fmt:message key="refill.balance" var="refill"/>
    <fmt:message key="users.manage.pagetitle" var="pagetitle"/>
    <fmt:message key="common.button.edit" var="edit"/>
    <fmt:message key="common.button.delete" var="delete"/>
</fmt:bundle>

<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>
<%--@elvariable id="user" type="com.epam.igor.electronicsshop.entity.User"--%>
<%--@elvariable id="pagesCount" type="java.lang.Integer"--%>
<%--@elvariable id="users" type="java.util.List"--%>
<%--@elvariable id="locale" type="java.util.Locale"--%>

<my:page-pattern pagetitle="${pagetitle}">
    <div class="row row-offcanvas row-offcanvas-right" style="width: 1200px; margin: auto">
        <my:user user="${loggedUser}"/>
        <div class="col-lg-10" align="center">
            <my:pagination url="/do/manage/users" pagesCount="${pagesCount}"/>
            <table class="table table-bordered" style="font-size: 14px">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>${email}</th>
                    <th>${password}</th>
                    <th>${firstName}</th>
                    <th>${lastName}</th>
                    <th>${role}</th>
                    <th>${gender_title}</th>
                    <th>${phoneNumber}</th>
                    <th>${balance_label}</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${users}" var="user">
                    <fmt:formatNumber var="formattedPrice" type="currency" currencyCode="KZT"
                                      maxFractionDigits="0" value="${user.cash.amount}"/>
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.email}</td>
                        <td>${user.password}</td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td>${user.role}</td>
                        <td>${user.gender.getName(locale)}</td>
                        <td>${user.phoneNumber}</td>
                        <td>${formattedPrice}</td>
                        <td><a class="btn btn-default"
                               href="<c:url value="/do/edit/user?id=${user.id}"/>"
                        >${edit}</a>
                            <a class="btn btn-default"
                               href="<c:url value="/do/refill/user?id=${user.id}"/>"
                            >${refill}</a>
                            <a class="btn btn-default"
                               href="<c:url value="/do/delete/user?id=${user.id}"/>"
                            >${delete}</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${deleteError.equals('true')}">
                <p class="text-danger"
                   style="height: 10px;font-size: 12px;">${delete_error_message}</p>
            </c:if>
        </div>
    </div>
</my:page-pattern>

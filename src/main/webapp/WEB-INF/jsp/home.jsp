<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="i18n">
   <fmt:message key="common.companyname" var="companyName"/>
</fmt:bundle>
<%--@elvariable id="pagesCount" type="java.lang.Integer"--%>
<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>
<%--@elvariable id="products" type="java.util.List"--%>

<my:page-pattern pagetitle="Home Page">
    <div class="row row-offcanvas row-offcanvas-right" style="width: 1200px; margin: auto;">
        <my:user user="${loggedUser}"/>

        <div class="col-lg-10" align="center">
            <div>
                <p align="center" style="font-size: 18px"><b>${companyName}</b></p>
                <hr>
                <my:pagination pagesCount="${pagesCount}" url="/do/home"/>
            </div>
        </div>
        <div class="row">
            <hr>
            <hr>
            <c:forEach items="${products}" var="product">
                <div class="col-lg-4">
                    <my:product product="${product}"/>
                </div>
            </c:forEach>
        </div>
    </div>

</my:page-pattern>

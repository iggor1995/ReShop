<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="i18n">
    <fmt:message key="welcome.register" var="register"/>
    <fmt:message key="register.gotoregister" var="gotoregister"/>
    <fmt:message key="login.getin" var="login"/>
</fmt:bundle>

<c:url var="login_url" value="/do/login"/>
<c:url var="register_url" value="/do/register"/>

<my:page-pattern pagetitle="REShop">
    <div class="container">
        <div class="col-lg-10" align="center">
            <a href="${login_url}" class="btn btn-default">${login}</a>
            <a href="${register_url}" class="btn btn-default">${gotoregister}</a>
        </div>
        <div class="col-lg-10" align="center">
            <my:pagination url="/do/welcome" pagesCount="${pagesCount}"/>
            <div class="row">
                <c:forEach items="${products}" var="product">
                    <div class="col-lg-4">
                        <my:product product="${product}"/>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

</my:page-pattern>
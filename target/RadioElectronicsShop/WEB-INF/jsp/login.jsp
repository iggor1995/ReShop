<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:bundle basename="i18n">
    <fmt:message key="pattern.welcome" var="welcome"/>
    <fmt:message key="common.email" var="email"/>
    <fmt:message key="common.password" var="password"/>
    <fmt:message key="login.error" var="error"/>
    <fmt:message key="login.getin" var="getin"/>
    <fmt:message key="register" var="register_label"/>
</fmt:bundle>
<c:url var="login_url" value="/do/login"/>
<c:url var="register_url" value="/do/register"/>

<my:page-pattern pagetitle="Log in">
    <div align="center">
        <div class="h3">${welcome}</div>
        <form role="form" action="${login_url}" method="post">
            <div class="form-group input-group" style="width: 300px">
                <span class="input-group-addon" id="basic-addon1">@</span>
                <input type="text" class="form-control" aria-describedby="basic-addon1" placeholder="${email}"
                       name="email">
            </div>
            <div class="form-group input-group" style="width: 300px">
                <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
                <input type="password" class="form-control" placeholder="${password}" name="password">
            </div>
            <div class="form-group">
                <c:if test="${not empty loginError}">
                    <p class="alert-danger" style="width: 250px;height: 30px;padding: 5px">${error}</p>
                </c:if>
                <button type="submit" class="btn bg-primary btn-primary-spacing" style="width: 200px">
                    ${getin}
                </button>
            </div>
        </form>
        <a class="btn btn-default"
           href="<c:url value="/do/register"/>"
        >${register_label}</a>
    </div>
</my:page-pattern>
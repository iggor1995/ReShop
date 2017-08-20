<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:message key="welcome.register" var="register"/>
<c:url var="register_url" value="/do/register"/>

<my:page-pattern role="guest"/>
<div class="container">
    <div class="col-lg-10" align="center">
        <a class="btn btn-default" role="button"
           href="${register_url}">${register}</a>
    </div>
</div>


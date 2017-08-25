<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<fmt:message key="welcome.register" var="register"/>
<c:url var="register_url" value="/do/register"/>

<my:page-pattern role="guest">
    <div class="container">
        <div class="col-lg-10" align="center">
            <a href="${register_url}" class="btn btn-default">Go to Registration</a>
        </div>
    </div>
</my:page-pattern>
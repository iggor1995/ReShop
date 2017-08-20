<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="role" required="true" %>

<fmt:setBundle basename="i18n" scope="request"/>
<fmt:message key="pattern.welcome" var="welcome"/>

<c:url value="/do/home"/>
<c:url value="/do/locale?locale=en" var="en_locale_url"/>
<c:url value="/do/locale?locale=ru" var="ru_locale_url"/>
<html>
    <head>
        <title>RadioStore</title>
        <link rel="stylesheet" href="<c:url value="webjars/bootstrap/3.3.7/css/bootstrap.css"/>">
    </head>
</html>

<div id="header">
    <div align="center" style="width: 1200px;margin: auto; color: #66afe9">
        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <div class="navbar-header">
                    <c:if test="${role.equals('guest')}">
                        <a class="row" align="left">
                            <div class="col-md-10 col-lg-10">
                                <h3>${welcome}</h3>
                            </div>
                            <ul class="dropdown-menu" style="min-width: 60px">
                            <a href="<c:url value="/do/locale?locale=ru"/>" class="language"><img
                                    style="height: 18px"
                                    src="<c:url value="/images/ru.png"/>" alt="Ru"/> Ru</a>
                            <a href="<c:url value="/do/locale?locale=en"/>" class="language"><img
                                    style="height: 18px"
                                    src="<c:url value="/images/us.png"/>" alt="En"/> En</a>
                            </ul>

                    </c:if>
                </div>
            </div>
        </nav>
    </div>
</div>

<div id="body" style="height: 100%">
    <jsp:doBody/>
</div>

<div id="footer" style="flex: 0 0 auto">
    <footer class="modal-footer" style="height: 80px; position: relative;">
        <ruby>Lapin Igor
            <rt>Author</rt>
        </ruby>
    </footer>
</div>
</body>
</html>
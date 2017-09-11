<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="pagetitle" required="true" %>

<%--@elvariable id="cart" type="com.epam.igor.electronicsshop.entity.Order"--%>
<%--@elvariable id="type" type="com.epam.igor.electronicsshop.entity.ProductType"--%>
<%--@elvariable id="locale" type="java.util.Locale"--%>
<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>
<%--@elvariable id="productTypes" type="java.util.List"--%>

<fmt:bundle basename="i18n">
    <fmt:message key="pattern.welcome" var="welcome"/>
    <fmt:message key="user.greetings" var="greetings"/>
    <fmt:message key="user.balance" var="balance"/>
    <fmt:message key="profile.myprofile" var="myprofile"/>
    <fmt:message key="common.products" var="products"/>
    <fmt:message key="common.empty" var="emptyLabel"/>

</fmt:bundle>

<c:url value="/do/locale?locale=en" var="en_locale_url"/>
<c:url value="/do/locale?locale=ru" var="ru_locale_url"/>
<head>
    <title>${pagetitle}</title>
    <link rel="stylesheet" href="<c:url value="/css/bootstrap.min.css"/>">
    <script src="<c:url value="/js/jquery-1.12.2.min.js"/>"></script>
    <script src="<c:url value="/js/jquery-2.2.2.min.js"/>"></script>
    <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
</head>
<body>
<div id="header">
    <div align="center" style="width: 1200px;margin: auto">
        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand" href="<c:url value="/do/home"/>"><span
                            class="glyphicon glyphicon-home"></span></a>
                    <c:if test="${loggedUser.role == 'user' || loggedUser.role == 'admin'}">
                        <a class="navbar-brand" href="<c:url value="/do/logout"/>"><span
                                class="glyphicon glyphicon-exclamation-sign"></span></a>
                    </c:if>

                </div>
                <ul class="nav navbar-nav">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">${products}<span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <c:forEach items="${productTypes}" var="type">
                                <li>
                                    <a href="<c:url value="/do/catalog?type=${type.id}"/>">${type.getName(locale)}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </li>
                    <div class="col-lg-1" align="right">
                        <li class="dropdown-menu-right" style="min-width: 60px">
                            <a href="<c:url value="/do/locale?locale=ru"/>" class="language"><img
                                    style="height: 18px"
                                    src="<c:url value="/images/ru.png"/>" align="right" alt="Ru"/> Ru</a>
                            <a href="<c:url value="/do/locale?locale=en"/>" class="language"><img
                                    style="height: 18px"
                                    src="<c:url value="/images/us.png"/>" align="right" alt="En"/> En</a>
                        </li>
                    </div>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a href="<c:url value="/do/cart"/>"><span class="glyphicon glyphicon-shopping-cart"></span>
                            <c:choose>
                                <c:when test="${empty cart.orderingItems || cart.orderingItems.size()==0}">
                                    (${emptyLabel})</c:when>
                                <c:otherwise>${cartLabel} (${cart.orderingItems.size()})</c:otherwise>
                            </c:choose>
                        </a>
                    </li>
                </ul>
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
<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="url" required="true" %>
<%@attribute name="pagesCount" required="true" %>
<%@attribute name="attributeName" required="false" %>
<%@attribute name="attributeValue" required="false" %>

<fmt:bundle basename="i18n">
    <fmt:message key="common.pageNumber" var="pageNumber"/>
</fmt:bundle>
<%--@elvariable id="page" type="java.lang.Integer"--%>
<%--@elvariable id="pageSize" type="java.lang.Integer"--%>
<nav>
    <ul class="pagination">
        <c:if test="${pagesCount>1}">
            <c:forEach begin="1" end="${pagesCount}" varStatus="loop">
                <li <c:if test="${page==loop.count}">class="active" </c:if>>
                    <a href="<c:url value="${url}">
                <c:if test="${not empty attributeName && not empty attributeValue}">
                    <c:param name="${attributeName}" value="${attributeValue}"/>
                    </c:if>
                    <c:param name="page" value="${loop.count}"/>
                    <c:param name="pageSize" value="${pageSize}"/>
                    </c:url>" title="${pageNumber}">${loop.count}</a>
                </li>
            </c:forEach>
        </c:if>
        <form name="form" method="get">
            <c:if test="${not empty attributeName && not empty attributeValue}">
                <input hidden name="${attributeName}" value="${attributeValue}">
            </c:if>
            <select style="margin-top: 10px" name="pageSize" onchange="this.form.submit()" title="${size}">
                <option value="2" <c:if test="${pageSize==2}"> selected </c:if>>2</option>
                <option value="4" <c:if test="${pageSize==4}"> selected </c:if>>4</option>
                <option value="8" <c:if test="${pageSize==8}"> selected </c:if>>8</option>
                <option value="16" <c:if test="${pageSize==16}"> selected </c:if>>16</option>
            </select>
        </form>
    </ul>
</nav>
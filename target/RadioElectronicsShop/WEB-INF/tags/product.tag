<%--@elvariable id="locale" type="java.util.locale"--%>
<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="product" required="true"  type="com.epam.igor.electronicsshop.entity.Product" %>
<fmt:bundle basename="i18n">
    <fmt:message key="common.details" var="details"/>
</fmt:bundle>
<div class="thumbnail">
    <img style="height: 200px"; alt="image" src="<c:url value="/img/${product.id}"/>"/>
    <h4 align="center"></h4><b>${product.name}</b>
    <p align="justify" style="height: 120px">${product.getDescription(locale)}</p>
    <fmt:formatNumber var="formattedPrice" type="currency" currencyCode="KZT"
                      maxFractionDigits="0" value="${product.price.amount}"/>
    <p align="right"><span class="pull-left"
                           style="color: #1c0098;font-size: 14px; margin-top: 8px">${formattedPrice}</span>
            <a class="btn btn-default" href="<c:url value="/do/product?id=${product.id}"/>" role="button" style="font-size: 12px">
                    ${details}
            </a></p>

</div>
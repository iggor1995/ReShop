<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="action" required="true" type="java.lang.String" %>
<%@attribute name="product" required="true" type="com.epam.igor.electronicsshop.entity.Product" %>
<%@attribute name="types" required="true" type="java.util.List" %>

<fmt:bundle basename="i18n">
    <fmt:message key="common.product.name" var="name"/>
    <fmt:message key="common.product.type" var="typeL"/>
    <fmt:message key="common.product.price" var="price"/>
    <fmt:message key="common.descriptionEN" var="descriptionEN"/>
    <fmt:message key="common.descriptionRU" var="descriptionRU"/>
    <fmt:message key="common.image" var="image"/>
    <fmt:message key="button.cancel" var="cancel"/>
    <fmt:message key="button.save" var="save"/>
    <fmt:message key="error.image" var="imageErrorMessage"/>
    <fmt:message key="error.money" var="moneyErrorMessage"/>
</fmt:bundle>
<%--@elvariable id="type" type="com.epam.igor.electronicsshop.entity.ProductType"--%>
<%--@elvariable id="types" type="java.util.List"--%>

<form name="product" action="<c:url value="${action}"/>" method="post" enctype="multipart/form-data">
    <input hidden name="id" value="${product.id}">
    <div class="col-lg-6" style="width: 300px">
        <div class="form-group input-group">
            <label for="name">${name}</label>
            <input type="text" class="form-control" id="name" name="name" value="${product.name}">
        </div>
        <div class="form-group input-group">
            <label for="image">${image}</label>
            <input type="file" class="form-control" id="image" name="image">
        </div>
        <c:if test="${imageError.equals('true')}">
            <p class="text-danger" style="height: 10px;font-size: 12px;">
                    ${imageErrorMessage}
            </p>
        </c:if>
        <div class="form-group input-group">
            <label for="type">${typeL}</label>
            <select class="form-control" id="type" name="typeId">
                <c:forEach items="${types}" var="type">
                    <option value="${type.id}"  <c:if test="${product.type.equals(type)}">selected</c:if>
                       >${type.getName(locale)}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group input-group">
            <label for="price">${price}</label>
            <fmt:formatNumber var="formattedPrice" type="number"
                              groupingUsed="false" maxFractionDigits="0" value="${product.price.amount}"/>
            <input type="text" class="form-control" id="price" name="price" value="${formattedPrice}" min="0">
        </div>
        <c:if test="${moneyError.equals('true')}">
            <p class="text-danger" style="height: 10px; font-size: 12px">
                    ${moneyErrorMessage}
            </p>
        </c:if>
    </div>
    <div class="col-lg-6" style="width: 400px">
        <div class="form-group input-group">
            <label for="descriptionRU">${descriptionRU}</label>
            <textarea style="width: 400px;height:100px;" class="form-control" id="descriptionRU" name="descriptionRU">
                ${product.ruDescription}
            </textarea>
        </div><div class="form-group input-group">
            <label for="descriptionEN">${descriptionEN}</label>
            <textarea style="width: 400px;height:100px;" class="form-control" id="descriptionEN" name="descriptionEN">
                ${product.enDescription}
            </textarea>
        </div>
    </div>
    <div class="col-lg-12">
        <button value="submit" class="btn btn-default" style="width: 120px">${save}</button>
        <a href="<c:url value="/do/manage/products"/>" class="btn btn-default">${cancel}</a>
    </div>
</form>
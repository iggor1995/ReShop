<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="i18n">
    <fmt:message key="storage.manage.pagetitle" var="pagetitle"/>
    <fmt:message key="storage.pagetitle" var="storage_label"/>
    <fmt:message key="common.product" var="product"/>
    <fmt:message key="common.amount" var="amount"/>
    <fmt:message key="error.amount" var="amount_error_message"/>
    <fmt:message key="common.button.edit" var="edit"/>
    <fmt:message key="common.button.delete" var="delete"/>
    <fmt:message key="button.save" var="save"/>
</fmt:bundle>

<%--@elvariable id="storageItems" type="java.util.List"--%>
<%--@elvariable id="storageItem" type="com.epam.igor.electronicsshop.entity.StorageItem"--%>
<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>
<%--@elvariable id="pagesCount" type="java.lang.Integer"--%>
<%--@elvariable id="locale" type="java.util.Locale"--%>
<%--@elvariable id="errorMap" type="java.util.Map"--%>

<my:page-pattern pagetitle="${pagetitle}">
    <div class="row row-offcanvas row-offcanvas-right" style="width: 1200px; margin: auto">
        <my:user user="${loggedUser}"/>
        <div class="col-lg-10" align="center">
            <my:pagination url="/do/manage/storage" pagesCount="${pagesCount}"/>
            <form action="<c:url value="/do/edit/storage/item"/>" method="post">
            <table class="table table-bordered" style="font-size: 14px">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>${storage_label}</th>
                    <th>${product}</th>
                    <th>${amount}</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${storageItems}" var="storageItem" varStatus="itemRow">
                    <tr>
                        <td>${storageItem.id}</td>
                        <td>${storageItem.storage.name}</td>
                        <td>${storageItem.product.name}</td>
                        <td>
                            <div class="form-group input-group">
                                <input hidden name="itemId${itemRow.index}" value="${storageItem.id}">
                                <input type="number" min="0" value="${storageItem.amount}" style="width: 70px;"
                                name="amount${itemRow.index}">
                                <c:if test="${errorMap.get(itemRow.index).equals('true')}">
                                    <p class="text-danger"
                                       style="height: 20px;font-size: 12px;">${amount_error_message}</p>
                                </c:if>
                            </div>
                        </td>
                        <td>
                            <a class="btn btn-default"
                               href="<c:url value="/do/delete/storageItem?id=${storageItem.id}"/>"
                            >${delete}</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <input hidden name="rowsCount" value="${storageItems.size()}">
            <button value="submit" class="btn btn-default">${save}</button>
            </form>
        </div>
    </div>
</my:page-pattern>

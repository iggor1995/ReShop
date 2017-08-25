<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<my:page-pattern role="user"/>
<%--@elvariable id="loggedUser" type="com.epam.igor.electronicsshop.entity.User"--%>
<my:user-page user="${loggedUser}">
    <div class="col-lg-10" align="center">
        <div>
            <p align="center" style="font-size: 18px"><b>${loggedUser.role}</b></p>
            <hr>
        </div>
    </div>
</my:user-page>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title><spring:message code="page.login.title"/></title>
</head>
<body>
<h1><spring:message code="page.login.title"/></h1>
<div class="panel panel-default">
    <div class="panel-body">
        <sec:authorize access="isAnonymous()">
            <c:if test="${param.error eq 'bad_credentials'}">
                <div id="login-error-alert" class="alert alert-danger alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <spring:message code="page.login.login.failed.error.message"/>
                </div>
            </c:if>
            <form id="login-form" action="${pageContext.request.contextPath}/user/login" method="POST">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="form-group">
                    <label for="email-address"><spring:message code="page.login.email.address.label"/></label>
                    <input id="email-address"
                           class="form-control"
                           name="username"
                           type="email"
                           placeholder="<spring:message code="page.login.email.address.placeholder"/>">
                </div>
                <div class="form-group">
                    <label for="password"><spring:message code="page.login.password.label"/></label>
                    <input id="password"
                           class="form-control"
                           name="password"
                           type="password"
                           placeholder="<spring:message code="page.login.password.placeholder"/>">
                </div>
                <button type="submit" class="btn btn-default">
                    <spring:message code="page.login.form.submit.button.label"/>
                </button>
            </form>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <div id="authenticated-user-error">
                <spring:message code="page.login.authenticated.user.error.message"/>
            </div>
        </sec:authorize>
    </div>
</div>
</body>
</html>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/app/form.js"></script>
</head>
<body>
<h1><spring:message code="page.update.task.title"/></h1>
<div class="panel panel-default">
    <div class="panel-body">
        <form:form id="update-task-form"
                   action="${pageContext.request.contextPath}/task/${task.id}/update"
                   method="POST"
                   modelAttribute="task"
                   enctype="utf8"
                   role="form">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <form:hidden id="task-id" path="id"/>
            <div class="form-group">
                <label for="title"><spring:message code="task.title.field.label"/></label>
                <form:input id="task-title" cssClass="form-control" path="title"/>
                <form:errors id="error-title" cssClass="help-block" path="title"/>
            </div>
            <div class="form-group">
                <label for="description"><spring:message code="task.description.textarea.label"/></label>
                <form:textarea id="task-description" cssClass="form-control" path="description"/>
                <form:errors id="error-description" cssClass="help-block" path="description"/>
            </div>
            <button type="submit" class="btn btn-default"><spring:message code="page.update.task.form.submit.button.label"/></button>
        </form:form>
    </div>
</div>
</body>
</html>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html>
<head>
    <title></title>
</head>
<body>
<h1 id="task-title"><c:out value="${task.title}"/></h1>
<div class="panel panel-default">
    <div class="panel-body">
        <div id="task-description">
            <c:choose>
                <c:when test="${empty task.description}">
                    <spring:message code="page.view.task.no.description.message"/>
                </c:when>
                <c:otherwise>
                    <c:out value="${task.description}"/>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<div id="lifecycle-fields" class="container-fluid">
    <c:if test="${task.assignee != null}">
        <div class="row">
            <div class="col-md-3">
                <spring:message code="page.view.task.assignee.description"/>:
                <span id="assignee-name"><c:out value="${task.assignee.name}"/></span>
            </div>
        </div>
    </c:if>
    <div class="row">
        <div class="col-md-3">
            <spring:message code="page.view.task.creator.description.prefix"/>
            <span id="creator-name"><c:out value="${task.creator.name}"/></span>
            <spring:message code="page.view.task.timestamp.prefix"/>
            <span id="creation-time">
                <javatime:format value="${task.creationTime}" pattern="yyyy-MM-dd H:mm:ss"/>
            </span>
        </div>
        <div class="col-md-3">
            <spring:message code="page.view.task.modifier.description.prefix"/>
            <span id="modifier-name"><c:out value="${task.modifier.name}"/></span>
            <spring:message code="page.view.task.timestamp.prefix"/>
            <span id="modification-time">
                <javatime:format value="${task.modificationTime}" pattern="yyyy-MM-dd H:mm:ss"/>
            </span>
        </div>
        <c:if test="${task.status == 'CLOSED'}">
            <div id="closed-task-fields" class="col-md-3">
                <spring:message code="page.view.task.closer.description.prefix"/>
                <span id="closer-name"><c:out value="${task.closer.name}"/></span>
                <spring:message code="page.view.task.timestamp.prefix"/>
                <span id="closing-time">
                    <javatime:format value="${task.modificationTime}" pattern="yyyy-MM-dd H:mm:ss"/>
                </span>
            </div>
        </c:if>
    </div>
    <div id="task-action-links" class="row">
        <div class="col-md-1">
            <a id="delete-task-link" href="${pageContext.request.contextPath}/task/${task.id}/delete">
                <spring:message code="page.view.task.delete.task.link.label"/>
            </a>
        </div>
        <div class="col-md-1">
            <a id="update-task-link" href="${pageContext.request.contextPath}/task/${task.id}/update">
                <spring:message code="page.view.task.edit.task.link.label"/>
            </a>
        </div>
    </div>
</div>
</body>
</html>
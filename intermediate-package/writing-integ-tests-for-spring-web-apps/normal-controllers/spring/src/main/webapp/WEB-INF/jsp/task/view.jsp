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
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-12">
                        <c:out value="${task.description}"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4">
                        <spring:message code="page.view.task.creator.description.prefix"/>
                        <c:out value="${task.creator.name}"/>
                        <spring:message code="page.view.task.timestamp.prefix"/>
                        <javatime:format value="${task.creationTime}" pattern="yyyy-MM-dd H:mm:ss"/>
                    </div>
                    <div class="col-md-4">
                        <spring:message code="page.view.task.modifier.description.prefix"/>
                        <c:out value="${task.modifier.name}"/>
                        <spring:message code="page.view.task.timestamp.prefix"/>
                        <javatime:format value="${task.modificationTime}" pattern="yyyy-MM-dd H:mm:ss"/>
                    </div>
                    <div class="col-md-4">
                        <c:if test="${task.status == 'CLOSED'}">
                            <spring:message code="page.view.task.closer.description.prefix"/>
                            <c:out value="${task.closer.name}"/>
                            <spring:message code="page.view.task.timestamp.prefix"/>
                            <javatime:format value="${task.modificationTime}" pattern="yyyy-MM-dd H:mm:ss"/>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
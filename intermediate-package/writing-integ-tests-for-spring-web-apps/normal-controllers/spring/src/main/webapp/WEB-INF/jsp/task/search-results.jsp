<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<h1><spring:message code="page.search.results.title"/></h1>
<div>
    <spring:message code="page.search.results.size.message" arguments="${tasks.size()},${searchTerm}"/>
</div>
<c:if test="${not empty tasks}">
    <table id="task-list" class="table table-striped">
        <thead>
        <tr>
            <th><spring:message code="task.property.title.header"/></th>
            <th><spring:message code="task.property.status.header"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${tasks}" var="task">
            <tr>
                <td>
                    <a href="${pageContext.request.contextPath}/task/${task.id}"><c:out value="${task.title}"/></a>
                </td>
                <td>
                    <spring:message code="${task.status.localizationKey}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
</body>
</html>
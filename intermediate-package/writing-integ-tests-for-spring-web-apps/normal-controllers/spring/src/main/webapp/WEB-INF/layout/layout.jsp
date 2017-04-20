<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous"/>
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp"
          crossorigin="anonymous"/>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/styles.css"/>

    <!-- Bootstrap Javascript -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>

    <sitemesh:write property="head"/>
    <title><sitemesh:write property="title"/></title>
</head>
<body>
<nav class="navbar navbar-inverse navbar-static-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#example-menu-collapse" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="https://www.testwithspring.com">TestWithSpring.com</a>
        </div>
        <div class="collapse navbar-collapse" id="example-menu-collapse">
            <sec:authorize access="isAuthenticated()">
                <ul class="nav navbar-nav">
                    <li>
                        <a id="task-list-link" href="${pageContext.request.contextPath}/">
                            <spring:message code="navigation.task.list.link.label"/>
                        </a>
                    </li>
                    <li>
                        <a id="create-task-link" href="${pageContext.request.contextPath}/task/create">
                            <spring:message code="navigation.create.task.link.label"/>
                        </a>
                    </li>
                </ul>
                <form action="${pageContext.request.contextPath}/task/search" class="navbar-form navbar-left" method="POST">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <div class="form-group">
                        <input id="task-search-term-field"
                               name="searchTerm"
                               type="text"
                               class="form-control"
                               placeholder="<spring:message code="navigation.search.form.field.placeholder"/>"/>
                    </div>
                    <button type="submit" class="btn btn-default">
                        <spring:message code="navigation.search.form.submit.button.label"/>
                    </button>
                </form>
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <form id="logout-form" action="${pageContext.request.contextPath}/user/logout" method="POST">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-default navbar-btn">
                                <spring:message code="navigation.logout.link.label"/>
                            </button>
                        </form>
                    </li>
                </ul>
            </sec:authorize>
        </div>
    </div>
</nav>
<div class="container-fluid">
    <div id="message-holder">
        <c:if test="${feedbackMessage != null}">
            <div class="alert alert-success alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                <c:out value="${feedbackMessage}"/>
            </div>
        </c:if>
    </div>
    <div id="view-holder">
        <sitemesh:write property="body"/>
    </div>
</div>
</body>
</html>
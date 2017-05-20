<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=768">
    <meta name="description" content="">

    <title translate="app.title.label"></title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/app.css">

    <!--[if lte IE 7]>
    <script src="${contextPath}/js/lib/json3.min.js"></script>
    <![endif]-->

    <!--[if lt IE 9]>
    <script src="${contextPath}/js/lib/es5-shim.min.js"></script>
    <![endif]-->

    <script>
        // include angular loader, which allows the files to load in any order
        <%@ include file="/js/lib/angular-loader.min.js" %>
        <%@ include file="/js/lib/script.min.js" %>
        // load all of the dependencies asynchronously.
        $script([
            '${contextPath}/js/vendor.min.js',
            '${contextPath}/js/app.min.js',
            '${contextPath}/js/partials.js'
        ], function () {
            // when all is done, execute bootstrap angular application
            angular.bootstrap(document, ['app']);
        });
    </script>
</head>
<body>

<div class="wrapper" ng-cloak class="ng-cloak">

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
            <div id="example-menu-collapse" class="collapse navbar-collapse" ng-if="currentUser.username">
                <ul class="nav navbar-nav">
                    <li>
                        <a id="task-list-link"
                           ui-sref="task.list"
                           translate="navigation.task.list.link.label">
                        </a>
                    </li>
                    <li>
                        <a id="create-task-link"
                           ui-sref="task.create"
                           translate="navigation.create.task.link.label">
                        </a>
                    </li>
                </ul>
                <div class="pull-right" log-out-button current-user="currentUser"></div>
                <div class="pull-right" search-form current-user="currentUser"></div>
            </div>
        </div>
    </nav>

    <div growl></div>
    <div class="main-content container-fluid" ui-view autoscroll="false"></div>

    <div class="push"></div>
</div>
</body>
</html>
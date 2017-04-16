<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Example Form Page</title>
</head>
<body>
    <h1 id="page-header">Example Form Page</h1>
    <form:form id="form"
               action="${pageContext.request.contextPath}/form"
               method="POST"
               modelAttribute="form"
               enctype="utf8"
               role="form">
        <div>
            <label for="message">Message</label>
            <form:input id="message" path="message"/>
        </div>
        <div>
            <label for="number">Number</label>
            <form:select id="number" path="number" multiple="false">
                <form:option value="1"/>
                <form:option value="2"/>
                <form:option value="3"/>
            </form:select>
        </div>
        <div>
            <label for="checkbox">Checkbox</label>
            <form:checkbox id="checkbox" path="checkbox" value="true"/>
        </div>
        <div>
            <label for="radio-button">RadioButton</label>
            <form:radiobutton id="radio-button" path="radioButton" value="true"/>
        </div>
        <button type="submit">Submit</button>
    </form:form>
</body>
</html>
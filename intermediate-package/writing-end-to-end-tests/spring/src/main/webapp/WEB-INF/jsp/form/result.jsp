<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Form Result Page</title>
</head>
<body>
    <h1 id="page-header">Form Result Page</h1>
    <div id="form-content">
        <div id="form-message">
            <div id="form-message-label">Message: </div>
            <div id="form-message-value">${form.message}</div>
        </div>
        <div id="form-number">
            <div id="form-number-label">Number: </div>
            <div id="form-number-value">${form.number}</div>
        </div>
        <div id="form-checkbox">
            <div id="form-checkbox-label">Checkbox: </div>
            <div id="form-checkbox-value">${form.checkbox}</div>
        </div>
        <div id="form-radio-button">
            <div id="form-radio-button-label">Radio Button: </div>
            <div id="form-radio-button-value">${form.radioButton}</div>
        </div>
    </div>
</body>
</html>
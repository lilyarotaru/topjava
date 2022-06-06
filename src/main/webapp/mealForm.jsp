<%@ page import="java.time.LocalDateTime" %>
<%@ page import="ru.javawebinar.topjava.model.Meal" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${param.action.equals("add") ? "Add meal" : "Edit meal"}</title>
</head>
<body>
<h2>${param.action.equals("add") ? "Add meal" : "Edit meal"}</h2>
<hr/>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<form method="POST" action='meals' name="formMeal">
    DateTime: <input
        type="datetime-local" name="dateTime"
        value="${meal.dateTime}"
        required/>
    <br/>
    Description: <input
        type="text" name="description"
        value="${meal.description}" required/>
    <br/>
    Calories : <input
        type="number" name="calories"
        value="${meal.calories}" min="1" required/>
    <br/>
    <input type="number" name="id"
           value="${meal.id}" hidden/>
    <input type="submit" value="Save"/>
    <a href="meals">
        <input type="button" value="Cancel">
    </a>
</form>
</body>
</html>


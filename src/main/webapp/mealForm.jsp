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
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<form method="POST" action='meals' name="formMeal">
    DateTime: <input
        type="datetime-local" name="dateTime"
        value="<%=meal.getDateTime()== null ? LocalDateTime.now().withSecond(0).withNano(0) : meal.getDateTime()%>"
        required/>
    <br/>
    Description: <input
        type="text" name="description"
        value="<%=meal.getDescription()%>" required/>
    <br/>
    Calories : <input
        type="number" name="calories"
        value="<%=meal.getCalories()%>" min="1" required/>
    <br/>
    <input type="number" name="id"
           value="<%=meal.getId()%>" hidden/>
    <input type="submit" value="Save"/>
    <a href="meals">
        <input type="button" value="Cancel">
    </a>
</form>
</body>
</html>


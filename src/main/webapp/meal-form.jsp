<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>Add meal</title>
</head>

<body>

<form method="POST" action='meals' name="formAddMeal">
    DateTime : <input
        type="datetime-local" name="dateTime"
        value="<c:out value="${meal.dateTime}" />"/> <br/>
    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.description}" />"/> <br/>
    Calories : <input
        type="number" name="calories"
        value="<c:out value="${meal.calories}" />"/> <br/>
    <input
            type="number" name="id"
            value="<c:out value="${meal.id}" default="-1"/>" hidden/>
    <input type="submit" value="Save"/>
    <a href="meals">
        <input type="button" value="Cancel">
    </a>
</form>
</body>
</html>
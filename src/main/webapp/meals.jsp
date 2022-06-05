<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Meal List</title>
</head>

<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<p><a href="meals?action=add">Add Meal</a></p>
<table border=1>
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr style="color: <%=meal.isExcess() ? "red" : "green"%>">
            <td><%=meal.getDateTime().format((DateTimeFormatter) request.getAttribute("dateFormatter"))%>
            </td>
            <td><%=meal.getDescription()%>
            </td>
            <td><%=meal.getCalories()%>
            </td>
            <td><a href="meals?action=edit&id=<%=meal.getId()%>">Update</a></td>
            <td><a href="meals?action=delete&id=<%=meal.getId()%>">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
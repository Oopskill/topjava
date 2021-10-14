<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<a href="meals?action=addMeal&namePage=addMeal">Add Meal</a>
<table border="1">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
<c:forEach items = "${meals}" var="meal">
    <c:set var="color" value="${meal.excess ? 'red' : 'green'}"/>
    <tr style="color: ${color}">
        <td>${meal.formatDate}</td>
        <td>${meal.description}</td>
        <td>${meal.calories}</td>
        <td><a href="meals?action=update&id=${meal.id.get()}&namePage=update"/>Update</td>
        <td><a href="meals?action=delete&id=${meal.id.get()}&namePage=delete"/>Delete</td>
    </tr>
</c:forEach>
</table>
</body>
</html>
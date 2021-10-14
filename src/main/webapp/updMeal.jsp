<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.topjava.dao.MealDao" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% MealDao mealDao = new MealDao();
    String namePage = request.getParameter("namePage");
    request.setAttribute("namePage", "Update!");
    if (namePage.equals("addMeal")){
        request.setAttribute("namePage", "Add Meal");
        request.setAttribute("meal", null);
    }
    else {
        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("button", "Update");
        request.setAttribute("meal", mealDao.getMeal(id));
    }
%>

<html lang="ru">
<head>
    <title><c:out value="${namePage}"/></title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2><c:out value="${namePage}"/> </h2>
<form action="/meals" method="post">
    <p>Date Time: <input type="datetime-local" name="date"
                value="<c:out value="${meal.dateTime}" />" /></p>
    <p>Description: <input type="text" name="description"
                         value="<c:out value="${meal.description}" />" /></p>
    <p>Calories: <input type="number" name="calories"
                         value="<c:out value="${meal.calories}" />" /></p>
    <input type="hidden" name="id" value="<c:out value="${meal.id.get()}"/>"/>
    <input type="submit" value="Execute">
</form>
</body>
</html>
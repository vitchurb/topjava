<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h2><a href="index.html">Home</a></h2>
    <form method="post" action="meals?action=changeUser">
        <table border="1" cellpadding="8" cellspacing="0">
            <tr>
                <td>Пользователи:</td>
                <td><select name="userSelectId">
                    <option value="1" <c:if test="${sessionScope.userSelectId == '1' }">selected</c:if>
                        >Пользователь 1</option>
                    <option value="2" <c:if test="${sessionScope.userSelectId == '2' }">selected</c:if>
                        >Пользователь 2</option>
                </select>
            </tr>
            <tr>
                <td>
                    <button type="submit">Сменить пользователя</button>
                </td>
            </tr>
        </table>
    </form>
    <form method="post" action="meals?action=search">
        <table border="1" cellpadding="8" cellspacing="0">
            <tr>
                <td>Дата От:</td>
                <td><input value="${sessionScope.filterDateFrom}" name="filterDateFrom"></td>
                <td>Время От:</td>
                <td><input value="${sessionScope.filterTimeFrom}" name="filterTimeFrom"></td>
            </tr>
            <tr>
                <td>Дата До:</td>
                <td><input value="${sessionScope.filterDateTo}" name="filterDateTo"></td>
                <td>Время До:</td>
                <td><input value="${sessionScope.filterTimeTo}" name="filterTimeTo"></td>
            </tr>
            <tr>
                <td>
                    <button type="submit">Поиск</button>
                </td>
            </tr>
        </table>
    </form>

    <h2>Meal list</h2>
    <a href="meals?action=create">Add Meal</a>
    <hr>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
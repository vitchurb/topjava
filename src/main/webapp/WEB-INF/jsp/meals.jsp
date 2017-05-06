<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <jsp:include page="fragments/headTag.jsp"/>
</head>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <h3><spring:message code="meals.title"/></h3>
    <form:form action="meals" method="post" >
        <dl>
            <dt><spring:message code="meal.filter.dateFrom"/>:</dt>
            <dd><input type="date" name="startDate" value="${mealFilter.startDate}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.filter.dateTo"/>:</dt>
            <dd><input type="date" name="endDate" value="${mealFilter.endDate}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.filter.timeFrom"/>:</dt>
            <dd><input type="time" name="startTime" value="${mealFilter.startTime}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.filter.timeTo"/>:</dt>
            <dd><input type="time" name="endTime" value="${mealFilter.endTime}"></dd>
        </dl>
        <button type="submit" name="filter"><spring:message code="common.filter"/></button>
    </form:form>

    <hr>
    <a href="meals/create"><spring:message code="meals.button.addMeal"/></a>
    <hr>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><spring:message code="meals.date"/></th>
            <th><spring:message code="meals.description"/></th>
            <th><spring:message code="meals.calories"/></th>
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
                <td><a href="meals/${meal.id}/update"><spring:message code="meals.button.update"/></a></td>
                <td><a href="meals/${meal.id}/delete"><spring:message code="meals.button.delete"/></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
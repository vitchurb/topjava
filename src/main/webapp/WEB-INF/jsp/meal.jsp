<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <jsp:include page="fragments/headTag.jsp"/>
</head>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <h2><a href="${pageContext.request.contextPath}/"><spring:message code="app.home"/></a></h2>
    <h2><spring:message code="${meal.id == null ? 'meals.create.title' : 'meals.update.title'}"/></h2>

    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form:form action="" method="post" modelAttribute="meal" >
        <input type="hidden" name="id" value="${id}">
        <dl>
            <dt><spring:message code="meals.dateTime"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories"></dd>
        </dl>
        <button type="submit" name="save"><spring:message code="common.save"/></button>
        <button type="submit" name="cancel"><spring:message code="common.cancel"/></button>
    </form:form>

</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>

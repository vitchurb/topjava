<%@ include file="/include/include.jsp" %>
<html>
<head>
    <title>Meals</title>
    <%@ include file="/include/style.jsp" %>
</head>
<body>
<div class="col-md-4">
    <h2><a href="index.html">Home</a></h2>
    <h2>Meals list</h2>

    <a href="meals?action=edit">Добавить запись</a>

    <table class="table table-bordered table-nonfluid" style="width: auto;">
        <thead>
        <tr>
            <td>Дата и время</td>
            <td>Описание</td>
            <td>Калории</td>
            <td></td>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${mealsWithExceeded}" var="meal">
            <tr class="
        <c:choose>
        <c:when test="${meal.exceed}">
            exceedLimit
        </c:when>
        <c:otherwise>
            notExceedLimit
        </c:otherwise>
        </c:choose>
        ">
                <td>${meal.dateTimeStr}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>
                    <a class="btn btn-xs btn-primary" href="meals?action=edit&id=<c:out value="${meal.id}"/>">
                        <span class="glyphicon glyphicon-pencil" aria-hidden="true"/>
                    </a>
                    <a class="btn btn-xs btn-danger" href="meals?action=delete&id=<c:out value="${meal.id}"/>">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"/>
                    </a>
                </td>
            </tr>

        </c:forEach>

        </tbody>

    </table>

</div>

</body>
</html>

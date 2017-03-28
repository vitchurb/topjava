<%@ include file="/include/include.jsp" %>
<html>
<head>
    <title>Редактирование записи о еде</title>
    <%@ include file="/include/style.jsp" %>
</head>
<body>
<div class="col-md-4">

    <h2>Редактирование записи о еде</h2>

    <c:if test="${not empty errors}">
        <div class="alert alert-danger" style="margin-top: 10px">
            <c:forEach items="${errors}" var="error">
                <div style="white-space: pre-wrap">${error}</div>
            </c:forEach>
        </div>
    </c:if>

    <form action="meals?action=edit" method="POST">
        <input type="hidden" name="id" value="${meal.id}"/>
        <div>
            <label class="control-label">Дата (формат ГГГГ-ММ-ДД)</label>
            <div class="controls">
                <input type="text" name="date" value="${meal.dateStr}"/>
            </div>
        </div>
        <div>
            <label class="control-label">Время (формат ЧЧ:ММ)</label>
            <div class="controls">
                <input type="text" name="time" value="${meal.timeStr}"/>
            </div>
        </div>
        <div>
            <label class="control-label">Описание</label>
            <div class="controls">
                <input type="text" name="description" value="${meal.description}"/>
            </div>
        </div>
        <div>
            <label class="control-label">Калории</label>
            <div class="controls">
                <input type="text" name="calories" value="${meal.calories}"/>
            </div>
        </div>


        <div style="margin-top: 10px">
            <input type="submit" class="btn btn-primary" name="save"
                   value="Сохранить">
            <input type="submit" class="btn btn-default" name="cancel"
                   value="Отмена">
        </div>


    </form>
</div>


</body>
</html>

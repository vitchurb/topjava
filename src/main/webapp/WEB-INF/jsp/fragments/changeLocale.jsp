<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul class="nav navbar-nav navbar-right">
    <li class="dropdown">
        <c:set var="clientLocale" value="${fn:toUpperCase(pageContext.response.locale.language)}"/>
        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
            ${clientLocale}
            </a>
        <ul class="dropdown-menu" role="menu">
            <li><a onclick="changeLang('en')"><spring:message code="app.lang.en"/></a></li>
            <li><a onclick="changeLang('ru')"><spring:message code="app.lang.ru"/></a></li>
        </ul>
        <script type="text/javascript">
            function changeLang(lang) {
                window.location.href = window.location.href.split('?')[0] + '?lang=' + lang;
            }
        </script>
    </li>
</ul>

<%@ page import="ru.javawebinar.topjava.util.exception.ErrorType" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    var i18n = [];
    <%-- user.add/user.edit or meal.add/meal.edit --%>
    i18n["addTitle"] = '<spring:message code="${param.page}.add"/>';
    i18n["editTitle"] = '<spring:message code="${param.page}.edit"/>';

    <c:forEach var='key' items='<%=new String[]{"common.deleted", "common.saved", "common.enabled", "common.disabled", "common.errorStatus", "common.confirm"}%>'>
    i18n['${key}'] = '<spring:message code="${key}"/>';
    </c:forEach>
    <c:forEach var='key' items='<%=new String[]{ErrorType.VALIDATION_ERROR.name(), ErrorType.DATA_NOT_FOUND.name(), ErrorType.DATA_ERROR.name(), ErrorType.APP_ERROR.name()}%>'>
    i18n['${key}'] = '<spring:message code="${key}"/>';
    </c:forEach>
<%--    <c:forEach var='error' items='<%=ErrorType.values()%>'>--%>
<%--    const key = "${error.name().toString()}";--%>           //why this way doesn't work
<%--    i18n['${key}'] = '<spring:message code="${key}"/>';--%>
<%--    </c:forEach>--%>
</script>
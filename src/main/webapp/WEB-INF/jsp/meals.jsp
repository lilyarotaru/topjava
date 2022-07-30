<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script src="resources/js/topjava.common.js" defer></script>
<script src="resources/js/topjava.meals.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center"><spring:message code="meal.title"/></h3>
        <form id="filterForm">
            <div class="form-row">
                <div class="col-2">
                    <label for="startDate" class="col-form-label"><spring:message code="meal.startDate"/></label>
                    <input type="date" id="startDate" name="startDate" class="form-control">
                </div>
                <div class="col-2">
                    <label for="endDate" class="col-form-label"><spring:message code="meal.endDate"/></label>
                    <input type="date" id="endDate" name="endDate" class="form-control">
                </div>
                <div class="col-2">
                    <label for="startTime" class="col-form-label"><spring:message code="meal.startTime"/></label>
                    <input type="time" id="startTime" name="startTime" class="form-control">
                </div>
                <div class="col-2">
                    <label for="endTime" class="col-form-label"><spring:message code="meal.endTime"/></label>
                    <input type="time" id="endTime" name="endTime" class="form-control">
                </div>
            </div>
            <br>
            <button class="btn btn-danger btn-sm" type="reset" onclick="updateTable()"><spring:message
                    code="common.cancel"/></button>
            <button class="btn btn-success btn-sm" type="button" onclick="filter()"><spring:message
                    code="meal.filter"/></button>
        </form>
        <hr>
        <button class="btn btn-primary" onclick="add()">
            <span class="fa fa-plus"></span>
            <spring:message code="meal.add"/>
        </button>
        <table class="table table-striped" id="datatable">
            <thead>
            <tr>
                <th><spring:message code="meal.dateTime"/></th>
                <th><spring:message code="meal.description"/></th>
                <th><spring:message code="meal.calories"/></th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <c:forEach items="${requestScope.meals}" var="meal">
                <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
                <tr id="${meal.id}" data-meal-excess="${meal.excess}">
                    <td>
                            ${fn:formatDateTime(meal.dateTime)}
                    </td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a><span class="fa fa-pencil"></span></a></td>
                    <td><a class="delete"><span class="fa fa-remove"></span></a></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
<div class="modal fade" tabindex="-1" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><spring:message code="meal.add"/></h4>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" id="id" name="id">

                    <div class="form-group">
                        <label for="dateTime" class="col-form-label"><spring:message code="meal.dateTime"/></label>
                        <input type="datetime-local" class="form-control" id="dateTime" name="dateTime"
                               placeholder="<spring:message code="meal.dateTime"/>">
                    </div>

                    <div class="form-group">
                        <label for="description" class="col-form-label"><spring:message
                                code="meal.description"/></label>
                        <input type="text" class="form-control" id="description" name="description"
                               placeholder="<spring:message code="meal.description"/>">
                    </div>

                    <div class="form-group">
                        <label for="calories" class="col-form-label"><spring:message code="meal.calories"/></label>
                        <input type="number" class="form-control" id="calories" name="calories"
                               placeholder="<spring:message code="meal.calories"/>">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.cancel"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="save()">
                    <span class="fa fa-check"></span>
                    <spring:message code="common.save"/>
                </button>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
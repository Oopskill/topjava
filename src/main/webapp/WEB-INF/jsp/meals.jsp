<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script src="resources/js/topjava.common.js" defer></script>
<script src="resources/js/topjava.meals.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron pt-4">

    <div class="row mt-3">
        <div class="col-7">
            <div class="card">
                <div class="card-body">
                    <form id="filter" method="post" action="meals/filter">
                        <div class="form-group row">
                            <label for="startDate" class="col-2 col-form-label col-form-label-sm text-right"><spring:message
                                    code="meal.startDate"/>:</label>
                            <div class="col-4">
                                <input class="form-control form-control-sm" type="date" name="startDate" id="startDate"
                                       value="${param.startDate}"/>
                            </div>
                            <label for="startTime" class="col-3 col-form-label col-form-label-sm text-right"><spring:message
                                    code="meal.startTime"/>:</label>
                            <div class="col-3">
                                <input name="startTime" type="time" class="form-control form-control-sm"
                                       id="startTime" value="${param.startTime}"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="endDate" class="col-2 col-form-label col-form-label-sm text-right"><spring:message
                                    code="meal.endDate"/>:</label>
                            <div class="col-4">
                                <input class="form-control form-control-sm" type="date" name="endDate" id="endDate" value="${param.endDate}"/>
                            </div>
                            <label for="endTime" class="col-3 col-form-label col-form-label-sm text-right"><spring:message
                                    code="meal.endTime"/>:</label>
                            <div class="col-3">
                                <input name="endTime" type="time" class="form-control form-control-sm" id="endTime"
                                       value="${param.endTime}"/>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="card-footer">
                    <div class="d-flex flex-row-reverse col">
                        <button href="meals" class="btn btn-Light btn-sm" onclick="clearFilter()"><spring:message code="meal.clear"/></button>
                        <button class="btn btn-success btn-sm mr-2" onclick="updateTable()"><spring:message code="meal.filter"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <h3 class="text-center"><spring:message code="meal.title"/></h3>
        <button class="btn btn-primary" onclick="add()">
            <span class="fa fa-plus"></span>
            <spring:message code="common.add"/>
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
                <tr id="${meal.id}">
                    <td>${fn:formatDateTime(meal.dateTime)}</td>
                    <td><c:out value="${meal.description}"/></td>
                    <td><c:out value="${meal.calories}"/></td>
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
                    <input type="hidden" id="id" name="id" value="${meal.id}">

                    <div class="form-group">
                        <label for="dateTime" class="col-form-label"><spring:message code="meal.dateTime"/></label>
                        <input type="datetime-local" class="form-control" name="dateTime" id="dateTime" value="${meal.description}"
                               placeholder="<spring:message code="meal.description"/>">
                    </div>

                    <div class="form-group">
                        <label for="description" class="col-form-label"><spring:message code="meal.description"/></label>
                        <input type="text" class="form-control" name="description" id="description" value="${meal.description}"
                               placeholder="<spring:message code="meal.description"/>">
                    </div>

                    <div class="form-group">
                        <label for="calories" class="col-form-label"><spring:message code="meal.calories"/></label>
                        <input type="number" class="form-control" id="calories" name="calories" value="${meal.calories}"
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
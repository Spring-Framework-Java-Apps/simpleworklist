<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1>Search Result for searchterm <i>${searchResult.searchterm}</i></h1>
<c:if test="${! empty searchResult.taskList}">
<h2>ActionItems</h2>
<table class="table table-striped table-hover">
<tr>
    <th>Status</th>
    <th>Title</th>
    <th>Text</th>
</tr>
<c:forEach var="task" items="${searchResult.taskList}">
    <tr>
        <td>
            <c:choose>
                <c:when test="${task.status eq 'NEW'}">
                    <button class="btn btn-small btn-danger" type="button">&nbsp;</button>
                </c:when>
                <c:when test="${task.status.name() eq 'WORK'}">
                    <button class="btn btn-small btn-warning" type="button">&nbsp;</button>
                </c:when>
                <c:when test="${task.status.name() eq 'DONE'}">
                    <button class="btn btn-small btn-success" type="button">&nbsp;</button>
                </c:when>
            </c:choose>
        </td>
        <td>
            <a href='<c:url value="/task/detail/${task.id}"/>' class="dataDetailListTitle"
               id="dataDetail_${task.id}" ><c:out
                    value="${task.title}" /></a>
        </td>
        <td>
            <c:out value="${task.textShortened}"></c:out>
        </td>
    </tr>
</c:forEach>
</table>
</c:if>
<c:if test="${! empty searchResult.projectList}">
<h2>Categories</h2>
    <table class="table table-striped table-hover">
        <tr>
            <th>Name</th>
            <th>Description</th>
        </tr>
    <c:forEach var="cat" items="${searchResult.projectList}">
        <tr>
            <td>
                <a href='<c:url value="/project/${cat.id}"/>'
                   id="categoryList_${cat.id}" ><c:out value="${cat.name}" /></a>
            </td>
            <td>
                <c:out value="${cat.description}" />
            </td>
        </tr>
    </c:forEach>
    </table>
</c:if>

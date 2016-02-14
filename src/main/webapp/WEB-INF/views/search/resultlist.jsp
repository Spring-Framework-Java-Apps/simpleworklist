<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1>Search Result for searchterm <i>${searchResult.searchterm}</i></h1>
<c:if test="${! empty searchResult.actionItemList}">
<h2>ActionItems</h2>
<table class="table table-striped table-hover">
<tr>
    <th>Status</th>
    <th>Title</th>
    <th>Text</th>
</tr>
<c:forEach var="actionItem" items="${searchResult.actionItemList}">
    <tr>
        <td>
            <c:choose>
                <c:when test="${actionItem.status eq 'NEW'}">
                    <button class="btn btn-small btn-danger" type="button">&nbsp;</button>
                </c:when>
                <c:when test="${actionItem.status.name() eq 'WORK'}">
                    <button class="btn btn-small btn-warning" type="button">&nbsp;</button>
                </c:when>
                <c:when test="${actionItem.status.name() eq 'DONE'}">
                    <button class="btn btn-small btn-success" type="button">&nbsp;</button>
                </c:when>
            </c:choose>
        </td>
        <td>
            <a href='<c:url value="/actionItem/detail/${actionItem.id}"/>' class="dataDetailListTitle"
               id="dataDetail_${actionItem.id}" ><c:out
                    value="${actionItem.title}" /></a>
        </td>
        <td>
            <c:out value="${actionItem.textShortened}"></c:out>
        </td>
    </tr>
</c:forEach>
</table>
</c:if>
<c:if test="${! empty searchResult.categoryList}">
<h2>Categories</h2>
    <table class="table table-striped table-hover">
        <tr>
            <th>Name</th>
            <th>Description</th>
        </tr>
    <c:forEach var="cat" items="${searchResult.categoryList}">
        <tr>
            <td>
                <a href='<c:url value="/category/${cat.id}"/>'
                   id="categoryList_${cat.id}" ><c:out value="${cat.name}" /></a>
            </td>
            <td>
                <c:out value="${cat.description}" />
            </td>
        </tr>
    </c:forEach>
    </table>
</c:if>

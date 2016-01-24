<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- Document Window -->
<c:if test="${! empty users}">
<table class="table table-striped">
<c:forEach items="${users}" var="user">
	<tr>
		<td><c:out value="${user.userEmail}"/></td>
		<td><c:out value="${user.userFullname}"/></td>
	</tr>
</c:forEach>
</table>
</c:if>
<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- Document Window -->
<c:if test="${! empty users}">
<h1>List of registered Users</h1>
<table class="table table-striped">
	<tr>
		<th>Email</th>
		<th>Name</th>
		<th>Date of registration</th>
	</tr>
<c:forEach items="${users}" var="user">
	<tr>
		<td><c:out value="${user.userEmail}"/></td>
		<td><c:out value="${user.userFullname}"/></td>
		<td><c:out value="${user.createdTimestamp}"/></td>
	</tr>
</c:forEach>
</table>
</c:if>
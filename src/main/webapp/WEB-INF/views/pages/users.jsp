<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- Document Window -->
<c:if test="${! empty users}">
<h1>List of registered Users</h1>
<table class="table table-striped">
	<tr>
		<th>Messages</th>
		<th>Email</th>
		<th>Name</th>
		<th>Last Login</th>
		<th>Date of registration</th>
	</tr>
<c:forEach items="${users}" var="user">
	<tr>
		<td>
			<c:if test="${user.id != thisUser.id}">
			<a href='<c:url value="/user/${user.id}/messages/"/>'>
				<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> Messages
				<c:if test="${usersToNewMessages.get(user.id) > 0}">
				<span class="badge"><c:out value="${usersToNewMessages.get(user.id)}"/></span>
				</c:if>
			</a>
			</c:if>
		</td>
		<td><c:out value="${user.userEmail}"/></td>
		<td><c:out value="${user.userFullname}"/></td>
		<td><c:out value="${user.lastLoginTimestamp}"/></td>
		<td><c:out value="${user.createdTimestamp}"/></td>
	</tr>
</c:forEach>
</table>
</c:if>
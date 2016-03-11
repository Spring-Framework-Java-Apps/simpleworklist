<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- Document Window -->
<c:if test="${! empty users}">
<h1><spring:message code="pages.users.h1" text="Bla Bla" /></h1>
<table class="table table-striped">
	<tr>
		<th><spring:message code="pages.users.messages" text="Messages" /></th>
		<th>Email</th>
		<th>Name</th>
		<th><spring:message code="pages.users.lastLogin" text="Last Login" /></th>
		<th><spring:message code="pages.users.dateOfRegistration" text="Date of registration" /></th>
	</tr>
<c:forEach items="${users}" var="user">
	<tr>
		<td>
			<c:if test="${user.id != thisUser.id}">
			<a href='<c:url value="/user/${user.id}/messages/"/>'>
				<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> <spring:message code="pages.users.messages" text="Messages" />
				<c:if test="${usersToNewMessages.get(user.id) > 0}">
				<span class="badge"><c:out value="${usersToNewMessages.get(user.id)}"/></span>
				</c:if>
			</a>
			</c:if>
		</td>
		<td><c:out value="${user.userEmail}"/></td>
		<td><c:out value="${user.userFullname}"/></td>
		<td><fmt:formatDate value="${user.lastLoginTimestamp}" type="BOTH" /></td>
		<td><fmt:formatDate value="${user.createdTimestamp}" type="BOTH" /></td>
	</tr>
</c:forEach>
</table>
</c:if>
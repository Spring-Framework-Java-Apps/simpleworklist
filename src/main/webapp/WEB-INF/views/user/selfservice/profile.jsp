<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1><span class="glyphicon glyphicon-user" aria-hidden="true"></span> &nbsp; <spring:message code="user.selfservice.profile.h1" text="User Profile" /></h1>
<ul>
    <li><spring:message code="user.selfservice.profile.userEmail" text="User Email:" /> <strong><sec:authentication property="principal.username" /></strong></li>
    <li><spring:message code="user.selfservice.profile.name" text="Name:" /> <a href="<c:url value="/user/selfservice/name"/>"><c:out value="${thisUser.userFullname}"/> <span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a></li>
    <li><a href="<c:url value="/user/selfservice/password"/>"><spring:message code="user.selfservice.profile.changePassword" text="Change Password" /> <span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a></li>
    <li><a href="<c:url value="/user/selfservice/contexts"/>"><spring:message code="user.selfservice.profile.changeContexts" text="Change Areas" /> <span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a></li>
    <li><a href="<c:url value="/user/selfservice/language"/>"><spring:message code="user.selfservice.profile.changeLanguage" text="Set Default Language" /> <span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a></li>
</ul>
<c:if test="${! empty users}">
    <h3><spring:message code="pages.users.h1" text="List of registered Users" /></h3>
    <table class="table table-striped">
        <tr>
            <th><spring:message code="pages.users.messages" text="Messages" /></th>
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
                <td><c:out value="${user.userFullname}"/></td>
                <td><fmt:formatDate value="${user.lastLoginTimestamp}" type="BOTH" timeZone="Europe/Berlin" /></td>
                <td><fmt:formatDate value="${user.createdTimestamp}" type="BOTH" timeZone="Europe/Berlin" /></td>
            </tr>
        </c:forEach>
    </table>
</c:if>
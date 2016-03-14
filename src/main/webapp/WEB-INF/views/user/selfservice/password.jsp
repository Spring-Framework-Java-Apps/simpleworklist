<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> <spring:message code="user.selfservice.profile.changePassword" text="Change Password" /></h1>
<h2>for User: <strong><sec:authentication property="principal.username" /></strong></h2>
<br/>
<a href="<c:url value="/user/selfservice"/>"><spring:message code="button.back" text="back" /></a>
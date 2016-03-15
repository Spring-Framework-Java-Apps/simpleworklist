<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> <spring:message code="user.selfservice.profile.changeAreas" text="Change Areas" /></h1>
<h2><spring:message code="user.selfservice.forUser" text="for User:" /> <strong><sec:authentication property="principal.username" /></strong></h2>
<br/>
<a href="<c:url value="/user/selfservice"/>"><spring:message code="button.back" text="back" /></a>
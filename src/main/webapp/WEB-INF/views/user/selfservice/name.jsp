<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> &nbsp; <spring:message code="user.selfservice.profile.name" text="Change Name" /> <c:out value="${thisUser.userFullname}"/></h1>
<h2>for User: <strong><sec:authentication property="principal.username" /></strong></h2>

<div>
    <form:form id="formId" commandName="username" method="post">
        <div class="form-group">
            <form:label path="userFullname"><spring:message code="user.registerConfirmed.userFullname" text="Full Name" /></form:label>
            <form:input path="userFullname" class="form-control"/><br/>
            <form:errors path="userFullname" delimiter=", " element="div" class="alert alert-danger"/>
        </div>
        <button id="save-username" type="submit" class="btn btn-default"><spring:message code="user.selfservice.name.button" text="Save Username" /></button>
    </form:form>
</div>

<br/>
<a href="<c:url value="/user/selfservice"/>"><spring:message code="button.back" text="back" /></a>
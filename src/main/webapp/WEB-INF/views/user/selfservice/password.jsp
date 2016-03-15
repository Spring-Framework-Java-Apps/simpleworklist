<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> <spring:message code="user.selfservice.profile.changePassword" text="Change Password" /></h1>
<h2>for User: <strong><sec:authentication property="principal.username" /></strong></h2>
<div>
    <form:form id="formId" commandName="userChangePasswordFormBean" method="post">
        <div class="form-group">
            <form:label path="oldUserPassword"><spring:message code="user.resetPasswordConfirmed.oldUserPassword" text="Current Password" /></form:label>
            <form:password path="oldUserPassword" class="form-control"/><br/>
            <form:errors path="oldUserPassword" delimiter=", " element="div" class="alert alert-danger"/>
        </div>
        <div class="form-group">
            <form:label path="userPassword"><spring:message code="user.resetPasswordConfirmed.password" text="Password" /></form:label>
            <form:password path="userPassword" class="form-control"/><br/>
            <form:errors path="userPassword" delimiter=", " element="div" class="alert alert-danger"/>
        </div>
        <div class="form-group">
            <form:label path="userPasswordConfirmation"><spring:message code="user.resetPasswordConfirmed.passwordAgain" text="Password again" /></form:label>
            <form:password path="userPasswordConfirmation" class="form-control"/><br/>
            <form:errors path="userPasswordConfirmation" delimiter=", " element="div" class="alert alert-danger"/>
        </div>
        <button id="saveNewPassword" type="submit" class="btn btn-default"><spring:message code="user.resetPasswordConfirmed.button" text="Save New Password" /></button>
    </form:form>
</div>
<br/>
<a href="<c:url value="/user/selfservice"/>"><spring:message code="button.back" text="back" /></a>
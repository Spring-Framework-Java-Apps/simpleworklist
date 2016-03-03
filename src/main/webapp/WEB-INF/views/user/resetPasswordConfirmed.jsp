<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1>Password Reset</h1>
<div class="well">
    <form:form id="formId" commandName="userAccountFormBean" method="post">
        <div class="form-group">
            <form:label path="userPassword">Password</form:label>
            <form:password path="userPassword" class="form-control"/><br/>
            <form:errors path="userPassword" delimiter=", " element="div" class="alert alert-danger"/>
        </div>
        <div class="form-group">
            <form:label path="userPasswordConfirmation">Password again</form:label>
            <form:password path="userPasswordConfirmation" class="form-control"/><br/>
            <form:errors path="userPasswordConfirmation" delimiter=", " element="div" class="alert alert-danger"/>
        </div>
        <form:hidden path="userFullname"/>
        <form:hidden path="userEmail"/>
        <button id="saveNewPassword" type="submit" class="btn btn-default">Save New Password</button>
    </form:form>
</div>
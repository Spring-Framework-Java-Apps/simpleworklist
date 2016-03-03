<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1>Register as new User</h1>
<div class="well">
<form:form id="formId" commandName="registerFormBean" method="post">
	<div class="form-group">
    	<form:label path="email">Email</form:label>
		<form:input path="email" class="form-control" placeholder="Email"/>
		<form:errors path="email" delimiter=", " element="div" class="alert alert-danger"/>
    </div>
	<button id="registerButton" type="submit" class="btn btn-default">Register</button>
</form:form>
</div>
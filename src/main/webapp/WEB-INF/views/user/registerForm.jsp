<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<form:form id="formId" commandName="registerFormBean" method="post">
	<div class="form-group">
    	<form:label path="email">Email</form:label>
		<form:input path="email" class="form-control" placeholder="Email"/>
		<form:errors path="email" class="alert-danger alert" element="div"/>
    </div>
	<button id="registerButton" type="submit" class="btn btn-default">Register</button>
</form:form>
<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<div>
	<c:if test="${not empty param.login_error}">
		<div class="alert alert-danger" role="alert">
			Your login attempt was not successful, try again.<br /> Caused :
			<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
		</div>
	</c:if>
	<form id="formId" action='<c:url value="j_spring_security_check"/>' method="post">
		<div class="form-group">
			<label for="j_username">Email</label>
			<input id="j_username" type='text' name="j_username" class="form-control"/>
		</div>
		<div class="form-group">
			<label for="j_password">Password</label>
			<input id="j_password" type="password" name="j_password" class="form-control"/>
		</div>
		<button id="loginButton" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-log-in" aria-hidden="true"></span> Login</button>
	</form>
	<br/>
</div>
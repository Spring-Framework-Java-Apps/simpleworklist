<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<div>
	<h1>Welcome to SimpleWorklist</h1>
	<h2>Your Todo-List for Getting Things Done&reg;</h2>
	<p>Please login or <a href="/register">register as new user</a>.</p>
</div>
<div class="well">
	<c:if test="${not empty param.login_error}">
		<div class="alert alert-danger" role="alert">
			Your login attempt was not successful, try again.<br /> Caused :
			<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
		</div>
	</c:if>
	<form id="formId" action='<c:url value="/j_spring_security_check"/>' method="post">
		<input type="hidden"
			   name="${_csrf.parameterName}"
			   value="${_csrf.token}"/>
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
	<a href='<c:url value="/resetPassword"/>'><span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span> Password forgotten?</a>
</div>
<div>
	<p>
		For More Information please visit:
	<ul>
		<li>Github: <a href="https://github.com/phasenraum2010/simpleworklist">https://github.com/phasenraum2010/simpleworklist</a></li>
		<li>My Blog: <a href="http://thomas-woehlke.blogspot.de/">http://thomas-woehlke.blogspot.de/</a></li>
		<li>The Project Page: <a href="http://woehlke.org/p/simpleworklist/">http://woehlke.org/p/simpleworklist/</a></li>
	</ul>
	</p>
	<p>
		<small>GTD&reg; and Getting Things Done&reg; are registered trademarks of the David Allen Company. SimpleWorklist is
			not affiliated with or endorsed by the David Allen Company.</small>
	</p>
</div>
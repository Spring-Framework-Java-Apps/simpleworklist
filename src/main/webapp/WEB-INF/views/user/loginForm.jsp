<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<div>
	<h1><spring:message code="user.loginForm.h1" text="Welcome to SimpleWorklist" /></h1>
	<h2><spring:message code="user.loginForm.h2" text="Your Todo-List for Getting Things Done&reg;" /></h2>
	<p><spring:message code="user.loginForm.login" text="Please login or" /> <a href="/register"><spring:message code="user.loginForm.register" text="register as new user" /></a>.</p>
</div>
<div class="well">
	<c:if test="${not empty param.login_error}">
		<div class="alert alert-danger" role="alert">
			<spring:message code="user.loginForm.loginError" text="Your login attempt was not successful, try again." /><br /><spring:message code="user.loginForm.loginError.caused" text="Caused:" />
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
			<label for="j_password"><spring:message code="user.loginForm.password" text="Password" /></label>
			<input id="j_password" type="password" name="j_password" class="form-control"/>
		</div>
		<button id="loginButton" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-log-in" aria-hidden="true"></span> <spring:message code="user.loginForm.loginButton" text="Login" /></button>
	</form>
	<br/>
	<a href='<c:url value="/resetPassword"/>'><span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span> <spring:message code="user.loginForm.passwordReset" text="Password forgotten?" /></a>
</div>
<div>
	<p>
		<spring:message code="user.loginForm.moreInformation" text="For More Information please visit:" />
	<ul>
		<li><spring:message code="user.loginForm.github" text="Github:" /> <a href="https://github.com/phasenraum2010/simpleworklist">https://github.com/phasenraum2010/simpleworklist</a></li>
		<li><spring:message code="user.loginForm.myBlog" text="My Blog:" /> <a href="http://thomas-woehlke.blogspot.de/">http://thomas-woehlke.blogspot.de/</a></li>
		<li><spring:message code="user.loginForm.projectPage" text="The Project Page:" /> <a href="http://woehlke.org/p/simpleworklist/">http://woehlke.org/p/simpleworklist/</a></li>
		<c:choose>
			<c:when test="${locale == 'de'}">
				<li>Getting Things Done&reg; @ Wikipedia:  <a href="https://de.wikipedia.org/wiki/Getting_Things_Done">https://de.wikipedia.org/wiki/Getting_Things_Done/</a></li>
			</c:when>
			<c:otherwise>
				<li>Getting Things Done&reg; @ Wikipedia:  <a href="https://en.wikipedia.org/wiki/Getting_Things_Done">https://en.wikipedia.org/wiki/Getting_Things_Done</a></li>
			</c:otherwise>
		</c:choose>
	</ul>
	</p>
	<p>
		<small><spring:message code="user.loginForm.gtd.copyright" text="GTD&reg; and Getting Things Done&reg; are registered trademarks of the David Allen Company. SimpleWorklist is
			not affiliated with or endorsed by the David Allen Company." /></small>
	</p>
</div>
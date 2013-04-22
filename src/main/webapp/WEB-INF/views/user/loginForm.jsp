<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
				<div>
					<c:if test="${not empty param.login_error}">
						<div class="alert alert-error">
							Your login attempt was not successful, try again.<br /> Caused :
							<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
						</div>
					</c:if>
					<form id="formId" action='<c:url value="j_spring_security_check"/>' method="post" class="form-horizontal">
                		<div class="control-group">
                			<label for="j_username" class="control-label">Email <sup class="ym-required">*</sup></label>
                			<div class="controls">
                				<input type='text' name="j_username" class="input-large"/>
                			</div>
                		</div>
                		<div class="control-group">
                			<label for="j_password" class="control-label">Password <sup class="ym-required">*</sup></label>
                			<div class="controls">
                				<input type="password" name="j_password" class="input-large"/>
                			</div>
                		</div>
                		<div class="controls">
                		<input id="loginButton" type="submit" name="data" value="Login" class="btn btn-primary"/>
                		</div>
            		</form>
				</div>
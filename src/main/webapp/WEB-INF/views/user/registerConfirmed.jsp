<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1><spring:message code="user.registerConfirmed.h1" text="Register as new User" /></h1>
<div class="well">
					<div>
					<form:form id="formId" commandName="userAccountFormBean" method="post">
                        <div class="form-group">
                		    <form:label path="userFullname"><spring:message code="user.registerConfirmed.userFullname" text="Full Name" /></form:label>
                            <form:input path="userFullname" class="form-control"/><br/>
                		    <form:errors path="userFullname" delimiter=", " element="div" class="alert alert-danger"/>
                        </div>
                        <div class="form-group">
                		    <form:label path="userPassword"><spring:message code="user.registerConfirmed.userPassword" text="Password" /></form:label>
							<form:password path="userPassword" class="form-control"/><br/>
							<form:errors path="userPassword" delimiter=", " element="div" class="alert alert-danger"/>
                        </div>
                        <div class="form-group">
                		    <form:label path="userPasswordConfirmation"><spring:message code="user.registerConfirmed.userPasswordConfirmation" text="Password again" /></form:label>
							<form:password path="userPasswordConfirmation" class="form-control"/><br/>
							<form:errors path="userPasswordConfirmation" delimiter=", " element="div" class="alert alert-danger"/>
                        </div>
                		<form:hidden path="userEmail"/>
						<button id="confirmRegistration" type="submit" class="btn btn-default"><spring:message code="user.registerConfirmed.button" text="Confirm Registration" /></button>
            		</form:form>
					</div>
</div>
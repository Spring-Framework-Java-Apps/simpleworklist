<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
					<div>
					<form:form id="formId" commandName="userAccountFormBean" method="post" class="ym-form ym-columnar linearize-form">
                        <div class="control-group">
                		    <form:label path="userFullname" class="control-label">Full Name  <sup class="ym-required">*</sup></form:label>
                            <div class="controls">
                                <form:input path="userFullname" class="input-large"/><br/>
                		        <form:errors path="userFullname" class="alert alert-error"/>
                		    </div>
                        </div>
                        <div class="control-group">
                		    <form:label path="userPassword" class="control-label">Password <sup class="ym-required">*</sup></form:label>
                            <div class="controls">
                                <form:password path="userPassword" class="input-large"/><br/>
                		        <form:errors path="userPassword" class="alert alert-error"/>
                            </div>
                        </div>
                        <div class="control-group">
                		    <form:label path="userPasswordConfirmation" class="control-label">Password again <sup class="ym-required">*</sup></form:label>
                            <div class="controls">
                                <form:password path="userPasswordConfirmation" class="input-large"/><br/>
                		        <form:errors path="userPasswordConfirmation" class="alert alert-error"/>
                		    </div>
                        </div>
                		<form:hidden path="userEmail"/>
                        <div class="controls">
                		    <input id="createNewDataLeaf" type="submit" name="actionItem" value="Confirm Registration"/>
                		</div>
            		</form:form>
					</div>
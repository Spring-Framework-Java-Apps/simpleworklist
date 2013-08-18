<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<form:form id="formId" commandName="registerFormBean" method="post" class="form-horizontal">
    <div class="control-group">
        <form:label path="email" class="control-label">Email</form:label>
        <div class="controls">
            <form:input path="email" class="input-large"/>
            <form:errors path="email" class="alert alert-error"/>
        </div>
    </div>
    <div class="controls">
        <input id="createNewDataLeaf" type="submit" name="actionItem" value="Request Password Reset" class="btn btn-primary"/>
    </div>
</form:form>
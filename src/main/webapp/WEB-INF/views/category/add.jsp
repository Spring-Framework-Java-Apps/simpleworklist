<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- New Category Form -->
<form:form id="formId" commandName="category" method="post">
	<div class="form-group">
		<form:label path="name" class="control-label">Name</form:label>
		<form:input path="name" class="form-control" />
		<form:errors path="name" class="alert alert-error"/>
	</div>
	<div class="form-group">
		<form:label path="description" class="control-label">Description</form:label>
		<form:input path="description" class="form-control" />
		<form:errors path="description" class="alert alert-error"/>
	</div>
	<button id="createNewCategory" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Add Category</button>
</form:form>
<!-- Document Window End -->
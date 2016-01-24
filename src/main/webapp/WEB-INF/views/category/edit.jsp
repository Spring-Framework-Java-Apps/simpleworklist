<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- New Category Form -->
<form:form id="formId" commandName="category" method="post">
	<div class="form-group">
		<form:label path="name">Name</form:label>
		<form:input path="name" class="form-control" />
		<form:errors path="name" class="alert alert-error"/>
	</div>
	<div class="form-group">
		<form:label path="description">Description</form:label>
		<form:input path="description" class="form-control" />
		<form:errors path="description" class="alert alert-error"/>
	</div>
	<form:hidden path="id"/>
	<button id="saveEditedCategory" type="submit" class="btn btn-default">Save Category</button>
</form:form>
<!-- Document Window End -->
<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- New Project Form -->
<h1><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span> Edit Project</h1>
<div>
<form:form id="formId" commandName="project" method="post">
	<div class="form-group">
		<form:label path="name">Name</form:label>
		<form:input path="name" class="form-control" />
		<form:errors path="name" delimiter=", " element="div" class="alert alert-danger"/>
	</div>
	<div class="form-group">
		<form:label path="description">Description</form:label>
		<form:input path="description" class="form-control" />
		<form:errors path="description" delimiter=", " element="div" class="alert alert-danger"/>
	</div>
	<form:hidden path="id"/>
	<button id="saveEditedProject" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Save Project</button>
</form:form>
</div>
<!-- Document Window End -->
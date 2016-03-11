<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- New Project Form -->
<h1><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span> &nbsp; <spring:message code="project.add.h1" text="Add Project" /></h1>
<div>
<form:form id="formId" commandName="project" method="post">
	<div class="form-group">
		<form:label path="name" class="control-label">Name</form:label>
		<form:input path="name" class="form-control" />
		<form:errors path="name" delimiter=", " element="div" class="alert alert-danger"/>
	</div>
	<div class="form-group">
		<form:label path="description" class="control-label"><spring:message code="project.add.description" text="Description" /></form:label>
		<form:input path="description" class="form-control" />
		<form:errors path="description"  delimiter=", " element="div" class="alert alert-danger"/>
	</div>
	<button id="createNewProject" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> <spring:message code="project.add.button" text="Add Project" /></button>
</form:form>
</div>
<!-- Document Window End -->
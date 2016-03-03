<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- Document Window -->
<h1><span class="glyphicon glyphicon-edit" aria-hidden="true"></span>&nbsp; Edit Task</h1>
<div>
<form:form id="formId" commandName="task" method="post">
	<form:hidden path="focusType" />
	<form:hidden path="userAccount.id" />
	<form:hidden path="createdTimestamp.time" />
 	<div class="form-group">
    	<form:label path="title">Titel</form:label>
		<form:input path="title" class="form-control"/>
		<form:errors path="title" delimiter=", " element="div" class="alert alert-danger"/>
	</div>
	<div class="form-group">
		<form:label path="dueDate">Due Date</form:label>
		<form:input id="taskDueDate" path="dueDate" class="form-control"/>
		<form:errors path="dueDate" delimiter=", " element="div" class="alert alert-danger"/>
	</div>
     <div class="form-group">
		 <form:label path="text">Text</form:label>
		 <form:textarea path="text" rows="10" cols="50" class="form-control"/>
		 <form:errors path="text" delimiter=", " element="div" class="alert alert-danger"/>
    </div>
	<button id="editDataLeaf" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Save Task</button>
	&nbsp;&nbsp;&nbsp;<a href="<c:url value="/task/transform/${task.id}"/>"><span class="glyphicon glyphicon-refresh" aria-hidden="true"></span> Transform into Project</a>
</form:form>
</div>

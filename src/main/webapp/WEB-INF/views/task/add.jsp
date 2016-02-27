<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>	
<!-- New Task Form -->
<h1><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> &nbsp; Add Task</h1>
<div>
<form:form id="formId" commandName="task" method="post">
	<form:hidden path="focusType" />
	<form:hidden path="userAccount.id" />
	<form:hidden path="createdTimestamp.time" />
	<div class="form-group">
    	<form:label path="title">Titel</form:label>
		<form:input path="title" class="form-control"/>
		<form:errors path="title" class="alert alert-error"/>
    </div>
	<div class="form-group">
		<form:label path="dueDate">Due Date</form:label>
		<form:input id="taskDueDate"  path="dueDate" class="form-control"/>
		<form:errors path="dueDate" class="alert alert-error"/>
	</div>
    <div class="form-group">
    	<form:label path="text">Text</form:label>
		<form:textarea path="text" rows="10" cols="50"  class="form-control"/>
		<form:errors path="text" class="alert alert-error"/>
    </div>
	<button id="createNewTask" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Add Task</button>
</form:form>
</div>
				


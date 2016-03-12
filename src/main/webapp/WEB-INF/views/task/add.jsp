<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<%@ page import="org.woehlke.simpleworklist.entities.enumerations.TaskEnergy" %>
<%@ page import="org.woehlke.simpleworklist.entities.enumerations.TaskTime" %>
<!-- New Task Form -->
<h1><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> &nbsp; <spring:message code="task.add.h1" text="Add Task" /></h1>
<div>
<form:form id="formId" commandName="task" method="post">
	<form:hidden path="taskState" />
	<form:hidden path="userAccount.id" />
	<form:hidden path="createdTimestamp.time" />
	<div class="form-group">
    	<form:label path="title"><spring:message code="task.add.title" text="Title" /></form:label>
		<form:input path="title" class="form-control"/>
		<form:errors path="title" delimiter=", " element="div" class="alert alert-danger"/>
    </div>
	<div class="form-group">
		<form:label path="dueDate"><spring:message code="task.add.dueDate" text="Due Date" /></form:label>
		<form:input id="taskDueDate"  path="dueDate" class="form-control"/>
		<form:errors path="dueDate" delimiter=", " element="div" class="alert alert-danger"/>
	</div>
    <div class="form-group">
    	<form:label path="text"><spring:message code="task.add.text" text="Text" /></form:label>
		<form:textarea path="text" rows="10" cols="50"  class="form-control"/>
		<form:errors path="text" delimiter=", " element="div" class="alert alert-danger"/>
    </div>
	<div>
	<span class="form-group">
		<form:label path="taskEnergy"><spring:message code="task.add.taskEnergy" text="Energy" /></form:label>
		<form:select  path="taskEnergy">
			<form:options items="${TaskEnergy.values()}" itemLabel="label" itemValue="value"></form:options>
		</form:select>
		<form:errors path="taskEnergy" delimiter=", " element="div" class="alert alert-danger"/>
	</span>&nbsp;
	<span class="form-group">
		<form:label path="taskTime"><spring:message code="task.add.taskTime" text="Time" /></form:label>
		<form:select  path="taskTime">
			<form:options items="${TaskTime.values()}" itemLabel="label" itemValue="value"></form:options>
		</form:select>
		<form:errors path="taskTime" delimiter=", " element="div" class="alert alert-danger"/>
	</span>
	</div>
	<button id="createNewTask" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> <spring:message code="task.add.button" text="Add Task" /></button>
</form:form>
</div>
				


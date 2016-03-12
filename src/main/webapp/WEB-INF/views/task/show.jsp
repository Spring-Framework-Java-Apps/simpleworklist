<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<%@ page import="org.woehlke.simpleworklist.entities.enumerations.TaskEnergy" %>
<%@ page import="org.woehlke.simpleworklist.entities.enumerations.TaskTime" %>
<!-- Document Window -->
<h1><span class="glyphicon glyphicon-edit" aria-hidden="true"></span>&nbsp; <spring:message code="task.show.h1" text="Edit Task" /></h1>
<div>
<form:form id="formId" commandName="task" method="post">
	<form:hidden path="taskState" />
	<form:hidden path="userAccount.id" />
	<form:hidden path="createdTimestamp.time" />
 	<div class="form-group">
    	<form:label path="title"><spring:message code="task.show.title" text="Title" /></form:label>
		<form:input path="title" class="form-control"/>
		<form:errors path="title" delimiter=", " element="div" class="alert alert-danger"/>
	</div>
	<div class="form-group">
		<form:label path="dueDate"><spring:message code="task.show.dueDate" text="Due Date" /></form:label>
		<form:input id="taskDueDate" path="dueDate" class="form-control"/>
		<form:errors path="dueDate" delimiter=", " element="div" class="alert alert-danger"/>
	</div>
 	<div class="form-group">
		 <form:label path="text"><spring:message code="task.show.text" text="Text" /></form:label>
		 <form:textarea path="text" rows="10" cols="50" class="form-control"/>
		 <form:errors path="text" delimiter=", " element="div" class="alert alert-danger"/>
    </div>
	<div>
	<span class="form-group">
		<form:label path="taskEnergy"><spring:message code="task.show.taskEnergy" text="Energy" /></form:label>
		<form:select  path="taskEnergy">
			<c:forEach items="${listTaskEnergy}" var="taskEnergyItem">
				<c:choose>
					<c:when test="${taskEnergyItem.id == task.taskEnergy.id}">
						<option value="${taskEnergyItem.value}" selected><spring:message code="${taskEnergyItem.code}" /></option>
					</c:when>
					<c:otherwise>
						<option value="${taskEnergyItem.value}"><spring:message code="${taskEnergyItem.code}" /></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</form:select>
		<form:errors path="taskEnergy" delimiter=", " element="div" class="alert alert-danger"/>
	</span>&nbsp;
	<span class="form-group">
		<form:label path="taskTime"><spring:message code="task.show.taskTime" text="Time" /></form:label>
		<form:select  path="taskTime">
			<c:forEach items="${listTaskTime}" var="taskTimeItem">
				<c:choose>
					<c:when test="${taskTimeItem.id == task.taskTime.id}">
						<option value="${taskTimeItem.value}" selected><spring:message code="${taskTimeItem.code}" text="${taskTimeItem.value}" /></option>
					</c:when>
					<c:otherwise>
						<option value="${taskTimeItem.value}"><spring:message code="${taskTimeItem.code}" text="${taskTimeItem.value}" /></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</form:select>
		<form:errors path="taskTime" delimiter=", " element="div" class="alert alert-danger"/>
	</span>
	</div>
	<button id="editDataLeaf" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> <spring:message code="task.show.button" text="Save Task" /></button>
	&nbsp;&nbsp;&nbsp;<a href="<c:url value="/task/transform/${task.id}"/>"><span class="glyphicon glyphicon-refresh" aria-hidden="true"></span> <spring:message code="task.show.transform" text="Transform into Project" /></a>
</form:form>
</div>

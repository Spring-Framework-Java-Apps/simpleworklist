<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- New Project Form -->
<h1><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span> <spring:message code="project.edit.h1" text="Edit Project" /></h1>
<div>
<form:form id="formId" commandName="project" method="post">
	<div class="form-group">
		<form:label path="name">Name</form:label>
		<form:input path="name" class="form-control" />
		<form:errors path="name" delimiter=", " element="div" class="alert alert-danger"/>
	</div>
	<div class="form-group">
		<form:label path="description"><spring:message code="project.edit.description" text="Description" /></form:label>
		<form:input path="description" class="form-control" />
		<form:errors path="description" delimiter=", " element="div" class="alert alert-danger"/>
	</div>
	<div class="form-group">
		<form:hidden path="id"/>
		<form:label path="area.id"><spring:message code="project.edit.area" text="Area" /></form:label>
		<form:select  path="area.id">
			<c:forEach items="${areas}" var="areaOption">
				<c:choose>
					<c:when test="${locale == 'de'}">
						<c:set var="label" value="${areaOption.nameDe}"/>
					</c:when>
					<c:otherwise>
						<c:set var="label" value="${areaOption.nameEn}"/>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${areaOption.id == project.area.id}">
						<option value="${areaOption.id}" selected><c:out value="${label}"/></option>
					</c:when>
					<c:otherwise>
						<option value="${areaOption.id}"><c:out value="${label}"/></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</form:select>
		<form:errors path="area.id" delimiter=", " element="div" class="alert alert-danger"/>
	</div>
	<form:hidden path="id"/>
	<button id="saveEditedProject" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> <spring:message code="project.edit.button" text="Save Project" /></button>
</form:form>
</div>
<!-- Document Window End -->
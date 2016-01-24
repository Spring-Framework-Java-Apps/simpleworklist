<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>	
<!-- New ActionItem Form -->
<form:form id="formId" commandName="data" method="post">
	<div class="form-group">
    	<form:label path="title">Titel</form:label>
		<form:input path="title" class="form-control"/>
		<form:errors path="title" class="alert alert-error"/>
    </div>
	<div class="form-group">
		<form:label path="status">Status</form:label>
		<c:forEach var="state" items="${stateValues}">
			<form:radiobutton path="status" value="${state}"/>
			<c:choose>
				<c:when test="${state.name() == 'NEW'}">
					<button class="btn btn-small btn-small btn-danger" type="button">&nbsp;</button>
				</c:when>
				<c:when test="${state.name() == 'WORK'}">
					<button class="btn btn-small btn-warning" type="button">&nbsp;</button>
				</c:when>
				<c:when test="${state.name() == 'DONE'}">
					<button class="btn btn-small btn-success" type="button">&nbsp;</button>
				</c:when>
			</c:choose>
		</c:forEach>
	</div>
    <div class="form-group">
    	<form:label path="text">Text</form:label>
		<form:textarea path="text" rows="20" cols="50"  class="form-control"/>
		<form:errors path="text" class="alert alert-error"/>
    </div>
	<button id="createNewDataLeaf" type="submit" class="btn btn-default">Add Action Item</button>
</form:form>
				


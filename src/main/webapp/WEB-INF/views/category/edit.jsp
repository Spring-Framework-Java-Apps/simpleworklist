<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- New Category Form -->
<form:form id="formId" commandName="category" method="post"
	class="form-horizontal">
	<div class="control-group">
		<form:label path="name" class="control-label">Name <sup
				class="ym-required">*</sup>
		</form:label>
		<div class="controls">
			<form:input path="name" class="input-large" />
		</div>
		<form:errors path="name" class="alert alert-error"/>
	</div>
	<div class="control-group">
		<form:label path="description" class="control-label">Description <sup
				class="ym-required">*</sup>
		</form:label>
		<div class="controls">
			<form:input path="description" class="input-large" />
		</div>
		<form:errors path="description" class="alert alert-error"/>
	</div>
	<div class="controls">
		<form:hidden path="id"/>
		<input id="createNewRootCategory" type="submit" 
			value="Save Category" class="btn btn-primary" />
	</div>
</form:form>
<!-- Document Window End -->
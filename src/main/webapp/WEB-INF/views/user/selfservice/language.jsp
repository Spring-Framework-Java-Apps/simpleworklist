<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> <spring:message code="user.selfservice.profile.changeLanguage" text="Set Default Language" /></h1>
<h2><spring:message code="user.selfservice.forUser" text="for User:" /> <strong><sec:authentication property="principal.username" /></strong></h2>
<form:form id="formId" commandName="userChangeLanguageFormBean" method="post">
	<div class="form-group">
		<form:label path="defaultLanguage"><spring:message code="user.selfservice.language.defaultLanguage" text="Language" /></form:label>
		<form:select  path="defaultLanguage">
            <c:forEach items="${languages}" var="language">
                <c:choose>
                    <c:when test="${language.id == thisUser.defaultLanguage.id}">
                        <option value="${language.value}" selected><spring:message code="${language.code}" text="${language.value}"/></option>
                    </c:when>
                    <c:otherwise>
                        <option value="${language.value}"><spring:message code="${language.code}" text="${language.value}"/></option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </form:select>
		<form:errors path="defaultLanguage" delimiter=", " element="div" class="alert alert-danger"/>
	</div>
    <button id="saveLanguageButton" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> <spring:message code="user.selfservice.language.button" text="Save Language" /></button>
</form:form>
<br/>
<a href="<c:url value="/user/selfservice"/>"><spring:message code="button.back" text="back" /></a>
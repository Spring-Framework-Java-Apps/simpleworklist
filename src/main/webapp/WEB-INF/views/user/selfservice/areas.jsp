<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> <spring:message code="user.selfservice.profile.changeAreas" text="Change Areas" /></h1>
<h2><spring:message code="user.selfservice.forUser" text="for User:" /> <strong><sec:authentication property="principal.username" /></strong></h2>
    <div>
        <a href='<c:url value="/user/selfservice/area/add"/>'><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> <spring:message code="user.selfservice.areas.add.button" text="Add Area" /></a><br />
    <!-- <h2>Tasks</h2> -->
    <table class="table table-striped table-hover">
    <tr>
        <th><spring:message code="enum.language.en" text="Englisch" /></th>
        <th><spring:message code="enum.language.de" text="Deutsch" /></th>
        <th>&nbsp;</th>
        <th>&nbsp;</th>
    </tr>
    <c:forEach items="${areas}" var="area">
        <tr>
            <td><c:out value="${area.nameEn}"/></td>
            <td><c:out value="${area.nameDe}"/></td>
            <td>
                <a href="<c:url value="/user/selfservice/area/edit/${area.id}"/>"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>
            </td>
            <td>
                <a href="<c:url value="/user/selfservice/area/delete/${area.id}"/>"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
            </td>
        </tr>
    </c:forEach>
    </table>
    </div>
<div>
    <form:form id="formId" commandName="thisUser" method="post">
        <div class="form-group">
            <form:hidden path="id"/>
            <form:label path="defaultArea.id"><spring:message code="user.selfservice.area.default" text="Default Area" /></form:label>
            <form:select  path="defaultArea.id">
                <c:forEach items="${areas}" var="area">
                    <c:choose>
                        <c:when test="${locale == 'de'}">
                            <c:set var="label" value="${area.nameDe}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="label" value="${area.nameEn}"/>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${area.id == thisUser.defaultArea.id}">
                            <option value="${area.id}" selected><c:out value="${label}"/></option>
                        </c:when>
                        <c:otherwise>
                            <option value="${area.id}"><c:out value="${label}"/></option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </form:select>
            <form:errors path="defaultArea.id" delimiter=", " element="div" class="alert alert-danger"/>
        </div>
        <button id="saveLanguageButton" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> <spring:message code="user.selfservice.area.default.button" text="Save Default Area" /></button>
    </form:form>
</div>
<br/>
<a href="<c:url value="/user/selfservice"/>"><spring:message code="button.back" text="back" /></a>
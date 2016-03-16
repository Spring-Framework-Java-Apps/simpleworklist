<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<h1><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> <spring:message code="user.selfservice.profile.changeAreas" text="Change Areas" /></h1>
<h2><spring:message code="user.selfservice.forUser" text="for User:" /> <strong><sec:authentication property="principal.username" /></strong></h2>
    <div>
        <a href='<c:url value="/area/add"/>'><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> <spring:message code="user.selfservice.areas.add.button" text="Add Area" /></a><br />
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
                <a href="<c:url value="/area/edit/${area.id}"/>"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>
            </td>
            <td>
                <a href="<c:url value="/area/delete/${area.id}"/>"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
            </td>
        </tr>
    </c:forEach>
    <tr>

    </tr>
    </table>
    </div>
<br/>
<a href="<c:url value="/user/selfservice"/>"><spring:message code="button.back" text="back" /></a>
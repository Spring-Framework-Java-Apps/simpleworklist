<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- Document Window -->
<h1><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> <spring:message code="tasks.next.h1" text="Next" /></h1>
<c:if test="${! empty message}">
    <div class="alert alert-danger alert-dismissible" role="alert">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <c:out value="${message}"></c:out>
    </div>
</c:if>
<c:if test="${! empty dataList}">
    <div>
        <table class="table table-striped table-hover">
            <tr>
                <th>&nbsp;</th>
                <th><spring:message code="tasks.next.title" text="Title" /></th>
                <th><spring:message code="tasks.next.text" text="Text" /></th>
                <th><spring:message code="tasks.next.project" text="Project" /></th>
                <th>&nbsp;</th>
            </tr>
            <c:forEach items="${dataList}" var="task">
                <tr>
                    <td>
                        <a alt="complete" href='<c:url value="/task/complete/${task.id}"/>'><span class="glyphicon glyphicon-unchecked" aria-hidden="true"></span></a>
                        &nbsp;&nbsp;
                        <c:if test="${task.focus}">
                            <a alt="focus" href='<c:url value="/task/unsetfocus/${task.id}"/>'><span class="glyphicon glyphicon-star" aria-hidden="true"></span></a>
                        </c:if>
                        <c:if test="${! task.focus}">
                            <a alt="focus" href='<c:url value="/task/setfocus/${task.id}"/>'><span class="glyphicon glyphicon-star-empty" aria-hidden="true"></span></a>
                        </c:if>
                    </td>
                    <td>
                        <a href='<c:url value="/task/detail/${task.id}"/>' class="dataDetailListTitle"
                           id="dataDetail_${task.id}" ><c:out
                                value="${task.title}" /></a>
                    </td>
                    <td>
                        <c:out value="${task.textShortened}" />
                    </td>
                    <td>
                        <c:if test="${task.project != null}">
                            <a href='<c:url value="/project/${task.project.id}"/>'><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span> <c:out value="${task.project.name}" /></a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${task.taskEnergy ne 'NONE'}">
                            <span class="badge"><spring:message code="${task.taskEnergy.code}" /></span>
                        </c:if>
                        <c:if test="${task.taskTime ne 'NONE'}">
                            <span class="badge"><spring:message code="${task.taskTime.code}" /></span>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <nav>
        <c:url var="firstUrl" value="/tasks/next?page=1" />
        <c:url var="lastUrl" value="/tasks/next?page=${totalPages}" />
        <c:url var="prevUrl" value="/tasks/next?page=${currentIndex - 1}" />
        <c:url var="nextUrl" value="/tasks/next?page=${currentIndex + 1}" />
        <div>
            <ul class="pagination">
                <c:choose>
                    <c:when test="${currentIndex == 1}">
                        <li class="disabled"><a href="#">&lt;&lt;</a></li>
                        <li class="disabled"><a href="#">&lt;</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${firstUrl}">&lt;&lt;</a></li>
                        <li><a href="${prevUrl}">&lt;</a></li>
                    </c:otherwise>
                </c:choose>
                <c:forEach var="i" begin="${beginIndex}" end="${endIndex}">
                    <c:url var="pageUrl" value="/tasks/next?page=${i}" />
                    <c:choose>
                        <c:when test="${i == currentIndex}">
                            <li class="active"><a href="${pageUrl}"><c:out value="${i}" /></a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${pageUrl}"><c:out value="${i}" /></a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:choose>
                    <c:when test="${currentIndex == totalPages}">
                        <li class="disabled"><a href="#">&gt;</a></li>
                        <li class="disabled"><a href="#">&gt;&gt;</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${nextUrl}">&gt;</a></li>
                        <li><a href="${lastUrl}">&gt;&gt;</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </nav>
</c:if>
<c:if test="${empty dataList}">
    <p><spring:message code="tasks.next.noTasks" text="You have no Tasks to do Next" /></p>
</c:if>

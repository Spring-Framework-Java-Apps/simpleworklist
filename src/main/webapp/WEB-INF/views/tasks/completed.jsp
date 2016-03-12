<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- Document Window -->
<h1><span class="glyphicon glyphicon-check" aria-hidden="true"></span> <spring:message code="tasks.completed.h1" text="Completed Tasks" /></h1>
<a href='<c:url value="/tasks/completed/deleteall"/>'><span class="glyphicon glyphicon-trash" aria-hidden="true"></span> <spring:message code="tasks.completed.delete" text="Delete All Completed Tasks" /></a>
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
                <th><spring:message code="tasks.completed.title" text="Title" /></th>
                <th><spring:message code="tasks.completed.text" text="Text" /></th>
                <th><spring:message code="tasks.completed.project" text="Project" /></th>
                <th>&nbsp;</th>
            </tr>
            <c:forEach items="${dataList}" var="task">
                <tr>
                    <td>
                        <del><a alt="incomplete" href='<c:url value="/task/incomplete/${task.id}"/>'><span class="glyphicon glyphicon-check" aria-hidden="true"></span></a></del>
                        &nbsp;&nbsp;
                        <c:if test="${task.focus}">
                            <a alt="focus" href='<c:url value="/task/unsetfocus/${task.id}"/>'><span class="glyphicon glyphicon-star" aria-hidden="true"></span></a>
                        </c:if>
                        <c:if test="${! task.focus}">
                            <a alt="focus" href='<c:url value="/task/setfocus/${task.id}"/>'><span class="glyphicon glyphicon-star-empty" aria-hidden="true"></span></a>
                        </c:if>
                    </td>
                    <td>
                        <del><a href='<c:url value="/task/detail/${task.id}"/>' class="dataDetailListTitle"
                           id="dataDetail_${task.id}" ><c:out
                                value="${task.title}" /></a></del>
                    </td>
                    <td>
                        <del><c:out value="${task.textShortened}" /></del>
                    </td>
                    <td>
                        <c:if test="${task.project != null}">
                            <a href='<c:url value="/project/${task.project.id}"/>'><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span> <c:out value="${task.project.name}" /></a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${task.taskEnergy ne 'NONE'}">
                            <span class="badge"><c:out value="${task.taskEnergy}"></c:out></span>
                        </c:if>
                        <c:if test="${task.taskTime ne 'NONE'}">
                            <span class="badge"><c:out value="${task.taskTime}"></c:out></span>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <nav>
        <c:url var="firstUrl" value="/tasks/completed?page=1" />
        <c:url var="lastUrl" value="/tasks/completed?page=${totalPages}" />
        <c:url var="prevUrl" value="/tasks/completed?page=${currentIndex - 1}" />
        <c:url var="nextUrl" value="/tasks/completed?page=${currentIndex + 1}" />
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
                    <c:url var="pageUrl" value="/tasks/completed?page=${i}" />
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
    <p><spring:message code="tasks.completed.noTasks" text="You have no completed Tasks" /></p>
</c:if>

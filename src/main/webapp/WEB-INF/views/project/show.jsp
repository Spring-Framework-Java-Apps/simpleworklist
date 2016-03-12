<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
				<!-- Document Window -->
				<c:if test="${thisProject.id > 0}">
					<h1><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span>&nbsp; <c:out value="${thisProject.name}" /></h1>
					<c:out value="${thisProject.description}" /><br /><br />
					<a href='<c:url value="/project/${thisProject.id}/edit"/>'><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> <spring:message code="project.show.edit" text="Edit Project" /></a> |
					<a href='<c:url value="/project/${thisProject.id}/delete"/>'><span class="glyphicon glyphicon-trash" aria-hidden="true"></span> <spring:message code="project.show.delete" text="Delete Project" /></a> |
					<a href='<c:url value="/task/addtoproject/${thisProject.id}"/>'><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> <spring:message code="project.show.addTask" text="Add Task" /></a><br />
				</c:if>
				<br />
				<c:if test="${! empty message}">
					<div class="alert alert-danger alert-dismissible" role="alert">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<c:out value="${message}"></c:out>
					</div>
				</c:if>
				<c:if test="${! empty dataList}">
				<div>
					<!-- <h2>Tasks</h2> -->
					<table class="table table-striped table-hover">
					<tr>
						<th>&nbsp;</th>
						<th><spring:message code="project.show.title" text="Title" /></th>
						<th><spring:message code="project.show.text" text="Text" /></th>
						<th><spring:message code="project.show.dueDate" text="Due Date" /></th>
						<th><spring:message code="project.show.taskState" text="Task State" /></th>
						<th>&nbsp;</th>
					</tr>
					<c:forEach items="${dataList}" var="task">
					<tr>
						<td>
							<c:if test="${task.taskState.name() eq 'COMPLETED'}">
								<a alt="complete" href='<c:url value="/task/incomplete/${task.id}"/>'><span class="glyphicon glyphicon-check" aria-hidden="true"></span></a>
							</c:if>
							<c:if test="${task.taskState.name() ne 'COMPLETED'}">
								<a alt="complete" href='<c:url value="/task/complete/${task.id}"/>'><span class="glyphicon glyphicon-unchecked" aria-hidden="true"></span></a>
							</c:if>
							&nbsp;&nbsp;
							<c:if test="${task.focus}">
								<a alt="focus" href='<c:url value="/task/unsetfocus/${task.id}?back=project"/>'><span class="glyphicon glyphicon-star" aria-hidden="true"></span></a>
							</c:if>
							<c:if test="${! task.focus}">
								<a alt="focus" href='<c:url value="/task/setfocus/${task.id}?back=project"/>'><span class="glyphicon glyphicon-star-empty" aria-hidden="true"></span></a>
							</c:if>
						</td>
						<td>
							<c:if test="${task.taskState.name() eq 'COMPLETED'}">
								<del>
							</c:if>
							<a href='<c:url value="/task/detail/${task.id}"/>' class="dataDetailListTitle"
							id="dataDetail_${task.id}" ><c:out
									value="${task.title}" /></a>
							<c:if test="${task.taskState.name() eq 'COMPLETED'}">
								</del>
							</c:if>
						</td>
						<td>
							<c:if test="${task.taskState.name() eq 'COMPLETED'}">
								<del>
							</c:if>
							<c:out value="${task.textShortened}" />
							<c:if test="${task.taskState.name() eq 'COMPLETED'}">
								</del>
							</c:if>
						</td>
						<td>
							<fmt:formatDate value="${task.dueDate}" />
						</td>
						<td>
							<c:choose>
								<c:when test="${task.taskState.name() eq 'INBOX'}">
									<a href='<c:url value="/tasks/inbox"/>'><span class="glyphicon glyphicon-inbox" aria-hidden="true"></span> <spring:message code="project.show.state.inbox" text="Inbox" /></a>
								</c:when>
								<c:when test="${task.taskState.name() eq 'TODAY'}">
									<a href='<c:url value="/tasks/today"/>'><span class="glyphicon glyphicon glyphicon-time" aria-hidden="true"></span> <spring:message code="project.show.state.today" text="Today" /></a>
								</c:when>
								<c:when test="${task.taskState.name() eq 'NEXT'}">
									<a href='<c:url value="/tasks/next"/>'><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> <spring:message code="project.show.state.next" text="Next" /></a>
								</c:when>
								<c:when test="${task.taskState.name() eq 'WAITING'}">
									<a href='<c:url value="/tasks/waiting"/>'><span class="glyphicon glyphicon-hourglass" aria-hidden="true"></span> <spring:message code="project.show.state.waiting" text="Waiting" /></a>
								</c:when>
								<c:when test="${task.taskState.name() eq 'SCHEDULED'}">
									<a href='<c:url value="/tasks/scheduled"/>'><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span> <spring:message code="project.show.state.scheduled" text="Scheduled" /></a>
								</c:when>
								<c:when test="${task.taskState.name() eq 'SOMEDAY'}">
									<a href='<c:url value="/tasks/someday"/>'><span class="glyphicon glyphicon-road" aria-hidden="true"></span> <spring:message code="project.show.state.someday" text="Someday" /></a>
								</c:when>
								<c:when test="${task.taskState.name() eq 'COMPLETED'}">
									<a href='<c:url value="/tasks/completed"/>'><span class="glyphicon glyphicon-check" aria-hidden="true"></span> <spring:message code="project.show.state.completed" text="Completed Tasks" /></a>
								</c:when>
								<c:when test="${task.taskState.name() eq 'TRASHED'}">
									<a href='<c:url value="/tasks/trash"/>'><span class="glyphicon glyphicon-trash" aria-hidden="true"></span> <spring:message code="project.show.state.trash" text="Trash" /></a>
								</c:when>
							</c:choose>
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
					<c:url var="firstUrl" value="/project/${thisProject.id}/page/1" />
					<c:url var="lastUrl" value="/project/${thisProject.id}/page/${totalPages}" />
					<c:url var="prevUrl" value="/project/${thisProject.id}/page/${currentIndex - 1}" />
					<c:url var="nextUrl" value="/project/${thisProject.id}/page/${currentIndex + 1}" />
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
					            <c:url var="pageUrl" value="/project/${thisProject.id}/page/${i}" />
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
					<spring:message code="project.show.nothingToDo" text="There is nothing to do in this project." />
				</c:if>

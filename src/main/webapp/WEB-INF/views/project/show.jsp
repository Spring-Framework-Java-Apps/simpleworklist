<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
				<!-- Document Window -->
				<c:if test="${thisProject.id > 0}">
					<h1><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span>&nbsp; <c:out value="${thisProject.name}" /></h1>
					<c:out value="${thisProject.description}" /><br /><br />
					<a href='<c:url value="/project/${thisProject.id}/edit"/>'><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit Project</a> |
					<a href='<c:url value="/project/${thisProject.id}/delete"/>'><span class="glyphicon glyphicon-trash" aria-hidden="true"></span> Delete Project</a> |
					<a href='<c:url value="/task/addtoproject/${thisProject.id}"/>'><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> Add Task</a><br />
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
						<th>Title</th>
						<th>Text</th>
						<th>Due Date</th>
					</tr>
					<c:forEach items="${dataList}" var="task">
					<tr>
						<td>
							<a alt="complete" href='<c:url value="/task/complete/${task.id}"/>'><span class="glyphicon glyphicon-unchecked" aria-hidden="true"></span></a>
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
							<c:out value="${task.dueDate}" />
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

<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
				<!-- Document Window -->
				<c:if test="${thisCategory.id > 0}">
					<fieldset>
					<legend><c:out value="${thisCategory.name}" /></legend>
					<span class="span7"><c:out value="${thisCategory.description}" /></span>
					<span class="span3"><a href='<c:url value="/category/${thisCategory.id}/edit"/>'>Edit Category</a> | 
					<a href='<c:url value="/category/${thisCategory.id}/delete"/>'>Delete</a></span>
					</fieldset>
				</c:if>
				<br />
				<c:if test="${! empty dataList}">
				<div>
					<table class="table table-striped table-hover">
					<c:forEach items="${dataList}" var="actionItem">
					<tr>
						<td>
							<a href='<c:url value="/actionItem/detail/${actionItem.id}"/>' class="dataDetailListTitle"
							id="dataDetail_${actionItem.id}" ><c:out
									value="${actionItem.title}" /></a>
						</td>
						<td>
							<c:choose>
								<c:when test="${actionItem.status eq 'NEW'}">
									<button class="btn btn-small btn-danger" type="button">&nbsp;</button>
								</c:when>
								<c:when test="${actionItem.status.name() eq 'WORK'}">
									<button class="btn btn-small btn-warning" type="button">&nbsp;</button>
								</c:when>
								<c:when test="${actionItem.status.name() eq 'DONE'}">
									<button class="btn btn-small btn-success" type="button">&nbsp;</button>
								</c:when>
							</c:choose>
						</td>
						<td>
							<a href='<c:url value="/actionItem/delete/${actionItem.id}"/>'>Delete</a>
						</td>
					</tr>
					</c:forEach>
					</table>
				</div>
				<nav>
					<c:url var="firstUrl" value="/category/${thisCategory.id}/page/1" />
					<c:url var="lastUrl" value="/category/${thisCategory.id}/page/${totalPages}" />
					<c:url var="prevUrl" value="/category/${thisCategory.id}/page/${currentIndex - 1}" />
					<c:url var="nextUrl" value="/category/${thisCategory.id}/page/${currentIndex + 1}" />
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
					            <c:url var="pageUrl" value="/category/${thisCategory.id}/page/${i}" />
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

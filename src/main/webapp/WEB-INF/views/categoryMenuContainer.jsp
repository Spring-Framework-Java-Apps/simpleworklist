<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<ul id="menu" class="nav nav-list">
<c:choose>
	<c:when test="${thisProject.id == 0}">
		<li><span id="categoryNode_0" class="categoryNode"><strong>Home</strong></span></li>
	</c:when>
	<c:otherwise>
		<li><span id="categoryNode_0" class="categoryNode"><a href='<c:url value="/project/0"/>'>Home</a></span></li>
	</c:otherwise>
</c:choose>
<c:set var="categories" value="${rootCategories}" scope="request"/>
<ul class="nav nav-list">
<jsp:include page="/WEB-INF/views/categoryMenu.jsp"/>
</ul>
</ul>

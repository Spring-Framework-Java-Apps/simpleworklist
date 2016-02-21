<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
	<c:forEach var="project" items="${categories}">
	  <c:choose>
	  	<c:when test="${project==thisProject}">
	  	    <!-- http://jqueryui.com/droppable/ -->
	  		<li id="category_<c:out value="${project.id}"/>" class="active">
	  		<strong><span id="categoryNode_${project.id}" class="categoryNode"><c:out value="${project.name}"/></span></strong>
	  	</c:when>
	  	<c:otherwise>
	  		<li id="category_<c:out value="${project.id}"/>">
	  		<span id="categoryNode_${project.id}" class="categoryNode"><a href="<c:url value="/project/${project.id}"/>"><c:out value="${project.name}"/></a></span>
	  	</c:otherwise>
	  </c:choose>
	  <c:if test="${fn:length(project.children) > 0}">
	  	<ul class="nav nav-list">
	    <c:set var="categories" value="${project.children}" scope="request"/>
      	<jsp:include page="/WEB-INF/views/categoryMenu.jsp"/>
      	</ul>
      </c:if>
      </li>
	</c:forEach>
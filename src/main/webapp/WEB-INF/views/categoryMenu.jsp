<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
	<c:forEach var="category" items="${categories}">  		
	  <c:choose>
	  	<c:when test="${category==thisCategory}">
	  	    <!-- http://jqueryui.com/droppable/ -->
	  		<li id="category_<c:out value="${category.id}"/>" class="active">
	  		<strong><span id="categoryNode_${category.id}" class="categoryNode"><c:out value="${category.name}"/></span></strong>
	  	</c:when>
	  	<c:otherwise>
	  		<li id="category_<c:out value="${category.id}"/>">
	  		<span id="categoryNode_${category.id}" class="categoryNode"><a href="<c:url value="/category/${category.id}"/>"><c:out value="${category.name}"/></a></span>
	  	</c:otherwise>
	  </c:choose>
	  <c:if test="${fn:length(category.children) > 0}">
	  	<ul class="nav nav-list">
	    <c:set var="categories" value="${category.children}" scope="request"/>
      	<jsp:include page="/WEB-INF/views/categoryMenu.jsp"/>
      	</ul>
      </c:if>
      </li>
	</c:forEach>
<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Simple Worklist</title>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>" media="screen">
    <link rel="stylesheet" href="<c:url value='/css/main.css'/>"  type="text/css">
	<script src="<c:url value='/js/bootstrap.min.js'/>"></script> 
	<script src="<c:url value='/js/jquery-1.9.1.min.js'/>"></script>
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css"  type="text/css">
	<script src="http://code.jquery.com/ui/1.10.1/jquery-ui.js"></script>
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
        	<div class="nav-collapse collapse">
				<span class="brand">Simple Worklist</span>
				<ul class="nav">
						<li><a href='<c:url value="/"/>'>Home</a></li>
						<li><a
							href='<c:url value="/category/addchild/${thisCategory.id}"/>'>New
								Category</a></li>
						<li><a
							href='<c:url value="/data/addtocategory/${thisCategory.id}"/>'>New
								Data</a></li>
						<li class="active"><strong>Show Category</strong></li>
				</ul>
				</div>
			</div>
		</div>
	</div>
	
	<div class="container-fluid">
      <div class="row-fluid">
			<ul class="breadcrumb">
			<li><a href='<c:url value="/category/0/page/1"/>'>Home</a></li>
			<c:forEach items="${breadcrumb}" var="bc"> 
			<li><span class="divider">&gt;&gt;</span><a href='<c:url value="/category/${bc.id}"/>'><c:out value="${bc.name}" /></a></li>
			</c:forEach>
			</ul>
      </div>
    </div>
	
	<div class="container-fluid">
      <div class="row-fluid">
        <div class="span3">
          <div id="categoryNavigationWell" class="well sidebar-nav">
					<sec:authorize access="fullyAuthenticated">
					<ul class="nav nav-list">
						<jsp:include page="/WEB-INF/views/categoryMenuContainer.jsp" />
					</ul>
					</sec:authorize>
				</div>
			</div>
			<div class="span6">
				<div class="row-fluid">
					<!-- Document Window -->
					<tiles:insertAttribute name="content" />
				</div>
			</div>
				<div class="span3">
				<div class="row-fluid">	
          		<div class="well sidebar-nav">
					<ul class="nav">
						<sec:authorize access="isAuthenticated()"><li><a
							href="<c:url value="/users" />">Users</a></li></sec:authorize>
						<sec:authorize access="isAuthenticated()"><li><a
							href="<c:url value="/test/helper/category/createTree" />">Create
								a Category Tree</a></li></sec:authorize>
						<sec:authorize access="isAnonymous()">
						<li><a href='<c:url value="/register"/>'>Register User</a></li>
						<li><a href='<c:url value="/login"/>'>Login User</a></li></sec:authorize>
						<sec:authorize access="isAuthenticated()"><li>User: <sec:authentication property="principal.username" /></li>
						<li><a href='<c:url value="/j_spring_security_logout"/>'>Logout</a></li></sec:authorize>
					</ul>
				</div>
				</div>
			</div>
		</div>
		
		<footer>
			<p>
				&copy; 2013 Thomas W&ouml;hlke
			</p>
		</footer>
		
		</div>
		
	 <script>
		$(function() {
			$( ".dataDetailListTitle").draggable({ cursor: "crosshair", revert: true  });
			$( ".categoryNode" ).draggable({ cursor: "crosshair", revert: true  });
			$( ".categoryNode" ).droppable({
				drop: function( event, ui ) {
					var selfId = ("" + $( this ).attr("id")).split("_")[1];
					var draggableId = "" +ui.draggable.attr("id").split("_")[1];
					var draggableType = ""+ui.draggable.attr("id").split("_")[0];
					var rootUrl = "";
					if(draggableType == "dataDetail"){
						rootUrl += '<c:url value="/data"/>';	
					} else {
						rootUrl += '<c:url value="/category"/>';
					}
					//var html4move = '<a href="'+rootUrl+'/'+draggableId+'/moveto/'+selfId+'">move</a>';
					//$( this ).html(html4move);
					var html4move = rootUrl+'/'+draggableId+'/moveto/'+selfId;
					window.location.replace(html4move);
				}
			});
		});
	</script>
</body>
</html>

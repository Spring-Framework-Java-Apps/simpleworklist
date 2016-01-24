<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Simple Worklist</title>
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
	<!-- Optional theme -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<!-- Latest compiled and minified JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/cupertino/jquery-ui.css" type="text/css">
	<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
	<link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
</head>
<body>
	<nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
						data-toggle="collapse" data-target="#mynavbar"
						aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="<c:url value="/"/>">Simple Worklist</a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse" id="mynavbar">
				<ul class="nav navbar-nav">
					<sec:authorize access="isAuthenticated()">
						<li class="dropdown">
							<a href="#" class="dropdown-toggle"
							   data-toggle="dropdown"
							   role="button" aria-haspopup="true"
							   aria-expanded="false">New Content<span class="caret"></span></a>
							<ul class="dropdown-menu">
								<li>
									<a href='<c:url value="/category/addchild/${thisCategory.id}"/>'>Add a Category</a>
								</li>
								<li>
									<a href='<c:url value="/actionItem/addtocategory/${thisCategory.id}"/>'>Add an Action Item</a>
								</li>
								<li role="separator" class="divider"></li>
								<li>
									<a href="<c:url value="/test/helper/category/createTree" />">Create Test Data</a>
								</li>
							</ul>
						</li>
						<li>
							<a href="<c:url value="/users" />">Show Users</a>
						</li>
						<li>
							<a href='<c:url value="/j_spring_security_logout"/>'>Logout</a>
						</li>
					</sec:authorize>
					<sec:authorize access="isAnonymous()">
						<li>
							<a href='<c:url value="/register"/>'>Register as New User</a>
						</li>
						<li>
							<a href='<c:url value="/login"/>'>Login</a>
						</li>
					</sec:authorize>
				</ul>
				<sec:authorize access="isAuthenticated()">
				<form class="navbar-form navbar-right" role="search">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
				<ul class="nav navbar-nav navbar-right">
					<li>
						<a href='<c:url value="/j_spring_security_logout"/>'>
							Signed in as <sec:authentication property="principal.username" />
						</a>
					</li>
				</ul>
				</sec:authorize>
			</div>
		</div>
	</nav>

	<sec:authorize access="isAuthenticated()">
	<div class="container-fluid">
	<ol class="breadcrumb">
		<c:choose>
			<c:when test="${breadcrumb.size() > 1}">
				<li><a href='<c:url value="/category/0/page/1"/>'>Home</a></li>
				<c:forEach items="${breadcrumb}" var="bc" varStatus="i">
					<c:choose>
						<c:when test="${i.last}">
							<li class="active"><c:out value="${bc.name}" /></li>
						</c:when>
						<c:otherwise>
							<li><a href='<c:url value="/category/${bc.id}"/>'><c:out value="${bc.name}" /></a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<li class="active">Home</li>
			</c:otherwise>
		</c:choose>
	</ol>
	</div>
	</sec:authorize>

	<div class="container-fluid">
	<div class="row">

        <div class="col-md-3">
			<sec:authorize access="fullyAuthenticated">
             	<div id="categoryNavigationWell" class="well sidebar-nav">
					<ul id="rootCategory" class="nav nav-list">
						<jsp:include page="/WEB-INF/views/categoryMenuContainer.jsp" />
					</ul>
				</div>
			</sec:authorize>
			</div>

			<div class="col-md-9">
					<!-- Document Window -->
					<tiles:insertAttribute name="content" />
			</div>

		</div>

	</div>

	<footer class="footer">
		<div class="container-fluid">
			<p class="text-muted">&copy; 2016 Thomas W&ouml;hlke</p>
		</div>
	</footer>

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
						rootUrl += '<c:url value="/actionItem"/>';
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

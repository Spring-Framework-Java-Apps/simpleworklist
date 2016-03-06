<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Simple Worklist</title>
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/cupertino/jquery-ui.css" type="text/css">
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
	<!-- Optional theme -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">
	<link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
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
							   aria-expanded="false"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> New Content<span class="caret"></span></a>
							<ul class="dropdown-menu">
								<c:set var="pid" value="0"/>
								<c:if test="${! empty thisProject}">
									<c:set var="pid" value="${thisProject.id}"/>
								</c:if>
								<li>
									<a href='<c:url value="/project/addchild/${pid}"/>'>Add a Project</a>
								</li>
								<li>
									<a href='<c:url value="/task/addtoproject/${pid}"/>'>Add a Task</a>
								</li>
								<li role="separator" class="divider"></li>
								<li>
									<a href="<c:url value="/test/helper/project/createTree" />">Create Test Data</a>
								</li>
							</ul>
						</li>
						<li>
							<a href="<c:url value="/users" />"><span class="glyphicon glyphicon-user" aria-hidden="true"></span> Show Users
							<c:if test="${numberOfNewIncomingMessages > 0}">
								<span class="badge">${numberOfNewIncomingMessages}</span>
							</c:if>
							</a>
						</li>
						<li>
							<a href='<c:url value="/logout"/>'><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Logout</a>
						</li>
					</sec:authorize>
					<sec:authorize access="isAnonymous()">
						<li>
							<a href='<c:url value="/register"/>'><span class="glyphicon glyphicon-user" aria-hidden="true"></span> Register as New User</a>
						</li>
						<li>
							<a href='<c:url value="/login"/>'><span class="glyphicon glyphicon-log-in" aria-hidden="true"></span> Login</a>
						</li>
						<li>
							<a href='<c:url value="/resetPassword"/>'><span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span> Password forgotten?</a>
						</li>
					</sec:authorize>
				</ul>
				<sec:authorize access="isAuthenticated()">
				<form action='<c:url value="/search"/>' class="navbar-form navbar-right" role="search" method="get">
					<div class="form-group">
						<input name="searchterm" type="text" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></button>
				</form>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
				<ul class="nav navbar-nav navbar-right">
					<li>
						<a href='<c:url value="/logout"/>'>
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
				<li><a href='<c:url value="/project/0/page/1"/>'>Home</a></li>
				<c:forEach items="${breadcrumb}" var="bc" varStatus="i">
					<c:choose>
						<c:when test="${i.last}">
							<li class="active"><c:out value="${bc.name}" /></li>
						</c:when>
						<c:otherwise>
							<li><a href='<c:url value="/project/${bc.id}"/>'><c:out value="${bc.name}" /></a></li>
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
					<ul id="rootProject" class="nav nav-list">
						<li><a id="focus_inbox" href='<c:url value="/tasks/inbox"/>'><span class="glyphicon glyphicon-inbox" aria-hidden="true"></span> Inbox</a></li>
						<li><a id="focus_today" href='<c:url value="/tasks/today"/>'><span class="glyphicon glyphicon glyphicon-time" aria-hidden="true"></span> Today</a></li>
						<li><a id="focus_next" href='<c:url value="/tasks/next"/>'><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Next</a></li>
						<li><a id="focus_waiting" href='<c:url value="/tasks/waiting"/>'><span class="glyphicon glyphicon-hourglass" aria-hidden="true"></span> Waiting</a></li>
						<li><a id="focus_scheduled" href='<c:url value="/tasks/scheduled"/>'><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span> Scheduled</a></li>
						<li><a id="focus_someday" href='<c:url value="/tasks/someday"/>'><span class="glyphicon glyphicon-road" aria-hidden="true"></span> Someday</a></li>
						<li><jsp:include page="/WEB-INF/views/categoryMenuContainer.jsp" /></li>
						<li><a id="focus_completed" href='<c:url value="/tasks/completed"/>'><span class="glyphicon glyphicon-check" aria-hidden="true"></span> Completed Tasks</a></li>
						<li><a id="focus_trash" href='<c:url value="/tasks/trash"/>'><span class="glyphicon glyphicon-trash" aria-hidden="true"></span> Trash</a></li>
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
	<!--
	<script>
		(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
					(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
				m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		})(window,document,'script','//www.google-analytics.com/analytics.js','ga');

		ga('create', 'UA-17174370-6', 'auto');
		ga('send', 'pageview');

	</script>
	-->
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<!-- Latest compiled and minified JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
	<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
	 <script>
		$(window).load( function() {
			$( ".dataDetailListTitle").draggable({ cursor: "crosshair", revert: true  });
			$( ".categoryNode" ).draggable({ cursor: "crosshair", revert: true  });
			$( ".categoryNode" ).droppable({
				drop: function( event, ui ) {
					var selfId = ("" + $( this ).attr("id")).split("_")[1];
					var draggableId = "" +ui.draggable.attr("id").split("_")[1];
					var draggableType = ""+ui.draggable.attr("id").split("_")[0];
					var rootUrl = "";
					if(draggableType == "dataDetail"){
						rootUrl += '<c:url value="/task"/>';
					} else {
						rootUrl += '<c:url value="/project"/>';
					}
					//var html4move = '<a href="'+rootUrl+'/'+draggableId+'/moveto/'+selfId+'">move</a>';
					//$( this ).html(html4move);
					var html4move = rootUrl+'/'+draggableId+'/moveto/'+selfId;
					window.location.replace(html4move);
				}
			});
			$("#focus_inbox").droppable({
				drop: function( event, ui ) {
					var selfId = ("" + $( this ).attr("id")).split("_")[1];
					var draggableId = "" +ui.draggable.attr("id").split("_")[1];
					var draggableType = ""+ui.draggable.attr("id").split("_")[0];
					var rootUrl = "";
					if(draggableType == "dataDetail"){
						var html4move = '/tasks/move/'+draggableId+'/to/inbox';
						window.location.replace(html4move);
					}
				}
			});
			$("#focus_today").droppable({
				drop: function( event, ui ) {
					var selfId = ("" + $( this ).attr("id")).split("_")[1];
					var draggableId = "" +ui.draggable.attr("id").split("_")[1];
					var draggableType = ""+ui.draggable.attr("id").split("_")[0];
					var rootUrl = "";
					if(draggableType == "dataDetail"){
						var html4move = '/tasks/move/'+draggableId+'/to/today';
						window.location.replace(html4move);
					}
				}
			});
			$("#focus_next").droppable({
				drop: function( event, ui ) {
					var selfId = ("" + $( this ).attr("id")).split("_")[1];
					var draggableId = "" +ui.draggable.attr("id").split("_")[1];
					var draggableType = ""+ui.draggable.attr("id").split("_")[0];
					var rootUrl = "";
					if(draggableType == "dataDetail"){
						var html4move = '/tasks/move/'+draggableId+'/to/next';
						window.location.replace(html4move);
					}
				}
			});
			$("#focus_waiting").droppable({
				drop: function( event, ui ) {
					var selfId = ("" + $( this ).attr("id")).split("_")[1];
					var draggableId = "" +ui.draggable.attr("id").split("_")[1];
					var draggableType = ""+ui.draggable.attr("id").split("_")[0];
					var rootUrl = "";
					if(draggableType == "dataDetail"){
						var html4move = '/tasks/move/'+draggableId+'/to/waiting';
						window.location.replace(html4move);
					}
				}
			});
			$("#focus_someday").droppable({
				drop: function( event, ui ) {
					var selfId = ("" + $( this ).attr("id")).split("_")[1];
					var draggableId = "" +ui.draggable.attr("id").split("_")[1];
					var draggableType = ""+ui.draggable.attr("id").split("_")[0];
					var rootUrl = "";
					if(draggableType == "dataDetail"){
						var html4move = '/tasks/move/'+draggableId+'/to/someday';
						window.location.replace(html4move);
					}
				}
			});
			$("#focus_completed").droppable({
				drop: function( event, ui ) {
					var selfId = ("" + $( this ).attr("id")).split("_")[1];
					var draggableId = "" +ui.draggable.attr("id").split("_")[1];
					var draggableType = ""+ui.draggable.attr("id").split("_")[0];
					var rootUrl = "";
					if(draggableType == "dataDetail"){
						var html4move = '/tasks/move/'+draggableId+'/to/completed';
						window.location.replace(html4move);
					}
				}
			});
			$("#focus_trash").droppable({
				drop: function( event, ui ) {
					var selfId = ("" + $( this ).attr("id")).split("_")[1];
					var draggableId = "" +ui.draggable.attr("id").split("_")[1];
					var draggableType = ""+ui.draggable.attr("id").split("_")[0];
					var rootUrl = "";
					if(draggableType == "dataDetail"){
						var html4move = '/tasks/move/'+draggableId+'/to/trash';
						window.location.replace(html4move);
					}
				}
			});
			$("#taskDueDate ").datepicker({
				dateFormat: 'mm/dd/yy',
				constrainInput: false
			});
		});
	</script>
</body>
</html>

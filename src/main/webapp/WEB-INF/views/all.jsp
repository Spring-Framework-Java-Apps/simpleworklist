<?xml version="1.0" encoding="UTF-8"?>
<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="de">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>BeachBox</title>
    <!-- Bootstrap -->
    <link href="webjars/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Optional theme -->
    <link rel="stylesheet" href="webjars/bootstrap/3.1.1/css/bootstrap-theme.min.css" />
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script type="text/javascript" src="webjars/jquery/2.1.0-2/jquery.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script type="text/javascript" src="webjars/bootstrap/3.1.1/js/bootstrap.min.js"></script>
</head>
<body>
    <div class="container">
    <h1>BeachBox</h1>

    <c:url var="searchUrl" value="/" />

    <div>
    <form:form class="navbar-form navbar-left" role="search" action="${searchUrl}" commandName="searchItem">
        <div class="form-group">
            <form:input class="form-control" path="searchString"/>
            <form:errors path="searchString" class="alert alert-error"/>
        </div>
        <button type="submit" class="btn btn-default">Suche</button>
    </form:form>
    </div>

    <c:url var="firstUrl" value="/?page.page=1&size=${page.size}&sort=${sort}&page.sort.dir=asc" />
    <c:url var="lastUrl" value="/?page.page=${page.totalPages}&page.size=${page.size}&page.sort=${sort}&page.sort.dir=asc" />
    <c:url var="prevUrl" value="/?page.page=${searchItem.currentIndex - 1}&page.size=${page.size}&page.sort=${searchItem.sort}&page.sort.dir=asc" />
    <c:url var="nextUrl" value="/?page.page=${searchItem.currentIndex + 1}&page.size=${page.size}&page.sort=${searchItem.sort}&page.sort.dir=asc" />

    <div>
        <ul class="pagination">
            <c:choose>
                <c:when test="${searchItem.currentIndex == 1}">
                    <li class="disabled"><a href="#">&lt;&lt;</a></li>
                    <li class="disabled"><a href="#">&lt;</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${firstUrl}">&lt;&lt;</a></li>
                    <li><a href="${prevUrl}">&lt;</a></li>
                </c:otherwise>
            </c:choose>
            <c:forEach var="i" begin="${searchItem.beginIndex}" end="${searchItem.endIndex}">
                <c:url var="pageUrl" value="/?page.page=${i}&page.size=${page.size}&page.sort=${searchItem.sort}&page.sort.dir=asc" />
                <c:choose>
                    <c:when test="${i == searchItem.currentIndex}">
                        <li class="active"><a href="${pageUrl}"><c:out value="${i}" /></a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${pageUrl}"><c:out value="${i}" /></a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:choose>
                <c:when test="${searchItem.currentIndex == page.totalPages}">
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

    <c:url var="neuUrl" value="/new" />

    <div>
    <table class="table table-striped table-hover">
        <tr>
            <c:url var="startUrl" value="/?page.page=1&page.size=${page.size}"/>
            <th><a href="${startUrl}&page.sort=id&page.sort.dir=asc">Id</a></th>
            <th><a href="${startUrl}&page.sort=rubrik&page.sort.dir=asc">Rubrik</th>
            <th><a href="${startUrl}&page.sort=tontraeger&page.sort.dir=asc">Tontraeger</th>
            <th><a href="${startUrl}&page.sort=interpret&page.sort.dir=asc">Interpret</th>
            <th><a href="${startUrl}&page.sort=song&page.sort.dir=asc">Song</th>
            <th><a href="${startUrl}&page.sort=name&page.sort.dir=asc">Name</th>
            <th><a href="${startUrl}&page.sort=seite&page.sort.dir=asc">Seite</th>
            <th><a href="${startUrl}&page.sort=jahr&page.sort.dir=asc">Jahr</th>
            <th><a href="${startUrl}&page.sort=genre&page.sort.dir=asc">Genre</th>
            <th><a href="${startUrl}&page.sort=label&page.sort.dir=asc">Label</th>
            <th><a href="${startUrl}&page.sort=bemerkung&page.sort.dir=asc">Bemerkung</th>
            <c:if test="${searchItem.bearbeiten}">
            <th>&auml;ndern</th>
            <th>l&ouml;schen</th>
            </c:if>
        </tr>
    <c:forEach items="${page.content}" var="v">
        <tr>
            <c:url var="editUrl" value="/edit"/>
            <c:url var="deleteUrl" value="/delete"/>
            <td><a href="${editUrl}/${v.id}" title="id">${v.id}</a></td>
            <td>${v.rubrik}</td>
            <td>${v.tontraeger}</td>
            <td>${v.interpret}</td>
            <td>${v.song}</td>
            <td>${v.name}</td>
            <td>${v.seite}</td>
            <td>${v.jahr}</td>
            <td>${v.genre}</td>
            <td>${v.label}</td>
            <td>${v.bemerkung}</td>
            <c:if test="${searchItem.bearbeiten}">
            <td><a href="${editUrl}/${v.id}" title="id"><span class="glyphicon glyphicon-edit" /></a></td>
            <td><a href="${deleteUrl}/${v.id}" title="id"><span class="glyphicon glyphicon-trash" /></a></td>
            </c:if>
        </tr>
    </c:forEach>
    </table>
        <c:if test="${searchItem.bearbeiten}">
            <span class="glyphicon glyphicon-plus-sign" ></span> <a href="${neuUrl}">Neuer Eintrag</a>
        </c:if>
        <c:if test="${not searchItem.bearbeiten}">
            <c:url var="unlockEditUrl" value="/unlockEdit" />
            <span class="glyphicon glyphicon-circle-arrow-right" ></span> <a href="${unlockEditUrl}">Bearbeiten</a>
        </c:if>
    </div>
</body>
</html>
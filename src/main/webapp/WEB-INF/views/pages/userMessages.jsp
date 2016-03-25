<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- Document Window -->
<h1><spring:message code="pages.userMessages.h1" text="Your Dialog with" /> ${otherUser.userFullname}</h1>
<div class="row">
<div class="well">
<form:form id="formId" commandName="newUserMessage" method="post">
    <div class="form-group">
        <form:label path="messageText"><spring:message code="pages.userMessages.newMessage" text="New Message:" /></form:label>
        <form:textarea path="messageText" rows="3" cols="50" class="form-control"/>
        <form:errors path="messageText" delimiter=", " element="div" class="alert alert-danger"/>
    </div>
    <form:button id="newUserMessageButton" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> <spring:message code="pages.userMessages.newUserMessageButton" text="Send Message" /></form:button>
</form:form>
</div>
<c:forEach var="m" items="${userMessageList}">
<c:if test="${m.sender.id == otherUser.id}">
<div class="alert alert-danger" role="alert" style="width: 66%; float:right">
</c:if>
<c:if test="${m.sender.id != otherUser.id}">
<div class="alert alert-info" role="alert" style="width: 66%; float:left">
</c:if>
    <c:out value="${m.messageText}" />
    <span style="float:right">
    <small>
    <fmt:formatDate type="BOTH" value="${m.createdTimestamp}" />
    <c:if test="${m.readByReceiver}"><span class="glyphicon glyphicon-ok" aria-hidden="true"></c:if>
    </small>
    </span>
</div>
</c:forEach>
</div>
<div class="row">
    <c:if test="${userMessageList.size() < 21}">
    <a href='<c:url value="/user/${otherUser.id}/messages/all"/>'><spring:message code="pages.userMessages.showAllText1" text="Zeige alle Nachrichten" /></a> <spring:message code="pages.userMessages.showAllText2" text="(hier werden nur die letzten 20 Angezeigt)" />
    </c:if>
</div>

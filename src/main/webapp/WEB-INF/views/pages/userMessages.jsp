<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- Document Window -->
<h1>Your Dialog with ${otherUser.userFullname}</h1>
<div class="well">
<form:form id="formId" commandName="newUserMessage" method="post">
    <div class="form-group">
        <form:label path="messageText">New Message:</form:label>
        <form:textarea path="messageText" rows="3" cols="50" class="form-control"/>
        <form:errors path="messageText" delimiter=", " element="div" class="alert alert-danger"/>
    </div>
    <form:button id="newUserMessageButton" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> Send Message</form:button>
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
</div>
</c:forEach>


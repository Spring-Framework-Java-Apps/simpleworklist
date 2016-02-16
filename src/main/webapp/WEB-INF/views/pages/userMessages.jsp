<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!-- Document Window -->
<h1>Your Dialog with ${otherUser.userFullname}</h1>
<div class="well">
<h2>New Message:</h2>
<form:form id="formId" commandName="newUserMessage" method="post">
    <div class="form-group">
        <form:label path="messageText">Text</form:label>
        <form:textarea path="messageText" rows="10" cols="50" class="form-control"/>
        <form:errors path="messageText" delimiter=", " element="div" class="alert alert-danger"/>
    </div>
    <form:button id="newUserMessageButton" type="submit" class="btn btn-default"><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> Send Message</form:button>
</form:form>
</div>

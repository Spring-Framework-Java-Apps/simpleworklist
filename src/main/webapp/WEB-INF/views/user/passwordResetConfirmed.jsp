<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<div>
    <form:form id="formId" commandName="userAccount" method="post" class="ym-form ym-columnar linearize-form">
        <div class="ym-fbox-text">
            <form:label path="userPassword" class="ym-label ym-full">Password <sup class="ym-required">*</sup></form:label>
            <form:password path="userPassword" class="ym-required"/><br/>
            <form:errors path="userPassword" class="ym-message"/>
        </div>
        <div class="ym-fbox-text">
            <form:label path="userPasswordConfirmation" class="ym-label ym-full">Password again <sup class="ym-required">*</sup></form:label>
            <form:password path="userPasswordConfirmation" class="ym-required"/><br/>
            <form:errors path="userPasswordConfirmation" class="ym-message"/>
        </div>
        <form:hidden path="userFullname"/>
        <form:hidden path="userEmail"/>
        <div class="ym-fbox-button ym-columnar linearize-form">
            <input id="createNewDataLeaf" type="submit" name="data" value="Confirm Registration"/>
        </div>
    </form:form>
</div>
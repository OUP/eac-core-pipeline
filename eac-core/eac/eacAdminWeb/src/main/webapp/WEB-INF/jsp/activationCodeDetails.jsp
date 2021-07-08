<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div class="field">
	<div class="fieldLabel">
		<label for="exampleUrl">Activation Code</label>
	</div>
	<div class="fieldValue">
		${activationCode.code}
	</div>
</div>
<div class="field">
    <div class="fieldLabel">
        <label for="exampleUrl">Batch</label>
    </div>
    <div class="fieldValue">
        ${activationCode.activationCodeBatch.batchId}
    </div>
</div>  
<div class="field">
    <div class="fieldLabel">
        <label for="exampleUrl">Product</label>
    </div>
    <div class="fieldValue">
        ${activationCode.activationCodeBatch.activationCodeRegistrationDefinition.product.productName}
    </div>
</div>
<div class="field">
    <div class="fieldLabel">
        <label for="exampleUrl">Product Group</label>
    </div>
    <div class="fieldValue">
        ${activationCode.activationCodeBatch.activationCodeRegistrationDefinition.eacGroup.groupName}
    </div>
</div>   
<div class="field">
    <div class="fieldLabel">
        <label for="exampleUrl">Validity Period</label>
    </div>
    <div class="fieldValue">
        ${activationCode.activationCodeBatch.validityPeriod}
    </div>
</div>  
<div class="field">
    <div class="fieldLabel">
        <label for="exampleUrl">Allowed Usage</label>
    </div>
    <div class="fieldValue">
        ${activationCode.allowedUsage}
    </div>
</div>  
<div class="field">
    <div class="fieldLabel">
        <label for="exampleUrl">Actual Usage</label>
    </div>
    <div class="fieldValue">
        ${activationCode.actualUsage}
    </div>
</div>
<c:if test="${not empty activationCode.registrations}">
<c:forEach items="${activationCode.registrations}" var="reg">
<c:if test="${not empty reg.customer.username}">
<table class="summary">
    <thead>
        <tr>
	        <th style="width: 40%;">User</th>
	        <th style="width: 40%;">username</t>
	        <th>Activation Date</th>
	    </tr>
    </thead>
    <tbody>
        <tr>
            <td>${reg.customer.firstName} ${reg.customer.familyName}</td>
            <td>${reg.customer.username}</td>
            <td><joda:format value="${reg.createdDate}" pattern="dd/MM/YYYY" dateTimeZone="UTC" /></td>
        </tr>
    </tbody>
</table>
</c:if>
</c:forEach>
</c:if>


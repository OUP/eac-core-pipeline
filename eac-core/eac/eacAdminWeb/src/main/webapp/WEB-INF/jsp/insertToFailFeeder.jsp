<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<c:url value="/mvc/support/insertToFailFeeder.htm" var="formUrl" />
<div id="insertToFailFeederTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.cloudsearchFeeder"/></h1>
	</div>
	
	<c:if test="${not empty statusMessageKey}">
		<div class="success">
		<c:out value="${statusMessageKey}"/>
			<%-- <spring:message code="${statusMessageKey}"/>
			<c:if test="${adminEmail ne null}">
				<c:out value="${adminEmail}"/>
			</c:if> --%>
		</div>
	</c:if>
	
	<spring:hasBindErrors name="insertIntoCloudFailFeederBean">
		<div class="error">
			 <spring:bind path="insertIntoCloudFailFeederBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind> 
		</div>
	</spring:hasBindErrors>
	<form:form modelAttribute="insertIntoCloudFailFeederBean" method="POST">
		<fieldset>
			<legend><spring:message code="label.cloudsearchFeeder"/></legend>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.oupId" /></label>
				</div>
				<div class="fieldValue">
					<form:input id="oupId" path="oupId"/>
				</div>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.erightsId" /></label>
				</div>
				<div class="fieldValue">
					<form:input id="erightsId" path="erightsId"/>
				</div>
			</div>
			<div class="field">
			<div class="fieldLabel">
				<label for="entity"><spring:message code="label.entity" /></label>
			</div>
			<div class="fieldValue">
				<form:select id="entity" path="entity" cssStyle="width: auto">
					<form:option value="USER">User</form:option>
					<form:option value="GROUP">Group</form:option>
					<form:option value="PRODUCT">Product</form:option>
					<form:option value="PRODUCT_GROUP">ProductGroup</form:option>
					<form:option value="ACTIVITY">Activity</form:option>
					<form:option value="ACTIVATIONCODE">ActivationCode</form:option>
					<form:option value="ACTIVATIONCODE_ASSIGNMENT">ActivationCodeAssignment</form:option>
				</form:select>
			</div>
		</div>
			<div class="field">
			<div class="fieldLabel">
				<label for="searchDomainName"><spring:message code="label.searchDomainName" /></label>
			</div>
			<div class="fieldValue">
				<form:select id="searchDomainName" path="searchDomainName" cssStyle="width: auto">
					<form:option value="USER">User</form:option>
					<form:option value="GROUP">Group</form:option>
					<form:option value="LICENSE">License</form:option>
					<form:option value="PRODUCT">Product</form:option>
					<form:option value="PRODUCT_GROUP">ProductGroup</form:option>
					<form:option value="USER_GROUP_MEMBERSHIP">UserMembership</form:option>
					<form:option value="ACTIVITY">Activity</form:option>
					<form:option value="ACTIVATIONCODE">ActivationCode</form:option>
					<form:option value="ACTIVATIONCODE_ASSIGNMENT">ActivationCodeAssignment</form:option>
				</form:select>
			</div>
		</div>
		</fieldset>
		<div id="buttons">
	    	<p>   
	    		<button type="button" id="reset"><spring:message code="button.reset"/></button>
	    		<button type="submit" id="save"><spring:message code="button.send" /></button>
	        </p>
	    </div>
	</form:form>
</div>
<script type="text/javascript">
	$('#reset').click(function() {
		$('#oupId').val('') ;
		$('#erightsId').val('') ;
	});
</script>
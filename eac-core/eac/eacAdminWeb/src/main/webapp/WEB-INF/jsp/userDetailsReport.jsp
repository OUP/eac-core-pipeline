<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<c:url value="/mvc/reports/userDetails.htm" var="formUrl" />
<div id="userDetailsReportTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.userDetails"/></h1>
	</div>
	
	<c:if test="${not empty statusMessageKey}">
		<div class="success">
			<spring:message code="${statusMessageKey}"/>
			<%-- <c:if test="${adminEmail ne null}">
				<c:out value="${adminEmail}"/>
			</c:if> --%>
		</div>
	</c:if>
	
	<spring:hasBindErrors name="userDetailsBean">
		<div class="error">
			<spring:bind path="userDetailsBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>
	<form:form modelAttribute="userDetailsBean" method="POST">
		<fieldset>
			<legend><spring:message code="label.userDetailsCriteria"/></legend>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.userName" /></label>
				</div>
				<div class="fieldValue">
					<form:input id="username" path="searchUserName"/>
				</div>
			</div>
		</fieldset>
		<div id="buttons">
	    	<p>   
	    		<button type="button" id="reset"><spring:message code="button.reset"/></button>
	    		<button type="submit" id="save"><spring:message code="button.report" /></button>
	        </p>
	    </div>
	</form:form>
</div>
<script type="text/javascript">
	$('#reset').click(function() {
		$('#username').val('') ;
	});
</script>
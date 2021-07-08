<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="activationCodeBatchSearchTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title" /></h1>
	</div>
	<c:set var="statusMessageKey" value="${param['statusMessageKey']}"/>
	<c:if test="${statusMessageKey ne null}">
		 <div class="success">
			<spring:message code="${statusMessageKey}"/>
		</div>
	</c:if>
	<c:set var="errorMessageKey" value="${param['errorMessageKey']}"/>
	<c:if test="${errorMessageKey ne null}">
		 <div class="error">
			<spring:message code="${errorMessageKey}"/>
		</div>
	</c:if>
	
	<spring:hasBindErrors name="activationCodeBatchSearchCriteriaBean">
		<div class="error">
			<spring:bind path="activationCodeBatchSearchCriteriaBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>
	
	<div id="help">
		This facility allows you to search for existing activation code batches. All fields are optional. Please bear in mind that you will only be able to search for batches for which you are an administrator. 
		<br/>
		 <br/>
		The following field support partial matching:
		<ul>
			<li><spring:message code="label.batchname" /></li>
			<%-- <li><spring:message code="label.activationcode" /></li> --%>
		</ul>
	</div>
	 
	<div style="width: 50em; float:left; margin-right: 1em">
		<form:form modelAttribute="activationCodeBatchSearchCriteriaBean" action="${flowExecutionUrl}" id="searchForm">
	    	<tiles:insertAttribute name="activationCodeBatchSearchFormTile"/>
		</form:form>
	</div>
	
	<tiles:insertAttribute name="searchControlTile"/>
	<tiles:insertAttribute name="searchResultsTile"/>
</div>
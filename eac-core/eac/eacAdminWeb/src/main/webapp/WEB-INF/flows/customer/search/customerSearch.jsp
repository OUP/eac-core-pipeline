<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="customerSearchTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title" /></h1>
	</div>
	
	<c:if test="${statusMessageKey ne null}">
		 <div class="success">
			<spring:message code="${statusMessageKey}"/>
		</div>
	</c:if>
	<c:if test="${errorMessageKey ne null}">
		 <div class="error">
			<spring:message code="${errorMessageKey}"/>
		</div>
	</c:if>
         
	<spring:hasBindErrors name="customerSearchCriteriaBean">
		<div class="error">
			<spring:bind path="customerSearchCriteriaBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>
	
	<div id="help">
		This facility allows you to search for existing customers. All fields are optional. 
		<br/>
		<br/>
		The following fields support partial matching:
		<ul>
			<li><spring:message code="label.username" /></li>
			<li><spring:message code="label.email" /></li>
			<li><spring:message code="label.firstName" /></li>
			<li><spring:message code="label.familyName" /></li>
			<li><spring:message code="label.externalId" /></li>
		</ul>
	</div>
	 
	<div style="width: 50em; float:left; margin-right: 1em">
		<form:form modelAttribute="customerSearchCriteriaBean" action="${flowExecutionUrl}" id="searchForm">
	    	<tiles:insertAttribute name="customerSearchFormTile"/>
		</form:form>
	</div>
	
	<tiles:insertAttribute name="searchControlTile"/>
	<tiles:insertAttribute name="searchResultsTile"/>
</div>


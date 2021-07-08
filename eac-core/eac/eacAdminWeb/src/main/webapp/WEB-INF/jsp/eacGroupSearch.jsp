<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="eacGroupSearchTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.groupSearch"/></h1>
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
         
	<spring:hasBindErrors name="eacGroupSearchBean">
		<div class="error">
			<spring:bind path="eacGroupSearchBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>
	
	<div id="help">
		This facility allows you to search for product groups. 
		<br/>
		<br/>
		The following fields support partial matching:
		<ul>
			<li><spring:message code="label.groupName" /></li>
			<li><spring:message code="label.productName" /></li>
			<li><spring:message code="label.productId" /></li>
			<li><spring:message code="label.externalId" /></li>
		</ul>
	</div>
	
	<div style="float:left; margin-right: 1em">
		<c:url value="/mvc/eacGroups/search" var="formUrl"/>
		<form:form modelAttribute="eacGroupSearchBean" action="" id="searchForm">
	    	<tiles:insertAttribute name="eacGroupSearchFormTile"/>
		</form:form>
	</div>
	
	<tiles:insertAttribute name="searchControlTile"/>
	<tiles:insertAttribute name="eacGroupSearchResultsTile"/>
	
</div>
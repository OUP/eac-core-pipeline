<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="activationCodeSearchTabsTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.activationcodesearch"/></h1>
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

	<spring:hasBindErrors name="activationCodeSearchBean">
		<div class="error">
			<spring:bind path="activationCodeSearchBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>
	
	<tiles:insertAttribute name="activationCodeSearchTile"/>
	
	<tiles:insertAttribute name="activationCodeSearchResultsTile"/>

</div>
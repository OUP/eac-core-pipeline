<%@include file="/WEB-INF/jsp/taglibs.jsp"%>

<div id="manageQuestionTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.manage.org.units" text="?title.manage.org.units?"/></h1>
	</div>

	<c:set var="statusMessageKey" value="${param['statusMessageKey']}"/>
	
	<c:if test="${not empty statusMessageKey}">
		 <div class="success">
			<spring:message code="${statusMessageKey}" text="The operation was successful"/>
		</div>
	</c:if>
	
	 <spring:bind path="orgUnitsBean">
	 	<c:if test="${status.errors.errorCount > 0}">
        <div class="error">
        <ul>
        <c:forEach items="${status.errorMessages}" var="error">
        	<li><c:out value="${error}"/></li>
        </c:forEach>
        </ul>
        </div>
        </c:if>
    </spring:bind>
    
	<div id="manageOrgUnitsFormTile">
		<c:if test="${not empty orgUnitsBean.orgUnits}">
			<tiles:insertAttribute name="manageOrgUnitsFormTile"/>
		</c:if>
	</div>

</div>
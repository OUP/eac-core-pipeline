<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@ attribute name="name" required="true"%>

<c:if test="${!empty statusMessageKey}">
	<div class="success">
		<spring:message code="${statusMessageKey}"/>
	</div>
</c:if>

<spring:hasBindErrors name="${name}">
	<div class="error">
		<spring:bind path="${name}.*">
			<c:forEach var="error" items="${errors.allErrors}">
				<span><eac-common:message code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}"/></span><br>
			</c:forEach>
		</spring:bind>
	</div>
</spring:hasBindErrors>
	

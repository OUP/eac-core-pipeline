<%@include file="/WEB-INF/jsp/include.jsp" %>
<%@attribute name="name" required="true"%>


<spring:hasBindErrors name="${name}">
    <div id="errors" class="message message--error">
        <h4><spring:message code="label.errors" text="?label.errors?" /></h4>
	            <c:forEach var="error" items="${errors.allErrors}">
	            	<c:choose>
	            		<c:when test="${error.code eq 'error.username.taken'}">
	            			<eac:loginurl var="loginUrl" propertyName="eac.login.url" />
	            			<eac-common:objectError var="error" error="${error}" argument="${loginUrl}" />
	            		</c:when>
	            	</c:choose>
					<p><eac-common:message code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}"/></p>					
	            </c:forEach>
   </div>
</spring:hasBindErrors>
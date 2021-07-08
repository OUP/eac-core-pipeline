<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="statusTile">
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

	<spring:hasBindErrors name="customerBean">
		<div class="error">
			<spring:bind path="customerBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>
</div>
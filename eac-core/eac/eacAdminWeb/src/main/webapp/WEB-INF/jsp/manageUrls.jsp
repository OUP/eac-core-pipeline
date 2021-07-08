<%@include file="/WEB-INF/jsp/taglibs.jsp"%>

<div id="heading" class="ui-corner-top">
	<h1>
		<spring:message code="title.whiteListUrls" />
	</h1>
</div>

<c:set var="statusMessageKey" value="${param['statusMessageKey']}" />

<c:if test="${not empty statusMessageKey}">
	<div class="success">
		<spring:message code="${statusMessageKey}"
			text="The operation was successful" />
	</div>
</c:if>

<spring:bind path="whiteListUrlBean">
	<c:if test="${status.errors.errorCount > 0}">
		<div class="error">
			<ul style = "margin:0 1.5em 0 1.5em;">
				<c:forEach items="${status.errorMessages}" var="error">
					<li><c:out value="${error}" /></li>
				</c:forEach>
			</ul>
		</div>
	</c:if>
</spring:bind>

<div id="manageUrlFormTile">
	<tiles:insertAttribute name="manageUrlFormTile" />
</div>

</div>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:choose>
	<c:when test="${not empty results}">
		<c:forEach items="${results}" var="eacGroup" varStatus="status">
			<a href="#">
			<div id="${eacGroup.id}" title="${eacGroup.groupName}" class="product result">
				<div class="assetName">
					<span class="shorten"><c:out value="${eacGroup.groupName}"/></span>
					${eacGroup.id}
				</div>
			</div>
			</a>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<div style="text-align: center; padding-top: 50px">
			<spring:message code="label.noResults" />
		</div>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
	 $(function() {
		$('.shorten').shorten();
	}); 
</script>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:url value="/images/add.png" var="addIconUrl"/>

<ul id="breadcrumb">
	<c:forEach var="entry" items="${breadCrumb.entries}" varStatus="status">
		<c:choose>
			<c:when test="${status.last}">
				<li class="last"><spring:message code="${entry.key}" /></li>
			</c:when>
			<c:otherwise>
				<c:url value="${entry.url}" var="url"/>
				<li><a href="${url}"><spring:message code="${entry.key}" /></a></li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	<li style="position: absolute; top: 0px; right: 0px; background-image: none">
		<c:set value="${breadCrumb.nextLevelDown}" var="nextLevel"/>
		<c:if test="${not empty nextLevel}">
			<c:url value="${nextLevel.url}" var="nextLevelHref"/>
			<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a href="${nextLevelHref}"><spring:message code="${nextLevel.key}" /></a>
		</c:if>
	</li>
</ul>	

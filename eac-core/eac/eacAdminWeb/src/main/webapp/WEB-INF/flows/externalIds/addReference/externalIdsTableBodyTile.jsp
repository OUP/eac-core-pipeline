<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:choose>
	<c:when test="${!empty customerBean.externalIds}">
		<c:url value="/images/pencil.png" var="pencilUrl"/>
		<c:url value="/images/delete.png" var="deleteUrl"/>
		<spring:message code="label.edit" var="edit"/>	
		<spring:message code="label.remove" var="remove"/>	
		<c:forEach var="externalId" items="${customerBean.externalIds}" varStatus="status">
			<c:set var="externalId" value="${externalId}" scope="request"/>
			<tr class="${status.index}">
				<td class="externalSystem"><c:out value="${externalId.externalSystemIdType.externalSystem.name}"/></td>
				<td class="externalSystemIdType"><c:out value="${externalId.externalSystemIdType.name}"/></td>
				<td class="externalId"><c:out value="${externalId.externalId}"/></td>
				<td>
					<a class="editExternalIdLink actionLink" href="#"><img class="actionImg" src="${pencilUrl}" title="${edit}" alt="${edit}"/></a>&#160;<a class="removeExternalIdLink actionLink" href="#"><img class="actionImg" src="${deleteUrl}" title="${remove}" alt="${remove}"/></a>
				</td>
			</tr>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<tr>
			<td colspan="4" style="text-align:center"><spring:message code="label.externalIds.noExternalIdsCustomer" /></td>
		</tr>
	</c:otherwise>
</c:choose>
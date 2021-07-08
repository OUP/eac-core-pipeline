<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="searchResultsTile">
	<c:if test="${not empty pageInfo}">
	 <div id="totalSearchCount">
		<table class="summary">
			<tr>
			<c:if test="${pageInfo.totalItems== 0 or pageInfo.totalItems== 1 }">
				<label for="totalRecord"><spring:message code="label.totalUserFoundOne" /><c:out value="${pageInfo.totalItems}"></c:out> <spring:message code="label.totalUserFoundTwo" /> </label>
			</c:if>
			<c:if test="${pageInfo.totalItems!= 0 and pageInfo.totalItems> 1}">
				<label for="totalRecord"><spring:message code="label.totalUserFoundOne" /><c:out value="${pageInfo.totalItems}"></c:out> <spring:message code="label.totalUserFoundTwoMul" /></label>
			</c:if>
			</tr>
		</table>
	</div>
		<table class="summary">
			<thead>
				<tr>
					<th style="width: 30%"><spring:message code="label.groupId" /></th>
					<th><spring:message code="label.groupName" /></th>
					<th><spring:message code="label.actions" /></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty eacGroups}">
						<c:url value="/images/pencil.png" var="pencilUrl"/>
						<c:url value="/images/delete.png" var="deleteUrl"/>
						<spring:message code="label.edit" var="edit"/>					
						<spring:message code="button.delete" var="delete"/>
						<c:forEach var="eacGroup" items="${eacGroups}">
							<tr>
								<td><c:out value="${eacGroup.id}"/></td>
								<td><c:out value="${eacGroup.groupName}"/></td>
															
								<c:set var="canDelete" value="${false}" />
								<c:set var="clazz" value="productActions" />
									<security:authorize ifAllGranted="ROLE_DELETE_PRODUCT_GROUP">
									<eac-common:isEacGroupUsed eacGroupId="${eacGroup.id}" var="isEacGroupUsed"/>
										<c:if test="${not isEacGroupUsed}">
											<c:set var="canDelete" value="${true}" />
											<c:set var="clazz" value="${clazz} deleteGroup" />
										</c:if>
									</security:authorize>
								<td class="${clazz}">
									 <c:if test="${canDelete}"> 
										<a class="actionLink deleteProductLink" eacLabel="${eacGroup.groupName}/${eacGroup.id}" href="<c:url value="/mvc/eacGroups/delete.htm?id=${eacGroup.id}"/>"><img class="actionImg" src="${deleteUrl}" title="${delete}" alt="${delete}"/></a>
									 </c:if> 
							 		<security:authorize ifAllGranted="ROLE_UPDATE_PRODUCT_GROUP">	
										<a class="actionLink" href="<c:url value="/mvc/eacGroups/edit.htm?id=${eacGroup.id}"/>"><img class="actionImg" src="${pencilUrl}" title="${edit}" alt="${edit}"/></a> 
									</security:authorize>	
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="8" style="text-align:center"><spring:message code="info.noGroupsFound"/></td>
						</tr>					
					</c:otherwise>
				</c:choose>
				<tiles:insertAttribute name="searchNavigationTile"/>
			</tbody>
		</table>	 
	</c:if>
</div>
	<spring:message code="confirm.title.delete.product" text="" var="message" />
	<script type="text/javascript">
	$(document).ready(function() {
		$('#searchResultsTile').undelegate('a.deleteProductLink', 'click');<%--was getting 2 button clicks--%>
		$('#searchResultsTile').delegate('a.deleteProductLink', 'click', eacConfirm({title:'${message}', width: 700}));
	});
	</script>
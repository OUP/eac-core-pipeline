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
					<th style="width: 30%"><spring:message code="label.productName" /></th>
					<th><spring:message code="label.productId" /></th>
					<th><spring:message code="label.division" /></th>		
					<th><spring:message code="label.product.registerableType" /></th>
					<th><spring:message code="label.registration.activation" /></th>
					<th><spring:message code="label.actions" /></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty productBeans}">
						<c:url value="/images/pencil.png" var="pencilUrl"/>
						<c:url value="/images/delete.png" var="deleteUrl"/>
						<spring:message code="label.edit" var="edit"/>					
						<spring:message code="button.delete" var="delete"/>
						<c:forEach var="productBean" items="${productBeans}">
							<tr>
								<td><c:out value="${productBean.productName}"/></td>
								<td><c:out value="${productBean.productId}"/></td>
								<td><c:out value="${productBean.divisionType}"/></td>
								<td><spring:message code="label.product.registerableType.${productBean.registerableType}" text=""/></td>
								<td><spring:message code="label.registration.activation.${productBean.activationStrategy}" text="" /></td>
								
								<c:set var="canDelete" value="${false}" />
								<c:set var="clazz" value="productActions" />
								<security:authorize ifAllGranted="ROLE_DELETE_PRODUCT">
									<c:set var="canDelete" value="${true}" />
									<c:set var="clazz" value="${clazz} deleteProduct" />
									
									</security:authorize> 
								<td class="${clazz}">
									<c:if test="${canDelete}">
										<a class="actionLink deleteProductLink" eacLabel="${productBean.productName}/${productBean.productId}" href="<c:url value="/mvc/product/delete.htm?id=${productBean.productId}"/>"><img class="actionImg" src="${deleteUrl}" title="${delete}" alt="${delete}"/></a>
									</c:if>
									<security:authorize ifAllGranted="ROLE_UPDATE_PRODUCT">
										<a class="actionLink" href="<c:url value="/mvc/product/edit.htm?id=${productBean.productId}"/>"><img class="actionImg" src="${pencilUrl}" title="${edit}" alt="${edit}"/></a>
									</security:authorize>
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="8" style="text-align:center"><spring:message code="info.noProductsFound"/></td>
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
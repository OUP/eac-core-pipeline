<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div id="searchResultsTile">
	<div id="totalSearchCount">
		<table class="summary">
			<tr>
			<c:if test="${pageInfo.totalItems== 0 or pageInfo.totalItems== 1 }">
				<label for="totalRecord"><spring:message code="label.totalUserFoundOne" /><c:out value="${pageInfo.totalItems}"></c:out> <spring:message code="label.totalUserFoundTwo" /></label>
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
				<th><spring:message code="label.username" /></th>
				<th><spring:message code="label.email" /></th>
				<th><spring:message code="label.firstName" /></th>							
				<th><spring:message code="label.familyName" /></th>
				<th><spring:message code="label.registrations" /></th>
				<th><spring:message code="label.createdDate" /></th>
				<th><spring:message code="label.lastLoginDateTime" /></th>				
				<th width="10%"><spring:message code="label.actions" /></th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty customers}">
				<tr>
					<td colspan="8" style="text-align:center">No customers found</td>
				</tr>
			</c:if>
			<c:if test="${!empty customers}">
				<c:url value="/images/pencil.png" var="pencilUrl"/>
				<c:url value="/images/delete.png" var="deleteUrl"/>
				<spring:message code="label.edit" var="edit"/>
				<spring:message code="button.delete" var="delete"/>
				<c:forEach var="customer" items="${customers}">
					<c:set var="customer" value="${customer}" scope="request"/>
					<tr>
						<td><c:out value="${customer.username}"/></td>
						<td><c:out value="${customer.emailAddress}"/></td>
						<td><c:out value="${customer.firstName}"/></td>
						<td><c:out value="${customer.familyName}"/></td>
						<td><c:out value="${customer.licenseCount}"/></td>
						<td><joda:format value="${customer.createdDate}" style="SM" dateTimeZone="UTC" /></td>
						<td><joda:format value="${customer.lastLoginDateTime}" style="SM" dateTimeZone="UTC"/></td>	
						<td class="customerActions">
							<eac-common:canAdminDeleteUser var="canDeleteUser" userId="${customer.id}" />
							<c:if test="${canDeleteUser}">
								<a class="actionLink deleteCustomerLink" eacLabel="${customer.username}" href="<c:url value="/mvc/customer/delete.htm" />?id=${customer.id}"><img class="actionImg" src="${deleteUrl}" title="${delete}" alt="${delete}"/></a>
							</c:if>
							<security:authorize ifAllGranted="ROLE_UPDATE_CUSTOMER">
								<a class="actionLink" href="${flowExecutionUrl}&amp;_eventId=edit&amp;id=${customer.id}"><img class="actionImg" src="${pencilUrl}" title="${edit}" alt="${edit}"/></a>
							</security:authorize>
						</td>
					</tr>
				</c:forEach>
			</c:if>
			<tiles:insertAttribute name="searchNavigationTile"/>
		</tbody>
	</table>
	<div style="float:right; min-height: 100px\9">
		Last Search: <%= new java.util.Date().toString()%>
	</div>
</div>
<spring:message code="confirm.title.delete.customer" text="" var="message" />
<script type="text/javascript">
	$(document).ready(function() {
		$('#searchResultsTile').undelegate('a.deleteCustomerLink', 'click');<%--was getting 2 button clicks--%>
		var $confirmAlert = $('<div class="confirmDialog"><div class="confirmDialogLabel"/><div class="confirmDialogWarning"><spring:message code="delete.confirm.customer" /></div></div>') ;
		$('#searchResultsTile').delegate  ('a.deleteCustomerLink', 'click', eacConfirm({title:'${message}',contents:$confirmAlert}));
	});
	</script>
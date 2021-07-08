<%@include file="/WEB-INF/jsp/taglibs.jsp"%>

<div id="searchResultsMergeCustomersTile">
	<div id="totalSearchCount">
		<table class="summary">
			<tr>
				<c:if test="${count== 0 or count== 1 }">
					<label for="totalRecord"><spring:message
							code="label.totalUserFoundOne" />
						<c:out value="${count}"></c:out> <spring:message
							code="label.totalUserFoundTwo" /></label>
				</c:if>
				<c:if test="${count!= 0 and count> 1}">
					<label for="totalRecord"><spring:message
							code="label.totalUserFoundOne" />
						<c:out value="${count}"></c:out> <spring:message
							code="label.totalUserFoundTwoMul" /></label>
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
				<th width="10%"><spring:message code="label.select.master.customer" /></th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty customers}">
				<tr>
					<td colspan="8" style="text-align: center">No customers found</td>
				</tr>
			</c:if>
			<c:if test="${!empty customers}">
				<c:forEach var="customer" items="${customers}">
					<c:set var="customer" value="${customer}" scope="request" />
					<tr>
						<td><c:out value="${customer.username}" /></td>
						<td><c:out value="${customer.emailAddress}" /></td>
						<td><c:out value="${customer.firstName}" /></td>
						<td><c:out value="${customer.familyName}" /></td>
						<td><c:out value="${customer.licenseCount}" /></td>
						<td><joda:format value="${customer.createdDate}" style="SM"
								dateTimeZone="UTC" /></td>
						<td><joda:format value="${customer.lastLoginDateTime}"
								style="SM" dateTimeZone="UTC" /></td>
						<td>
							<%-- <radiobutton id="customerId" path="${customer.erightsId}" value="${customer.erightsId}"/> --%>
							<c:if test="${count<= 1 }">
								<input type="radio" name="customerId"
									value="${customer.id}:${customer.username}"
									disabled="true" />
							</c:if> <c:if test="${count> 1 }">
								<input type="radio" name="customerId"
									value="${customer.id}:${customer.username}" />
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</c:if>

		</tbody>
	</table>
	<div id="mergebuttons">
		<button type="button" id="merge">
			<spring:message code="button.save" />
		</button>
		<button type="button" id="cancel">
			<spring:message code="button.cancel" />
		</button>
	</div>
	<input type="hidden" name="customerIdSelected" id="customerIdSelected" />
	<input type="hidden" name="customerNameSelected"
		id="customerNameSelected" />
	<div style="float: right; min-height: 100px\9">
		Last Search:
		<%=new java.util.Date().toString()%>
	</div>
</div>

<%-- <spring:message code="confirm.title.delete.customer" text="" var="message" />
<script type="text/javascript">
	$(document).ready(function() {
		
		/* $('#searchResultsMergeCustomersTile').undelegate('a.deleteCustomerLink', 'click');was getting 2 button clicks
		$('#searchResultsMergeCustomersTile').delegate  ('a.deleteCustomerLink', 'click', eacConfirm({title:'${message}'})); */
	});
	</script> --%>
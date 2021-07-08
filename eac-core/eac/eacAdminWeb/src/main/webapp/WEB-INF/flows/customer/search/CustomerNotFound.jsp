<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="customerNotFoundTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.customer.info" /></h1>
	</div>
	
	<div class="error">
		<h4>There is a problem with this customer record.</h4>
		<h4>As the data is corrupted and not recoverable, please delete this customer record - <b><c:out value="${customerUsername}"/></b>.</h4>
		<h4>The user will need to register again.</h4>
	</div>
	
    <c:if test="${customerId ne null}">
		 <div class="customerActions">
			<eac-common:canAdminDeleteUser var="canDeleteUser" userId="${customerId}" />
			<c:if test="${canDeleteUser}">
			<a class="actionLink deleteCustomerLink" eacLabel="${customer.username}" href="<c:url value="/mvc/customer/delete.htm" />?id=${customerId}"><button type="button" id="delete"><spring:message code="button.delete"/></button></a>
			</c:if>
		</div>
	</c:if> 
</div>
<spring:message code="confirm.title.delete.customer" text="" var="message" />
<script type="text/javascript">
	$(document).ready(function() {
		$('#searchResultsTile').undelegate('a.deleteCustomerLink', 'click');<%--was getting 2 button clicks--%>
		$('#searchResultsTile').delegate  ('a.deleteCustomerLink', 'click', eacConfirm({title:'${message}'}));
	});
	</script>
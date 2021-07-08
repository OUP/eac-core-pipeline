<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<spring:message code="title.registration.edit" var="editRegistrationTitle" scope="request" />
<c:choose>
	<c:when test="${!empty customerBean.productEntitlementGroups}">
		<table class="registrationSummary">
			<thead>
				<tr>
					<th><spring:message code="label.productName" /></th>
					<th><spring:message code="label.licenceActive" /></th>
					<th><spring:message code="label.licenceExpired" /></th>
					<th><spring:message code="label.expiryDateAndTime" /></th>
					<th><spring:message code="label.licenceType" /></th>							
					<th style="width:5%"><spring:message code="label.actions"/></th>
				</tr>
			</thead>
			<tbody id="registrationSummaryTableBody">
			<c:url value="/images/pencil.png" var="pencilUrl"/>
			<spring:message code="label.edit" var="edit"/>	
			<c:forEach var="entitlementGroup1" items="${customerBean.productEntitlementGroups}" varStatus="egStatus1">
				<c:set var="registration1" value="${entitlementGroup1.entitlement.registration}" scope="request"/>
				<c:set var="licence1" value="${entitlementGroup1.entitlement.licence}" scope="request"/>
				<c:set var="licenceId1" value="${licence1.licenseId}"/>
				<tr class="${egStatus1.index}">
					<td class=""><c:out value="${entitlementGroup1.entitlement.productList[0].product.productName}"/></td>
					<td class=""><spring:message code="label.${licence1.active}" /></td>
					<td class=""><spring:message code="label.${licence1.expired}" /></td>
					<td class="">
						<c:choose>
							<c:when test="${not empty licence1.expiryDateAndTime}">
								<joda:format value="${licence1.expiryDateAndTime}" pattern="dd/MM/yyyy HH:mm"  dateTimeZone="UTC"/>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td class=""><spring:message code="label.licenceType.${licence1.licenceDetail.licenceType}" /></td>
					<td class="actionClass">
						<div class="editRegistrationContainer">
							<a class="editRegistrationLink actionLink" href="#"><img class="actionImg" src="${pencilUrl}" title="${edit}" alt="${edit}"/></a>
							<div id="${licenceId1}" class="editRegistrationPopup" title="${editRegistrationTitle}"></div>
						</div>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</c:when>
	<c:otherwise>		
	</c:otherwise>
</c:choose>

<script type="text/javascript">
	
$('.editRegistrationContainer').each(function() {
	var $editRegistrationPopup = $(this).find('.editRegistrationPopup');
	var registrationId = $editRegistrationPopup.attr('id');
	var customerId = '${customerBean.customer.id}';
    $editRegistrationPopup.dialog({ 
	      buttons: [{ 
	    	  text: '<spring:message code="button.edit"/>', 
	    	  click: function() { 
	    		  $.ajax({
	    			  url: '<c:url value="/mvc/customer/editRegistration.htm"/>',
	    			  type: 'POST',
	    			  data: $editRegistrationPopup.find('form').serialize() + '&registration_id=' + registrationId + '&customer_id=' + customerId
	    		  }).done(function(html) {
	    			  $editRegistrationPopup.empty();
	    			  $editRegistrationPopup.append(html); 
	    			  $editRegistrationPopup.scrollTop(0);
	    			  setTimeout(function() {
	    				$('.success').slideUp();
	    			  }, 2000);
	    		  });
	    	  }
	      }, 
	      {	
	    	  text: '<spring:message code="button.close"/>',
	    	  click: function() { 
	    		  $(this).dialog('close'); 
	    	  } 
	      }], 
		  autoOpen: false, 
	      open: function(event, ui) {
	    	  $editRegistrationPopup.append('<div class="progressMessageContainer"><h2 class="progressMessage"><spring:message code="label.pleaseWait"/></h2><img src="<c:url value="/images/progress-indicator-large.gif"/>"/></div>');
	    	  $.ajax({
	    		  url: '<c:url value="/mvc/customer/editRegistration.htm"/>',
	    		  type: 'GET',
	    		  data: 'registration_id=' + registrationId + '&customer_id=' + customerId
	    	  }).done(function(html) { 
				setTimeout(function() {
					$editRegistrationPopup.empty();
					$editRegistrationPopup.append(html); 
				}, 750);
	    	  });
	    	  $('.ui-dialog-titlebar-close').hide(); 
	    	  $('.ui-widget-overlay').css('width','100%');},
	      close: function(event, ui) {
	    	  $editRegistrationPopup.empty();
	      },
	      dialogClass: 'dialog', 
	      closeOnEscape: true,
	      modal: true,
	      height: 500,
	      width: 600,
	      resizable: false
	 });
    
    var $editRegistrationLink = $(this).find('.editRegistrationLink');
    
    $editRegistrationLink.click(function() {
		$editRegistrationPopup.dialog('open');
		return false;
	});
});
	 		
</script>

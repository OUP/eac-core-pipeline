<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<tags:status name="customerBean" />

<c:if test="${errorMessageKey ne null}">
	<div class="error">
		<c:forEach items="${errorMessageKey}" var="error">
				<spring:message code="${error}"/><br>
		</c:forEach>
	</div>
</c:if>
<spring:message code="title.registration.editAnswers" var="editRegistrationAnswersTitle" scope="request"/>
<spring:message code="title.product.linked" var="linked"/>
<form:form id="editRegistrationForm" method="post" commandName="customerBean">
	<c:set var="customerBean" value="${customerBean}"></c:set>
	<c:forEach var="entitlementGroup" items="${customerBean.productEntitlementGroups}" varStatus="egStatus">
	<c:set var="registration" value="${entitlementGroup.entitlement.registration}" scope="request"/>
	<c:set var="licence" value="${entitlementGroup.entitlement.licence}" scope="request"/>
	<c:set var="licencePath" value="productEntitlementGroups[${egStatus.index}].entitlement.licence" scope="request"/>

	<%-- this code assumes there is only 1 product associated with a licence. Can be more than 1 product per licence in Atypon. --%>
	<c:set var="emptyProducts" value="${empty entitlementGroup.entitlement.productList}" />
	<c:choose>
		<c:when test="${emptyProducts}">
			<c:set var="productId">${entitlementGroup.entitlement.licence.productIds[0]}</c:set><%--productId will now be a string --%>
			<spring:message var="productName" code="label.product.in.atypon.but.not.eac" text="?label.product.in.atypon.but.not.eac?" arguments="${productId}" />
		</c:when>
		<c:otherwise>
			<c:set var="productName" value="${entitlementGroup.entitlement.productList[0].product.productName}"/>
		</c:otherwise>
	</c:choose>	
	<fieldset><%-- START PRODUCT --%>
		<legend><span style="max-width: 50em; display: block" class="shorten"><c:out value="${productName}"/></span></legend>
		<c:choose>
			<c:when test="${empty registration}">
				<div class="error">
				<c:set var="erightsLicenceId">${entitlementGroup.entitlement.licence.licenseId}</c:set><%-- erightsLicenceId id will now be string --%>
				<spring:message code="label.extra.licence.in.atypon" text="?label.extra.licence.in.atypon?" arguments="${erightsLicenceId}" />
				</div>
			</c:when>
			<c:otherwise>
			<c:if test="${entitlementGroup.orphan}">
				<div class="error">
				<spring:message code="label.parent.registration.problem" text="?label.parent.registration.problem?" arguments="${registration.id}" />
				</div>
			</c:if>
			<tiles:insertAttribute name="registrationDetailsTile" />
			</c:otherwise>
		</c:choose>				
			
		<c:choose> 
		    <c:when test="${not empty entitlementGroup.entitlement.activationCode}">
  				    <c:set var="ac">${entitlementGroup.entitlement.activationCode}</c:set>
      			</c:when>
                  <c:otherwise>
		        <c:set var="ac">-</c:set>
		    </c:otherwise>
		</c:choose>
		
		<spring:message code="title.licence" var="licenceLegend"/>											
		<tiles:insertAttribute name="licencesTile">
			<tiles:putAttribute name="licenceLegend" value="${licenceLegend}"/>
			<tiles:putAttribute name="actCode" value="${ac}"/>  
		</tiles:insertAttribute>				
		<c:forEach var="linkedEntitlement" items="${entitlementGroup.linkedEntitlements}" varStatus="leStatus">
			<c:forEach var="linkedProductDetails" items="${linkedEntitlement.productList}" varStatus="lpdStatus">
				<c:set var="licence" value="${linkedEntitlement.licence}" scope="request"/>
				<c:set var="licencePath" value="productEntitlementGroups[${egStatus.index}].linkedEntitlements[${leStatus.index}].licence" scope="request"/>
				<c:choose> 
                    <c:when test="${not empty linkedEntitlement.activationCode}">
                        <c:set var="ac">${linkedEntitlement.activationCode}</c:set>
                    </c:when>
                    <c:otherwise>
                        <c:set var="ac">-</c:set>
                    </c:otherwise>
                </c:choose>						
				<tiles:insertAttribute name="linkedLicenceTile">
					<tiles:putAttribute name="licenceLegend" value="${linked}&#160;-&#160;${linkedProductDetails.product.productName}"/>
					<tiles:putAttribute name="actCode" value="${ac}"/>
				</tiles:insertAttribute>						
			</c:forEach>
		</c:forEach>
		<c:forEach var="linkedRegistration" items="${entitlementGroup.entitlement.registration.linkedRegistrations}" varStatus="lrStatus" >						
				<c:if test="${not empty linkedRegistration.erightsLicenceId}">							
					<c:if test="${not fn:contains(customerBean.matchedErightsLicenceIds, linkedRegistration.erightsLicenceId) }">
						<fieldset>
							<legend>${linked}&#160;-&#160;<c:out value="${linkedRegistration.linkedProduct.productName}"/></legend>
							<div class="error">
							<c:set var="problemLinkedErightsId">${linkedRegistration.erightsLicenceId}</c:set><%-- problemLinkedErightsId is now a string --%>
							<spring:message code="label.linked.licence.not.in.atypon" text="?label.linked.licence.not.in.atypon?" arguments="${problemLinkedErightsId}" />
							</div>
						</fieldset>
					</c:if>
				</c:if>
		</c:forEach>
	</fieldset><%--END PRODUCT--%>
	<script type="text/javascript">
		$(function() {
			$('#regSendEmail-${registration.id}-div').hide();
		});
	</script>		
</c:forEach><%-- Entitlement Group --%>
</form:form>
<script type="text/javascript">
	$('.editAnswersPopupContainer').each(function() {
		var $editAnswersPopup = $(this).find('.editAnswersPopup');
		var registrationId = $editAnswersPopup.attr('id');
		var customerId = '${customerBean.customer.id}';
	    $editAnswersPopup.dialog({ 
		      buttons: [{ 
		    	  text: '<spring:message code="button.edit"/>', 
		    	  click: function() { 
		    		  $.ajax({
		    			  url: '<c:url value="/mvc/customer/editRegistrationAnswers.htm"/>',
		    			  type: 'POST',
		    			  data: $editAnswersPopup.find('form').serialize() + '&registration_id=' + registrationId + '&customer_id=' + customerId
		    		  }).done(function(html) {
		    			  $editAnswersPopup.empty();
		    			  $editAnswersPopup.append(html); 
		    			  $editAnswersPopup.scrollTop(0);
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
		    	  $editAnswersPopup.append('<div class="progressMessageContainer"><h2 class="progressMessage"><spring:message code="label.pleaseWait"/></h2><img src="<c:url value="/images/progress-indicator-large.gif"/>"/></div>');
		    	  $.ajax({
		    		  url: '<c:url value="/mvc/customer/editRegistrationAnswers.htm"/>',
		    		  type: 'GET',
		    		  data: 'registration_id=' + registrationId + '&customer_id=' + customerId
		    	  }).done(function(html) { 
					setTimeout(function() {
						$editAnswersPopup.empty();
						$editAnswersPopup.append(html); 
					}, 750);
		    	  });
		    	  $('.ui-dialog-titlebar-close').hide(); 
		    	  $('.ui-widget-overlay').css('width','100%');},
		      close: function(event, ui) {
		    	  $editAnswersPopup.empty();
		      },
		      dialogClass: 'dialog', 
		      closeOnEscape: true,
		      modal: true,
		      height: 600,
		      width: 500,
		      resizable: false
		 });
	    
	    var $editAnswersLink = $(this).find('.editAnswersLink');
	    
	    $editAnswersLink.click(function() {
			$editAnswersPopup.dialog('open');
			return false;
		});
	});
</script>
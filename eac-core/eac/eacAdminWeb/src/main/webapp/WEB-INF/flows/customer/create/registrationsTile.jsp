<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:url value="/images/add.png" var="addIconUrl" scope="request"/>
<c:url value="/images/pencil.png" var="editIconUrl" scope="request"/>
<div id="registrationsTile" style="min-height: 7em;">
	<c:if test="${empty customerBean.productEntitlementGroups}">
		<div class="error">
			<spring:message code="label.registrations.noRegistrations" />
		</div>
	</c:if>
	<spring:message code="title.registration.addNew" var="addNewRegistrationTitle" scope="request" />
	<security:authorize ifAllGranted="ROLE_CREATE_REGISTRATION">
		<div>
			<div class="span-4" style="margin-top: 5px; margin-bottom:15px; margin-bottom:0px\9">
				<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a id="addRegistrationLink" href="#"><spring:message code="label.registration.addNew" /></a>
			</div>
			<div id="addRegistrationPopup" title="${addNewRegistrationTitle}">
				<fieldset>
					<legend><spring:message code="title.product"/></legend>
					<%-- we do not allow admins to add registrations for REMOVED products --%>
					<tags:productFinder returnFieldId="selectedProduct" productStates="ACTIVE, SUSPENDED, RETIRED"/>
					<input type="hidden" name="selectedProduct" id="selectedProduct"/>
				</fieldset>
				<div id="registrationDetails"></div>
			</div>
		</div>
	</security:authorize>
	
	<tiles:insertAttribute name="registrationSummaryTableBodyTile"/>

	
	<script type="text/javascript">
	
		function createXMLHttpRequest()
	    {
	        var xmlhttp = false;
	        if(window.ActiveXObject) {
	                try {
	                    xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
	                } catch (e) {
	                    try {
	                        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	                    } catch (e) {
	                    xmlhttp = false;
	                    }
	            }
	        } 
	        else if (window.XMLHttpRequest) {
	                xmlhttp = new XMLHttpRequest();
	        }
	        return xmlhttp;
	    } 
		
		$(function() {
			<security:authorize ifAllGranted="ROLE_CREATE_REGISTRATION">
			var $addRegistrationPopup = $('#addRegistrationPopup');
			
			$addRegistrationPopup.dialog({ 
			      buttons: [{ 
			    	  text: '<spring:message code="button.add"/>', 
			    	  click: function() { 
			    		  $('#isAdmin').val(true);
			    		  $.ajax({
			    			 url: '<c:url value="/mvc/customer/addRegistration.htm"/>',
			    			 type: 'POST',
			    			 cache: false,
			    			 beforeSend : function(){
                                 createXMLHttpRequest();
                             }, 
			    			 data: $addRegistrationPopup.find('form').serialize() + '&customer_id=${customerBean.customer.id}'
			    		  }).done(function(html) {
			    			    $addRegistrationPopup.parent().find('.ui-button').each(function() {
			    			    	$(this).css('visibility', 'visible');
			    			    });
			    			    $addRegistrationPopup.css('opacity', '1.0');
			    			    $addRegistrationPopup.empty();
			    			    $addRegistrationPopup.append(html);
			    		  });
			    		  $addRegistrationPopup.parent().find('.ui-button').each(function() {
		    			     $(this).css('visibility', 'hidden');
		    			  });
			    		  $addRegistrationPopup.css('opacity', '0.5');
			    	  }
			      }, 
			      {	
			    	  text: '<spring:message code="button.cancel"/>',
			    	  click: function() { 
			    		  $(this).dialog('close'); 
			    	  } 
			      }], 
				  autoOpen: false, 
			      open: function(event, ui) {
			    	  $('.ui-dialog-titlebar-close').hide(); 
			    	  $('.ui-widget-overlay').css('width','100%');
			    	  toggleAddButton('hidden');
			   		},
			      dialogClass: 'dialog', 
			      closeOnEscape: true,
			      modal: true,
			      height: 600,
			      width: 500,
			      resizable: false
			 });
			
			$('#addRegistrationLink').click(function() {
				$addRegistrationPopup.dialog('open');
				return false;
			});
			
			$('#selectedProduct').change(function() {
				if ($(this).val()) {
		    	    $.ajax({
		    		    url: '<c:url value="/mvc/customer/addRegistration.htm"/>',
		    		    type: 'GET',
		    		    data: 'product_id=' + $(this).val()
		    	    }).done(function(html) {
		    			$('#registrationDetails').empty().append(html); 
			    	    if ($('#registrationDetails form:input[name="product_id"]') != null) {
			    	    	toggleAddButton('visible');	
			    	    }
		    	    });
				} else {
					$('#registrationDetails').empty();
					toggleAddButton('hidden');			
				}
			});			
			
			function toggleAddButton(visibility) {
	    	  $addRegistrationPopup.parent().find('.ui-button').each(function() {
	    		  var $this = $(this);
	    		  if ($this.children('span').text() == '<spring:message code="button.add"/>') {
	    			  $this.css('visibility', visibility);
	    			  $this.blur();
	    		  }
	    	  });	
			}			
			
			</security:authorize>			
		});
	</script>	
</div>

<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:url value="/images/add.png" var="addIconUrl"/>
<div id="externalIdsTile">
	<table class="summary">
		<thead>
			<tr>
				<th style="width:20%"><spring:message code="label.externalIds.externalSystem" /></th>
				<th style="width:20%"><spring:message code="label.externalIds.externalSystemIdType" /></th>
				<th><spring:message code="label.externalIds.externalId" /></th>							
				<th style="width:10%"><spring:message code="label.actions" /></th>
			</tr>
		</thead>
		<tbody id="externalIdsTableBody">
			<tiles:insertAttribute name="externalIdsTableBodyTile"/>
		</tbody>
	</table>
	<div>
		<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a id="addExternalId" href="#"><spring:message code="label.externalIds.add" /></a>
	</div>
	<div id="externalIdPopup">
		<div class="field">
			<div class="fieldLabelNarrow">
				<label for="externalId"><spring:message code="label.externalIds.externalId" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<spring:bind path="customerBean.externalId">
               		<input type="text" name="${status.expression}" value="${status.value}" id="${status.expression}" maxlength="255"/>
           		</spring:bind>
			</div>
		</div>	
		<div class="field">
			<div class="fieldLabelNarrow">
				<label for="externalSystem"><spring:message code="label.externalIds.externalSystem" /></label>
			</div>
			<div class="fieldValue">
				<spring:bind path="customerBean.externalSystem">
					<select name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" style="width: auto">
						<c:forEach var="externalSystem" items="${externalSystems}">
							<option value="${externalSystem.id}" <c:if test="${status.value == externalSystem.id}">selected="selected"</c:if>><c:out value="${externalSystem.name}"/></option>
						</c:forEach>
					</select>
				</spring:bind>	
			</div>
		</div>
		<div>
			<tiles:insertAttribute name="externalSystemIdTypeTile"/>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			$('#externalIdPopup').dialog({ 
				  autoOpen: false, 
			      closeOnEscape: true, 
			      open: onOpenDialog,
			      dialogClass: 'dialog', 
			      modal: true,
			      height: 'auto',
			      width: '400px',
			      resizable: false
			 });
			
			$('#addExternalId').click(function() {
				$('#externalIdPopup').dialog({ 
					title: '<spring:message code="label.externalIds.addNewExternalId.title" />',
			        buttons: [{ 
				    	  text: '<spring:message code="button.add"/>', 
				    	  id: 'saveExternalId', 
				    	  click: function() {
				  			var systemIdExists = false;							
							$('table tr').each(function() {								
							    if( $(this).find('td.externalSystem').text()== $('#externalSystem option:selected').text() ){							    
							        if($(this).find('td.externalSystemIdType').text()== $('#externalSystemIdType option:selected').text()){							     
							            systemIdExists = true;
							            return false;
							        }							        
							    }
							});							
							if (!systemIdExists) {
								saveExternalId();
								$(this).dialog('close'); 
								$('#externalSystem').attr('disabled', true);
					    		$('#externalSystemIdType').attr('disabled', true); 
							} else {
								alert('<spring:message code="error.externalSystemIdType.inUse"/>');
							}
				    	  }
				      }, 
				      {	
				    	  text: '<spring:message code="button.cancel"/>',
				    	  click: function() { 
				    		  $(this).dialog('close');
				    		  $('#externalSystem').attr('disabled', true);
				    		  $('#externalSystemIdType').attr('disabled', true); 
				    	  } 
				      }]
				});
				$('#externalId').val('');
				$('#externalSystem').removeAttr('disabled');
				$('#externalIdPopup').data('_eventId', 'addExternalId').dialog('open');
			});
			
			$('#externalSystem').change(function() {
				changeExternalSystem($('#externalSystem').val());
			});
			
			$('.editExternalIdLink').live('click', function() {
				var externalId = $(this).parent().siblings('.externalId');
				var externalSystem = $(this).parent().siblings('.externalSystem');
				$('#externalSystem option').each(function() {
					if ($(this).text() == externalSystem.text()) {
						$(this).attr('selected', 'selected');
					}
				});
				$('#externalSystem').attr('disabled', true);
				$('#externalId').val(externalId.text());
				$('#externalIdPopup').dialog({
					title: '<spring:message code="label.externalIds.editExternalId.title" />',
				    buttons: [{ 
				    	  text: '<spring:message code="button.edit"/>', 
				    	  id: 'saveExternalId', 
				    	  click: function() {
							saveExternalId();
							$(this).dialog('close'); 
				    	  }
				      }, 
				      {	
				    	  text: '<spring:message code="button.cancel"/>',
				    	  click: function() { 
				    		  $(this).dialog('close'); 
				    	  } 
				      }]
				});
				
				changeExternalSystem($('#externalSystem').val());
				
				$('#externalIdPopup').data('_eventId', 'updateExternalId').dialog('open');
			});
			
			$('.removeExternalIdLink').live('click', function() {
				var rowIndex = $(this).parent().parent('tr').attr('class');
				$.ajax({
					url: window.location.href,
					type: 'POST',
					data: "_eventId=removeExternalId&removalIndex=" + rowIndex
				}).done(function(html) {
					$('#externalIdsTableBody').empty();
					$('#externalIdsTableBody').append(html);
					//toggleAddExternalIdLinkVisibility();
				});
			});
			
			//toggleAddExternalIdLinkVisibility();
		});
		
		function saveExternalId() {
			$.ajax({
				url: window.location.href,
				type: 'POST',
				data: '_eventId=' + $('#externalIdPopup').data('_eventId') + '&externalSystemStr=' + $('#externalSystem').val() + '&externalSystemIdTypeStr=' + $('#externalSystemIdType').val() + '&externalId=' + encodeURIComponent($('#externalId').val())
			}).done(function(html) {
				$('#externalIdsTableBody').empty();
				$('#externalIdsTableBody').append(html);
				//toggleAddExternalIdLinkVisibility();
			});
		}
		
		function onOpenDialog(event, ui) {
			$(this).parent().appendTo($('#customerBean')),
			$(".ui-dialog-titlebar-close").hide(); 
			$('.ui-widget-overlay').css('width','100%');
			$('#externalId').select();
		}
		
		function changeExternalSystem(externalSystemId) {
			$.ajax({
				url: window.location.href,
				type: 'POST',
				data: "_eventId=changeExternalSystem&externalSystemStr=" + externalSystemId
			}).done(function(html) {
				$('#externalSystemIdTypeTile').empty();
				$('#externalSystemIdTypeTile').append(html);
				$('#externalSystemIdType').attr('disabled', true);
			});
		}
		
		function toggleAddExternalIdLinkVisibility() {
			if ($('table tbody tr').length < $('#externalSystem option').size()) {
				$('#addExternalId').show();
			} else {
				$('#addExternalId').hide();
			}
		}
	</script>  	
</div>
    

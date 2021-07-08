<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:url value="/images/add.png" var="addIconUrl"/>
<div id="productExternalIdsTile">
	<table class="summary">
		<thead>
			<tr>
				<th style="width:20%"><spring:message code="label.externalIds.externalSystem" /></th>
				<th style="width:20%"><spring:message code="label.externalIds.externalSystemIdType" /></th>
				<th><spring:message code="label.externalIds.externalId" /></th>							
				<th style="width:10%"><spring:message code="label.actions" /></th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	<div>
		<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a id="addExternalId" href="#"><spring:message code="label.externalIds.add" /></a>
	</div>
	<div id="externalIdPopup">
		<div class="field">
			<div class="fieldLabelNarrow">
				<label><spring:message code="label.externalIds.externalId" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<input type="text" name="externalId" id="externalId"/>
			</div>
		</div>	
		<div class="field">
			<div class="fieldLabelNarrow">
				<label><spring:message code="label.externalIds.externalSystem" /></label>
			</div>
			<div class="fieldValue">
				<select name="externalSystem" id="externalSystem" style="width:auto">
					<c:forEach var="entry" items="${productBean.externalSystemMap}">
						<option value="${entry.key.id}">${entry.key.name}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabelNarrow">
				<label><spring:message code="label.externalIds.externalSystemIdType" /></label>
			</div>
			<div class="fieldValue">
				<select name="externalSystemIdType" id="externalSystemIdType" style="width:auto">
				</select>
			</div>
		</div>
	</div>
	<c:url value="/images/pencil.png" var="pencilUrl"/>
	<c:url value="/images/delete.png" var="deleteUrl"/>
	<spring:message code="label.edit" var="edit"/>	
	<spring:message code="label.remove" var="remove"/>	
	<script type="text/javascript">
		$(function() {
			var externalIdModel = [];
			var guidsToDelete = [];
			var externalId = null;
			var $externalId = $('#externalId');
			var $externalSystem = $('#externalSystem');
			var $externalSystemIdType = $('#externalSystemIdType');
			var $externalIdPopup = $('#externalIdPopup');
			
			<c:forEach var="externalId" items="${productBean.externalIds}" varStatus="status">
				externalId = Object();
				externalId.externalId = '${fn:replace(externalId.externalId, "'", "\\'")}';
				externalId.guid = '${externalId.id}';
				externalId.externalSystem = {guid: '${externalId.externalSystemIdType.externalSystem.id}', name: '${fn:replace(externalId.externalSystemIdType.externalSystem.name, "'", "\\'")}'};
				externalId.externalSystemIdType = {guid: '${externalId.externalSystemIdType.id}', name: '${fn:replace(externalId.externalSystemIdType.name, "'", "\\'")}'};
				externalIdModel.push(externalId);
			</c:forEach>
			
			function modelAdd(externalId, externalSystem, externalSystemIdType) {
				var externalIdObj = Object();
				externalIdObj.externalId = externalId;
				externalIdObj.externalSystem = externalSystem;
				externalIdObj.externalSystemIdType = externalSystemIdType;
				externalIdModel.push(externalIdObj);
				redraw();
			}
			
			function modelDelete(rowIdx) {
				var deleted = externalIdModel.splice(rowIdx, 1);
				if ('guid' in deleted[0]) {
					guidsToDelete.push(deleted[0].guid);
				}
				redraw();
			}
			
			function modelUpdate(rowIdx, externalId) {
				for (var i = 0; i < externalIdModel.length; i++) {
					if (i == rowIdx) {
						var externalIdObj = externalIdModel[i];
						externalIdObj.externalId = externalId;
						break;
					}
				}
				redraw();
			}
			
			var $tbody = $('#productExternalIdsTile tbody');
			
			function redraw() {
				$tbody.empty();
				if (externalIdModel.length > 0) {
					for (var i = 0; i < externalIdModel.length; i++) {
						var externalId = externalIdModel[i];
						$tbody.append('<tr id="' + i + '"><td>' + externalId.externalSystem.name + '</td><td>' + externalId.externalSystemIdType.name + '</td><td>' + externalId.externalId + '</td><td><a class="editExternalIdLink actionLink" href="#"><img class="actionImg" src="${pencilUrl}" title="${edit}" alt="${edit}"/></a>&#160;<a class="removeExternalIdLink actionLink" href="#"><img class="actionImg" src="${deleteUrl}" title="${remove}" alt="${remove}"/></a></td>');
					}
				} else {
					$tbody.append('<td colspan="4" style="text-align:center"><spring:message code="label.externalIds.noExternalIdsProduct" /></td>');
				}
			}
			
			redraw();
			
			function createButtons(actionName, clickAction) {
				return [{ 
			    	  text: actionName, 
			    	  id: 'saveExternalId', 
			    	  click: clickAction
			      }, 
			      {	
			    	  text: '<spring:message code="button.cancel"/>',
			    	  click: function() { 
			    		  $(this).dialog('close'); 
			    	  } 
			      }];
			}
			
			$externalIdPopup.dialog({ 
				  autoOpen: false, 
			      closeOnEscape: true, 
			      open: function(event, ui) {
						$(".ui-dialog-titlebar-close").hide(); 
						$('.ui-widget-overlay').css('width','100%');
						$('#externalId').select();  
			      },
			      dialogClass: 'dialog', 
			      modal: true,
			      height: 'auto',
			      width: '400px',
			      resizable: false
			 });
			
			$('#addExternalId').click(function() {
				$externalIdPopup.dialog({ 
					title: '<spring:message code="label.externalIds.addNewExternalId.title" />',
			        buttons: createButtons('<spring:message code="button.add"/>', function() {
			    		  var externalId = $externalId.val();
			    		  var externalSystem = {guid: $externalSystem.val(), name: $externalSystem.find('option:selected').text()};
			    		  var externalSystemIdType = {guid: $externalSystemIdType.val(), name: $externalSystemIdType.find('option:selected').text()};
			    		  $errorFlag = false;
			    		  $('table tbody tr').each(function(index) {    
                              $rowValue = $(this);
                              $system = false;

                              $($rowValue.find('td')).each(function(index1) {                           
                                  if(index1==0 && $(this).text() == $('#externalSystem option:selected').text()){
                                      $system = true;           
                                  }else if($system){
                                      if(index1==1 && $(this).text() == $('#externalSystemIdType option:selected').text()){
                                          $errorFlag = true;
                                          return false;
                                      }            
                                  }else{
                                      return false;
                                  }
                              });
                              if($errorFlag){
                              alert('<spring:message code="error.externalSystemIdType.inUse"/>');
                                  return false;
                              }

                          });
			    		  if ($errorFlag == false && externalId) {
                              modelAdd(externalId, externalSystem, externalSystemIdType);
                              $(this).dialog('close');
                          }
			    	  })
				});
				$externalId.val('');
				$externalIdPopup.dialog('open');
				$externalIdPopup.find('select').attr('disabled', false);
				$('#popupMsg').remove();
				$('#saveExternalId').attr('disabled', true);
				return false;
			});		
			
			function externalSystemInUse(externalSystemGuid) {
				var externalSystemInUse = false;
				for (var i = 0; i < externalIdModel.length; i++) {
					var externalId = externalIdModel[i];
					if (externalSystemGuid == externalId.externalSystem.guid) {
						externalSystemInUse = true;
						break;
					}
				}
				return externalSystemInUse;
			}
			
			var externalSystemMap = {};
			
			<c:forEach var="entry" items="${productBean.externalSystemMap}">
				externalSystemMap['${entry.key.id}'] = [<c:forEach var="externalSystemIdType" items="${entry.value}" varStatus="status">{guid: '${externalSystemIdType.id}', name: '${fn:replace(externalSystemIdType.name, "'", "\\'")}'}<c:if test="${not status.last}">,</c:if></c:forEach>];
			</c:forEach>
			
			$externalSystem.change(function() {
				var guid = $(this).find('option:selected').val();
				var externalSystemIdTypes = externalSystemMap[guid];
				$externalSystemIdType.find('option').remove();
				
				for (var i = 0; i < externalSystemIdTypes.length; i++) {
					var externalSystemIdType = externalSystemIdTypes[i];
					$externalSystemIdType.append('<option value="' + externalSystemIdType.guid + '">' + externalSystemIdType.name + '</option');
				}
			}).live("keypress", function() {
				$externalSystem.trigger('change');
			});
			
			$externalSystem.trigger('change');
			
			$('#productExternalIdsTile table').delegate('a', 'click', function() {
				$this = $(this);
				$parentRow = $this.closest('tr');
				var rowIdx = ($parentRow.attr('id'));
				var editExtProId = $parentRow.find("td:eq(2)").text() ;
				var editExtTypeId = $parentRow.find("td:eq(1)").text() ;
				var editExtId = $parentRow.find("td:eq(0)").text() ;
				if ($this.hasClass('editExternalIdLink')) {
					$externalIdPopup.dialog({ 
						title: '<spring:message code="label.externalIds.editExternalId.title" />',
				        buttons: createButtons('<spring:message code="button.edit"/>', function() {
				    		  var externalId = $externalId.val();
				    		  modelUpdate(rowIdx, externalId);
				    		  $(this).dialog('close'); 
				    	  })
					});
					
					$externalId.val(editExtProId);
					$externalSystem.find('option').each(function() {
						if ($(this).text() == editExtId) {
							$(this).attr('selected', true);
							$externalSystem.trigger('change');
						}					
					});
					$externalSystemIdType.find('option').each(function() {
						if ($(this).text() == editExtTypeId) {
							$(this).attr('selected', true);
						}
					});
					$externalIdPopup.dialog('open');
					$externalIdPopup.find('select').attr('disabled', true);
					return false;
				}
				
				if ($this.hasClass('removeExternalIdLink')) {
					modelDelete(rowIdx);
					return false;
				}
			});
			
			$externalId.bind("keyup", function() {
				if ($externalId.val() != '' && $externalId.val().length != 0) {
					scheduleCheck(0);
				}
			});
			
			$externalIdPopup.find('select').change(function() {
				if ($externalId.val() != '' && $externalId.val().length != 0) {
					scheduleCheck(0);
				}
			});
			
			function scheduleCheck(wait) {
				$('#saveExternalId').attr('disabled', true);
				if (scheduleCheck.previous) {
					clearTimeout(scheduleCheck.previous);
				}
				scheduleCheck.previous = setTimeout(function() {
					checkExternalIdInUse();
				}, wait);
			}
			
			function checkExternalIdInUse() {
				$.ajax({
					url: '<c:url value="/mvc/product/edit.htm"/>',
					type: 'GET',
					dataType: 'json',
					data: 'id=${productBean.productId}&externalId=' + encodeURIComponent($externalId.val()) + '&externalSystem=' + $externalSystem.find('option:selected').text() + '&externalSystemType=' + $externalSystemIdType.find('option:selected').text()
				}).done(function(response) {
					if (response.inUse == 'true') {
						if ($('#popupMsg').length == 0) {
							$externalId.after('<span id="popupMsg" style="display:block; color:red"><spring:message code="error.external.product.id.taken" /></span>');
						}
						$('#saveExternalId').attr('disabled', true);
					} else if (response.inUse == 'false') {
						$('#popupMsg').remove();
						$('#saveExternalId').attr('disabled', false);
					}
				});
			}
			
			$('#productBean').submit(function() {
				var externalIdsList = '';
				var externalIdsToRemove = '' ;
				for (var i = 0; i < externalIdModel.length; i++) {
					var externalId = externalIdModel[i];
					externalIdsList += encodeURIComponent(externalId.externalId) + ',' + externalId.externalSystem.name + ',' + externalId.externalSystemIdType.name + '|';
				}
				for (var i = 0; i < guidsToDelete.length; i++) {
					externalIdsToRemove += guidsToDelete[i] + '|';
				}
				$(this).append('<input type="hidden" name="externalIdsToRemove" value="' + externalIdsToRemove + '" />');
				$(this).append('<input type="hidden" name="externalIdsToAdd" value="' + externalIdsList + '" />');
			});
		});
	</script>
</div>
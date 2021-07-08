<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:url value="/images/add.png" var="addIconUrl"/>
<div id="productUsedByPlatformTile">
	<table class="summary">
		<thead>
			<tr>
				<th style="width:20%"><spring:message code="label.platformIds.platformCode" /></th>
				<th style="width:20%"><spring:message code="label.platformIds.platformName" /></th>
				<th style="width:10%"><spring:message code="label.actions" /></th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	<div>
		<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a id="addPlatformId" href="#"><spring:message code="label.platformIds.add" /></a>
	</div>
	<div id="platformIdPopup">
		<div class="field">
			<div class="fieldLabelNarrow">
				<label><spring:message code="label.platformIds.platformCode" /></label>
			</div>
			<div class="fieldValue">
				<select name="platformMapping" id="platformMapping" style="width:auto">
					<c:forEach var="entry" items="${productBean.platformMap}">
						<option value="${entry.key}">${entry.value.code}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabelNarrow">
				<label><spring:message code="label.platformIds.platformName" /></label>
			</div>
			<div class="fieldValue">
				<input type="text" name="platformName" id="platformName"/>
			</div>
		</div>	
	</div>
	<c:url value="/images/delete.png" var="deleteUrl"/>
	<spring:message code="label.remove" var="remove"/>	
	<script type="text/javascript">
		$(function() {
			var productPlatformMapping = [] ;
			
			var $platformIdPopup = $('#platformIdPopup');
			<c:forEach var="platformExist" items="${productBean.platformList}" varStatus="status" >
				var platformObj = Object();
				platformObj.platformId = '${platformExist.platformId}';
				platformObj.platformCode = '${platformExist.code}';
				platformObj.platformName = '${platformExist.name}';
				productPlatformMapping.push(platformObj) ;
			</c:forEach>
			
			
			
			function modelAdd(platformId, platformCode, platformName) {
				var platformObj = Object();
				platformObj.platformId = platformId;
				platformObj.platformCode = platformCode;
				platformObj.platformName = platformName;
				productPlatformMapping.push(platformObj) ;
				redraw();
			}
			
			function modelDelete(rowIdx) {
				var index = productPlatformMapping.findIndex(x => x.platformId==rowIdx);
				productPlatformMapping.splice(index, 1);
				redraw();
			}
			
			var $tbody = $('#productUsedByPlatformTile tbody');
			
			function redraw() {
				$tbody.empty();
				if (productPlatformMapping.length > 0) {
					for (index in productPlatformMapping) {
						var platform = productPlatformMapping[index];
						$tbody.append('<tr id="' + platform.platformId + '"><td>' + platform.platformCode + '</td><td>' + platform.platformName + '</td><td><a class="removePlatformIdLink actionLink" href="#"><img class="actionImg" src="${deleteUrl}" title="${remove}" alt="${remove}"/></a></td>');
					}
				} else {
					$tbody.append('<td colspan="4" style="text-align:center"><spring:message code="label.platformIds.noPlatformIdsProduct" /></td>');
				}
			}
			
			redraw();
			
			function createButtons(actionName, clickAction) {
				return [{ 
			    	  text: actionName, 
			    	  id: 'savePlatformId', 
			    	  click: clickAction
			      }, 
			      {	
			    	  text: '<spring:message code="button.cancel"/>',
			    	  click: function() { 
			    		  $(this).dialog('close'); 
			    	  } 
			      }];
			}
			
			$platformIdPopup.dialog({ 
				  autoOpen: false, 
			      closeOnEscape: true, 
			      open: function(event, ui) {
						$(".ui-dialog-titlebar-close").hide(); 
						$('.ui-widget-overlay').css('width','100%');
						/* $('#platformId').select();   */
			      },
			      dialogClass: 'dialog', 
			      modal: true,
			      height: 'auto',
			      width: '400px',
			      resizable: false
			 });
			
			$('#addPlatformId').click(function() {
				$platformIdPopup.dialog({ 
					title: '<spring:message code="label.platformIds.add" />',
			        buttons: createButtons('<spring:message code="button.add"/>', function() {
			    		  var platformId = $('#platformMapping').find('option:selected').val();
			    		  var platformCode = $('#platformMapping').find('option:selected').text();
			    		  var platformName = $('#platformName').val();
			    		  $errorFlag = false;
			    		  $('table tbody tr').each(function(index) {    
			    			  var id = this.id;
			    			  
                             //error for duplicates
                              if(id == platformId ){
								$errorFlag = true ;
								alert('<spring:message code="error.platformIds.inUse"/>');
								return false;
                              }

                          });
			    		  if ($errorFlag == false && platformId) {
                              modelAdd(platformId, platformCode, platformName);
                              $(this).dialog('close');
                          }
			    	  })
				});
				var p1 = platformMap[$('#platformMapping').find('option:selected').val()] ;
				if (p1 != null && p1[0] != null ) 
					$('#platformName').val(p1[0].name);
				$('#platformName').attr('disabled', true) ;
				$platformIdPopup.dialog('open');
				$platformIdPopup.find('select').attr('disabled', false);
				$('#savePlatformId').attr('disabled', false);
				return false;
			});		
			
			
			
			var platformMap = {};
			
			<c:forEach var="entry" items="${productBean.platformMap}">
				platformMap["${entry.key}"] = [{platformId: "${entry.key}", name: "${entry.value.name}",code: "${entry.value.code}"}];
			</c:forEach>
			
			$('#platformMapping').change(function() {
				var guid = $(this).find('option:selected').val() ;
				var platform = platformMap[guid] ;
				if (platform != null )
				
					$('#platformName').val(platform[0].name);
			}).live("keypress", function() {
				$('#platformMapping').trigger('change');
			});
			
			$('#platformMapping').trigger('change');
			
			$('#productUsedByPlatformTile table').delegate('a', 'click', function() {
				$this = $(this);
				$parentRow = $this.closest('tr');
				var rowIdx = ($parentRow.attr('id'));
				if ($this.hasClass('removePlatformIdLink')) {
					modelDelete(rowIdx);
					return false;
				}
			});
			
			$('#productBean').submit(function() {
				var platformList = '' ;
				for (index in productPlatformMapping) {
					var platform = productPlatformMapping[index];
					platformList += encodeURIComponent(platform.platformId) + ',' + platform.platformCode + ',' + platform.platformName + '|';
				}
				$(this).append('<input type="hidden" name="platformToAdd" value="' + platformList + '" />');
			});
		});
	</script>
</div>
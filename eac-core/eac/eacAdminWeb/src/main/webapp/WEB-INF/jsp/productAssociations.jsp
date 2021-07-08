<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:url value="/images/add.png" var="addIconUrl"/>
<div id="productAssociationsTile">
	<table class="summary">
		<thead>
			<tr>
				<th><spring:message code="label.product" /></th>
				<th style="width:10%"><spring:message code="label.actions" /></th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	<div>
		<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a id="addLinkedProduct" href="#"><spring:message code="label.addLinkedProduct" /></a>
		<spring:message code="info.addLinkedProduct" var="addLinkedProductHelp" />
		<img id="addLinkedProductHelp" class="infoIcon" style="margin-left: 5px; padding-top: 0px" src="<c:url value="/images/information.png"/>" title="${addLinkedProductHelp}"/>
		<script type="text/javascript">
			$("#addLinkedProductHelp").tipTip({edgeOffset: 7});
		</script>		
	</div>
	<div id="linkedProductPopup" style="overflow: visible">
		<div class="field">
			<div class="fieldLabelNarrow">
				<label><spring:message code="label.product" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue" style="max-width: 60em">
				<tags:productFinder returnFieldId="linkedProductAssetId" returnNameFieldId="linkedProductName" initOnChangeId="editAssetGuid"/>
				<input type="hidden" name="linkedProductAssetId" id="linkedProductAssetId"/>
				<input type="hidden" name="linkedProductName" id="linkedProductName"/>
				<input type="hidden" name="editAssetGuid" id="editAssetGuid"/>
			</div>	
		</div>	
		
	</div>	
	<c:url value="/images/pencil.png" var="pencilUrl"/>
	<c:url value="/images/delete.png" var="deleteUrl"/>
	<spring:message code="label.edit" var="edit"/>	
	<spring:message code="label.remove" var="remove"/>
	<script type="text/javascript">
		$(function() {
			var linkedProductModel = [];
			var guidsToDelete = [];
			var linkedProduct = null;
			var guidsWhichAreUsed = [];
			var $linkedProductPopup = $('#linkedProductPopup');
			//alert("hi" + ${fn:length((productBean.linkedProducts))});
			<c:forEach var="linkedProduct" items="${productBean.linkedProducts}">
				linkedProduct = {};
				linkedProduct.guid = '${linkedProduct.productId}',
				linkedProduct.product = {guid: '${linkedProduct.productId}', name: "${linkedProduct.name}"};
				linkedProductModel.push(linkedProduct);
				
				
			</c:forEach>
			
			function modelAdd(product) {
				var linkedProduct = {};
				linkedProduct.product = product;
				linkedProductModel.push(linkedProduct);
				redraw();
			}
			
			function modelDelete(rowIdx) {
				var deleted = linkedProductModel.splice(rowIdx, 1);
				if ('guid' in deleted[0]) {
					guidsToDelete.push(deleted[0].guid);
				}				
				redraw();
			}
			
			function modelUpdate(rowIdx, product) {
				for (var i = 0; i < linkedProductModel.length; i++) {
					if (i == rowIdx) {
						var linkedProduct = linkedProductModel[i];
						linkedProduct.product = product;
						break;
					}
				}
				redraw();
			}
			
			var $tbody = $('#productAssociationsTile tbody');
			
			function redraw() {
				$tbody.empty();
				if (linkedProductModel.length > 0) {
					for (var i = 0; i < linkedProductModel.length; i++) {
						var linkedProduct = linkedProductModel[i];
						var removeLinkHtml = '';
						if($.inArray(linkedProduct.guid,guidsWhichAreUsed)){
							removeLinkHtml = '&#160;<a class="removeLinkedProductLink actionLink" href="#"><img class="actionImg" src="${deleteUrl}" title="${remove}" alt="${remove}"/></a>';
						}
						$tbody.append('<tr id="' + i + '"><td>' + linkedProduct.product.name + '</td><td><a class="editLinkedProductLink actionLink" href="#"><img class="actionImg" src="${pencilUrl}" title="${edit}" alt="${edit}"/>'+removeLinkHtml+'</a></td>');
					}
				} else {
					$tbody.append('<td colspan="4" style="text-align:center"><spring:message code="label.noLinkedProducts" /></td>');
				}
			}
			
			redraw();	
			
			function createButtons(actionName, clickAction) {
				return [{ 
			    	  text: actionName, 
			    	  id: 'saveLinkedProduct', 
			    	  click: clickAction
			      }, 
			      {	
			    	  text: '<spring:message code="button.cancel"/>',
			    	  click: function() { 
			    		  $(this).dialog('close'); 
			    	  } 
			      }];
			}			
			
			$linkedProductPopup.dialog({ 
				  autoOpen: false, 
			      closeOnEscape: true, 
			      open: function(event, ui) {
						$(".ui-dialog-titlebar-close").hide(); 
						$('.ui-widget-overlay').css('width','100%');
			      },
			      dialogClass: 'dialog', 
			      modal: true,
			      height: 'auto',
			      width: 550,
			      resizable: false
			 });	
			
			$('#addLinkedProduct').click(function() {
				$linkedProductPopup.dialog({ 
					title: '<spring:message code="title.addLinkedProduct" />',
			        buttons: createButtons('<spring:message code="button.add"/>', function() {
			    		  var productId = $('#linkedProductAssetId').val();
			    		  if (productId) {
			    			  var product = {guid: productId, name: $('#linkedProductName').val()};
				    		  modelAdd(product);
			    		  }
			    		  $(this).dialog('close'); 
			    	  })
				});
				$linkedProductPopup.dialog('open');
				return false;
			});		
			
			$('#productAssociationsTile table').delegate('a', 'click', function() {
				$this = $(this);
				$parentRow = $this.closest('tr');
				var rowIdx = ($parentRow.attr('id'));
				
				if ($this.hasClass('editLinkedProductLink')) {
					$linkedProductPopup.dialog({ 
						title: '<spring:message code="title.editLinkedProduct" />',
				        buttons: createButtons('<spring:message code="button.edit"/>', function() {
				        	var productGuid = $('#linkedProductAssetId').val();
				        	if (productGuid) {
					        	var product = {guid: $('#linkedProductAssetId').val(), name: $('#linkedProductName').val()};
					    		modelUpdate(rowIdx, product);
				        	}
				    		$(this).dialog('close'); 
				    	  })
					});
					$('#editAssetGuid').val(linkedProductModel[rowIdx].product.guid).trigger('change');
					$linkedProductPopup.dialog('open');
					return false;
				}
				
				if ($this.hasClass('removeLinkedProductLink')) {
					modelDelete(rowIdx);
					return false;
				}
			});	
			
			$('#productBean').submit(function() {
				var linkedProductsToAdd = '';
				var linkedProductsToUpdate = '';
				var linkedProductsToRemove = '';
				
				for (var i = 0; i < linkedProductModel.length; i++) {
					var linkedProduct = linkedProductModel[i];
					if ('guid' in linkedProduct) {
						linkedProductsToUpdate += linkedProduct.guid + ',' + linkedProduct.product.guid + '|';
					} else {
						linkedProductsToAdd += linkedProduct.product.guid + '|';
					}
				}
				
				for (var i = 0; i < guidsToDelete.length; i++) {
					linkedProductsToRemove += guidsToDelete[i] + '|';
				}
				
				var $this = $(this);
				$this.append('<input type="hidden" name="linkedProductsToAdd" value="' + linkedProductsToAdd + '" />');
				$this.append('<input type="hidden" name="linkedProductsToUpdate" value="' + linkedProductsToUpdate + '" />');
				$this.append('<input type="hidden" name="linkedProductsToRemove" value="' + linkedProductsToRemove + '" />');
			});			
		});
	</script>
</div>
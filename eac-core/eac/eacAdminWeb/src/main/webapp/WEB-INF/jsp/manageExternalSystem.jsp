<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:url value="/images/add.png" var="addIconUrl"/>
<div id="manageExternalSystemTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.manageExternalSystems"/></h1>
	</div>

	<c:if test="${not empty statusMessageKey}">
		 <div class="success">
			<spring:message code="${statusMessageKey}"/>
		</div>
	</c:if>
	
	<form:form modelAttribute="externalSystemBean" method="POST">
	
		<spring:hasBindErrors name="externalSystemBean">
			<div class="error">
				<spring:bind path="externalSystemBean.*">
					<c:forEach items="${status.errorMessages}" var="error">
						<span><c:out value="${error}"/></span><br>
					</c:forEach>
				</spring:bind>
			</div>
		</spring:hasBindErrors>	
		
		<fieldset>
			<legend><spring:message code="label.externalSystem"/></legend>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.externalIds.externalSystem" /></label>
				</div>
				<div class="fieldValue">
					<form:select id="selectedExternalSystem" path="selectedExternalSystemGuid">
						<c:forEach var="externalSystem" items="${externalSystemBean.externalSystems}">
							<form:option value="${externalSystem.id}" label="${externalSystem.name}"/>
						</c:forEach>
						<spring:message code="label.newEntry" var="newEntry"/>
						<form:option value="new" label="${newEntry}"/>
					</form:select>
				</div>
			</div>	
		</fieldset>
		<fieldset id="new" class="externalSystemId">
			<legend><spring:message code="label.externalSystemSummary"/></legend>		
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.externalIds.externalSystem" />&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:input path="newExternalSystemName"/>
				</div>
			</div>	
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.description"/></label>
				</div>
				<div class="fieldValue">
					<form:input path="newExternalSystemDescription"/>
				</div>
			</div>
			<fieldset style="margin-top: 20px; margin-top: 0px\9">
				<legend><spring:message code="label.externalSystemIdTypes"/></legend>
				<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a href="#" class="addTypeLink"><spring:message code="label.addType"/></a>
			</fieldset>
		</fieldset>
		<c:forEach var="externalSystem" items="${externalSystemBean.externalSystems}" varStatus="systemStatus">
			<fieldset id="${externalSystem.id}" class="externalSystemId">
				<legend><spring:message code="label.externalSystemSummary"/></legend>
				<div class="field">
					<div class="fieldLabel">
						<label><spring:message code="label.externalIds.externalSystem" />&#160;<span class="mandatory">*</span></label>
					</div>
					<div class="fieldValue">
						<form:input path="externalSystems[${systemStatus.index}].name"/>
					</div>
				</div>	
				<div class="field">
					<div class="fieldLabel">
						<label><spring:message code="label.description"/></label>
					</div>
					<div class="fieldValue">
						<form:input path="externalSystems[${systemStatus.index}].description"/>
					</div>
				</div>	
				<fieldset style="margin-top: 20px; margin-top: 0px\9">
					<legend><spring:message code="label.externalSystemIdTypes"/></legend>
					<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a href="#" class="addTypeLink"><spring:message code="label.addType"/></a>
					<c:forEach var="systemType" items="${externalSystemBean.externalSystemIdTypes[systemStatus.index]}" varStatus="typeStatus">
						<c:set var="deletable" value="deletable"/>
						<c:if test="${not empty systemType.externalIds}">
							<c:set var="deletable" value=""/>
						</c:if>
						<fieldset id="${typeStatus.index}" class="externalSystemTypeId ${deletable}">
							<div class="field">
								<div class="fieldLabel">
									<label><spring:message code="label.type" />&#160;<span class="mandatory">*</span></label>
								</div>
								<div class="fieldValue">
									<form:input path="externalSystemIdTypes[${systemStatus.index}][${typeStatus.index}].name"/>		
								</div>
								<c:if test="${not empty deletable}">
									<div class="removeType" style="float: right; cursor: pointer"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
								</c:if>
							</div>	
							<div class="field">
								<div class="fieldLabel">
									<label><spring:message code="label.description"/></label>
								</div>
								<div class="fieldValue">
									<form:input path="externalSystemIdTypes[${systemStatus.index}][${typeStatus.index}].description"/>		
								</div>
							</div>
						</fieldset>
					</c:forEach>
				</fieldset>
			</fieldset>
		</c:forEach>
        <div id="buttons">
        	<p>                 
        		<button type="button" id="delete"><spring:message code="button.delete"/></button>
        		<button type="submit" id="save"><spring:message code="button.save" /></button>
            </p>
        </div>		
        <form:hidden id="systemTypesToRemove" path="systemTypesToRemove" />
        <form:hidden id="oldSystemName" path="oldSystemName" />
        
	</form:form>
	
	<div id="newTypePrototype">
		<fieldset class="newType">
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.type" />&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<input type="text" name="newExternalSystemIdTypes[%idx%].name" disabled="disabled"/>		
				</div>
				<div class="removeNewType" style="float: right; cursor: pointer"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
			</div>	
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.description"/></label>
				</div>
				<div class="fieldValue">
					<input type="text" name="newExternalSystemIdTypes[%idx%].description" disabled="disabled"/>		
				</div>
			</div>
		</fieldset>
	</div>
	<spring:message code="confirm.title.delete.external.system" var="confirmDelete" text="" />
	<script type="text/javascript">
		$(function() {
			var $newTypeFieldset = $('#newTypePrototype fieldset');
			var $selectedExternalSystem = $('#selectedExternalSystem');
			var $deleteButton = $('#delete');
			var $form = $('form');
			$newTypeFieldset.hide();
			
			$selectedExternalSystem.change(function() {
				var $this = $(this);
				var selectedValue = $this.find('option:selected').val();
				if (selectedValue == 'new') {
					$deleteButton.hide();
				} else {
					if ($('fieldset[id=\'' + selectedValue + '\']').find('fieldset.externalSystemTypeId:not(.deletable)').length > 0) {
						$deleteButton.hide();
					} else {
						$deleteButton.show();
					}
				}
				$('.externalSystemId').css('display', 'none');
				$('fieldset[id=\'' + selectedValue + '\']').css('display', '');
				$('#systemTypesToRemove').val('');
				$form.find('.newType').remove();
				$deleteButton.attr('eacLabel',$('option:selected', $selectedExternalSystem).text());
				$('fieldset.externalSystemTypeId').show();
			}).keypress(function() {
				$(this).trigger('change');
			});
			
			$selectedExternalSystem.trigger('change');
			
			$('.addTypeLink').click(function() {
				$clonedTypeFieldset = $newTypeFieldset.clone();
				$clonedTypeFieldset.find('input').each(function() {
					$(this).removeAttr('disabled');
				});
				var $existingSystemTypes = $(this).siblings('.externalSystemTypeId');
				if ($existingSystemTypes.length > 0) {
					$clonedTypeFieldset.insertBefore($existingSystemTypes.first());
				} else {
					$(this).after($clonedTypeFieldset);
				}
				$clonedTypeFieldset.addClass('externalSystemTypeId');
				$clonedTypeFieldset.show();
				$clonedTypeFieldset.find('input:first').focus();
				return false;
			});
			
			$('.removeType').click(function() {
				var $fieldset = $(this).closest('fieldset');
				var $systemTypesToRemove = $('#systemTypesToRemove');
				var systemTypesToRemove = $systemTypesToRemove.val();
				if (systemTypesToRemove != "") {
					systemTypesToRemove += ",";
				}
				systemTypesToRemove += $fieldset.attr('id');
				$systemTypesToRemove.val(systemTypesToRemove);
				$fieldset.hide();
			});			
			
			$form.delegate('.removeNewType', 'click', function() {
				$(this).closest('fieldset').remove();
			});
			
			var callback = function(){
				$form.append('<input type="hidden" name="delete" value="1" />');
				$form.submit();
			};
			var clickHandler = eacConfirm({callbackYes:callback, title:'${confirmDelete}'});
			$deleteButton.click(clickHandler);

			$form.submit(function() {
				$('#oldSystemName').val($('option:selected', $selectedExternalSystem).text());
				var newTypeCount = 0;
				$('.newType').each(function() {
					$(this).find('input').each(function() {
						var name = $(this).attr('name');
						$(this).attr('name', name.replace(/%idx%/, newTypeCount));
					});
					newTypeCount++;
				});
			});
		});
	</script>
</div>
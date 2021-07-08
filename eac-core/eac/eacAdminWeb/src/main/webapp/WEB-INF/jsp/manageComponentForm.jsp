<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<c:url value="/mvc/component/manage.htm" var="formUrl"/>
<c:url value="/images/add.png" var="addIconUrl"/>
<spring:message code="label.remove" var="labelRemove"/>
<spring:message code="label.edit" var="edit"/>
<form:form modelAttribute="componentBean" action="${formUrl}">
	<fieldset>
		<legend><spring:message code="label.componentSummary"/></legend>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.componentName"/>&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input path="selectedComponent.name" />
			</div>
			<spring:message code="info.component.name" var="componentNameHelp" />
			<img id="componentNameHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${componentNameHelp}"/>
			<script type="text/javascript">
				$("#componentNameHelp").tipTip({edgeOffset: 7, maxWidth: '400px'});
			</script>			
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.title"/>&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input path="selectedComponent.labelKey" id="titleKey" />
			</div>
		</div>
		<fieldset id="pageFieldsFieldset">
			<legend><spring:message code="label.fields"/></legend>
			<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a href="#" id="addFieldLink"><spring:message code="link.addField" /></a>
			<c:forEach var="field" items="${componentBean.newFields}" varStatus="status">
				<fieldset id="${field.id}" class="pageField newPageField" data-delete-id="${field.id}">
					<img class="scrollArrow" src="<c:url value="/images/arrow_scroll.jpg"/>"/>
					<div class="elementActions"><img class="actionImg removeNewField" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
					<div class="field">
						<div class="fieldLabel">
							<label><spring:message code="label.element"/></label>
						</div>
						<div class="fieldValue">
							<form:select path="newFields[${status.index}].element" items="${componentBean.elements}" itemValue="id" itemLabel="name" cssClass="selectedElement"/>
						</div>
					</div>
					<div class="field">
						<div class="fieldLabel">
							<label><spring:message code="label.defaultValue"/></label>
						</div>
						<div class="fieldValue">
							<form:input path="newFields[${status.index}].defaultValue" />
						</div>
					</div>
					<div class="field">
						<div class="fieldLabel">
							<label><spring:message code="label.required"/></label>
						</div>
						<div class="fieldValue">
							<form:checkbox path="newFields[${status.index}].required" cssClass="checkbox" />
						</div>
					</div>
					<form:hidden path="newFields[${status.index}].id"/>
				</fieldset>
			</c:forEach>
			<c:forEach var="field" items="${componentBean.selectedComponent.fields}" varStatus="status">
				<fieldset id="${field.id}" class="pageField" data-delete-id="${field.id}">
					<img class="scrollArrow" src="<c:url value="/images/arrow_scroll.jpg"/>"/>
					<div class="elementActions"><a class="actionLink" href="<c:url value="/mvc/element/manage.htm?id=${field.element.id}&deep=1"/>"><img class="actionImg" src="<c:url value="/images/pencil.png"/>" title="${edit}" alt="${edit}"/></a><img class="actionImg removeField" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
					<div class="field">
						<div class="fieldLabel">
							<label><spring:message code="label.element"/></label>
						</div>
						<div class="fieldValue">
							<form:select path="selectedComponent.fields[${status.index}].element" items="${componentBean.elements}" itemValue="id" itemLabel="name" cssClass="selectedElement"/>
						</div>
					</div>
					<div class="field">
						<div class="fieldLabel">
							<label><spring:message code="label.defaultValue"/></label>
						</div>
						<div class="fieldValue">
							<form:input path="selectedComponent.fields[${status.index}].defaultValue" />
						</div>
					</div>
					<div class="field">
						<div class="fieldLabel">
							<label><spring:message code="label.required"/></label>
						</div>
						<div class="fieldValue">
							<form:checkbox path="selectedComponent.fields[${status.index}].required" cssClass="checkbox" />
						</div>
					</div>
				</fieldset>
			</c:forEach>
		</fieldset>
	</fieldset>
    <div id="buttons" style="padding-bottom: 100px\9;">
    <security:authorize ifAllGranted="ROLE_MODIFY_COMPONENT">
    	<p>                 
    		<button type="button" eacLabel="${componentBean.selectedComponent.labelKey}" id="delete"><spring:message code="button.delete"/></button>
    		<button type="submit" id="save"><spring:message code="button.save" /></button>
    		<button type="button" id="cancel"><spring:message code="button.cancel"/></button>
        </p>
    </security:authorize>    
    </div>		
    <c:choose>
    	<c:when test="${componentBean.newComponent}">
		    <input type="hidden" name="id" value="new"/>
    	</c:when>
    	<c:otherwise>
		    <input type="hidden" name="id" value="${componentBean.selectedComponent.id}"/>
    	</c:otherwise>
    </c:choose>	
	<form:hidden id="fieldsToRemove" path="fieldsToRemove" />    
	<form:hidden id="seq" path="seq" />    
</form:form>

<spring:message code="confirm.title.delete.component" var="confirmDelete" text="" />

<div id="newPageFieldPrototype">
	<fieldset class="newPageField">
		<img class="scrollArrow" src="<c:url value="/images/arrow_scroll.jpg"/>"/>
		<div class="elementActions"><img class="actionImg removeNewField" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.element"/></label>
			</div>
			<div class="fieldValue">
				<select name="newFields[%idx%].element" class="selectedElement" disabled="disabled">
					<c:forEach var="element" items="${componentBean.elements}">
						<option value="${element.id}"><c:out value="${element.name}"/></option>
					</c:forEach> 
				</select>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.defaultValue"/></label>
			</div>
			<div class="fieldValue">
				<input type="text" name="newFields[%idx%].defaultValue" disabled="disabled" />
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.required"/></label>
			</div>
			<div class="fieldValue">
				<input type="checkbox" name="newFields[%idx%].required" class="checkbox" />
			</div>
		</div>
		<input type="hidden" name="newFields[%idx%].id"/>
	</fieldset>
</div>

<script type="text/javascript">
	$(function() {
		var $form = $('form');
		
		var submitForm = function() {
			$form.append('<input type="hidden" name="delete" value="1" />');
			$form.submit();
		};			
	
		var $deleteButton = $('#delete');
		var clickHandler = eacConfirm({callbackYes:submitForm, title:'${confirmDelete}'});
		$deleteButton.click(clickHandler);
		
		$deleteButton.hide();
		var isComponentSelected = '${not empty componentBean.selectedComponent}';
		var isDeletable = ${componentBean.selectedComponentDeletable};
		if (isComponentSelected && isDeletable) {
			$deleteButton.show();
		}
		var isNewComponent = ${componentBean.newComponent};
		if (isNewComponent) {
			$deleteButton.hide();
		}
		
		$('#cancel').click(function() {
			$('#selectedComponent').change();
		});		
		
		var $pageFieldsFieldset = $('#pageFieldsFieldset');
		
		$form.submit(function() {
			var newFieldCount = ${fn:length(componentBean.newFields)};
			$(this).find('.newPageField').each(function() {
				$(this).find('input,select').each(function() {
					var name = $(this).attr('name');
					$(this).attr('name', name.replace(/%idx%/, newFieldCount));
				});
				$(this).find('input[type="hidden"]').val(newFieldCount);
				$(this).attr('id', newFieldCount);
				newFieldCount++;
			});
			$('#seq').val($pageFieldsFieldset.sortable('toArray'));
		});
		
		var $scrollArrow = $('.scrollArrow');
		
		$pageFieldsFieldset.sortable({ 
									items: '> fieldset',
									revert: false,
									start: function(e, ui) {
										ui.placeholder.height(ui.item.height());
									},
									deactivate: function() {
										$scrollArrow.hide();
									},
									opacity: 0.6,
									scroll: false,
									tolerance: 'pointer'
		});
		
		$pageFieldsFieldset.sortable('disable');
		$scrollArrow.hide();
		
		
		$form.delegate('.pageField', 'mouseover', function(e) {
			var $this = $(this);
			var $thisScrollArrow = $this.find('.scrollArrow');
			$thisScrollArrow.show();
			$this.css('cursor', 'move');
			$pageFieldsFieldset.sortable('enable');
		});
		
		$form.delegate('.pageField', 'mouseout', function(e) {
			var $this = $(this);
			var $thisScrollArrow = $this.find('.scrollArrow');
			$thisScrollArrow.hide();
			$this.css('cursor', '');
			$pageFieldsFieldset.sortable('disable');
		});		
		
		var $newPageField = $('#newPageFieldPrototype fieldset');
		$newPageField.hide();
		
		var newCount = 0;
		$('#addFieldLink').click(function() {
			var $clonedPageField = $newPageField.clone();
			$clonedPageField.find('input,select').each(function() {
				$(this).removeAttr('disabled');
			});
			var $existingFields = $(this).siblings('.pageField');
			if ($existingFields.length > 0) {
				$clonedPageField.insertBefore('.pageField:first');
			} else {
				$pageFieldsFieldset.append($clonedPageField);
			}
			$clonedPageField.addClass('pageField');
			$clonedPageField.show();
			$clonedPageField.find('select').focus();
			$pageFieldsFieldset.sortable('refresh');
			newCount++;
			return false;
		});
		
		function sortFields() {
			var $existingFields = $pageFieldsFieldset.find('.pageField');
			var seq = $('#seq').val();
			if (seq) {
				$pageFieldsFieldset.remove('.pageField');
				var orderedSeqKeys = seq.split(',');
				for (var i = 0; i < orderedSeqKeys.length; i++) {
					var id = orderedSeqKeys[i];
					$existingFields.each(function() {
						if ($(this).attr('id') == id) {
							$pageFieldsFieldset.append($(this));
						}
					});
				}
			}
		}
		
		sortFields();
		
		var fieldsToRemove = '';
		$('.removeField').click(function() {
			var $fieldset = $(this).closest('fieldset');
			var $fieldsToRemove = $('#fieldsToRemove');
			fieldsToRemove = $fieldsToRemove.val();
			if (fieldsToRemove != '') {
				fieldsToRemove += ',';
			}
			fieldsToRemove += $fieldset.attr('data-delete-id');
			$fieldsToRemove.val(fieldsToRemove);
			$fieldset.hide();
		});
		
		$form.delegate('.removeNewField', 'click', function() {
			$(this).closest('fieldset').remove();
		});
		
		addAutocompleteMessageTo('#titleKey');
		
		<c:if test="${not empty componentBean.selectedComponent.pageComponents}">
			createLinkedIcon('form > fieldset', '${fn:replace(componentBean.selectedComponent.name, "'", "\\'")}', [<c:forEach items="${componentBean.selectedComponent.pageComponents}" var="pageComponent" varStatus="status">'${fn:replace(pageComponent.pageDefinition.name, "'", "\\'")}',</c:forEach>], ${empty param['statusMessageKey']});
		</c:if>
	});
</script>
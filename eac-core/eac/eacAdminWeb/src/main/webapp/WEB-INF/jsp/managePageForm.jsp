<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<c:url value="/mvc/page/manage.htm" var="formUrl"/>
<c:url value="/images/add.png" var="addIconUrl"/>
<spring:message code="label.remove" var="labelRemove"/>
<spring:message code="label.edit" var="edit"/>
<form:form modelAttribute="pageDefinitionBean" action="${formUrl}">
	<fieldset>
		<legend><spring:message code="label.pageSummary" text="Page Summary"/></legend>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.pageName"/>&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input path="selectedPageDefinition.name" />
			</div>
			<spring:message code="info.page.name" var="pageNameHelp" />
			<img id="pageNameHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${pageNameHelp}"/>
			<script type="text/javascript">
				$("#pageNameHelp").tipTip({edgeOffset: 7, maxWidth: '400px'});
			</script>				
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.title"/></label>
			</div>
			<div class="fieldValue">
				<form:input path="selectedPageDefinition.title" id="titleKey" />
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.type"/></span></label>
			</div>
			<spring:message code="label.pageDefinition.${pageDefinitionBean.selectedPageDefinition.pageDefinitionType}" var="pageDefTypeLabel" text=""/>
			<div class="fieldValue"><c:out value="${pageDefTypeLabel}"/></div>
		</div>
		<div class="field">
			<div class="fieldLabel">
			<label><spring:message code="label.division"/></label>
			</div>
			<div class="fieldValue">
				<form:select path="selectedPageDefinition.division" items="${pageDefinitionBean.divisions}" itemValue="erightsId" itemLabel="divisionType" />
			</div>
		</div>		
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.preamble"/></label>
			</div>
			<div class="fieldValue">
				<form:input path="selectedPageDefinition.preamble" id="preambleKey" />
			</div>
		</div>
		<fieldset id="componentsFieldset">
			<legend><spring:message code="label.components"/></legend>
			<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a href="#" id="addComponentLink"><spring:message code="link.addComponent" /></a>
			<c:forEach var="component" items="${pageDefinitionBean.newComponents}" varStatus="status">
				<fieldset id="${component.id}" class="component newComponent" data-delete-id="${component.id}">
					<img class="scrollArrow" src="<c:url value="/images/arrow_scroll.jpg"/>"/>
					<div class="componentActions"><img class="actionImg removeComponent" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
					<form:select path="newComponents[${status.index}].component" items="${pageDefinitionBean.components}" itemValue="id" itemLabel="name" />
					<form:hidden path="newComponents[${status.index}].id"/>
				</fieldset>
			</c:forEach>
			<c:forEach var="component" items="${pageDefinitionBean.selectedPageDefinition.pageComponents}" varStatus="status">
				<fieldset id="${component.id}" class="component" data-delete-id="${component.id}">
					<img class="scrollArrow" src="<c:url value="/images/arrow_scroll.jpg"/>"/>
					<div class="componentActions"><a class="actionLink editComponentLink" href="<c:url value="/mvc/component/manage.htm?id=${component.component.id}&deep=1"/>"><img class="actionImg" src="<c:url value="/images/pencil.png"/>" title="${edit}" alt="${edit}"/></a><img class="actionImg removeComponent" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
					<form:select path="selectedPageDefinition.pageComponents[${status.index}].component" items="${pageDefinitionBean.components}" itemValue="id" itemLabel="name" />
				</fieldset>
			</c:forEach>
		</fieldset>
	</fieldset>
    <div id="buttons" style="padding-bottom: 100px\9;">
    	<p>                 
    		<button type="button" eacLabel="${pageDefinitionBean.selectedPageDefinition.name}" id="delete"><spring:message code="button.delete"/></button>
    		<button type="submit" id="save"><spring:message code="button.save" /></button>
    		<button type="button" id="cancel"><spring:message code="button.cancel"/></button>
        </p>
    </div>		
    <c:choose>
    	<c:when test="${pageDefinitionBean.newAccountPageDefinition}">
		    <input type="hidden" name="id" value="newAccountPage"/>
    	</c:when>
    	<c:when test="${pageDefinitionBean.newProductPageDefinition}">
		    <input type="hidden" name="id" value="newProductPage"/>
    	</c:when>
    	<c:otherwise>
		    <input type="hidden" name="id" value="${pageDefinitionBean.selectedPageDefinition.id}"/>
    	</c:otherwise>
    </c:choose>	
	<form:hidden id="componentsToRemove" path="componentsToRemove" />    
	<form:hidden id="seq" path="seq" />   	
</form:form>

<spring:message code="confirm.title.delete.page" var="confirmDelete" text="" />

<div id="newComponentPrototype">
	<fieldset class="newComponent">
		<img class="scrollArrow" src="<c:url value="/images/arrow_scroll.jpg"/>"/>
		<div class="componentActions"><img class="actionImg removeNewComponent" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
		<select name="newComponents[%idx%].component" disabled="disabled">
			<c:forEach var="component" items="${pageDefinitionBean.components}">
				<option value="${component.id}"><c:out value="${component.name}"/></option>
			</c:forEach>
		</select>
		<input type="hidden" name="newComponents[%idx%].id"/>
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
		var isPageDefinitionSelected = '${not empty pageDefinitionBean.selectedPageDefinition}';
		var isDeletable = ${pageDefinitionBean.selectedPageDefinitionDeletable};
		if (isPageDefinitionSelected && isDeletable) {
			$deleteButton.show();
		}
		var isNewPageDefinition = ${pageDefinitionBean.newPageDefinition};
		if (isNewPageDefinition) {
			$deleteButton.hide();
		}
		
		$('#cancel').click(function() {
			$('#selectedPageDefinition').change();
		});		
		
		var $componentsFieldset = $('#componentsFieldset');
		
		$form.submit(function() {
			var newComponentCount = ${fn:length(pageDefinitionBean.newComponents)};
			$(this).find('.newComponent').each(function() {
				$(this).find('select,input').each(function() {
					var name = $(this).attr('name');
					$(this).attr('name', name.replace(/%idx%/, newComponentCount));
				});
				$(this).find('input[type="hidden"]').val(newComponentCount);
				$(this).attr('id', newComponentCount);
				newComponentCount++;
			});
			$('#seq').val($componentsFieldset.sortable('toArray'));
		});
		
		var $scrollArrow = $('.scrollArrow');
		
		$componentsFieldset.sortable({ 
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
		
		$componentsFieldset.sortable('disable');
		$scrollArrow.hide();
		
		$form.delegate('.component', 'mouseover', function(e) {
			var $this = $(this);
			var $thisScrollArrow = $this.find('.scrollArrow');
			$thisScrollArrow.show();
			$this.css('cursor', 'move');
			$componentsFieldset.sortable('enable');
		});
		
		$form.delegate('.component', 'mouseout', function(e) {
			var $this = $(this);
			var $thisScrollArrow = $this.find('.scrollArrow');
			$thisScrollArrow.hide();
			$this.css('cursor', '');
			$componentsFieldset.sortable('disable');
		});		
		
		var $newComponent = $('#newComponentPrototype fieldset');
		$newComponent.hide();
		
		var newCount = 0;
		$('#addComponentLink').click(function() {
			var $clonedComponent = $newComponent.clone();
			$clonedComponent.find('select').each(function() {
				$(this).removeAttr('disabled');
			});
			var $existingComponents = $(this).siblings('.component');
			if ($existingComponents.length > 0) {
				$clonedComponent.insertBefore('.component:first');
			} else {
				$componentsFieldset.append($clonedComponent);
			}
			$clonedComponent.addClass('component');
			$clonedComponent.show();
			$clonedComponent.find('select').focus();
			$clonedComponent.sortable('refresh');
			newCount++;
			return false;
		});
		
		function sortComponents() {
			var $existingComponents = $componentsFieldset.find('.component');
			var seq = $('#seq').val();
			if (seq) {
				$existingComponents.remove('.component');
				var orderedSeqKeys = seq.split(',');
				for (var i = 0; i < orderedSeqKeys.length; i++) {
					var id = orderedSeqKeys[i];
					$existingComponents.each(function() {
						if ($(this).attr('id') == id) {
							$componentsFieldset.append($(this));
						}
					});
				}
			}
		}
		
		sortComponents();
		
		var componentsToRemove = '';
		$('.removeComponent').click(function() {
			var $fieldset = $(this).closest('fieldset');
			var $componentsToRemove = $('#componentsToRemove');
			componentsToRemove = $componentsToRemove.val();
			if (componentsToRemove != '') {
				componentsToRemove += ',';
			}
			componentsToRemove += $fieldset.attr('data-delete-id');
			$componentsToRemove.val(componentsToRemove);
			$fieldset.find('select').each(function() {
				$(this).attr('disabled', 'disabled');
			});
			$fieldset.hide();
		});
		
		$form.delegate('.removeNewComponent', 'click', function() {
			$(this).closest('fieldset').remove();
		});
		
		addAutocompleteMessageTo('#titleKey');
		addAutocompleteMessageTo('#preambleKey');
		
		<c:if test="${not empty pageDefinitionBean.selectedPageDefinition.registrationDefinitions}">
			createLinkedIcon('form > fieldset', '${pageDefinitionBean.selectedPageDefinition.name}', [<c:forEach items="${pageDefinitionBean.selectedPageDefinition.registrationDefinitions}" var="registrationDefinition" varStatus="status">'${fn:replace(registrationDefinition.product.productName, "'", "\\'")}',</c:forEach>], ${empty param['statusMessageKey']});
		</c:if>
		
	});
</script>
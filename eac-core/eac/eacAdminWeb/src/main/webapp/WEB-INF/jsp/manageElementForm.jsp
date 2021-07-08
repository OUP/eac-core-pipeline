<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<c:url value="/mvc/element/manage.htm" var="formUrl"/>
<c:url value="/images/add.png" var="addIconUrl"/>
<spring:message code="label.remove" var="labelRemove"/>
<spring:message code="label.edit" var="edit"/>
<form:form modelAttribute="elementBean" action="${formUrl}">
	<fieldset>
		<legend><spring:message code="label.elementSummary"/></legend>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.elementName"/>&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input path="selectedElement.name" />
			</div>
			<spring:message code="info.element.name" var="elementNameHelp" />
			<img id="elementNameHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${elementNameHelp}"/>
			<script type="text/javascript">
				$("#elementNameHelp").tipTip({edgeOffset: 7, maxWidth: '400px'});
			</script>				
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.title"/>&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input path="selectedElement.titleText" id="titleKey" />
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.question"/></label>
			</div>
			<div class="fieldValue" style="max-width: 40em">
				<form:select path="selectedElement.question" id="selectedQuestion">
					<c:forEach var="question" items="${elementBean.questions}">
						<spring:message code="${question.elementText}" var="title" text=""/>
						<form:option value="${question.id}" label="${question.displayName}" title="${title}"/>
					</c:forEach>
				</form:select>
				<a class="actionLink" href="<c:url value="/mvc/question/manage.htm?id=${elementBean.selectedElement.question.id}&deep=1"/>"><img class="actionImg" src="<c:url value="/images/pencil.png"/>" title="${edit}" alt="${edit}"/></a>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.helpText"/></label>
			</div>
			<div class="fieldValue">
				<form:input id="helpText" path="selectedElement.helpText" />
			</div>
		</div>		
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.validation"/></label>
			</div>
			<div class="fieldValue" style="max-width: 45em">
				<form:input id="validation" path="selectedElement.regularExpression" cssClass="wide"/>
				<c:url value="/images/tick.png" var="tickIcon"/>
				<spring:message code="label.regexValid" var="validRegexLabel" text=""/>
				<img id="tickIcon" src="${tickIcon}" width="16px" height="16px" style="padding-left: 3px; vertical-align: middle; vertical-align: bottom\9; display: none" title="${validRegexLabel}"/>	
				<c:url value="/images/cross.jpg" var="warningIcon"/>
				<spring:message code="label.regexInvalid" var="invalidRegexLabel" text=""/>		
				<img id="warningIcon" src="${warningIcon}" width="16px" height="16px" style="padding-left: 3px; vertical-align: middle; vertical-align: bottom\9; display: none" title="${invalidRegexLabel}"/>	
			</div>
		</div>			
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.displayType"/></label>
			</div>
			<div class="fieldValue">
				<form:select path="tagType" id="tagType">
					<c:forEach items="${displayTypes}" var="tagType">
						<spring:message code="label.displayType.${tagType}" var="tagTypeLabel"/>
						<form:option value="${tagType}" label="${tagTypeLabel}"/>
					</c:forEach>
				</form:select>
			</div>
		</div>
		<fieldset id="urlLinkFieldset" style="margin-top: 10px; margin-top: 0px\9; margin-bottom: 20px; padding-top: 0px\9">
			<legend><spring:message code="label.urlLink"/></legend>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.url"/>&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:input id="url" path="url" cssClass="wide"/>
				</div>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.newWindow"/></label>
				</div>
				<div class="fieldValue">
					<form:checkbox id="newWindow" path="newWindow" cssClass="checkbox"/>
				</div>
			</div>
		</fieldset>
		<fieldset id="multiOptionFieldset" style="margin-top: 10px; margin-top: 0px\9; margin-bottom: 20px; padding-top: 0px\9">
			<div id="emptyOption" class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.emptyOption" text="Empty Option"/></label>
				</div>
				<div class="fieldValue">
					<form:checkbox path="emptyOption" cssClass="checkbox"/>
				</div>
				<spring:message code="info.element.emptyOption" var="emptyOptionHelp" />
				<img id="emptyOptionHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${emptyOptionHelp}"/>
				<script type="text/javascript">
					$("#emptyOptionHelp").tipTip({edgeOffset: 7});
				</script>				
			</div>			
			<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a href="#" id="addOptionLink"><spring:message code="link.addOption" /></a>
			<c:forEach items="${elementBean.newOptions}" var="option" varStatus="status">
				<fieldset id="${option.id}" class="option newOption" data-delete-id="${option.id}">
					<img class="scrollArrow" src="<c:url value="/images/arrow_scroll.jpg"/>"/>
					<div class="removeNewOption"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
					<div class="field">
						<div class="fieldLabel">
							<label><spring:message code="label.label"/>&#160;<span class="mandatory">*</span></label>
						</div>
						<div class="fieldValue">
							<form:input path="newOptions[${status.index}].label" cssClass="optionLabel" />
						</div>
					</div>
					<div class="field">
						<div class="fieldLabel">
							<label><spring:message code="label.value"/>&#160;<span class="mandatory">*</span></label>
						</div>
						<div class="fieldValue">
							<form:input path="newOptions[${status.index}].value" cssClass="optionValue" />
						</div>
					</div>
					<form:hidden path="newOptions[${status.index}].id"/>
				</fieldset>
			</c:forEach>
			<c:if test="${elementBean.selectedElementReferencingOptionsTag}">
				<c:forEach items="${elementBean.selectedElement.tag.optionsAsList}" var="option" varStatus="status">
					<fieldset id="${option.id}" class="option" data-delete-id="${option.id}">
						<img class="scrollArrow" src="<c:url value="/images/arrow_scroll.jpg"/>"/>
						<div class="removeOption"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
						<div class="field">
							<div class="fieldLabel">
								<label><spring:message code="label.label"/>&#160;<span class="mandatory">*</span></label>
							</div>
							<div class="fieldValue">
								<form:input path="selectedElement.tag.optionsAsList[${status.index}].label" cssClass="optionLabel" />
							</div>
						</div>
						<div class="field">
							<div class="fieldLabel">
								<label><spring:message code="label.value"/>&#160;<span class="mandatory">*</span></label>
							</div>
							<div class="fieldValue">
								<form:input path="selectedElement.tag.optionsAsList[${status.index}].value" />
							</div>
						</div>
					</fieldset>
				</c:forEach>
			</c:if>
		</fieldset>
		<fieldset style="margin-top: 20px; margin-top: 0px\9; padding-top: 0px\9;">
			<legend><spring:message code="label.restrictedTo"/></legend>
			<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a href="#" id="addRestrictionLink"><spring:message code="link.addRestriction" /></a>
			<c:forEach items="${elementBean.newRestrictions}" var="restriction" varStatus="status">
				<fieldset class="newType restriction">
					<label style="margin-right: 10px"><spring:message code="label.language"/>&#160;<span class="mandatory">*</span></label><form:input path="newRestrictions[${status.index}].language" maxlength="2" cssClass="locale language"/>
					<label style="margin-right: 10px"><spring:message code="label.country"/></label><form:input path="newRestrictions[${status.index}].country" maxlength="2" cssClass="locale country"/>
					<div class="removeNewRestriction" style="float: right; cursor: pointer"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
				</fieldset>
			</c:forEach>
			<c:forEach items="${elementBean.existingRestrictions}" var="restriction" varStatus="status">
				<fieldset class="restriction" id="${restriction.id}">
					<div style="float: left">
						<label style="margin-right: 10px"><spring:message code="label.language"/>&#160;<span class="mandatory">*</span></label><form:input path="existingRestrictions[${status.index}].language" maxlength="2" cssClass="locale language"/>
						<label style="margin-right: 10px"><spring:message code="label.country"/></label><form:input path="existingRestrictions[${status.index}].country" maxlength="2" cssClass="locale country"/>
					</div>
					<div class="removeRestriction"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
				</fieldset>
			</c:forEach>			
		</fieldset>		
	</fieldset>
    <div id="buttons" style="padding-bottom: 100px\9;">
    <security:authorize ifAllGranted="ROLE_MODIFY_COMPONENT">
    	<p>                 
    		<button type="button" eacLabel="${elementBean.selectedElement.titleText}" id="delete"><spring:message code="button.delete"/></button>
    		<button type="submit" id="save"><spring:message code="button.save" /></button>
    		<button type="button" id="cancel"><spring:message code="button.cancel"/></button>
        </p>
    </security:authorize>    
    </div>		
    <c:choose>
    	<c:when test="${elementBean.newElement}">
		    <input type="hidden" name="id" value="new"/>
    	</c:when>
    	<c:otherwise>
		    <input type="hidden" name="id" value="${elementBean.selectedElement.id}"/>
    	</c:otherwise>
    </c:choose>	
	<form:hidden id="restrictionsToRemove" path="restrictionsToRemove" />    
	<form:hidden id="optionsToRemove" path="optionsToRemove" />    
	<form:hidden id="seq" path="seq" />    
</form:form>

<spring:message code="confirm.title.delete.element" var="confirmDelete" text="" />

<div id="newTypePrototype">
	<fieldset class="newType">
		<label style="margin-right: 10px"><spring:message code="label.language"/>&#160;<span class="mandatory">*</span></label><input type="text" name="newRestrictions[%idx%].language" maxlength="2" class="locale language" disabled="disabled"/>
		<label style="margin-right: 10px"><spring:message code="label.country"/></label><input type="text" name="newRestrictions[%idx%].country" maxlength="2" class="locale country" disabled="disabled"/>
		<div class="removeNewRestriction"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
	</fieldset>
</div>

<div id="newOptionPrototype">
	<fieldset class="newOption">
		<img class="scrollArrow" src="<c:url value="/images/arrow_scroll.jpg"/>"/>
		<div class="removeNewOption"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.label"/>&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<input class="optionLabel" type="text" name="newOptions[%idx%].label" disabled="disabled" />
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.value"/>&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<input class="optionValue" type="text" name="newOptions[%idx%].value" disabled="disabled" />
			</div>
		</div>
		<input type="hidden" name="newOptions[%idx%].id"/>
	</fieldset>
</div>

<script type="text/javascript">
	$(function() {
		var $validation = $('#validation');
		var $tickIcon = $('#tickIcon');
		var $warningIcon = $('#warningIcon');
		var $form = $('form');
		
		checkRegexValidity();
		
		$validation.bind("keyup", function() {
			if ($.trim($validation.val())) {
				checkRegexValidity();
			} else {
				$tickIcon.hide();
				$warningIcon.hide()
			}
		});
		
		function checkRegexValidity() {
			if ($.trim($validation.val())) {
				$.ajax({
					url: '<c:url value="/mvc/element/manage.htm"/>',
					type: 'POST',
					dataType: 'json',
					data: 'regex=' + encodeURIComponent($validation.val())
				}).done(function(response) {
					if (response.valid == 'true') {
						$tickIcon.show();
						$warningIcon.hide();
					} else if (response.valid == 'false') {
						$tickIcon.hide();
						$warningIcon.show();
					}
				});
			}
		}
		
		var submitForm = function() {
			$form.append('<input type="hidden" name="delete" value="1" />');
			$form.submit();
		};			
	
		var $deleteButton = $('#delete');
		var clickHandler = eacConfirm({callbackYes:submitForm, title:'${confirmDelete}'});
		$deleteButton.click(clickHandler);
		
		$deleteButton.hide();
		var isElementSelected = '${not empty elementBean.selectedElement}';
		var isDeletable = ${elementBean.selectedElementDeletable};
		if (isElementSelected && isDeletable) {
			$deleteButton.show();
		}
		var isNewElement = ${elementBean.newElement};
		if (isNewElement) {
			$deleteButton.hide();
		}
		
		$('#cancel').click(function() {
			$('#selectedElement').change();
		});
		
		var restrictionsToRemove = '';
		$('.removeRestriction').click(function() {
			var $fieldset = $(this).closest('fieldset');
			var $restrictionsToRemove = $('#restrictionsToRemove');
			restrictionsToRemove = $restrictionsToRemove.val();
			if (restrictionsToRemove != '') {
				restrictionsToRemove += ',';
			}
			restrictionsToRemove += $fieldset.attr('id');
			$restrictionsToRemove.val(restrictionsToRemove);
			$fieldset.find('input').each(function() {
				$(this).attr('disabled', 'disabled');
			});
			$fieldset.hide();
		});
		
		$form.delegate('.removeNewRestriction', 'click', function() {
			$(this).closest('fieldset').remove();
		});
		
		var $newTypeFieldset = $('#newTypePrototype fieldset');
		$newTypeFieldset.hide();
		
		$('#addRestrictionLink').click(function() {
			var $clonedTypeFieldset = $newTypeFieldset.clone();
			$clonedTypeFieldset.find('input').each(function() {
				$(this).removeAttr('disabled');
			});
			var $existingRestrictions = $(this).siblings('.restriction');
			if ($existingRestrictions.length > 0) {
				$clonedTypeFieldset.insertBefore('.restriction:first');
			} else {
				$(this).after($clonedTypeFieldset);
			}
			$clonedTypeFieldset.addClass('restriction');
			$clonedTypeFieldset.show();
			$clonedTypeFieldset.find('input:first').focus();
			return false;
		});
		
		var $tagType = $('#tagType');
		var $urllinkFieldset = $('#urlLinkFieldset');
		var $multiOptionFieldset = $('#multiOptionFieldset');
		$tagType.change(function() {
			var val = $(this).val();
			if (val == 'URLLINK') {
				$urllinkFieldset.show();
				$multiOptionFieldset.hide();
			} else if (val == 'RADIO' || val == 'SELECT' || val == 'MULTISELECT') {
				var $emptyOption = $('#emptyOption');
				if (val == 'SELECT') {
					$multiOptionFieldset.find('legend').remove();
					$multiOptionFieldset.prepend('<legend><spring:message code="label.select"/></legend>')
					$emptyOption.show();
				} else if (val == 'RADIO') {
					$multiOptionFieldset.find('legend').remove();
					$multiOptionFieldset.prepend('<legend><spring:message code="label.radio"/></legend>')
					$emptyOption.hide();
				} else {
					$multiOptionFieldset.find('legend').remove();
					$multiOptionFieldset.prepend('<legend><spring:message code="label.multiSelect"/></legend>')
					$emptyOption.hide();
				}
				$multiOptionFieldset.show();
				$urllinkFieldset.hide();
			} else {
				$multiOptionFieldset.hide();
				$urllinkFieldset.hide();
			}
		}).keypress(function() {
			$(this).trigger('change');
		});
		
		$tagType.change();
		
		$form.delegate('.language', 'keyup', function() {
			$(this).val($(this).val().toLowerCase());
		});
		
		$form.delegate('.country', 'keyup', function() {
			$(this).val($(this).val().toUpperCase());
		});
		
		var $multiOptionFieldset = $('#multiOptionFieldset');
		
		$form.submit(function() {
			var newTypeCount = ${fn:length(elementBean.newRestrictions)};
			$(this).find('.newType').each(function() {
				$(this).find('input').each(function() {
					var name = $(this).attr('name');
					$(this).attr('name', name.replace(/%idx%/, newTypeCount));
				});
				newTypeCount++;
			});
			var newOptionCount = ${fn:length(elementBean.newOptions)};
			$(this).find('.newOption').each(function() {
				$(this).find('input').each(function() {
					var name = $(this).attr('name');
					$(this).attr('name', name.replace(/%idx%/, newOptionCount));
				});
				$(this).find('input[type="hidden"]').val(newOptionCount);
				$(this).attr('id', newOptionCount);
				newOptionCount++;
			});
			$('#seq').val($multiOptionFieldset.sortable('toArray'));
		});
		
		var $scrollArrow = $('.scrollArrow');
		
		$multiOptionFieldset.sortable({ 
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
		
		$multiOptionFieldset.sortable('disable');
		$scrollArrow.hide();
		
		$form.delegate('.option', 'mouseover', function(e) {
			var $this = $(this);
			var $thisScrollArrow = $this.find('.scrollArrow');
			$thisScrollArrow.show();
			$this.css('cursor', 'move');
			$multiOptionFieldset.sortable('enable');
		});
		
		$form.delegate('.option', 'mouseout', function(e) {
			var $this = $(this);
			var $thisScrollArrow = $this.find('.scrollArrow');
			$thisScrollArrow.hide();
			$this.css('cursor', '');
			$multiOptionFieldset.sortable('disable');
		});
		
		var $newOptionFieldset = $('#newOptionPrototype fieldset');
		$newOptionFieldset.hide();
		
		var newCount = 0;
		$('#addOptionLink').click(function() {
			var $clonedOptionFieldset = $newOptionFieldset.clone();
			$clonedOptionFieldset.find('input,hidden').each(function() {
				$(this).removeAttr('disabled');
			});
			var $existingOptions = $(this).siblings('.option');
			if ($existingOptions.length > 0) {
				$clonedOptionFieldset.insertBefore('.option:first');
			} else {
				$(this).after($clonedOptionFieldset);
			}
			$clonedOptionFieldset.addClass('option');
			$clonedOptionFieldset.show();
			$clonedOptionFieldset.find('input:first').focus();
			addAutocompleteMessageTo($clonedOptionFieldset.find('.optionLabel'));
			$multiOptionFieldset.sortable('refresh');
			newCount++;
			return false;
		});
		
		function sortOptions() {
			var $existingOptions = $multiOptionFieldset.find('.option');
			var seq = $('#seq').val();
			if (seq) {
				$multiOptionFieldset.remove('.option');
				var orderedSeqKeys = seq.split(',');
				for (var i = 0; i < orderedSeqKeys.length; i++) {
					var id = orderedSeqKeys[i];
					$existingOptions.each(function() {
						if ($(this).attr('id') == id) {
							$multiOptionFieldset.append($(this));
						}
					});
				}
			}
		}
		
		sortOptions();
		
		var optionsToRemove = '';
		$('.removeOption').click(function() {
			var $fieldset = $(this).closest('fieldset');
			var $optionsToRemove = $('#optionsToRemove');
			optionsToRemove = $optionsToRemove.val();
			if (optionsToRemove != '') {
				optionsToRemove += ',';
			}
			optionsToRemove += $fieldset.attr('data-delete-id');
			$optionsToRemove.val(optionsToRemove);
			$fieldset.find('input').each(function() {
				$(this).attr('disabled', 'disabled');
			});
			$fieldset.hide();
		});
		
		$form.delegate('.removeNewOption', 'click', function() {
			$(this).closest('fieldset').remove();
		});
		
		addAutocompleteMessageTo('#titleKey');
		addAutocompleteMessageTo('#helpText');
		addAutocompleteMessageTo('.optionLabel');
		
		<c:if test="${not empty elementBean.selectedElement.fields}">
			createLinkedIcon('form > fieldset', '${fn:replace(elementBean.selectedElement.name, "'", "\\'")}', [<c:forEach items="${elementBean.selectedElement.fields}" var="field" varStatus="status">'${fn:replace(field.component.name, "'", "\\'")}',</c:forEach>], ${empty param['statusMessageKey']});
		</c:if>
		
		<spring:message code="${elementBean.selectedElement.question.elementText}" text="" var="questionTitle"/>
		$('#selectedQuestion').attr('title', '${fn:replace(questionTitle, "'", "\\'")}');
	});
</script>
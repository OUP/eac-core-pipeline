<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<c:url value="/mvc/question/manage.htm" var="formUrl"/>
<c:url value="/images/add.png" var="addIconUrl"/>
<spring:message code="label.remove" var="labelRemove"/>
<form:form modelAttribute="questionBean" action="${formUrl}">
	<fieldset>
		<legend><spring:message code="label.questionSummary"/></legend>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.name"/>&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input id="name" path="selectedQuestion.description" />
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.elementMessage"/>&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input id="elementMessage" path="selectedQuestion.elementText" />
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="productSpecific"><spring:message code="label.productSpecific"/></label>
			</div>
			<div class="fieldValue">
				<form:checkbox id="productSpecific" path="selectedQuestion.productSpecific" cssClass="checkbox" />
			</div>
		</div>
		<fieldset style="margin-top: 20px; margin-top: 0px\9">
			<legend><spring:message code="label.exportNames"/></legend>
			<div>
				<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a href="#" id="addExportNameLink"><spring:message code="link.addExportName" /></a>
			</div>	
			<c:forEach items="${questionBean.newExportNames}" var="exportName" varStatus="status">
				<fieldset class="newType exportName">
					<div class="field">
						<div class="fieldLabel">
							<label><spring:message code="label.exportAs" />&#160;<span class="mandatory">*</span></label>
						</div>
						<div class="fieldValue">
							<form:input path="newExportNames[${status.index}].name"/>
						</div>
						<div class="removeNewExportName" style="float: right; cursor: pointer"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
					</div>	
					<div class="field">
						<div class="fieldLabel">
							<label><spring:message code="label.exportType"/></label>
						</div>
						<div class="fieldValue">
							<form:select path="newExportNames[${status.index}].exportType">
								<c:forEach items="${exportTypes}" var="exportType">
									<form:option value="${exportType}" label="${exportType}"/>
								</c:forEach>
							</form:select>	
						</div>
					</div>
				</fieldset>
			</c:forEach>
			<c:forEach items="${questionBean.selectedQuestion.exportNamesAsList}" var="exportName" varStatus="status">
				<fieldset class="exportName" id="${exportName.id}">
					<div class="field">
						<div class="fieldLabel">
							<label><spring:message code="label.exportAs"/>&#160;<span class="mandatory">*</span></label>
						</div>
						<div class="fieldValue">
							<form:input id="exportName" path="selectedQuestion.exportNamesAsList[${status.index}].name" />
						</div>
						<div class="removeExportName" style="float: right; cursor: pointer"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>				
					</div>
					<div class="field">
						<div class="fieldLabel">
							<label><spring:message code="label.exportType"/></label>
						</div>
						<div class="fieldValue">
							<form:select id="exportType" path="selectedQuestion.exportNamesAsList[${status.index}].exportType" >
								<c:forEach items="${exportTypes}" var="exportType">
									<form:option value="${exportType}" label="${exportType}"/>
								</c:forEach>
							</form:select>
						</div>
					</div>
				</fieldset>
			</c:forEach>			
		</fieldset>
	</fieldset>
    <div id="buttons">
    <security:authorize ifAllGranted="ROLE_MODIFY_COMPONENT">
    	<p>                 
    		<button type="button" eacLabel="${questionBean.selectedQuestion.description}" id="delete"><spring:message code="button.delete"/></button>
    		<button type="submit" id="save"><spring:message code="button.save" /></button>
    		<button type="button" id="cancel"><spring:message code="button.cancel"/></button>
        </p>
    </security:authorize>     
    </div>		
    <c:choose>
    	<c:when test="${questionBean.newQuestion}">
		    <input type="hidden" name="id" value="new"/>
    	</c:when>
    	<c:otherwise>
		    <input type="hidden" name="id" value="${questionBean.selectedQuestion.id}"/>
    	</c:otherwise>
    </c:choose>
    <form:hidden id="exportNamesToRemove" path="exportNamesToRemove" />    
</form:form>
<spring:message code="confirm.title.delete.question" var="confirmDelete" text="" />

<div id="newTypePrototype">
	<fieldset class="newType">
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.exportAs" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<input type="text" name="newExportNames[%idx%].name" disabled="disabled"/>		
			</div>
			<div class="removeNewExportName"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
		</div>	
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.exportType"/></label>
			</div>
			<div class="fieldValue">
				<select name="newExportNames[%idx%].exportType" disabled="disabled">
					<c:forEach items="${exportTypes}" var="exportType">
						<option value="${exportType}"><c:out value="${exportType}"/></option>
					</c:forEach>
				</select>	
			</div>
		</div>
	</fieldset>
</div>
	
<script type="text/javascript">
	$(function() {
		var $newTypeFieldset = $('#newTypePrototype fieldset');
		var $deleteButton = $('#delete');
		var $form = $('form');
		$newTypeFieldset.hide();

		var submitForm = function() {
			$form.append('<input type="hidden" name="delete" value="1" />');
			$form.submit();
		};			
		
		var clickHandler = eacConfirm({callbackYes:submitForm, title:'${confirmDelete}'});
		$deleteButton.click(clickHandler);
		
		$deleteButton.hide();
		var isQuestionSelected = '${not empty questionBean.selectedQuestion}';
		var isDeletable = ${questionBean.selectedQuestionDeletable};
		if (isQuestionSelected && isDeletable) {
			$deleteButton.show();
		}
		var isNewQuestion = ${questionBean.newQuestion};
		if (isNewQuestion) {
			$deleteButton.hide();
		}
		
		$('#cancel').click(function() {
			$('#selectedQuestion').change();
		});
		
		var exportNamesToRemove = '';
		$('.removeExportName').click(function() {
			var $fieldset = $(this).closest('fieldset');
			var $exportNamesToRemove = $('#exportNamesToRemove');
			exportNamesToRemove = $exportNamesToRemove.val();
			if (exportNamesToRemove != '') {
				exportNamesToRemove += ',';
			}
			exportNamesToRemove += $fieldset.attr('id');
			$exportNamesToRemove.val(exportNamesToRemove);
			$fieldset.hide();
		});
		
		$form.delegate('.removeNewExportName', 'click', function() {
			$(this).closest('fieldset').remove();
		});
		
		$('#addExportNameLink').click(function() {
			var $clonedTypeFieldset = $newTypeFieldset.clone();
			$clonedTypeFieldset.find('input,select').each(function() {
				$(this).removeAttr('disabled');
			});
			var $existingExportNames = $(this).siblings('.exportName');
			if ($existingExportNames.length > 0) {
				$clonedTypeFieldset.insertBefore('.exportName:first');
			} else {
				$(this).after($clonedTypeFieldset);
			}
			$clonedTypeFieldset.addClass('exportName');
			$clonedTypeFieldset.show();
			$clonedTypeFieldset.find('input:first').focus();
			return false;
		});
		
		$form.submit(function() {
			var newTypeCount = ${fn:length(questionBean.newExportNames)};
			$('.newType').each(function() {
				$(this).find('input,select').each(function() {
					var name = $(this).attr('name');
					$(this).attr('name', name.replace(/%idx%/, newTypeCount));
				});
				newTypeCount++;
			});
		});
		
		addAutocompleteMessageTo('#elementMessage');
		
		<c:if test="${not empty questionBean.selectedQuestion.elements}">
			createLinkedIcon('form > fieldset', '${fn:replace(questionBean.selectedQuestion.description, "'", "\\'")}', [<c:forEach items="${questionBean.selectedQuestion.elements}" var="element" varStatus="status">'${fn:replace(element.name, "'", "\\'")}',</c:forEach>], ${empty param['statusMessageKey']});
		</c:if>		
	});
</script>
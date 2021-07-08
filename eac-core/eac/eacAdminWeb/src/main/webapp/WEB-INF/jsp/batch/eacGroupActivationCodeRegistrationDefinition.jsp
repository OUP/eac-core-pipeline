<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="eacGroupActivationCodeRegistrationDefinitionTile" style="clear:both">
	
	<fieldset>
		<legend><spring:message code="title.product.group.summary" /></legend>
		<div>
			<div class="field" style="float:left; min-height: 3em">
				<div class="fieldLabel">
					<label for="groupName"><spring:message code="label.groupName" /></label>
				</div>
				<div class="fieldValue" style="max-width: 53em;">
					<span id="groupName"><c:out value="${activationCodeBatchBean.activationCodeRegistrationDefinition.eacGroup.groupName}"/></span>	
				</div>
			</div>
		</div>
	</fieldset>
</div>
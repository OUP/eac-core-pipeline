<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="activationCodeBatchSearchFormTile">

	<%-- <c:if test="${statusMessageKey ne null}">
		 <div class="success">
			<spring:message code="${statusMessageKey}"/>
		</div>
	</c:if>
	<c:if test="${errorMessageKey ne null}">
		 <div class="error">
			<spring:message code="${errorMessageKey}"/>
		</div>
	</c:if>
	 --%>
	<fieldset>
		<legend><spring:message code="title.searchcriteria" /></legend>
		<div class="field">
			<div class="fieldLabel">
				<label for="batchName"><spring:message code="label.batchname" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="batchName" path="batchName"/>
			</div>
		</div>	
		<div class="field">
			<div class="fieldLabel">
				<label for="activationCodeRegistrationDefinition"><spring:message code="label.product" /></label>
			</div>
			<div class="fieldValue" style="max-width: 32em" id="productDiv">
				<tags:productFinder returnFieldId="productId" type="ACTIVATION_CODE_REGISTRATION" resetButtonId="clear"/>
				<input type="hidden" name="product" id="productId" />
				<script type="text/javascript">
				$('#productId').change(function() {
					if($('#productId').val() != ""){
						$('#groupDiv').find('.iconClear').click();
					}
				});	
				</script>
			</div>
		</div>
		 <div class="field">
			<div class="fieldLabel">
				<label for="eacGroup"><spring:message code="label.product.group" /></label>
			</div>
			<div class="fieldValue" style="max-width: 32em" id="groupDiv">
				<tags:eacGroupFinder returnFieldId="eacGroupId" resetButtonId="clear"/>
				<input type="hidden" name="eacGroupId" id="eacGroupId" />
				<script type="text/javascript">
				$('#eacGroupId').change(function() {
					if($('#eacGroupId').val() != ""){
						$('#productDiv').find('.iconClear').click();
					}
				});		
				</script>
			</div>
		</div> 
		<div class="field">
			<div class="fieldLabel">
				<label for="activationCode"><spring:message code="label.activationcode" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="activationCode" path="activationCode"/>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="batchDateTime"><spring:message code="label.batchdate" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="batchDateTime" path="batchDateTime"/>
				<script type="text/javascript">
					$('#batchDateTime').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
				</script>
			</div>
		</div>		
		<div class="field">
			<div class="fieldLabel">
				<label for="licenceType"><spring:message code="label.licencetype" /></label>
			</div>
			<div class="fieldValue">
				<form:select id="licenceType" path="licenceType">
					<form:option value=""><spring:message code="label.any" /></form:option>
					<c:forEach items="${activationCodeBatchSearchCriteriaBean.getAllowedLicenceTypes()}" var="licenceType">
						<spring:message code="label.licenceType.${licenceType}" var="licenceTypeLabel"/>
						<form:option value="${licenceType}" label="${licenceTypeLabel}"/>
					</c:forEach>
				</form:select>				
			</div>
		</div>	
		<div class="field">
			<div class="fieldLabel">
				<label for="resultsPerPage"><spring:message code="label.resultsperpage" /></label>
			</div>
			<div class="fieldValue">
				<form:select id="resultsPerPage" path="resultsPerPage" cssStyle="width:auto">
					<form:option value="5">5</form:option>
					<form:option value="10">10</form:option>
					<form:option value="15">15</form:option>
					<form:option value="20">20</form:option>
				</form:select>
			</div>
		</div>
	</fieldset>
	<script type="text/javascript">
		$(function() {
			$('#batchName').focus();
		});
	</script>
</div>
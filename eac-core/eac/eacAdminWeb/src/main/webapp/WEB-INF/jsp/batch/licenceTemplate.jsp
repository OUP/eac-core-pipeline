<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="licenceTemplateTile">
<c:set var="safeNestedPath" value="${nestedPath}" scope="request"/>
<c:if test="${licenceTemplateWrapped}">
	<c:set var="nestedPath" value="${null}" scope="request"/>
</c:if>	
	    <div class="field">
			<div class="fieldLabel">
				<label for="licenceStartDate"><spring:message code="label.licence.startdate" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="licenceStartDate" path="activationCodeBatchBean.licenceStartDate" />
				<script type="text/javascript">
					$('#licenceStartDate').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
				</script>
			</div>
		</div>	
	    <div class="field">
			<div class="fieldLabel">
				<label for="licenceEndDate"><spring:message code="label.licence.enddate" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="licenceEndDate" path="activationCodeBatchBean.licenceEndDate" />
				<script type="text/javascript">
					$('#licenceEndDate').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
				</script>
			</div>
		</div>

	<div id="concurrent" class="licence">
		<div class="field">
			<div class="fieldLabel">
				<label for="totalConcurrency"><spring:message code="label.totalConcurrency" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input id="totalConcurrency" path="activationCodeBatchBean.totalConcurrency" />
			</div>
		</div>
	    <div class="field">
			<div class="fieldLabel">
				<label for="userConcurrency"><spring:message code="label.userConcurrency" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input id="userConcurrency" path="activationCodeBatchBean.userConcurrency" />
			</div>
		</div>
	</div>
	<div id="rolling" class="licence">
	    <div class="field">
			<div class="fieldLabel">
				<label for="timePeriod"><spring:message code="label.timePeriod" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input id="timePeriod" path="activationCodeBatchBean.timePeriod" />
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="rollingUnitType"><spring:message code="label.rollingunittype" /></label>
			</div>
			<div class="fieldValue">
				<form:select id="rollingUnitType" path="activationCodeBatchBean.rollingUnitType" >
					<c:forEach var="unitType" items="${rollingUnitTypes}">
						<spring:message code="label.licence.rolling.${unitType}" var="unitTypeLabel"/>
						<form:option value="${unitType}" label="${unitTypeLabel}"/>
					</c:forEach>
				</form:select>					
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="rollingBeginOn"><spring:message code="label.rollingbeginon" /></label>
			</div>
			<div class="fieldValue">
				<form:select id="rollingBeginOn" path="activationCodeBatchBean.rollingBeginOn" >
					<c:forEach var="beginOn" items="${rollingBeginOns}">
						<spring:message code="label.licence.rolling.${beginOn}" var="beginOnLabel"/>
						<form:option value="${beginOn}" label="${beginOnLabel}"/>
					</c:forEach>
				</form:select>						
			</div>
		</div>
	</div>
	<div id="usage" class="licence">
	    <div class="field">
			<div class="fieldLabel">
				<label for="licenceAllowedUsages"><spring:message code="label.licenceAllowedUsages" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input id="licenceAllowedUsages" path="activationCodeBatchBean.licenceAllowedUsages" />
			</div>
		</div>
	</div>
<c:if test="${not activationCodeBatchBean.editMode}">
<script type="text/javascript">
	$(function() {
		$('#licenceType').change(function() {
			var licenceType = $(this).find('option:selected').val().toLowerCase();			
			$('.licence').hide();
			$('#' + licenceType).show();
		}).live("keypress", function() {
			$('#licenceType').trigger("change");
		});
	});
</script>
</c:if>
<script type="text/javascript">
	$(function() {
		$('.licence').hide();
		var currentVal = '${activationCodeBatchBean.licenceType}';
		var currentSel = '#'+currentVal.toLowerCase()
		$('#licenceType').val(currentVal);
		$(currentSel).show();
	});
</script>
</div>
<c:set var="nestedPath" value="${safeNestedPath}" scope="request"/>
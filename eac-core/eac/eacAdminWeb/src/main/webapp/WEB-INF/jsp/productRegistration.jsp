<%@page import="com.oup.eac.domain.RegistrationActivation.ActivationStrategy"%>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<spring:message code="label.noneSelected" var="noneSelected" />
<div id="productRegistrationTile">
	<fieldset>
		<legend><spring:message code="title.accountCreation" /></legend>
		<div class="field">
			<div class="fieldLabel">
				<label for="accountCreationPage"><spring:message code="label.page" /></label>
			</div>
			<div class="fieldValue">
				<form:select path="accountPageDefinitionId" id="accountCreationPage">
					<form:option value="" label="${noneSelected}"/>
					<c:forEach var="pageDefinition" items="${productBean.accountPageDefinitions}">
						<form:option value="${pageDefinition.id}" label="${pageDefinition.name}"/>
					</c:forEach>
				</form:select>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="emailValidation"><spring:message code="label.emailValidation" /></label>
			</div>
			<div class="fieldValue">
				<form:checkbox id="emailValidation" path="emailValidation" cssClass="checkbox"/>
			</div>
			<spring:message code="info.emailValidation" var="emailValidationHelp" />
			<img id="emailValidationHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${emailValidationHelp}"/>
		</div>
	</fieldset>
	<fieldset>
		<legend><spring:message code="title.productRegistration" /></legend>
			<div class="field">
				<div class="fieldLabel">
					<label for="activationCode"><spring:message code="label.activationCode" /></label>
				</div>
				<div class="fieldValue">
					<c:choose>
						<c:when test="${not productBean.editMode}">
							<form:checkbox id="activationCode" path="activationCode" cssClass="checkbox"/>
						</c:when>
						<c:otherwise>
							<spring:message code="label.${productBean.activationCode}"  />
						</c:otherwise>
					</c:choose>
				</div>
				<c:if test="${not productBean.editMode}">
					<spring:message code="info.activationCode" var="activationCodeHelp" />
					<img id="activationCodeHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${activationCodeHelp}"/>
				</c:if>
			</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="productRegistrationPage"><spring:message code="label.page" /></label>
			</div>
			<div class="fieldValue">
				<form:select path="productPageDefinitionId" id="productRegistrationPage">
					<form:option value="" label="${noneSelected}"/>
					<c:forEach var="pageDefinition" items="${productBean.productPageDefinitions}">
						<form:option value="${pageDefinition.id}" label="${pageDefinition.name}"/>
					</c:forEach>
				</form:select>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="activation"><spring:message code="label.activation" /></label>
			</div>
			<div class="fieldValue">
				<spring:message code="label.newEntry" var="labelNew"/>
				<form:select path="registrationActivationId" id="activation">
					<c:forEach var="activation" items="${productBean.instantRegistrationActivations}">
						<spring:message code="label.registration.activation.${activation.name}" var="registrationActivationLabel" />
						<form:option value="${activation.id}" label="${registrationActivationLabel}"/>
					</c:forEach>
					<c:forEach var="activation" items="${productBean.selfRegistrationActivations}">
						<spring:message code="label.registration.activation.${activation.name}" var="registrationActivationLabel" />
						<form:option value="${activation.id}" label="${registrationActivationLabel}"/>
					</c:forEach>
					<c:forEach var="activation" items="${productBean.validatedRegistrationActivations}">
						<spring:message code="label.registration.activation.${activation.name}" var="registrationActivationLabel" />
						<form:option value="${activation.id}" label="${registrationActivationLabel} - ${activation.validatorEmail}"/>
					</c:forEach>
					<spring:message code="label.registration.activation.ValidatedRegistrationActivation" var="labelValidated" />
					<form:option value="newValidator" label="${labelValidated} - ${labelNew}"/>
					<!-- <c:forEach var="activation" items="${productBean.countryMatchRegistrationActivations}">
						<spring:message code="label.registration.activation.${activation.name}" var="registrationActivationLabel" />
						<form:option value="${activation.id}" label="${registrationActivationLabel} - ${activation.description}"/>
					</c:forEach>
					<spring:message code="label.registration.activation.CountryMatchRegistrationActivation" var="labelCountryMatch" />
					<form:option value="newCountryMatch" label="${labelCountryMatch} - ${labelNew}"/> -->
				</form:select>
			</div>
			<spring:message code="info.activation.instant" var="instantActivationHelp" />
			<spring:message code="info.activation.self" var="selfActivationHelp" />
			<spring:message code="info.activation.validated" var="validatedActivationHelp" />
			<spring:message code="info.activation.countryMatch" var="countryMatchActivationHelp" />
			<img id="activationHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${instantActivationHelp}<br/><br/>${selfActivationHelp}<br/><br/>${validatedActivationHelp}<br/><br/>${countryMatchActivationHelp}"/>
		</div>
		<div id="validatorField" class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.validator" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input id="validator" path="validator"/>
			</div>
		</div>
		<fieldset id="countryMatchFields" style="padding-top: 0px\9">
			<legend><spring:message code="label.countryMatchActivation"/></legend>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.description" />&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:input id="description" path="description" maxlength="100"/>
				</div>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.unmatched" />&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:select id="unmatched" path="unmatchedRegistrationActivationId">
						<c:forEach var="activation" items="${productBean.instantRegistrationActivations}">
							<spring:message code="label.registration.activation.${activation.name}" var="registrationActivationLabel" />
							<form:option value="${activation.id}" label="${registrationActivationLabel}"/>
						</c:forEach>
						<c:forEach var="activation" items="${productBean.selfRegistrationActivations}">
							<spring:message code="label.registration.activation.${activation.name}" var="registrationActivationLabel" />
							<form:option value="${activation.id}" label="${registrationActivationLabel}"/>
						</c:forEach>
						<c:forEach var="activation" items="${productBean.validatedRegistrationActivations}">
							<spring:message code="label.registration.activation.${activation.name}" var="registrationActivationLabel" />
							<form:option value="${activation.id}" label="${registrationActivationLabel} - ${activation.validatorEmail}"/>
						</c:forEach>
						<c:forEach var="activation" items="${productBean.countryMatchRegistrationActivations}">
							<spring:message code="label.registration.activation.${activation.name}" var="registrationActivationLabel" />
							<form:option value="${activation.id}" label="${registrationActivationLabel} - ${activation.description}"/>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.matched" />&#160;<span class="mandatory">*</span></label>
				</div>			
				<div class="fieldValue">
					<form:select id="matched" path="matchedRegistrationActivationId">
						<c:forEach var="activation" items="${productBean.instantRegistrationActivations}">
							<spring:message code="label.registration.activation.${activation.name}" var="registrationActivationLabel" />
							<form:option value="${activation.id}" label="${registrationActivationLabel}"/>
						</c:forEach>
						<c:forEach var="activation" items="${productBean.selfRegistrationActivations}">
							<spring:message code="label.registration.activation.${activation.name}" var="registrationActivationLabel" />
							<form:option value="${activation.id}" label="${registrationActivationLabel}"/>
						</c:forEach>
						<c:forEach var="activation" items="${productBean.validatedRegistrationActivations}">
							<spring:message code="label.registration.activation.${activation.name}" var="registrationActivationLabel" />
							<form:option value="${activation.id}" label="${registrationActivationLabel} - ${activation.validatorEmail}"/>
						</c:forEach>
						<c:forEach var="activation" items="${productBean.countryMatchRegistrationActivations}">
							<spring:message code="label.registration.activation.${activation.name}" var="registrationActivationLabel" />
							<form:option value="${activation.id}" label="${registrationActivationLabel} - ${activation.description}"/>
						</c:forEach>
					</form:select>					
				</div>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.localeList" />&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:input id="localeList" path="localeList" maxlength="255"/>
				</div>
				<spring:message code="info.activation.localeListHelp" var="localeListHelp" text="A comma separated list of Locales (e.g. zh_HK,en_AU,en_NZ)"/>
				<img id="localeListHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${localeListHelp}"/>
			</div>			
		</fieldset>
		<fieldset>
			<legend><spring:message code="title.licence" /></legend>
			<div class="field">
				<div class="fieldLabel">
					<label for="licenceType"><spring:message code="label.licenceType" /></label>
				</div>
				<div class="fieldValue">
					<c:choose>
						<c:when test="${not productBean.editMode}">
							<form:select path="licenceType" id="licenceType">
								<c:forEach items="${productBean.allowedLicenceTypes}" var="licenceType">
									<spring:message code="label.licenceType.${licenceType}" var="licenceTypeLabel"/>
									<form:option value="${licenceType}" label="${licenceTypeLabel}"/>
								</c:forEach>
							</form:select>
						</c:when>
						<c:otherwise>
							<spring:message code="label.licenceType.${productBean.licenceType}"/>
						</c:otherwise>
					</c:choose>
				</div>
				<c:if test="${not productBean.editMode}">
					<spring:message code="info.licence.concurrent" var="concurrentLicenceHelp"/>
					<spring:message code="info.licence.usage" var="usageLicenceHelp"/>
					<spring:message code="info.licence.rolling" var="rollingLicenceHelp"/>
					<img id="licenceTypeHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="${concurrentLicenceHelp}<br/><br/>${usageLicenceHelp}<br/><br/>${rollingLicenceHelp}"/>
				</c:if>
			</div>
		    <div class="field">
				<div class="fieldLabel">
					<label for="licenceStartDate"><spring:message code="label.licenceStartDate" /></label>
				</div>
				<div class="fieldValue">
					<form:input id="licenceStartDate" path="licenceStartDate" />
					<script type="text/javascript">
						$('#licenceStartDate').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
						
					</script>
				</div>
			</div>	
		    <div class="field">
				<div class="fieldLabel">
					<label for="licenceEndDate"><spring:message code="label.licenceEndDate" /></label>
				</div>
				<div class="fieldValue">
					<form:input id="licenceEndDate" path="licenceEndDate" />
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
						<form:input id="totalConcurrency" path="totalConcurrency" />
					</div>
				</div>
			    <div class="field">
					<div class="fieldLabel">
						<label for="userConcurrency"><spring:message code="label.userConcurrency" />&#160;<span class="mandatory">*</span></label>
					</div>
					<div class="fieldValue">
						<form:input id="userConcurrency" path="userConcurrency" />
					</div>
				</div>
			</div>
			<div id="rolling" class="licence">
			    <div class="field">
					<div class="fieldLabel">
						<label for="timePeriod"><spring:message code="label.timePeriod" />&#160;<span class="mandatory">*</span></label>
					</div>
					<div class="fieldValue">
						<form:input id="timePeriod" path="timePeriod" />
					</div>
				</div>
				<div class="field">
					<div class="fieldLabel">
						<label for="rollingUnitType"><spring:message code="label.rollingunittype" /></label>
					</div>
					<div class="fieldValue">
						<form:select id="rollingUnitType" path="rollingUnitType" >
							<c:forEach var="unitType" items="${productBean.allowedRollingUnitTypes}">
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
						<form:select id="rollingBeginOn" path="rollingBeginOn" >
							<c:forEach var="beginOn" items="${productBean.rollingBeginOns}">
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
						<form:input id="licenceAllowedUsages" path="licenceAllowedUsages" />
					</div>
				</div>
			</div>
		</fieldset>
        <div class="field" style="margin-top: 15px\9;">
            <div class="fieldLabel" style="width: 200px;">
                <label for="emailConfirmation"><spring:message code="label.emailConfirmation" /></label>
            </div>
            <div class="fieldValue">
                <form:checkbox id="emailConfirmation" path="emailConfirmationEnabled" cssClass="checkbox"/>
            </div>
            <spring:message code="info.emailConfirmation" var="emailConfirmationHelp" />
            <img id="emailConfirmationHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${emailConfirmationHelp}"/>
        </div>
	</fieldset>
</div>
<script type="text/javascript">
	$(function() {
		$("#emailConfirmationHelp").tipTip({edgeOffset: 7});
		$("#emailValidationHelp").tipTip({edgeOffset: 7});
		$("#activationCodeHelp").tipTip({edgeOffset: 7});
		$("#activationHelp").tipTip({edgeOffset: 7});
		$("#licenceTypeHelp").tipTip({edgeOffset: 7});
		$("#localeListHelp").tipTip({edgeOffset: 7});
		var $validatorField = $('#validatorField');
		var $validator = $('#validator');
		var $countryMatchFields = $('#countryMatchFields');
		
		var validatorGuidMap = {
			<c:forEach var="activation" items="${productBean.validatedRegistrationActivations}" varStatus="status">
				'${activation.id}': '${activation.validatorEmail}',
			</c:forEach>
			'newValidator': ''
		};
		
		if ('${productBean.registrationActivationId}' in validatorGuidMap) {
			$validatorField.show();
		} else{
			$validatorField.hide();
		}
		
		var countryMatchGuidMap = {
			<c:forEach var="activation" items="${productBean.countryMatchRegistrationActivations}" varStatus="status">
				'${activation.id}': {'description': '${activation.description}',
									 'unmatched': '${activation.unmatchedActivation.id}',
									 'matched': '${activation.matchedActivation.id}',
									 'localeList': '${activation.localeList}'},
			</c:forEach>
			'newCountryMatch': {'description': '',
				 'unmatched': '${productBean.instantRegistrationActivations[0].id}',
				 'matched': '${productBean.instantRegistrationActivations[0].id}',
				 'localeList': ''}
		};		
		
		if ('${productBean.registrationActivationId}' in countryMatchGuidMap) {
			$countryMatchFields.show();
		} else{
			$countryMatchFields.hide();
		}
		
		var $description = $('#description');
		var $unmatched = $('#unmatched');
		var $matched = $('#matched');
		var $localeList = $('#localeList');
		var selectedGuid = $('#activation').find('option:selected').val();
		$('#activation').change(function() {
			selectedGuid = $(this).find('option:selected').val();
			if (selectedGuid in validatorGuidMap) {
				$validatorField.show();
				$validator.val(validatorGuidMap[selectedGuid])
			} else {
				$validatorField.hide();
				$validator.val('');
			}
			if (selectedGuid in countryMatchGuidMap) {
				$countryMatchFields.show();
				$description.val(countryMatchGuidMap[selectedGuid]['description']);
				$unmatched.val(countryMatchGuidMap[selectedGuid]['unmatched']);
				$matched.val(countryMatchGuidMap[selectedGuid]['matched']);
				$localeList.val(countryMatchGuidMap[selectedGuid]['localeList']);
			} else {
				$countryMatchFields.hide();
				$description.val('');
				$unmatched.val('');
				$matched.val('');
				$localeList.val('');
			}
			toggleDisablingOfDropDownOptions();
		}).live("keypress", function() {
			$('#activation').trigger("change");
		});
		
		function toggleDisablingOfDropDownOptions() {
			$unmatched.find('option').removeAttr('disabled');
			$matched.find('option').removeAttr('disabled');
			$unmatched.find('option[value="' + selectedGuid + '"]').attr('disabled', 'disabled');
			$matched.find('option[value="' + selectedGuid + '"]').attr('disabled', 'disabled');
		}
		
		toggleDisablingOfDropDownOptions();
		
		$('#licenceType').change(function() {
			var licenceType = $(this).find('option:selected').val().toLowerCase();
			$('.licence').hide();
			$('#' + licenceType).show();
		}).live("keypress", function() {
			$('#licenceType').trigger("change");
		});
		
		$('.licence').hide();
		$('#' + '${productBean.licenceType}'.toLowerCase()).show();
	});
</script>
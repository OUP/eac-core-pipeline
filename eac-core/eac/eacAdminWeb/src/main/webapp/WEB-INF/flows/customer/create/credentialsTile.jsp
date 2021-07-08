<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="credentialsTile">
	<fieldset>
		<div class="field">
			<div class="fieldLabel">
				<label for="customerUsername"><spring:message code="label.username" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input id="customerUsername" path="customer.username" maxlength="255" />
			</div>
			<spring:message code="info.username.rules" var="usernameHelp"/>
			<img id="usernameHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="${usernameHelp}"/>
			<script type="text/javascript">
				$("#usernameHelp").tipTip({edgeOffset: 7});
			</script>
		</div>
		<c:if test="${editMode}">
			<div class="field">
				<div class="fieldLabel">
					<label for="changePassword"><spring:message code="label.changePassword" /></label>
				</div>
				<div class="fieldValue">
					<form:checkbox id="changePassword" path="changePassword" cssClass="checkbox"/>
				</div>
			</div>
		</c:if>
		<div id="changePasswordDiv" style="display:${(customerBean.changePassword) ? '' : 'none'}">
			<div class="field">
				<div class="fieldLabel">
					<label for="generatePassword"><spring:message code="label.generatePassword" /></label>
				</div>
				<div class="fieldValue">
					<form:checkbox id="generatePassword" path="generatePassword" cssClass="checkbox"/>
				</div>
				<spring:message code="info.credential.generatePassword" var="generatePasswordHelp" />
            	<img id="generatePasswordHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="${generatePasswordHelp}"/>
           		<script type="text/javascript">
           			$("#generatePasswordHelp").tipTip({edgeOffset: 7});
           		</script>
			</div>
			<div id="usernamePassDiv" style="display:${(customerBean.changePassword and !customerBean.generatePassword) ? '' : 'none'}">
				<div class="field">
					<div class="fieldLabel">
						<label for="password"><spring:message code="label.newPassword" />&#160;<span class="mandatory">*</span></label>
					</div>
					<div class="fieldValue">
						<form:password id="password" path="password"/>
					</div>
				</div>
				<div class="field">
					<div class="fieldLabel">
						<label for="passwordAgain"><spring:message code="label.passwordAgain" />&#160;<span class="mandatory">*</span></label>
					</div>
					<div class="fieldValue">
						<form:password id="passwordAgain" path="passwordAgain"/>
					</div>
				</div>
			</div>			
		</div>
	</fieldset>
	<script type="text/javascript">
		var $changePasswordCbx = $('#changePassword');
		var $generatePasswordCbx = $('#generatePassword');
		var $changePasswordDiv = $('#changePasswordDiv');
		var $usernamePassDiv = $('#usernamePassDiv');
		
		$changePasswordCbx.click(function() {
			if ($(this).prop('checked')) {
				$changePasswordDiv.show();
				$usernamePassDiv.show();
				$generatePasswordCbx.removeProp('checked');
			} else {
				$changePasswordDiv.hide();
				$usernamePassDiv.hide();
			}
		});
		
		$generatePasswordCbx.click(function() {
			if ($(this).prop('checked')) {
				$usernamePassDiv.hide();
			} else {
				$usernamePassDiv.show();
			}
		});
	</script>
</div>

<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<fieldset>
	<div class="field">
		<div class="fieldLabel">
			<label for="active"><spring:message code="label.active"/></label>
		</div>
		<div class="fieldValue">
			<form:checkbox id="active" path="selectedAdminUser.enabled" cssClass="checkbox" />
		</div>
		<spring:message code="info.accountActive" var="activeHelp" />
		<img id="activeHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${activeHelp}" style="margin-left: 5px; padding-top: 4px"/>
		<script type="text/javascript">
			$("#activeHelp").tipTip({edgeOffset: 7});
		</script>		
	</div>
	<div class="field">
		<div class="fieldLabel">
			<label><spring:message code="label.firstName"/>&#160;<span class="mandatory">*</span></label>
		</div>
		<div class="fieldValue">
			<form:input path="selectedAdminUser.firstName" />
		</div>
	</div>
	<div class="field">
		<div class="fieldLabel">
			<label><spring:message code="label.familyName"/>&#160;<span class="mandatory">*</span></label>
		</div>
		<div class="fieldValue">
			<form:input path="selectedAdminUser.familyName" />
		</div>
	</div>
	<div class="field">
		<div class="fieldLabel">
			<label><spring:message code="label.email"/>&#160;<span class="mandatory">*</span></label>
		</div>
		<div class="fieldValue">
			<form:input path="selectedAdminUser.emailAddress" />
		</div>
	</div>
	<div class="field">
		<div class="fieldLabel">
			<label><spring:message code="label.username"/>&#160;<span class="mandatory">*</span></label>
		</div>
		<div class="fieldValue">
			<form:input path="selectedAdminUser.username" />
		</div>
	</div>
	<c:if test="${accountBean.editMode}">
		<div class="field">
			<div class="fieldLabel">
				<label for="changePassword"><spring:message code="label.changePassword"/></label>
			</div>
			<div class="fieldValue">
				<form:checkbox id="changePassword" path="changePassword" cssClass="checkbox" />
			</div>
		</div>
    </c:if>			
	<div id="changePasswordFields" style="display:none">
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.newPassword"/>&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:password path="password" />
			</div>
			<spring:message code="info.passwordRequirement" var="passwordHelp" />
			<img id="passwordHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${passwordHelp}" />
			<script type="text/javascript">
				$("#passwordHelp").tipTip({edgeOffset: 7});
			</script>				
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.passwordAgain"/>&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:password path="confirmPassword" />
			</div>
		</div>
	</div>
</fieldset>
<script type="text/javascript">
	$(function() {
		
		var $changePassword = $('#changePassword');
		var $changePasswordFields = $('#changePasswordFields');		
		var editMode=${accountBean.editMode};        
		
        if(editMode){
            $changePassword.removeProp('checked');
        }
        
		$changePassword.click(function() {
			var $this = $(this);
			if ($this.prop('checked')) {
				$changePasswordFields.show();
			} else {
				$changePasswordFields.hide();
			}
		});
		if ($changePassword.prop('checked')) {
			if ($('.success').length > 0) {
				$changePassword.removeProp('checked');
				$changePasswordFields.hide();
			} else {
				$changePasswordFields.show();
			}
		}
		var guid = '${accountBean.selectedAdminUser.id}';
		if (!guid) {
			$changePassword.prop('checked', 'checked');
			$changePasswordFields.show();
		}
	});
</script>
<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<c:url value="/mvc/trustedSystems/manage.htm" var="formUrl" />
<div id="manageTrustedSystemTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.trustedSystem"/></h1>
	</div>
	
	<c:if test="${not empty statusMessageKey}">
		<div class="success">
			<spring:message code="${statusMessageKey}"/><br/>
			<c:if test="${not empty passwordEncrypt}">
				<spring:message code="${passwordEncryptMsg}"/><br/><br/>
				<p style="word-wrap: break-word">${passwordEncrypt}</p>
			</c:if>
		</div>
	</c:if>
	
	<spring:hasBindErrors name="trustedSystemBean">
		<div class="error">
			<spring:bind path="trustedSystemBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>
	<form:form modelAttribute="trustedSystemBean" method="POST">
		<fieldset>
			<legend><spring:message code="label.trustedSystem"/></legend>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.trustedSystemUserName" /></label>
				</div>
				<div class="fieldValue">
					<form:select id="selectedTrustedSystem" path="selectedTrustedSystemId">
						<c:forEach var="trustedSystem" items="${trustedSystemBean.trustedSystems}">
							<form:option value="${trustedSystem.userId}" label="${trustedSystem.userName}"/>
						</c:forEach>
						<spring:message code="label.newEntry" var="newEntry"/>
						<form:option value="-1" label="${newEntry}"/>
					</form:select>
				</div>
			</div>
		</fieldset>
		<fieldset id="trustedSystem" class=trustedSystem>
			<legend><spring:message code="label.trustedSystemSummary"/></legend>	
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.trustedSystemUserName" /><div class="mandtorySign">&#160;<span class="mandatory">*</span></div></label>
				</div>
				<div class="fieldValue">
					<form:input path="newUserName" id="newUserName"/>
				</div>
			</div>	
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.trustedSystemNewPassword" />&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:password path="newPassword"/>
				</div>
			</div>	
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.trustedSystemConfirmPassword" />&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:password path="newConfirmPassword"/>
				</div>
			</div>		
		</fieldset>
			
		<div id="buttons">
	    	<p>   
	    		<button type="button" id="delete"><spring:message code="button.delete"/></button>
	    		<button type="submit" id="save"><spring:message code="button.save" /></button>
	        </p>
	    </div>
	  </form:form>
</div>
<spring:message code="confirm.title.delete.trustedSystem" var="confirmDelete" text="" />
<script type="text/javascript">
	$(function() {
		var $selectedTrustedSystem = $('#selectedTrustedSystem');
		var $deleteButton = $('#delete');
		var $userName = $('#newUserName');
		var $form = $('form');
		$selectedTrustedSystem.change(function() {
			var $this = $(this);
			var selectedValue = $this.find('option:selected').val();
			if (selectedValue == '-1') {
				selectedValue = 'new' ;
				$userName.prop('readonly', false) ;
				$userName.val('') ;
				$('.mandtorySign').css('display', 'inline');
				$deleteButton.hide();
			} else{
				$userName.val($this.find('option:selected').text()) ;
				$userName.prop('readonly', true) ;
				$('.mandtorySign').css('display', 'none');
				$deleteButton.show();
			}
			
			$deleteButton.attr('eacLabel',$('option:selected', $selectedTrustedSystem).text());
		});
		$selectedTrustedSystem.trigger('change');
		
		var callback = function(){
			$form.append('<input type="hidden" name="delete" value="1" />');
			$form.submit();
		};
		var clickHandler = eacConfirm({callbackYes:callback, title:'${confirmDelete}'});
		$deleteButton.click(clickHandler);
	});
</script>
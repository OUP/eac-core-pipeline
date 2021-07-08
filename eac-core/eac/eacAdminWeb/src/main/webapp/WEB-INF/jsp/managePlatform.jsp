<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<c:url value="/mvc/PlatformMaster/platformMaster.htm" var="formUrl" />
<div id="managePlatformTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.platforms"/></h1>
	</div>
	
	<c:if test="${not empty statusMessageKey}">
		<div class="success">
			<spring:message code="${statusMessageKey}"/>
		</div>
	</c:if>
	
	<spring:hasBindErrors name="platformBean">
		<div class="error">
			<spring:bind path="platformBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>
	<form:form modelAttribute="platformBean" method="POST">
		<fieldset>
			<legend><spring:message code="label.platform"/></legend>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.platformName" /></label>
				</div>
				<div class="fieldValue">
					<form:select id="selectedPlatform" path="selectedPlatformGuid">
						<c:forEach var="platform" items="${platformBean.platforms}">
							<form:option value="${platform.platformId}" label="${platform.code}"/>
						</c:forEach>
						<spring:message code="label.newEntry" var="newEntry"/>
						<form:option value="-1" label="${newEntry}"/>
					</form:select>
				</div>
			</div>
		</fieldset>
		<fieldset id="new" class="platform">
			<legend><spring:message code="label.platformSummary"/></legend>		
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.platformCode" />&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:input path="newPlatformCode"/>
				</div>
			</div>	
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.platformName" />&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:input path="newPlatformName"/>
				</div>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.description" /></label>
				</div>
				<div class="fieldValue">
					<form:input path="newPlatformDescription"/>
				</div>
			</div>		
		</fieldset>
		<c:forEach var="platform" items="${platformBean.platforms}" varStatus="platformStatus">
			<fieldset id="${platform.platformId}" class="platform">
				<legend><spring:message code="label.platformSummary" /></legend>
				<div class="field">
					<div class="fieldLabel">
						<label><spring:message code="label.platformCode" />&#160;<span class="mandatory">*</span></label>
					</div>
					<div class="fieldValue">
						<form:input path="platforms[${platformStatus.index}].code" disabled="true"/>
					</div>
				</div>
				<div class="field">
					<div class="fieldLabel">
						<label><spring:message code="label.platformName" />&#160;<span class="mandatory">*</span></label>
					</div>
					<div class="fieldValue">
						<form:input path="platforms[${platformStatus.index}].name"/>
					</div>
				</div>	
				<div class="field">
					<div class="fieldLabel">
						<label><spring:message code="label.description" /></label>
					</div>
					<div class="fieldValue">
						<form:input path="platforms[${platformStatus.index}].description"/>
					</div>
				</div>		
			</fieldset>
		</c:forEach>
			
		<div id="buttons">
	    	<p>   
	    		<button type="button" id="delete"><spring:message code="button.delete"/></button>
	    		<button type="submit" id="save"><spring:message code="button.save" /></button>
	        </p>
	    </div>
	  </form:form>
</div>
<spring:message code="confirm.title.delete.platform" var="confirmDelete" text="" />
<script type="text/javascript">
	$(function() {
		var $selectedPlatform = $('#selectedPlatform');
		var $deleteButton = $('#delete');
		var $form = $('form');
		$selectedPlatform.change(function() {
			var $this = $(this);
			var selectedValue = $this.find('option:selected').val();
			if (selectedValue == '-1') {
				selectedValue = 'new' ;
				$deleteButton.hide();
			} else{
				$deleteButton.show();
			}
			$('.platform').css('display', 'none');
			$('fieldset[id=\'' + selectedValue + '\']').css('display', '');
			$deleteButton.attr('eacLabel',$('option:selected', $selectedPlatform).text());
		});
		$selectedPlatform.trigger('change');
		
		var callback = function(){
			$form.append('<input type="hidden" name="delete" value="1" />');
			$form.submit();
		};
		var clickHandler = eacConfirm({callbackYes:callback, title:'${confirmDelete}'});
		$deleteButton.click(clickHandler);
	});
</script>
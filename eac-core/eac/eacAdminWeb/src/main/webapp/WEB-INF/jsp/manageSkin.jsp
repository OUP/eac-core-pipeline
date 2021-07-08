<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<spring:message code="label.pleaseSelect" var="pleaseSelect"/>
<div id="manageSkinTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.manageSkins"/></h1>
	</div>

	<c:if test="${not empty statusMessageKey}">
		 <div class="success">
			<spring:message code="${statusMessageKey}"/>
		</div>
	</c:if>
	
	<spring:hasBindErrors name="skinBean">
		<div class="error">
			<spring:bind path="skinBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>	
	
	<c:url value="/mvc/skin/manage.htm" var="formUrl"/>
	<form:form modelAttribute="skinBean" action="${formUrl}">
		<fieldset>
			<legend><spring:message code="label.sites"/></legend>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.site"/></label>
				</div>
				<div class="fieldValue">
					<select id="selectedSkin" name="id">
						<c:if test="${empty skinBean.selectedSkin}"><option value="pleaseSelect">${pleaseSelect}</option></c:if>
						<c:forEach var="skin" items="${skinBean.skins}">
							<c:set var="selected" value=""/>
							<c:if test="${skin.id == skinBean.selectedSkin.id}">
								<c:set var="selected" value="selected"/>
							</c:if>
							<option value="${skin.id}" ${selected}><c:out value="${skin.siteName}"/></option>
						</c:forEach>
						<option value=""><spring:message code="label.newEntry" htmlEscape="true"/></option>
					</select>
				</div>
			</div>
		</fieldset>
		<fieldset id="skinDetails">
			<legend><spring:message code="label.skin"/></legend>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.siteName"/>&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:input id="siteName" path="selectedSkin.siteName" />
				</div>
			</div>		
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.siteUrl"/>&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:input id="siteUrl" path="selectedSkin.url" class="wide"/>
				</div>
			</div>		
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.contactUrl"/>&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:input id="contactUrl" path="selectedSkin.contactPath" class="wide" />
				</div>
			</div>		
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.skinUrl"/>&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:input id="skinUrl" path="selectedSkin.skinPath" class="wide" />
				</div>
			</div>		
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.customiser"/></label>
				</div>
				<div class="fieldValue">
					<form:input id="customiser" path="selectedSkin.urlCustomiserBean" />
				</div>
			</div>		
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.primarySite"/></label>
				</div>
				<div class="fieldValue">
					<form:checkbox id="primarySite" path="selectedSkin.primarySite" cssClass="checkbox"/>
				</div>
			</div>		
		</fieldset>
	    <div id="buttons">
	    	<p>                 
	    		<button type="button" eacLabel="${skinBean.selectedSkin.siteName}" id="delete"><spring:message code="button.delete"/></button>
	    		<button type="submit" id="save"><spring:message code="button.save" /></button>
	    		<button type="button" id="cancel"><spring:message code="button.cancel"/></button>
	        </p>
	    </div>		
	</form:form>
	<spring:message code="confirm.title.delete.skin" var="confirmDelete" text="" />
	<script type="text/javascript">
		$(function() {
			var $skinDetails = $('#skinDetails');
			var $buttons = $('#buttons');
			var $selectedSkin = $('#selectedSkin');
			
			$selectedSkin.change(function() {
				$(this).find('option').each(function() {
					if ($(this).val() == 'pleaseSelect') {
						$(this).remove();
					}
				});
				if ($(this).val()) {
					$.ajax({
						type: 'GET',
						url: '<c:url value="/mvc/skin/manage.htm"/>',
						dataType: 'json',
						data: 'id=' + $(this).val()
					}).done(function(data) {
						showSkinDetails();
						$('#siteName').val(data.siteName);
						$('#siteUrl').val(data.siteUrl);
						$('#contactUrl').val(data.contactUrl);
						$('#skinUrl').val(data.skinUrl);
						$('#customiser').val(data.customiser);
						if (data.primarySite === 'true') {
							$('#primarySite').prop('checked', 'checked');
						} else {
							$('#primarySite').removeProp('checked');
						}
						$('#delete').show();
						$('#delete').attr('eacLabel', data.siteName);
					});
				} else {
					showSkinDetails();
					$skinDetails.find('input[type="text"]').val(''); 
					$skinDetails.find('input:checkbox').removeProp('checked');
					$('#delete').hide();
				}
				$('.success').remove();
				$('.error').remove();
			}).keypress(function() {
				$(this).trigger('change');
			});

			var editing = '${editing}';
			if (!editing) {
				hideSkinDetails();
			}
			
			var hasSelectedSkin = '${skinBean.selectedSkin.id}';
			if (editing && !hasSelectedSkin) {
				$selectedSkin.find('option[value=""]').attr('selected', 'selected');
				$('#delete').hide();
			}
			
			function showSkinDetails() {
				$skinDetails.show();
				$buttons.show();
			}
			
			function hideSkinDetails() {
				$skinDetails.hide();
				$buttons.hide();
			}
			
			var submitForm = function() {
				var $form = $('form');
				$form.append('<input type="hidden" name="delete" value="1" />');
				$form.submit();
			};			
			
			var clickHandler = eacConfirm({callbackYes:submitForm, title:'${confirmDelete}'});
			$('#delete').click(clickHandler);
			
			$('#cancel').click(function() {
				$selectedSkin.change();
			});
			
		});
	</script>	
</div>
<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<spring:message code="label.pleaseSelect" var="pleaseSelect"/>
<div id="manageLanguageTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.manageLanguages"/></h1>
	</div>

	<c:if test="${not empty statusMessageKey}">
		 <div class="success">
			<spring:message code="${statusMessageKey}"/>
		</div>
	</c:if>
	
	<spring:hasBindErrors name="languageBean">
		<div class="error">
			<spring:bind path="languageBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>	
	
	<fieldset>
		<legend><spring:message code="label.languages"/></legend>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.locale"/></label>
			</div>
			<div class="fieldValue">
				<select id="selectedLocale" name="selectedLocale">
					<option value=""><spring:message code="label.default"/></option>
					<c:forEach var="locale" items="${languageBean.supportedLocales}">
						<c:set var="selected" value=""/>
						<c:if test="${locale == languageBean.selectedLocale}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${locale}" ${selected}><c:out value="${locale}"/></option>
					</c:forEach>
					<option value="new"<c:if test="${languageBean.newLocale}"> selected</c:if>><spring:message code="label.newEntry" htmlEscape="true"/></option>
				</select>
			</div>
			<c:url value="/images/progress-indicator.gif" var="progressIconUrl"/>
			<img id="progressIndicator" src="${progressIconUrl}" style="margin-left: 10px; margin-top: 2px; display:none"/>
		</div>
	</fieldset>
	
	<div id="manageLanguageFormTile">
		<tiles:insertAttribute name="manageLanguageFormTile"/>
	</div>
	
	<script type="text/javascript">
		$(function() {
			var $selectedLocale = $('#selectedLocale');
			var $progressIndicator = $('#progressIndicator');
			var waiting = false;
			
			$selectedLocale.change(function() {
				waiting = true;
				$.ajax({
					type: 'GET',
					url: '<c:url value="/mvc/language/manage.htm"/>',
					data: 'locale=' + $(this).val() + '&showForm=1'
				}).done(function(html) {
					$('#manageLanguageFormTile').empty().append(html);
					$('#progressIndicator').hide();
					waiting = false;
				});
				$('.success').remove();
				$('.error').remove();
				setTimeout(function() {
					if (waiting) {
						$('#progressIndicator').show();
					}
				}, 100);
			}).keypress(function() {
				$(this).trigger('change');
			});
		});
	</script>
</div>
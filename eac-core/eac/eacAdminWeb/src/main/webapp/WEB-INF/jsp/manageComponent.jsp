<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<spring:message code="label.pleaseSelect" var="pleaseSelect"/>
<div id="manageComponentTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.manageComponents"/></h1>
	</div>

	<c:if test="${not empty param['statusMessageKey']}">
		 <div class="success">
			<spring:message code="${param['statusMessageKey']}"/>
		</div>
	</c:if>
	
	<spring:hasBindErrors name="componentBean">
		<div class="error">
			<spring:bind path="componentBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>	
	
	<tags:breadcrumb/>
	
	<fieldset style="padding-top: 0px\9">
		<legend><spring:message code="label.components"/></legend>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.componentName"/></label>
			</div>
			<div class="fieldValue">
				<select id="selectedComponent" name="selectedComponent">
					<c:if test="${empty componentBean.selectedComponent}"><option value="pleaseSelect"><spring:message code="label.pleaseSelect"/></option></c:if>
					<c:forEach var="component" items="${componentBean.components}">
						<c:set var="selected" value=""/>
						<c:if test="${component.id == componentBean.selectedComponent.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${component.id}" ${selected}><c:out value="${component.name}"/></option>
					</c:forEach>
					<option value="new"<c:if test="${componentBean.newComponent}"> selected</c:if>><spring:message code="label.newEntry" htmlEscape="true"/></option>
				</select>
			</div>
			<c:url value="/images/progress-indicator.gif" var="progressIconUrl"/>
			<img id="progressIndicator" src="${progressIconUrl}" style="margin-left: 10px; margin-top: 2px; display:none"/>			
		</div>
	</fieldset>
	
	<div id="manageComponentFormTile">
		<c:if test="${not empty componentBean.selectedComponent || componentBean.newComponent}">
			<tiles:insertAttribute name="manageComponentFormTile"/>
		</c:if>
	</div>
		
	<script type="text/javascript">
		$(function() {
			var $selectedComponent = $('#selectedComponent');
			var $progressIndicator = $('#progressIndicator');
			var waiting = false;
			
			$selectedComponent.change(function() {
				waiting = true;
				$(this).find('option').each(function() {
					if ($(this).val() == 'pleaseSelect') {
						$(this).remove();
					}
				});				
				$.ajax({
					type: 'GET',
					url: '<c:url value="/mvc/component/manage.htm"/>',
					data: 'id=' + $(this).val()
				}).done(function(html) {
					$('#manageComponentFormTile').empty().append(html);
					$progressIndicator.hide();
					waiting = false;					
				});
				$('.success').remove();
				$('.error').remove();
				setTimeout(function() {
					if (waiting) {
						$progressIndicator.show();
					}
				}, 100);				
			}).keypress(function() {
				$(this).trigger('change');
			});
		});
	</script>
</div>
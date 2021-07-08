<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<spring:message code="label.pleaseSelect" var="pleaseSelect"/>
<div id="manageElementTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.manageElements"/></h1>
	</div>

	<c:if test="${not empty param['statusMessageKey']}">
		 <div class="success">
			<spring:message code="${param['statusMessageKey']}"/>
		</div>
	</c:if>
	
	<spring:hasBindErrors name="elementBean">
		<div class="error">
			<spring:bind path="elementBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>	
	
	<tags:breadcrumb/>
	
	<fieldset style="padding-top: 0px\9">
		<legend><spring:message code="label.elements"/></legend>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.elementName"/></label>
			</div>
			<div class="fieldValue">
				<select id="selectedElement" name="selectedElement">
					<c:if test="${empty elementBean.selectedElement}"><option value="pleaseSelect"><spring:message code="label.pleaseSelect"/></option></c:if>
					<c:forEach var="element" items="${elementBean.elements}">
						<c:set var="selected" value=""/>
						<c:if test="${element.id == elementBean.selectedElement.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${element.id}" ${selected}><c:out value="${element.name}"/></option>
					</c:forEach>
					<option value="new"<c:if test="${elementBean.newElement}"> selected</c:if>><spring:message code="label.newEntry" htmlEscape="true"/></option>
				</select>
			</div>
			<c:url value="/images/progress-indicator.gif" var="progressIconUrl"/>
			<img id="progressIndicator" src="${progressIconUrl}" style="margin-left: 10px; margin-top: 2px; display:none"/>					
		</div>
	</fieldset>
	
	<div id="manageElementFormTile">
		<c:if test="${not empty elementBean.selectedElement || elementBean.newElement}">
			<tiles:insertAttribute name="manageElementFormTile"/>
		</c:if>
	</div>
		
	<script type="text/javascript">
		$(function() {
			var $selectedElement = $('#selectedElement');
			var $progressIndicator = $('#progressIndicator');
			var waiting = false;
			
			$selectedElement.change(function() {
				waiting = true;
				$(this).find('option').each(function() {
					if ($(this).val() == 'pleaseSelect') {
						$(this).remove();
					}
				});				
				$.ajax({
					type: 'GET',
					url: '<c:url value="/mvc/element/manage.htm"/>',
					data: 'id=' + $(this).val()
				}).done(function(html) {
					$('#manageElementFormTile').empty().append(html);
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
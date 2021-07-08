<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<c:url value="/mvc/WhiteListUrls/whiteListUrls.htm" var="formUrl" />
<c:url value="/images/add.png" var="addIconUrl" />

<div id="help">
	Whitelist URL must follow below requirements:
	<ul>
		<li>Only Protocol and Host is allowed as URL.</li>
		<li>Allowed Protocol value is Http or Https</li>
    	<li>Sample for whitelist URL Http://www.oup.com, Https://www.oup.com</li>
	</ul>
	</div> 
<form:form modelAttribute="whiteListUrlBean" action="${formUrl}">
	<fieldset>
		<spring:message var="txtOrgUnitsLong" code="title.whiteListUrls" />
		<spring:message var="txtOrgUnits" code="navbar.item.whiteListUrl" />
		<spring:message var="txtOrgUnitLong" code="label.url.unit.short" />
		<spring:message var="txtOrgUnit" code="label.url.unit.short" />
		<spring:message var="txtAddOrgUnit" code="label.add.url" />

		<legend>${txtOrgUnits}</legend>
		<div id="urlWrapper"
			style="max-height: 620px; overflow-x: hidden; overflow-y: auto; margin-bottom: 15px">
			<c:forEach var="urlList" items="${whiteListUrlBean.newUrl}"
				varStatus="status">
				<fieldset class="newUrl">
					<div class="field urlField">
						<div class="fieldLabelNarrow">
							<label>${txtOrgUnit}&#160;<span class="mandatory">*</span></label>
						</div>
						<form:input path="newUrl[${status.index}].url" maxlength="255" />
						<form:errors path="newUrl[${status.index}].url"
							cssClass="orgUnitError" />
						<div class="removeNewUrl" style="float: right; cursor: pointer">
							<img class="actionImg" src="<c:url value="/images/delete.png"/>"
								width="16" height="16"  alt="${labelRemove}" title="${labelRemove}"/>
						</div>
					</div>
				</fieldset>
			</c:forEach>
			
			<c:forEach var="url" items="${whiteListUrlBean.urlList}" varStatus="status">
			<c:set var="deleted" value="${false}" />
			<c:forEach var="removedIdx" items ="${whiteListUrlBean.urlIndexesToRemove}">				
				<c:if test="${not deleted}">
					<c:if test="${status.index eq removedIdx}">
						<c:set var="deleted" value="${true}" />
					</c:if> 
				</c:if>
			</c:forEach>
			 <c:if test="${not deleted}">
				<fieldset class="url" id="${status.index}">
					<div class="field urlField">
						<div class="fieldLabelNarrow">
							<label for="">${txtOrgUnit}&#160;<span class="mandatory">*</span></label>
						</div>				
						<form:input  path="urlList[${status.index}].url" maxlength="255"/>
						<form:errors path="urlList[${status.index}].url" cssClass="orgUnitError" />
						<div class="removeUrl" style="float: right; cursor: pointer"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
					</div>
				</fieldset>
			</c:if>
		</c:forEach>
			 
		</div>
		<img src="${addIconUrl}"
			style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;" /><a
			href="#" id="addNewUrlLink">${txtAddOrgUnit}</a>
	</fieldset>
	<div id="buttons">
		<p>
			<button type="submit" id="save">
				<spring:message code="button.save" />
			</button>
			<button type="button" id="cancel">
				<spring:message code="button.cancel" />
			</button>
		</p>
	</div>
	<spring:message code="label.remove" var="labelRemove"/>
	
	<div id="newUrlPrototype">
		<fieldset class="newUrl">
			<div class="field urlField">
				<div class="fieldLabelNarrow">
					<label>${txtOrgUnit}&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<input type="text" name="newUrl[%idx%].url" disabled="disabled" maxlength="255" />
				</div>
				<div class="removeNewUrl" style="float: right; cursor: pointer"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16"/></div>
			</div>
		</fieldset>
	</div>
	<form:hidden id="urlIndexesToRemove" path="urlIndexesToRemove" />
</form:form>

<script type="text/javascript">
	$('#cancel').click(function() {
		window.location.replace('<c:url value="/home"/>');
	});
	var $newUrl = $('#newUrlPrototype');
	$newUrl.hide();
	$(function() {
		$('#addNewUrlLink').click(function() {
			var $urlWrapper = $('#urlWrapper');
			$clonedUrl = $newUrl.clone();
			$clonedUrl.removeAttr('id');
			$clonedUrl.find('input').each(function() {
				$(this).removeAttr('disabled');
			});
			$urlWrapper.prepend($clonedUrl);
			$clonedUrl.show();
			$clonedUrl.find('input:first').focus();
			$urlWrapper.scrollTop(0);
			return false;
		});
		
		$('.removeUrl').click(function() {
			var $fieldset = $(this).closest('fieldset');
			var $urlIndexesToRemove = $('#urlIndexesToRemove');
			var urlIndexesToRemove = $urlIndexesToRemove.val();
			if (urlIndexesToRemove != "") {
				urlIndexesToRemove += ",";
			}
			urlIndexesToRemove += $fieldset.attr('id');
			$urlIndexesToRemove.val(urlIndexesToRemove);
			$fieldset.hide();
		});
		
		$('#urlWrapper').delegate('.removeNewUrl', 'click', function() {
			$(this).closest('fieldset').remove();
		});
				
		$('form').submit(function() {
			var newUrlCount = 0;
			$('#urlWrapper .newUrl').each(function() {			
				$(this).find('input').each(function() {									
					$(this).attr('name', 'newUrl['+newUrlCount+'].url');
				});
				newUrlCount++;
			});
		});
	});
</script>
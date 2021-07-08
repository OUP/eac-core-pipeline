<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:url value="/images/add.png" var="addIconUrl"/>
<div id="productUrlsTile">
 <form:hidden id="urlIndexesToRemove" path="urlIndexesToRemove" /> 

		<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a id="addNewUrlLink" href="#"><spring:message code="label.addNewUrl"/></a>
		<spring:message code="info.addProductUrl" var="addProductUrlHelp" />
		<img id="addProductUrlHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${addProductUrlHelp}" style="margin-left: 5px; padding-top: 0px"/>
	</div>
	<spring:message code="title.addProductUrl" var="addProductUrlTitle"/>
	<spring:message code="info.product.url.path" var="pathHelp" />
	<spring:message code="info.product.url.expression" var="expressionHelp" />
	<div id="productUrls">
		<c:forEach var="url" items="${productBean.urls}" varStatus="status">
			<fieldset id="${status.index}">
				<div class="field">
					<div class="fieldLabel">
						<label><spring:message code="label.protocol"/></label>
					</div>
					<div class="fieldValue">
						<form:input path="urls[${status.index}].protocol" />
					</div>
					<spring:message code="label.remove" var="labelRemove"/>
					<div class="removeUrl" style="float: right; cursor: pointer"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
				</div>
				<div class="field">
					<div class="fieldLabel">
						<label><spring:message code="label.host"/></label>
					</div>
					<div class="fieldValue">
						<form:input path="urls[${status.index}].host" />
					</div>
				</div>
				<div class="field">
					<div class="fieldLabel">
						<label><spring:message code="label.path"/></label>
					</div>
					<div class="fieldValue" style="max-width: 100%">
						<form:input path="urls[${status.index}].path" class="wide"/>
					</div>
					<img class="pathHelp infoIcon" src="<c:url value="/images/information.png"/>" title="${pathHelp}"/>
				</div>
				<div class="field">
					<div class="fieldLabel">
						<label><spring:message code="label.query"/></label>
					</div>
					<div class="fieldValue">
						<form:input path="urls[${status.index}].query" class="wide" />
					</div>
				</div>
				<div class="field">
					<div class="fieldLabel">
						<label><spring:message code="label.fragment"/></label>
					</div>
					<div class="fieldValue">
						<form:input path="urls[${status.index}].fragment" class="wide"/>
					</div>
				</div>
				<div class="field">
					<div class="fieldLabel">
						<label><spring:message code="label.expression"/></label>
					</div>
					<div class="fieldValue" style="max-width: 100%">
						<form:input path="urls[${status.index}].expression" class="wide" />
					</div>
					<img class="expressionHelp infoIcon" src="<c:url value="/images/information.png"/>" title="${expressionHelp}"/>
				</div>
			</fieldset>
		</c:forEach>
	</div>
	<div id="newUrlPrototype">
		<fieldset class="newUrl">
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.protocol"/></label>
				</div>
				<div class="fieldValue">
					<input name="newUrls[%idx%].protocol" type="text" disabled="disabled"/>
				</div>
				<spring:message code="label.remove" var="labelRemove"/>
				<div class="removeNewUrl" style="float: right; cursor: pointer"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.host"/></label>
				</div>
				<div class="fieldValue">
					<input name="newUrls[%idx%].host" type="text" disabled="disabled" />
				</div>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.path"/></label>
				</div>
				<div class="fieldValue" style="max-width: 100%">
					<input name="newUrls[%idx%].path" type="text" disabled="disabled" class="wide" />
				</div>
				<img class="pathHelp infoIcon" src="<c:url value="/images/information.png"/>" title="${pathHelp}"/>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.query"/></label>
				</div>
				<div class="fieldValue">
					<input name="newUrls[%idx%].query" type="text" disabled="disabled" class="wide" />
				</div>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.fragment"/></label>
				</div>
				<div class="fieldValue">
					<input name="newUrls[%idx%].fragment" type="text" disabled="disabled" class="wide" />
				</div>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.expression"/></label>
				</div>
				<div class="fieldValue" style="max-width: 100%">
					<input name="newUrls[%idx%].expression" type="text" disabled="disabled" class="wide" />
				</div>
				<img class="expressionHelp infoIcon" src="<c:url value="/images/information.png"/>" title="${expressionHelp}"/>
			</div>
		</fieldset>
	</div>
<script type="text/javascript">
	$(function() {
		var $newUrlFieldset = $('#newUrlPrototype fieldset');
		$newUrlFieldset.hide();
		$('#addNewUrlLink').click(function() {
			$clonedUrlFieldset = $newUrlFieldset.clone();
			$clonedUrlFieldset.removeAttr('id');
			$clonedUrlFieldset.find('input').each(function() {
				$(this).removeAttr('disabled');
			});
			$('#productUrls').append($clonedUrlFieldset);
			$clonedUrlFieldset.show();
			$clonedUrlFieldset.find('input:first').focus();
			enableToolTips();
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
		
		$('#productUrls').delegate('.removeNewUrl', 'click', function() {
			$(this).closest('fieldset').remove();
		});
		
		$('form').submit(function() {
			var newUrlCount = 0;
			$('#productUrls .newUrl').each(function() {
				$(this).find('input').each(function() {
					
					var name = $(this).attr('name');
					$(this).attr('name', name.replace(/%idx%/, newUrlCount));
				});
				newUrlCount++;
			});
		});
		
		$("#addProductUrlHelp").tipTip({edgeOffset: 7});
		
		// A bug in TipTip prevents us from reinitialising it
		// for the info icons in the new URL blocks that are
		// added when the user clicks the 'Add new URL' link.
		// As a result, we have to use the standard browser
		// title tooltips.
		  
	});
</script>
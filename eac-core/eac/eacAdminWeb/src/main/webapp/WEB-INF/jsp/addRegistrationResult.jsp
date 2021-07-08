<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<script type="text/javascript">
	$(function() {
		$('#addRegistrationPopup').append('<div class="progressMessageContainer"><h2 class="progressMessage"><spring:message code="label.loading"/></h2></div>');
		$('#addRegistrationPopup').parent().find('.ui-button').each(function() {
			$(this).css('visibility', 'hidden');
		});
		var pageAddress = window.location.href;
		if (pageAddress.substr(pageAddress.length-1) === '#') {
			pageAddress = pageAddress.substr(0, pageAddress.length-1);
		}
		
		<c:choose>
			<c:when test="${failedToAddRegistration}">
				console.log("FAILED TO ADD REGISTRATION");
				window.location.replace(pageAddress + '&_eventId_reloadRegistrationsError=1');
			</c:when>
			<c:otherwise>
				console.log("ADDED REGISTRATION SUCCESSFULLY");
				window.location.replace(pageAddress + '&_eventId_reloadRegistrations=1');
			</c:otherwise>
		</c:choose>
	});
</script>
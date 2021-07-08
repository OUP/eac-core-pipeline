<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="resetPasswordTile">
	<c:if test="${not empty processing}">
		<fieldset>
			<c:url value="/" var="loginUrl"/>
			<spring:message code="info.resetPassword"/>&#160;<a href="${loginUrl}"><spring:message code="label.logIn"/></a>
		</fieldset>
	</c:if>
	<c:url value="/mvc/password/reset.htm" var="resetPasswordUrl"/>
	<form method="POST" action="${resetPasswordUrl}">
		<fieldset>
				<legend><spring:message code="title.resetPassword"/></legend>
				<div class="field" style="margin-top: 20px">
					<div class="fieldLabelNarrow">
						<label><spring:message code="label.username"/></label>
					</div>
					<div class="fieldValue">
						<input id="username" type="text" name="username"/>
					</div>
				</div>
				<div id="buttons" style="margin-top: 10px; margin-right: 18px">
					<button id="submit" type="submit" class="button"><spring:message code="button.resetPassword"/></button>
				</div>
		</fieldset>
	</form>
	<script type="text/javascript">
		$(function() {
			$('#username').focus();
		});
	</script>
</div>
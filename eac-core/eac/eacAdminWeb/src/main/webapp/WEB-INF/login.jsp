<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@page import="com.oup.eac.admin.security.UsernameCachingAuthenticationFailureHandler"%>
<div id="login" style="width: 400px;">
	<div>
		<c:if test="${not empty param.login_error}">
			<div class="error">
				<p><spring:message code="error.login.unsuccessful"/></p>
			</div>
		</c:if>
		<div id="capsLock" class="error" style="display:none">
				<p><spring:message code="error.login.capson"/></p>
		</div>
	</div>
	<c:set var="statusMessageKey" value="${param['statusMessageKey']}"/>
	<c:if test="${statusMessageKey ne null}">
		 <div class="success">
			<spring:message code="${statusMessageKey}"/>
		</div>
	</c:if>					
	<form name="f" action="<c:url value="/loginProcess" />" method="post">
		<fieldset style="padding-bottom: 0px\9; padding-left: 40px; padding-right: 40px; text-align: left">
			<legend><spring:message code="label.loginInformation"/></legend>
			<div style="padding-top:20px">
				<div style="width: 8em; float:left; text-align: left">
					<label for="j_username"><spring:message code="label.username"/></label>
				</div>
				<div>
					<p><input type="text" name="j_username" style="width: 20em; font-size: 1.1em; font-family: Verdana,Arial,sans-serif" id="j_username" <c:if test="${not empty param.login_error}">value="<%= session.getAttribute(UsernameCachingAuthenticationFailureHandler.SPRING_SECURITY_LAST_USERNAME_KEY) %>"</c:if> /></p>
				</div>
			</div>
			<div>
				<div style="width: 8em; float:left; text-align: left">
					<label for="j_password"><spring:message code="label.password"/></label>
				</div>
				<div>
					<p><input type="password" name="j_password" style="width: 20em; font-size: 1.1em; font-family: Verdana,Arial,sans-serif" id="j_password" /></p>
				</div>
			</div>
			<div id="resetPassword" style="float: left; padding-top: 10px">
				<c:url value="/mvc/password/reset.htm" var="resetPasswordUrl"/>
				<a href="${resetPasswordUrl}"><spring:message code="label.forgottenPassword"/></a>
			</div>
			<div id="buttons" style="float:right; padding-right: 2px">
				<button id="submit" type="submit"><spring:message code="label.login"/></button>
			</div>				
		</fieldset>
	</form>
	<p style="padding-top: 15px\9; text-align: center"><spring:message code="info.login.misuse"/></p>
</div>
<script type="text/javascript">

	$(function() {
		var options = {
				caps_lock_on: function() {
					$('#capsLock').show();
				},
				caps_lock_off: function() {
					$('#capsLock').hide();
				},
				caps_lock_undetermined: function() {
					$('#capsLock').hide();
				}
			};
			$("input[type='password']").capslock(options);
			
			if (window.location.href.indexOf('login_error') != -1 && $('#j_username').val()) {
				$('#j_password').focus();
			} else {
				$('#j_username').focus();
			}
	});
	
</script>
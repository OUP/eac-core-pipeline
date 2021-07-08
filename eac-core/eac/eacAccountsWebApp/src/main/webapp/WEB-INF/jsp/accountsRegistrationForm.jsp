<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="no-js" lang="en">
<head>
	<title><spring:message code="title.accountregistration" text="?title.accountregistration?" /></title>
	<meta name="WT.cg_s" content="Registration"/>
</head>
<body>
	<div class="row">
		<div class="col small-12">
			<main class="box box--noPadding">
				<div class="stepHeading stepHeading--active"><span class="stepHeading_number">1</span> Log in or register</div>
				<div class="box_inner">
					<h1>Register</h1>
					<p class="message message--info">If you have previously registered for an account, please <strong><a href="<c:url value="/login.htm"/>">Log in</a></strong> now.</p>
					<spring:hasBindErrors name="accountRegistrationDto">
						<div class="error">
							 <c:forEach var="error" items="${errors.allErrors}">
								<p class="message message--error"><eac-common:message code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}"/></p>					
				            </c:forEach>
						</div>
					</spring:hasBindErrors>
					
					<form:form modelAttribute="accountRegistrationDto" method="POST" id="register-form" cssClass="form">
						<p class="form_row">
							<label class="form_label" for="firstName"><spring:message code="label.firstname" text="?label.firstname?"/></label>
							<form:input class="form_input" id="firstName" name="firstName" path="firstName"/>
						</p>
						<p class="form_row">
							<label class="form_label" for="familyName"><spring:message code="label.familyname" text="?label.familyname?" /></label>
							<form:input class="form_input" path="familyName" id="familyName" name="familyName"/>
						</p>
						<p class="form_row">
							<label class="form_label" for="username">Username</label>
							<form:input class="form_input" name="username" path="username" id="username" type="email"/>
							<span class="form_hint">Your username must be a valid email address.</span>
						</p>
						<form:hidden path="email" id="email"/>
						<form:hidden id="timezone" path="timeZoneId" />
						
						<script type="text/javascript">
							$(function() {
								//To set username as email
								var $email    = $('#email');
								var $username = $('#username');
								
								$username.change(function() {
									$email.val($username.val());
								});
								
								//To set timezone
								var timezone = jstz.determine_timezone();
				                var timezoneName = timezone.name();
				                
				                if(timezoneName) {
				                    $('#timezone').attr('value',timezoneName);
				                    debug('Using olson timeZoneId',timezoneName); // Olson database timezone key (ex: Europe/Berlin)
				                }
							});
						</script>
						<p class="form_row">
							<label class="form_label" for="password"><spring:message code="label.password.account" text="?label.password?" /></label>
							<form:password class="form_input" id="password" name="password" path="password" />
							<span class="form_hint">Minimum of 6 characters, one or more letters and capital letters.</span>
						</p>
						<p class="form_row">
							<label class="form_label" for="confirmPassword"><spring:message code="label.confirmpassword" text="?label.confirmpassword?" /></label>
							<form:password class="form_input" id="confirmPassword" name="confirmPassword" path="confirmPassword"/>
						</p>
						<p class="form_row">
							<label class="form_label form_label--checkbox" for="readOnly">
							<c:set var="eacUrl">${pageContext.request.requestURL}</c:set>
							<c:set var="eacWebUrlIndex">${fn:indexOf(eacUrl,'eacAccounts')}</c:set>
							<c:set var="eacWebUrl">${fn:substring(eacUrl,0,eacWebUrlIndex)}eac/</c:set>
								<form:checkbox class="form_checkbox" name="readOnly" path="readOnly" id="readOnly"/>I agree to the <a href="${eacWebUrl}privacyAndLegal.htm" target="_blank">terms and conditions</a> and, if I am 13 or under, I have obtained permission to agree from a parent or guardian.
							</label>
						</p>
						<p class="form_row">
							<label class="form_label form_label" for="readOnly">								
								<input type="hidden" name="_readOnly" value="on">Our <a href="https://global.oup.com/privacy">Privacy Policy</a> sets out how Oxford University Press handles your personal information, and your rights to object to your personal information being used for marketing to you or being processed as part of our business activities.

							</label>
						</p>
						<p class="form_row">
							<label class="form_label form_label" for="readOnly">								
								<input type="hidden" name="_readOnly" value="on">We will only use your personal information to provide you with access to OUP's online products and services.

							</label>
						</p>
						<p><button class="button" type="submit"><spring:message code="submit.accountregistration" text="?submit.accountregistration?" /></button></p>
					</form:form>
				</div>
				<div class="stepHeading"><span class="stepHeading_number">2</span> Activate content</div>
				<div class="stepHeading"><span class="stepHeading_number">3</span> Download app</div>
			</main>
		</div>
	</div>
		<script type="text/javascript">
			$(function() {
				$('#firstName').focus();
			});
		</script>
</body>
</html>
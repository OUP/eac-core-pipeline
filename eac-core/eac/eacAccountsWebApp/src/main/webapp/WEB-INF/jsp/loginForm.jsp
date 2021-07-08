<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="no-js" lang="en">
	<head>
		<title><spring:message code="title.login" text="?title.login?" /></title>
		<meta name="WT.cg_s" content="Login"/>
	</head>
	<body>
		<div class="row">
			<div class="col small-12">
				<main class="box box--noPadding">
					<div class="stepHeading stepHeading--active"><span class="stepHeading_number">1</span> Log in or register</div>
					<div class="box_inner">
						<h1><spring:message code="label.login" text="Log in" /></h1>
						<p class="message message--info">Don't have an account? <strong><a href="<c:url value="/registration.htm"/>"><spring:message code="label.register" text="?label.register?"/></a></strong> now.</p>
						
						<spring:hasBindErrors name="loginDto">
							<div class="error">
								 <c:forEach var="error" items="${errors.allErrors}">
									<p class="message message--error"><eac-common:message code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}"/></p>					
					            </c:forEach>
							</div>
						</spring:hasBindErrors>
						
						<form:form modelAttribute="loginDto" method="POST" id="sign-in-form" cssClass="form">
							<p class="form_row">
								<label class="form_label" for="username"><spring:message code="label.username" text="?label.username?"/></label>
								<form:input cssClass="form_input" id="username" path="username" name="username" placeholder="e.g. your email address"/>
							</p>
							<p class="form_row">
								<label class="form_label" for="password"><spring:message code="label.password" text="?label.password?"/></label>
								<form:password  cssClass="form_input" id="password" name="password" path="password" />
							</p>
							<p><strong><a href="<c:url value="/passwordReset.htm"/>"><spring:message code="label.forgot.password" text="?label.forgot.password?"/>?</a></strong></p>
							<p><button class="button" type="submit"><spring:message code="submit.login" text="?submit.login?" /></button></p>
						</form:form>
					</div>
					<div class="stepHeading"><span class="stepHeading_number">2</span> Activate content</div>
					<div class="stepHeading"><span class="stepHeading_number">3</span> Download app</div>
				</main>
			</div>
		</div>
		<script type="text/javascript">
			$(function() {
				$('#username').focus();
			});
		</script>
	</body>
</html>
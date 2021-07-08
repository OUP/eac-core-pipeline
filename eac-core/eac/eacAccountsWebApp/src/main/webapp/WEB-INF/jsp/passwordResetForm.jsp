<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><spring:message code="title.resetpassword" text="?title.resetpassword?" /></title>
	<meta name="WT.cg_s" content="Reset Password"/>
</head>
<body>
	<div class="row">
		<div class="col small-12">
			<main class="box">
				<h1>Reset password</h1>
				<spring:hasBindErrors name="passwordResetDto">
							<div class="error">
								 <c:forEach var="error" items="${errors.allErrors}">
									<p class="message message--error"><eac-common:message code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}"/></p>					
					            </c:forEach>
							</div>
						</spring:hasBindErrors>
				<form:form id="reset-password-form" cssClass="form" modelAttribute="passwordResetDto" method="POST">
					<p class="form_row">
						<label class="form_label" for="username"><spring:message code="label.username" text="?label.username?" /></label>
						<form:input class="form_input" id="username" name="username" path="username" />
					</p>
					<p><button class="button" type="submit"><spring:message code="submit.resetpassword" text="?submit.resetpassword?" /></button></p>
				</form:form>
				<p><a href="<c:url value="/login.htm"/>">&larr; Back to sign in</a></p>
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
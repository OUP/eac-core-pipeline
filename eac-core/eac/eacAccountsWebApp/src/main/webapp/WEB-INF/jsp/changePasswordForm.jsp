<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><spring:message code="title.changepassword" text="?title.changepassword?" /></title>
	<meta name="WT.cg_s" content="Change Password"/>
</head>

<body>
	<div class="row">
		<div class="col small-12">
			<%@ include file="/WEB-INF/jsp/info.jsp" %>
			<main class="box">
				<h1><spring:message code="title.changepassword" text="?title.changepassword?" /></h1>
				<spring:hasBindErrors name="changePasswordDto">
					<div class="error">
						 <c:forEach var="error" items="${errors.allErrors}">
							<p class="message message--error"><eac-common:message code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}"/></p>					
			            </c:forEach>
					</div>
				</spring:hasBindErrors>
				<form:form id="change-password-form" cssClass="form" modelAttribute="changePasswordDto" method="POST">
					<p class="form_row">
						<label class="form_label" for="newPassword"><spring:message code="label.newpassword" text="?label.newpassword?" /></label>
						<form:password  class="form_input" id="newPassword" name="newPassword" path="newPassword" />
					</p>
					<p class="form_row">
						<label class="form_label" for="confirmNewPassword"><spring:message code="label.confirmnewpassword" text="?label.confirmnewpassword?" /></label>
						<form:password  class="form_input" id="confirmNewPassword" name="confirmNewPassword" path="confirmNewPassword" />
					</p>
					<p><button class="button" type="submit"><spring:message code="submit.changepassword" text="?submit.changepassword?" /></button></p>
					<input type="hidden" name="username"  id="username" value="${username}" >
				</form:form>
			</main>
		</div>
	</div>
	<script type="text/javascript">
			$(function() {
				$('#newPassword').focus();
			});
		</script>
</body>
</html>
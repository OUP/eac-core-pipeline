<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
	  <title><spring:message code="title.resetpassword" text="?title.resetpassword?" /></title>
	  <meta name="WT.cg_s" content="Reset Password"/>
	</head>
    <body class="stickyFooter">
    <section class="content">
			<div class="row">
				<div class="col large-8 large-centered">
					<div class="box">
						<h1><spring:message code="label.resetpassword" text="?label.resetpassword?" /></h1>
						<tags:errors name="passwordResetFbo"/>
							<p><spring:message code="help.resetpassword" text="?help.resetpassword?"/></p>
						<form:form cssClass="form" id="reset-form" method="post" commandName="passwordResetFbo">
						<tags:inSync/>
							<div class="form_row">
							<spring:message var="elementTitle" code="title.username" text="?title.username?" />
		                    <label class="form_label" for="username"><spring:message code="label.username" text="?label.username?" /></label>
		                    <form:input id="username" path="username" cssClass="form_input" title="${elementTitle}" maxlength="255" />
							</div>

							<div class="form_row">
								<button class="button" type="submit"><spring:message code="submit.resetpassword" text="?submit.resetpassword?" /></button>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</section>
    
		<script src="js/core.min.js"></script>
			<script>
			(function($) {

				$("#reset-form").validate({
						rules: {
								username: {
								required: true,
							},
						},
						messages: {
							username: "Please enter your username.",
							}
					});
			}(jQuery));
		</script>
    
    </body>
</html>
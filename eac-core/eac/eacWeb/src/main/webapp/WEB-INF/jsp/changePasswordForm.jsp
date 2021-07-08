<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en"> -->
<!DOCTYPE html>
<html class="no-js" lang="en">
	<head>
	  <title><spring:message code="title.changepassword" text="?title.changepassword?" /></title>
	  <meta name="WT.cg_s" content="Change Password"/>
	</head>
    <body class="stickyFooter">
 <section class="content">
			<div class="row">
				<div class="col large-8 large-centered">
					<div class="box">
					
						<h1><spring:message code="title.changepassword" text="?title.changepassword?" /></h1>
						<tags:inSync/>
					<tags:errors name="changePasswordDto"/>
						<form:form cssClass="form" id="change-Pass-form" method="post" commandName="changePasswordDto">
						<tags:inSync/>
							<div class="form_row">
							<spring:message var="elementTitle" code="title.newpassword" text="?title.newpassword?" />
								<label class="form_label" for="passwordcheck"><spring:message code="label.newpassword" text="?label.newpassword?" /></label>
								<form:password id="passwordcheck" cssClass="form_input myPassword" path="passwordcheck" />
								<span class="form_hint"><spring:message code="label.password.info" text="Minimum of 6 characters, one or more letters and capital letters." /></span>
							</div>

							<div class="form_row">
							<spring:message var="elementTitle" code="title.confirmnewpassword" text="?title.confirmnewpassword?" />
								<label class="form_label" for="confirmNewPassword"><spring:message code="label.confirmnewpassword" text="?label.confirmnewpassword?" /></label>
								<form:password id="confirmNewPassword" path="confirmNewPassword" cssClass="form_input"/>
							</div>

							<div class="form_row">
								<div>
									<%-- <input type="button" id="submitButton" value="<spring:message code="submit.changepassword" text="?submit.changepassword?" />" onClick = "onSubmit();"/> --%>
									<button class="button" id="submitButton" type="submit"><spring:message code="submit.changepassword" text="?submit.changepassword?" /></button>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</section>
	 <script src="js/core.min.js"></script>
		<script>
			(function($) {

				$("#change-Pass-form").validate({
						rules: {
							passwordcheck: {
								required: true,
								EACpassword: true
							},
							confirmNewPassword: {
								required: true,
								equalTo: "#passwordcheck"
							},
						},
						messages: {
							passwordcheck: { 
								required: "Please enter new password."
							},
							confirmNewPassword: {
								required: "Please confirm new password.",
								equalTo: "Please enter the same password as above."
							},
						},
						submitHandler: function (form) {
							document.forms[0].submit();
						}
					});
			}(jQuery));
		</script>
		<script type="text/javascript">
	         /* $(document).ready(function() { 
                 $("#passwordcheck").addClass("passwordcheck");
                 $("#passwordcheck").valid();                
            });   */
        
	        /* function onSubmit(){           
	            document.forms[0].submit();
	        } */
	        
	        $("#change-Pass-form").submit(function(e){
		       
				e.preventDefault();   
			});
      </script>
    </body>
    
</html>
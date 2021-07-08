<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
	  <title><spring:message code="title.login" text="?title.login?" /></title>
	  <meta name="WT.cg_s" content="Login"/>
	  	
		
		
		<!-- Global site tag (gtag.js) - Google Analytics -->
<!-- <script async src="https://www.googletagmanager.com/gtag/js?id=UA-142235805-1"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-142235805-1');
</script> -->


 
	</head>
	<body class="stickyFooter">
	
		
	
	<section class="content">
				<div class="row">
					<div class="col large-8 large-centered">
						<div class="box">
							<h1><spring:message code="label.login" text="Log in" /></h1>
							<div class="row">
								<div class="col medium-7">
									<form:form cssClass="form" id="login-form" method="post" commandName="loginFbo">

										<tags:errors name="loginFbo"/>
							<div class="form_row">
								<spring:message var="elementTitle" code="title.username"
									text="?title.username?" />
								<label class="form_label" for="username"><spring:message
										code="label.username" text="?label.username?" /></label>
								<form:input id="username" path="username" 
									title="${elementTitle}" maxlength="255" 
									cssClass="form_input form_input--fullWidth"
									 />
							</div>
							<div class="form_row">
							<spring:message var="elementTitle" code="title.password" text="?title.password?" />
							<label class="form_label" for="password"><spring:message code="label.password" text="?label.password?"/></label>
											<form:password id="password" path="password" cssClass="form_input form_input--fullWidth" title="${elementTitle}" maxlength="255" />
											<!-- <input class="form_input form_input--fullWidth" type="password" name="password" id="password" required> --><br />
											<span class="form_hint"><a href="passwordReset.htm" class="position-link"><spring:message code="label.forgot.password" text="?label.forgot.password?"/></a></span>
										</div>
										<div class="form_row">
                        <button class="button" type="submit"><spring:message code="submit.login" text="?submit.login?" /></button>
                        
                    </div>
									</form:form>
								</div>
								<c:if test="${sessionScope.PRODUCT ne null && sessionScope.PRODUCT.selfRegisterable}">
	                           <div class="col medium-5">
									<div class="message">
										<h3><spring:message code="login.not.already.registered" text="Not already registered?" /></h3>
										<p><spring:message code="label.create.login.account" text="If you are a new customer, please create an account to log in." /></p>
										<a href="register.htm?url=${param.url}" class="button button--secondary"><spring:message code="account.create" text="Create Account" /></a>
									</div>
								</div>
	                         </c:if>
							</div>
						</div>
					</div>
				</div>
			</section>

		<script src="js/core.min.js"></script>
		<script>
			(function($) {

				$("#login-form").validate({
					rules: {
						username: "required",
						password: "required"
					},
					messages: {
						username: "Please enter your username.",
						password: "Please enter your password."
					}
				});
				
				dataLayer.push({
					 'event':'VirtualPageview',
				      'virtualPageURL':'/eac/login.htm',
				      'virtualPageTitle':'OUP EAC login page'
				})

			}(jQuery));
		</script>
	</body>
</html>
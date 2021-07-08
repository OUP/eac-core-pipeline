<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<title><spring:message code="title.accountregistration" text="?title.accountregistration?" /></title>
    	<meta name="WT.cg_s" content="Registration"/>
    	<!-- <link rel="stylesheet" type="text/css" media="screen" href="css/jquery.validate.password.css" /> -->
    	 
    	<script type="text/javascript">
            /* <![CDATA[ */
            $(function(){
                var debug = function() {
                    if(typeof console != 'undefined'){
                       // console.log(arguments);
                    }
                };
                var timezone = jstz.determine_timezone();
                var timezoneName = timezone.name();
                
                if(timezoneName) {
                    $('#timezone').attr('value',timezoneName);
                    debug('Using olson timeZoneId',timezoneName); // Olson database timezone key (ex: Europe/Berlin)
                }
                keepPageAlive({ hours: 2 });
		  	});
		  	
            /* keepPageAlive({ hours: 2 }); */
            
            /* ]]> */
        </script>
	</head>
	<body class="stickyFooter">
	<section class="content">
			<eac:progressbar page="ACCOUNT_REGISTRATION"/>
			<c:set var="disableUserNameInput" value="${accountRegistrationDto.disableInputFlag}"/>
			
			<div class="row">
				<div class="col large-8 large-centered">
					<div class="box">
						<tags:errorsAccountProductRegistration name="accountRegistrationDto"/>
						<h1><spring:message code="${accountRegistrationDto.title}" text="Account Registration" /></h1>
						
						<p><spring:message code="label.form.mandatory" text="Fields marked with * are mandatory" /></p>
						<div class="message message--info">
						<eac:loginurl var="loginUrl" propertyName="eac.login.url"/>
							<p><spring:message code="label.create.login.info" text="?label.create.login.info?" arguments="${loginUrl}"/></p>
						</div>
						<spring:message var="emailUsernameMistmatchWarning" code="warning.email.username.mismatch" text="?warning.email.username.mismatch?" />
						<form:form cssClass="form" method="post" commandName="accountRegistrationDto" id="register-form">
						<tags:inSync/>
							<h2><spring:message code="label.personaldetails" text="?label.personaldetails?" /></h2>
							<div class="form_row">
						<spring:message var="elementTitle" code="title.firstname"
							text="?title.firstname?" />
						<label class="form_label" for="firstName"><spring:message
								code="label.firstname" text="?label.firstname?" /> <span
							class="form_required">*</span></label>
						<form:input id="firstName" path="firstName"
							cssClass="form_input" title="${elementTitle}" 
							maxlength="255" />
							<form:errors path="firstName" cssClass="error">
														</form:errors>
							
					</div>
							<div class="form_row">
						<spring:message var="elementTitle" code="title.familyname"
							text="?title.familyname?" />
						<label class="form_label" for="familyName"><spring:message
								code="label.familyname" text="?label.familyname?" /> <span
							class="form_required">*</span></label>
						<form:input id="familyName" path="familyName"
							cssClass="form_input" title="${elementTitle}"
							maxlength="255" />
							<form:errors path="familyName" cssClass="error"></form:errors>
					</div>
							<div class="form_row">
								<spring:message var="elementTitle" code="title.email" text="?title.email?" />
						<label class="form_label" for="email"><spring:message
								code="label.email" text="?label.email?" /> <span
							class="form_required">*</span></label>
						<form:input id="email" path="email" cssClass="form_input"
							title="${elementTitle}" maxlength="255" />
							<form:errors path="email" cssClass="error"></form:errors>
					</div>
							<h2><spring:message code="label.logindetails" text="?label.logindetails?" /></h2>
							<div class="form_row">
								<spring:message var="elementTitle" code="title.username" text="?title.username?" />
                            <label class="form_label" for="username"><spring:message code="label.username.account" text="?label.username?" /> <span class="form_required">*</span></label>
                            <form:input id="username" path="username" cssClass="form_input" title="${elementTitle}" maxlength="255" readonly="${disableUserNameInput}"/>
                            <form:errors path="username" cssClass="error"></form:errors>
							</div>
							<div class="form_row">
								<spring:message var="elementTitle" code="title.password" text="?title.password?" />
							<label class="form_label" for="passwordcheck">
							<spring:message code="label.password.account" text="?label.password?" /> <span class="form_required">*</span></label>
							<form:password id="passwordcheck" path="passwordcheck" cssClass="form_input myPassword"/>
								<span class="form_hint">Minimum of 6 characters, one or more letters and capital letters </span>
								<form:errors path="passwordcheck" cssClass="error"></form:errors>
							</div>
							<div class="form_row">
								<spring:message var="elementTitle" code="title.confirmpassword" text="?title.confirmpassword?" />
						<label class="form_label" for="confirmPassword"> <spring:message
								code="label.confirmpassword" text="?label.confirmpassword?" /> <span
							class="form_required">*</span></label>
						<form:password id="confirmPassword" path="confirmPassword"
							cssClass="form_input"/>
							<form:errors path="confirmPassword" cssClass="error"></form:errors>
					</div>
					<form:hidden id="timezone" path="timeZoneId" />
 							<%-- <eac-common:eacform regDto="${accountRegistrationDto}"/> --%>
 							<c:set var="regDto" value="${accountRegistrationDto}"></c:set>
 							<%@include file="/WEB-INF/jsp/eacFormTag.jsp"%>
 							<div class="form_row">
								<button type="submit" class="button" id="submitButton" onclick="onSubmit();"/><spring:message code="submit.accountregistration" text="?submit.accountregistration?" /></button>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</section>
	<script src="js/core.min.js"></script>
      <script>
			(function($) {
			
				    
				   $('#passwordcheck').keyup(function(e){
					   $("#register-form").validate({
							rules: {
								passwordcheck: {
									//required: true,
									EACpassword: true
								}
								}
							
						}); 
					   var t=$('#passwordcheck').attr("class");	
					   if(t.toLowerCase().indexOf("error") >= 0)
						   $('.form_hint').next().hide();
						   
					   });
						if($('#username').next().text().toLowerCase().indexOf("href")>=0)
							$('#username').next().hide();
							   
				$("#email").on('keyup blur', function() {
					if (!$("#username").data('manually-changed')) { $("#username").val($(this).val()); }
					});
				
					var isUserNameReadOnly = $('#username').is('[readonly]')
					
					if(!isUserNameReadOnly){
						$("#username").one('keyup', function() { $(this).data('manually-changed', true); });
					}

					if($("#username").val().toLowerCase().indexOf("href")>=0)
						$("#username").hide();
					var ids = $('.error').map(function () {
					    return this.id;
					}).get().join();
					
					if (!$("#username").data('manually-changed') && isUserNameReadOnly) { 
						$("#username").css("background-color", "#dddddd");
					}

					var result = ids.split('.errors').join('');

					var array = result.split(',');
					jQuery.each(array, function(index, value) {
					    //   console.log(value);
					    	if (value.toLowerCase().indexOf("answer") >= 0)
						    	{
						    	}
					    	else{
					       $('#'+value).removeClass('form_input').addClass('form_input error');

					       $(function(){
					    	    $('#'+value).keyup(function(e){
					    	        var This_id = $(this).attr("id");
					    	        $($('#'+This_id).next()).hide();
					    	        $('#'+This_id).removeClass('error');
					    	        console.log($('#'+This_id));
					    	       
					    	    });
					    	});



					       }

						       
						       
					   });
					
						var temp=$(array).first().get(0);
						if (temp.toLowerCase().indexOf("answer") >= 0){}
						else{
						$('#'+temp).focus();}

						
												
					
					}(jQuery));

			</script>
					<script>

		function removeText(arg){
						console.log($(arg).attr("id"));
						var id=$(arg).attr("id");
						$('#'+id).removeClass("error");
						$($('#'+id).next()).hide();
						}

		function removeCheckBoxText(arg){
			//console.log($(arg).attr("id"));
			var id=$(arg).attr("id");;
			var classTemp=$($($('#'+id).parent()).next()).attr("class");
			if(classTemp=='error')
			$($($('#'+id).parent()).next()).hide();
			
			}

		function removeCheckBoxTextMulti(arg)
		{

			var classTemp=$($('.form_checkboxWrap_tmp.error').prev()).attr("class")
			if(classTemp=='error')
			$($('.form_checkboxWrap_tmp.error').prev()).hide()
			
			}

		function onSubmit(){
            document.forms[0].submit();
        }
		
		</script>
	</body>
</html>
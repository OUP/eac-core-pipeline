<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title><spring:message code="profile.basic.title" text="?profile.basic.title?" /></title>
    <meta name="WT.cg_s" content="My Profile"/>
    <eac-common:requestMethod var="requestMethod"/>
    <c:set var="isGet" value="${requestMethod eq 'GET' }" />
    <c:if test="${isGet}" >
        <script type="text/javascript">
            if (typeof console === "undefined" || typeof console.log === "undefined") var console = { log: function() {} };
            
            var debug = function() {
                console.log(arguments);
            };

            jQuery(function($){
                var existingTZ = $('#timezone').attr('value');
                debug("existing TZ",existingTZ);
                
                if(existingTZ == ''){
                    
                    var timezone = jstz.determine_timezone(); // Now you have an instance of the TimeZone object.
                    var timezoneName = timezone.name();

                    debug('offset in hours and minutes from UTC',timezone.offset()); // Offset in hours and minutes from UTC.
                    debug('olson database key',timezone.name()); // Olson database timezone key (ex: Europe/Berlin)
                    debug('uses Daylight Saving?',timezone.dst()); // bool for whether the tz uses daylight saving time

                    if(timezoneName) {
                        var option = $('#timezone option:contains("'+timezoneName+'")');
                        if(option) {
                            $('#timezone').attr('value', timezoneName);
                            debug('just set derived timezone', timezoneName);
                        }
                    }
                }
            });
        </script>
    </c:if>
</head>
<body class="stickyFooter">
    <spring:message var="titleRegTable" code="profile.basic.title.registration.table" text="?profile.basic.title.registration.table?" />      
    <spring:message var="titleForm"     code="profile.basic.title.form" text="?profile.basic.title.form?" />      
    <spring:message var="titleFormReadOnly"     code="profile.basic.title.form.read.only" text="?profile.basic.title.form.read.only?" />
    <spring:message var="pleaseSelect"  code="profile.basic.please.select" text="?profile.basic.please.select?" />
    
    <section class="content">
			<h1><spring:message code="profile.basic.title" text="?profile.basic.title?" /></h1>
			<tags:errors name="basicProfileDto"/>
			<div class="row">
				<div class="col medium-5">
					<div class="box">
					<c:if test="${not empty profileFlash}">
					<div class="message message--success" id="successMessage">
							<c:forEach items="${profileFlash}" var="item">
							<p><spring:message code="${item.key}" arguments="${item.value}" text="?${item.key}?"/> <br/>
							</p>
							</c:forEach>
						</div>
					</c:if>
					<spring:message var="emailUsernameMistmatchWarning" code="warning.email.username.mismatch" text="?warning.email.username.mismatch?" />
					<form:form cssClass="form" id="profile-form" method="post" commandName="basicProfileDto">
					<tags:inSync/>
					<c:if test="${basicProfileDto.readOnly}">
	                	<div class="form_row">
								<h2>${titleFormReadOnly}</h2>
						</div>
                	</c:if>
							<div class="form_row">
								<h2><spring:message code="label.personaldetails" text="?label.personaldetails?" /></h2>
								<spring:message var="elementTitle" code="title.firstname" text="?title.firstname?" />
		                        <label class="form_label"  for="firstName">${elementTitle}</label>
        		                <form:input id="firstName" path="firstName" cssClass="form_input" title="${elementTitle}" maxlength="255" disabled="${basicProfileDto.readOnly}"/>
							</div>
							<div class="form_row">
								<spring:message var="elementTitle" code="title.familyname" text="?title.famlyname?" />
                        		<label class="form_label" for="familyName">${elementTitle}</label>
                        		<form:input id="familyName" path="familyName" cssClass="form_input" title="${elementTitle}" maxlength="255" disabled="${basicProfileDto.readOnly}"/>
							</div>
							<div class="form_row">
								<spring:message var="elementTitle" code="title.email" text="?title.email?" />
		                        <label class="form_label" for="email">${elementTitle}&#160;<span class="form_required">*</span></label>
        		                <form:input id="email" path="email" cssClass="form_input" title="${elementTitle}" maxlength="255" disabled="${basicProfileDto.readOnly}"/>
							</div>
							<div class="form_row">
								<h2><spring:message code="label.logindetails" text="?label.logindetails?" /></h2>
								<spring:message var="elementTitle"     code="title.username" text="?title.username?" />
		                        <label class="form_label" for="username">${elementTitle}&#160;<span class="form_required">*</span></label>
        		                <form:input id="username" path="username" cssClass="form_input" title="${elementTitle}" maxlength="255" disabled="${basicProfileDto.readOnly}"/>
							</div>
							<div class="form_row">
							<c:if test="${ not basicProfileDto.readOnly}">
								<a id="changePasswordLink" href="profileChangePassword.htm" class="button button--secondary"><strong><spring:message code="profile.basic.change.password" text="?profile.basic.change.password?" /></strong></a>
								</c:if>
							</div>
							<div class="form_row">
								<h2><spring:message code="label.language.details" text="?label.language.details?" /></h2>
								<div class="form_row">
								<spring:message var="elementTitle" code="title.locale" text="?title.locale?" />
									<label class="form_label" for="locale">${elementTitle}</label>
									<form:select id="locale" path="userLocale" cssClass="form_input" title="${elementTitle}" disabled="${basicProfileDto.readOnly}">
			                            <form:option value="" label="${pleaseSelect}" />
            			                <form:options items="${profileLocales}" />
                        			</form:select>
								</div>
								<div class="form_row">
									<spring:message var="elementTitle" code="title.timezone" text="?title.timezone?" />
                        			<label class="form_label" for="timezone">${elementTitle}</label>
									<form:select id="timezone" path="timezone" cssClass="form_input" title="${elementTitle}" disabled="${basicProfileDto.readOnly}">                                    
		                            <form:option value="" label="${pleaseSelect}"/>
        		                    <form:options items="${profileTimezoneIds}" />
                			        </form:select>
								</div>
								<c:if test="${ not basicProfileDto.readOnly}">
								<div class="form_row">
									<button class="button" type="submit"><spring:message code="profile.basic.submit" text="?profile.basic.submit?" /></button>
								</div>
								</c:if>
							</div>
						</form:form>
					
					
					</div></div>
					<div class="col medium-7">
					<div class="box">
					<tags:profileRegistrationsTable id="userRegTable" heightPx="250" profileRegistrations="${profileRegistrationDtos}" title="${titleRegTable}" readOnly="${basicProfileDto.readOnly}"/>
						<c:if test="${not basicProfileDto.readOnly}">
						<p><a href="profileRedeemActivationCode.htm" class="arrowAfter"><strong><spring:message code="profile.basic.redeem.activation.code" text="?profile.basic.redeem.activation.code?" /></strong></a>
						</p>
						</c:if>
					</div>
				</div>
					
					
					</div></section>
    <script src="js/core.min.js"></script>
    	<script>
		(function($) {
			$("#profile-form").validate({
				rules: {
					username: {
						required: true,
						email: false
					},
					email: {
						required: true,
						email: true,
					}
				},
				messages: {
					username: "Please enter your username.",
					email: "Please enter valid email address.",
				}
			});
		}(jQuery));
	</script>
</body>
</html>

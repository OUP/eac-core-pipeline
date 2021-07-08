<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
	  <title><spring:message code="title.redeemcode" text="?title.redeemcode?" /></title>
	  <meta name="WT.cg_s" content="Redeem Token"/>
	</head>
    <body class="stickyFooter">
    	<eac:progressbar page="ACTIVATION_CODE"/>
    	<section class="content">
			<div class="row">
				<div class="col large-8 large-centered">
					<div class="box">
					
						<h1><spring:message code="title.redeemcode" text="?title.redeemcode?" /></h1>
						<tags:errors name="activationCode"/>
						<div class="message message--info">
							<spring:message code="help.activationcode" text="?help.activationcode?"/>
						</div>

						<form:form cssClass="form" id="activate-form" method="post" commandName="activationCode">
							<tags:inSync/>
							<div class="form_row">
								<label class="form_label" for="code"><spring:message code="label.redeemcode" text="?label.redeemcode?" /></label>
								<form:input id="code" path="code" cssClass="form_input" title="${elementTitle}" />
							</div>
							<button class="button" type="submit"><spring:message code="submit.redeemcode" text="?submit.redeemcode?" /></button>	
							<c:if test="${not empty  productReturn}">
							<a href="${productReturn}" class="arrowAfter flRight"><strong><spring:message code="label.product.return" text="?label.product.return?"/></strong> </a>
							</c:if>
						</form:form>
					</div>
				</div>
			</div>
		</section>
	<script src="js/core.min.js"></script>
	<script>
		(function($) {

			$("#activate-form").validate({
				rules: {
					code: "required"
				},
				messages: {
					code: "Please enter your access code."
				}

			});
			
		}(jQuery));
	</script>
    </body>
</html>
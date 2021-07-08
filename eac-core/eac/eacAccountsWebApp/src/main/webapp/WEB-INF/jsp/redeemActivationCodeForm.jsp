<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="no-js" lang="en">
<head xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<title><spring:message code="title.redeemcode" text="?title.redeemcode?" /></title>
	<meta name="WT.cg_s" content="Redeem Token"/>
</head>
<body>
	<div class="row">
		<div class="col small-12">
		<%@ include file="/WEB-INF/jsp/info.jsp" %>
			<main class="box box--noPadding">
				<div class="stepHeading stepHeading--complete"><span class="stepHeading_number">1</span> Log in or register</div>
				
				<%-- <div class="stepHeading stepHeading--complete" style="height: 43px;">
					<div style="float:left">
						<span class="stepHeading_number">1</span>
						<span>Log in or register</span>
					</div>
					<div style="float:right;"> 
					 <a style="text-align:right;margin-right: 34px;  line-height: 23px;
					/* padding-top: 5px; *//* margin-top: 5px; */line-height: 150%;text-decoration: none;color: #545454;" href="<c:url value="/logout.htm"/>"><strong>Logout</strong></a>
					</div>
				</div> --%>
				
				
				<div class="stepHeading stepHeading--active"><span class="stepHeading_number">2</span> Activate content</div>
				<div class="box_inner">
					<h1>Activate content</h1>
					<c:set var="webUsername" value="${fn:escapeXml(webUserName)}"/>
					<c:if test="${webUsername != ''}">
						<p style="font-family: Arial,sans-serif; font-size: .875rem;"><spring:message code="log.in.comfirmation" text="?log.in.comfirmation?" argumentSeparator=";" arguments="${fn:escapeXml(webUserName)}"/> <strong><a href="<c:url value="/logout.htm"/>">Logout</a></strong>.</p>
					</c:if>
					
					<spring:hasBindErrors name="codeRedeemDto">
						<div class="error">
							 <c:forEach var="error" items="${errors.allErrors}">
								<p class="message message--error"><eac-common:message code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}"/></p>					
				            </c:forEach>
						</div>
					</spring:hasBindErrors>
					<form:form modelAttribute="codeRedeemDto" method="POST" id="activate-form" cssClass="form">
						<c:if test="${codeRedeemDto.sharedUser}">
							<p class="message message--error"><spring:message code="error.shared.user.registration.denied" text="?error.shared.user.registration.denied?" /></p>
						</c:if>
						<c:if test="${not codeRedeemDto.sharedUser}">
							<p class="form_row">
								<label class="form_label" for="code"><spring:message code="label.redeemcode" text="?label.redeemcode?" /></label>
								<form:input class="form_input" id="code" path="code" placeholder="0000-0000-0000"/>
							</p>
							<p><button class="button" type="submit"><spring:message code="submit.redeemcode" text="?submit.redeemcode?" /></button></p>
						</c:if>
					</form:form>
				</div>
				<div class="stepHeading"><span class="stepHeading_number">3</span> Download app</div>
			</main>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			$('#code').focus();
		});
	</script>
</body>

</html>
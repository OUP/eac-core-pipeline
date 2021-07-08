<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><spring:message code="title.resetpasswordfailure" text="?title.resetpasswordfailure?" /></title>
	<meta name="WT.cg_s" content="Reset Password Failure"/>
</head>
<body>
	<div class="row">
		<div class="col small-12">
			<main class="box">
				<h1><spring:message code="title.resetpasswordfailure" text="?title.resetpasswordfailure?" /></h1>
				<p class="message message--success"><spring:message code="label.resetpasswordfailure" text="?label.resetpasswordfailure?" /></p>
				<p><a href="<c:url value="/login.htm"/>">&larr; Back to sign in</a></p>
			</main>
		</div>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><spring:message code="title.change.password.denied" text="?title.change.password.denied?" /></title>
</head>
<body>
	<div class="row">
		<div class="col small-12">
			<main class="box">
				<h1><spring:message code="title.changepassword" text="?title.changepassword?" /></h1>
				<p class="message message--error"><spring:message code="label.change.password.denied" text="?label.change.password.denied?" /></p>
				<p><a href="<c:url value="/login.htm"/>">&larr; Back to sign in</a></p>
			</main>
		</div>
	</div>
</body>
</html>
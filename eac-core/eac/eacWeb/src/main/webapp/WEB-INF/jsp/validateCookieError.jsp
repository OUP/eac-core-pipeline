<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
  <title><spring:message code="title.validate.cookie.error" text="?title.validate.cookie.error?" /></title>
  <meta name="WT.cg_s" content="Validate Cookie Error"/>
</head>
<body class="stickyFooter">
<section class="content">
			<div class="row">
				<div class="col large-8 large-centered">
					<div class="box">
	<div class="message message--error">
		<h4><spring:message code="label.validate.cookie.error" text="?label.validate.cookie.error?" /></h4>
		<c:if test="${not empty errorCode}">
		<p><spring:message code="${errorCode}" text="?${errorCode}?" arguments="${errorParams}"/></p>
		</c:if>
	</div>
</div></div></div></section>
<script src="js/core.min.js"></script>
</body>
</html>
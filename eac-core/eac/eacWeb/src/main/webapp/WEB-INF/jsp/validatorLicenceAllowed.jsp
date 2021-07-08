<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
  <title><spring:message code="title.validatorlicenceallowed" text="?title.validatorlicenceallowed?" /></title>
  <meta name="WT.cg_s" content="Validator Licence Allowed"/>
</head>

 

<body class="stickyFooter">
<section class="content">
<div class="row">
	<div class="col large-8 large-centered">
		<div class="box">
			<div class="message message--success">
				<h4>
					<spring:message code="title.validatorlicenceallowed"
						text="?title.validatorlicenceallowed?" />
				</h4>
				<p>
					<spring:message code="label.validatorlicenceallowed"
						text="?label.validatorlicenceallowed?" />
					<c:out value="${username}" escapeXml="false"/>
					<spring:message code="label.touse" text="?label.touse?" />
					<c:out value="${product}" escapeXml="false"/>
				</p>
			</div>
		</div>
	</div>
</div>
</section>
	<script src="js/core.min.js"></script>
</body>
</html>
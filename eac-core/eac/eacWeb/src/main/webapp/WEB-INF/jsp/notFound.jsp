<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
  <title><spring:message code="title.notFound" text="?title.notFound?" /></title>
  <meta name="WT.cg_s" content="Page Not Found"/>
</head>
<body class="stickyFooter">
<section class="content">
			<div class="row">
				<div class="col large-8 large-centered">
					<div class="box">
						<h1><spring:message code="label.notFound" text="?label.notFound?" /></h1>
						<div class="message message--error">
							<p><spring:message code="label.pageNotFound" text="?label.pageNotFound?" /></p>
						</div>
					</div>
				</div>
			</div>
		</section>
<script src="js/core.min.js"></script>
</body>
</html>
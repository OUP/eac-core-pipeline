<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
  <title><spring:message code="title.change.password.denied" text="?title.change.password.denied?" /></title>  
</head>
<body class="stickyFooter">
<section class="content">
			<div class="row">
				<div class="col large-8 large-centered">
					<div class="box">
					  <h1><spring:message code="title.change.password.denied" text="?title.change.password.denied?" /></h1>
						<div class="message message--error">
							<p><spring:message code="label.change.password.denied" text="?label.change.password.denied?" />
								</p>
						</div>
						<p><spring:message code="label.change.password.denied.continue" text="?label.change.password.denied.continue?" argumentSeparator=";" arguments="${afterChangePasswordErrorUrl}"/></p>
					</div>
				</div>
			</div>
		</section>
		<script src="js/core.min.js"></script>
</body>
</html>
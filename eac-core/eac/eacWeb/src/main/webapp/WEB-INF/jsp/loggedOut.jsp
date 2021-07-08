<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
  <title><spring:message code="title.loggedout" text="?title.loggedout?" /></title>
</head>
<body class="stickyFooter">
    <%-- <div id="content-middle">
		<h1><spring:message code="title.you.are.logged.out" text="?title.you.are.logged.out?" /></h1>
		<p><spring:message code="label.you.are.logged.out" text="?label.you.are.logged.out?" /></p>
    </div> --%>
    <section class="content">
			<div class="row">
				<div class="col large-8 large-centered">
					<div class="box">
						<h1><spring:message code="title.you.are.logged.out" text="?title.you.are.logged.out?" /></h1>
						</br>
						<p><spring:message code="label.you.are.logged.out" text="?label.you.are.logged.out?" /></p>
						</br>
						<eac:loginurl var="loginUrl" propertyName="eac.login.url"/><a href="${loginUrl}" class="button"><spring:message code="label.login" text="?label.login?" /></a>
					</div>
				</div>
			</div>
		</section>
		<script src="js/core.min.js"></script>
</body>
</html>
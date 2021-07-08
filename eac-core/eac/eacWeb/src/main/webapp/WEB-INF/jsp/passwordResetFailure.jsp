<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
  <title><spring:message code="title.resetpasswordfailure" text="?title.resetpasswordfailure?" /></title>
  <meta name="WT.cg_s" content="Reset Password Failure"/>
</head>
<body class="stickyFooter">
<section class="content">
			<div class="row">
				<div class="col large-8 large-centered">
					<div class="box">
<div class="message message--info">
					<c:choose>
						<c:when test="${not empty url}">
							<h4>Unable to change password</h4>
							<p>
								Your link has expired.<br />Please choose ‘Forgotten your login
								details?’ on Oxford Learner’s Bookshelf to try again.
							</p>
						</c:when>
						<c:otherwise>
							<h4>
								<spring:message code="title.resetpasswordfailure"
									text="?title.resetpasswordfailure?" />
							</h4>
							<p>
								<spring:message code="label.resetpasswordfailure"
									text="?label.resetpasswordfailure?" />
							</p>
						</c:otherwise>
					</c:choose>
					<%-- <p><eac:loginurl var="loginUrl" propertyName="eac.login.url"/><a href="${loginUrl}"><spring:message code="label.login" text="?label.login?" /></a></p> --%>
						</div>
						<%-- Added as part of OLB need to be removed after Go Live --%>
				<c:choose>
				<c:when test="${not empty url}">
					<eac:loginurl var="loginUrl" propertyName="eac.login.url" />
					<a href="${url}" class="button"><spring:message
							text="OLB Home" /></a>
				</c:when>
				<c:otherwise>
					<eac:loginurl var="loginUrl" propertyName="eac.login.url" />
					<a href="${loginUrl}" class="button"><spring:message
							code="label.login" text="?label.login?" /></a>
				</c:otherwise>
				</c:choose>
			</div></div></div></section>
						<script src="js/core.min.js"></script>
</body>
</html>
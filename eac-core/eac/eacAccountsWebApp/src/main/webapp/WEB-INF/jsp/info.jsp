<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<div style="text-align: right; padding-right: 10px; font-family: Arial,sans-serif; font-size: .875rem;">
	<eacAccounts:isLoggedIn var="isLoggedIn" />
	<eacAccounts:isError var="isError" />
	<c:choose >
		<c:when test="${ (not isError) and isLoggedIn}">
			<spring:message var="hello" code="label.hello" text="?label.hello?" />
			<span>${hello} ${fn:escapeXml(webUserName)}</span>
			<span>&nbsp; | &nbsp;<a href="<c:url value="/logout.htm"/>"><strong>Logout</strong></a></span>
		</c:when>
	</c:choose>
</div>

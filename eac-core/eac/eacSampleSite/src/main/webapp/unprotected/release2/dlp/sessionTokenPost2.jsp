<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@page import="com.oup.eac.common.utils.crypto.SimpleCipher"%>
<%@page import="com.oup.eac.common.utils.EACSettings"%>
<html>
<head>
</head>
<body>
<c:set var="sessionToken" value="${param['sessionToken']}" />
<c:set var="successUrl"   value="${param['successUrl']}" />
<br/>SessionToken : ${sessionToken}
<br/>SuccessUrl   : ${successUrl}
<%
	String sessionToken = request.getParameter("sessionToken");
	String sharedSecret = EACSettings.getRequiredProperty(EACSettings.EAC_SESSION_TOKEN_ENCRYPTION_KEY);
	pageContext.setAttribute("sharedSecret",sharedSecret);
	String encryted = SimpleCipher.encrypt(sessionToken, sharedSecret);
	pageContext.setAttribute("encrypted",encryted);
%>
<br/>Shared Secret : ${sharedSecret}
<br/>Encrypted     : ${encrypted}

<hr/>
				<form method="post" action="/eac/cookieFromSession.htm" >					
  					<label for="id1">Session Token : </label>
  					<input size="80" type="text" name="sessionToken" id="id1" value="${encrypted}" />
  					<br/>
  					<label for="id2">Success Url : </label>
  					<input size="80" type="text" name="successUrl" id="id2" value="${successUrl}" />
  					<br/>
  					<input type="submit" name="submit" />
				</form>

<hr/>
<a href="sessionTokenPost1.jsp">Back</a>

</body>
</html>

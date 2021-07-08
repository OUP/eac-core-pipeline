<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>EAC Basic Login Test Page</title>
	
	</head>
	<body>
	
	<%@ include file="eacCookie.jspf" %>
	
	<form method="post" action="/eac/basicLogin.htm" >					
			<label for="id1">User Name : </label>
			<input type="text" name="username" id="id1" size="40"/>
			<br/>
			<label for="id2">Password : </label>
			<input type="text" name="password" id="id2" />
			<br/>
			<label for="id3">Success URL : </label>
			<input type="text" size="80" name="success_url" id="id2" value="${urlPrefix}/eacSampleSite/unprotected/release2/teachersclub/basicLoginSuccess.jsp" />
			<br/>
			<label for="id4">Error URL : </label>
			<input type="text" size="80" name="error_url" id="id2" value="${urlPrefix}/eacSampleSite/unprotected/release2/teachersclub/basicLoginError.jsp" />
			<br/>
			<input type="submit" name="submit" />
		</form>
	</body>
	 
</html>
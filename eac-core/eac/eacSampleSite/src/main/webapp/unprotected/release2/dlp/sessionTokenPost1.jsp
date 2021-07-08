<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>Session Token Post 1</title>
	</head>
	<body>
		<form method="post" action="sessionTokenPost2.jsp" >					
			<label for="id1">Session Token : </label>
			<input type="text" name="sessionToken" id="id1" size="80"/>
			<br/>
			<label for="id2">Success Url : </label>
			<input type="text" name="successUrl" id="id2" size="80" value="${urlPrefix}/eacSampleSite/unprotected/release2/dlp/showEacCookie.jsp" />
			<br/>
			<input type="submit" name="submit" />
		</form>
	</body>

	 
</html>
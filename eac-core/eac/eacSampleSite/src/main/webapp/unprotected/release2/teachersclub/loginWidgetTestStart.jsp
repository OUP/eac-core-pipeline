<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>Test Page for Eac Login Widget 1/2</title>

<%@ include file="/WEB-INF/jsp/include.jsp" %>

<c:set var="dir" value="/eacSampleSite/unprotected/release2/teachersclub" />
<c:set var="locale" value="en_GB" />
<c:set var="successUrl" value="${urlPrefix}${dir}/loginWidgetTest.jsp" />
<c:set var="errorUrl" value="${urlPrefix}${dir}/basicLoginError.jsp" />
<style type="text/css">
span {
    width:100px;
	float:left;
}
</style>
</head>
<body>
<div>
<form method="post" action="loginWidgetTest.jsp">
	<br/><span>success url : </span><input type="text" name="urlSuccess" value="${successUrl}" size="120"/>
	<br/><span>error url : </span><input type="text" name="urlError" value="${errorUrl}" size="120"/>
	<br/><span>locale : </span><input type="text" name="locale" value="${locale}" />
	<br/><span>cc : </span>         <input type="text" name="cc" value="${cc}" />
    <br/><span>selLanguage : </span><input type="text" name="selLanguage" value="${selLanguage}" />
	<br/><input type="submit" />
</form>
</div>
</body>
</html>
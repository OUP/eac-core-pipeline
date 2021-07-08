<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>Basic Test Page for Eac Login Widget 2/2</title>
<style type="text/css">
#wrapper
{
	border:2px solid;
	width:25%;	
}
#eacLoginWidgetContainer{
	padding: 20px;
	position: relative;
	background-color: white;
}
#eacLogout {
	position:absolute;
	bottom:5px;
	right:5px;
}
#eacLoginHeader {
	font-size: 175%; font-weight: bold; text-align: center;
}
#eacLoginUsername {
	font-style:italic;
}
#eacLoginPassword {
	font-style:italic;
}
label {
    width:150px;
	float:left;
}
</style>

<%@ include file="/WEB-INF/jsp/include.jsp" %>

<c:set var="locale" value="${param['locale']}" />
<c:set var="cc" value="${param['cc']}" />
<c:set var="selLanguage" value="${param['selLanguage']}" />
<c:set var="urlError" value="${param['urlError']}" />
<c:set var="urlSuccess" value="${param['urlSuccess']}" />

<c:url var="successUrl" value="${urlSuccess}" >
	<c:if test="${not empty locale}">
		<c:param name="locale" value="${locale}" />
	</c:if>
    <c:if test="${not empty cc}">
        <c:param name="cc" value="${cc}" />
    </c:if>
    <c:if test="${not empty selLanguage}">
        <c:param name="selLanguage" value="${selLanguage}" />
    </c:if>
	<c:param name="urlError" value="${urlError}" />
	<c:param name="urlSuccess" value="${urlSuccess}" />
</c:url>
<c:url var="errorUrl" value="${urlError}" />

<c:url var="loginWidgetUrl" value="${urlPrefix}/eac/loginWidget.js" >
	<c:param name="success_url" value="${successUrl}" />
	<c:param name="error_url" value="${errorUrl}" />
	<c:if test="${not empty locale}">
		<c:param name="lang" value="${locale}" />	
	</c:if>
    <c:if test="${not empty cc}">
        <c:param name="cc" value="${cc}" />
    </c:if>
    <c:if test="${not empty selLanguage}">
        <c:param name="selLanguage" value="${selLanguage}" />
    </c:if>
	<c:param name="logout_url" value="${successUrl}" />
</c:url>

<script type="text/javascript" src="/eac/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="${loginWidgetUrl}" ></script>
<script type="text/javascript">
$(document).ready(function() {
	  if(typeof console !== 'undefined' && console){
      	console.log('about to call showLoginWidget(\'wrapper\')');
		console.log('EAC_LOGIN_WIDGET',EAC_LOGIN_WIDGET);
	  }

	  
	  
	  EAC_LOGIN_WIDGET.showLoginWidget('wrapper');

	  if(typeof console !== 'undefined' && console){
      	console.log("isLoggedIn : ",   	EAC_LOGIN_WIDGET.isLoggedIn);
  	  	console.log("eacSessionId : ", 	EAC_LOGIN_WIDGET.eacSessionId);
	  	console.log("eacUserId : ", 	EAC_LOGIN_WIDGET.eacUserId);
	    console.log("eacUserName : ", 	EAC_LOGIN_WIDGET.eacUserName);
	    console.log("success_url : ", 	"${successUrl}");
	    console.log("error_url : ", 	"${errorUrl}");
	    console.log("lang : ", 			"${locale}");
	  }	 

	  	 
	  if(EAC_LOGIN_WIDGET.isLoggedIn){
		  var $eacLoginBody = $("#eacLoginBody");
		  $eacLoginBody.prepend('<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et</p>');
		  $eacLoginBody.prepend('<p>Added by jquery..</p>')
	  }
});
</script>
</head>
<body>
<h1>Test Page for EAC Login Widget</h1>
		<%@ include file="eacCookie.jspf" %>
<p/>
<div id="wrapper">
	<span>Loading...</span>
</div>
<a href="loginWidgetTestBasicStart.jsp">Reconfigure</a>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
  <title><spring:message code="title.registrationnotallowed" text="?title.registrationnotallowed?" /></title>
  <meta name="WT.cg_s" content="Registration Not Allowed"/>
</head>
<body class="stickyFooter">
<section class="content">
			<div class="row">
				<div class="col large-8 large-centered">
					<div class="box">
	<c:set var="emailLink">
   		<tags:mailTo email="${email}"/>
   	</c:set>
   	<c:set var="logoutLink">
   		<tags:logout lower="true" />
   	</c:set>
   	<c:set var="contcatUsLink">
   		<tags:contactUs lower="true" />
   	</c:set>
   	<%-- we replace comma characters in product names with HTML Code for Comma to avoid confusion with comma separator used in 'spring:msessage' for 'msg'--%>
   	<c:set var="productName" value="${fn:replace(product,',','&#44;')}" />
    <spring:message var="msg" code="${messageCode}" text="?${msg}?" argumentSeparator="," arguments="${contcatUsLink},${productName},${emailLink},${logoutLink}"/>

<div class="message message--error">
		<h4><spring:message code="title.registrationnotallowed" text="?title.registrationnotallowed?" /></h4>
			<p>${msg}</p>
</div>    
    </div></div></div></section>
    
   <script src="js/core.min.js"></script> 
</body>
</html>
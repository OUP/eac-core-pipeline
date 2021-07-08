<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@page import="com.oup.eac.common.utils.EACSettings"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>EAC UAT Test Site</title>
		
		<link rel="stylesheet" href="<c:url value="/styles/blueprint/screen.css" />" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="<c:url value="/styles/blueprint/print.css" />" type="text/css" media="print" />
		<!--[if lt IE 8]>
		        <link rel="stylesheet" href="<c:url value="/resources/blueprint/ie.css" />" type="text/css" media="screen, projection" />
		<![endif]-->
		<link rel="stylesheet" href="<c:url value="/styles/eacuat.css" />" type="text/css" media="screen, projection" />
        <script type="text/javascript" src="<c:url value="/js/eac.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/cookies.js" />"></script>
	</head>
	<body onload="init('<%=EACSettings.getProperty("cookie.host")%>')" style="background:<%=EACSettings.getProperty("deploy.bgcolor")%>">
		<div id="page" class="container">
			<div id="header" class="span-24" style="padding-top:30px;">
				<div class="span-16">
					<h3>EAC Test Site - <b><%=EACSettings.getProperty("deploy.host")%></b></h3>
				</div>
				<div class="span-8 last">
					<div id="loginLogout" style="display:none">			
					    <h3>
					    	<a href="javascript:logout('<%=EACSettings.getProperty("deploy.protocol")%>','<%=EACSettings.getProperty("deploy.host")%>','<%=EACSettings.getProperty("deploy.eac.host")%>','<%=EACSettings.getProperty("deploy.sampleSite.context.path")%>')">Logout</a>
					    	&nbsp;&nbsp;
							<a href="<c:url value="/index.jsp" />">Home</a>
						</h3>
					</div>						
				</div>	
			</div>
			<div id="main" class="span-14">				
				<b>HE Student Mathematics for Matriculation Semester 1 Fourth Edition</b>
			</div>
		</div>
	</body>
</html>
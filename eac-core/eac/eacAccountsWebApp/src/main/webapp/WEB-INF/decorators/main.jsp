<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="no-js" lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="description" content="">
	
	<title><decorator:title default="EAC" /></title>
	
	<!--[if (gt IE 8)]><!-->
		<link rel="stylesheet" href="css/style.min.css">
	<!--<![endif]-->
	<!--[if (lt IE 9)]>
		<link rel="stylesheet" href="css/style-px.min.css">
	    <script src="js/polyfills/html5shiv.min.js"></script>
	    <script src="js/polyfills/respond.min.js"></script>
	<![endif]-->
	<link rel="stylesheet" href="css/footer-style.css" type="text/css" />
	<script type="text/javascript" src="js/jquery-1.5.1.min.js"></script>
	<script type="text/javascript" src="js/tools.js"></script>
	<script type="text/javascript" src="js/jstz.min.js"></script>
	
	<!-- START OF SmartSource Data Collector TAG v10.2.0 -->
	<!-- Copyright (c) 2012 Webtrends Inc.  All rights reserved. -->
	<script>
	window.webtrendsAsyncInit=function(){
	    var dcs=new Webtrends.dcs().init({
	        dcsid:"<eac-common:settings propertyName="eac.webtrends.reporting.id"/>"
	        ,domain:"statse.webtrendslive.com"
	        ,timezone:0
	        ,i18n:true
	        ,offsite:true
	        ,download:true
	        ,downloadtypes:"xls,doc,pdf,txt,csv,zip,docx,xlsx,rar,gzip"
	        ,javascript: true
	        ,onsitedoms:"<eac-common:settings propertyName="eac.webtrends.reporting.domain"/>"
	        ,plugins:{
	            hm:{src:"//s.webtrends.com/js/webtrends.hm.js"}
	        }
	        }).track();
	};
	(function(){
	    var s=document.createElement("script"); s.async=true; s.src="js/webtrends.min.js";
	    var s2=document.getElementsByTagName("script")[0]; s2.parentNode.insertBefore(s,s2);
	}());
	</script>
	<noscript><img alt="dcsimg" id="dcsimg" width="1" height="1" src="//statse.webtrendslive.com/<eac-common:settings propertyName="eac.webtrends.reporting.id"/>/njs.gif?dcsuri=/nojavascript&amp;WT.js=No&amp;WT.tv=10.2.0&amp;dcssip=<eac-common:settings propertyName="eac.webtrends.reporting.domain"/>"/></noscript>
	<!-- END OF SmartSource Data Collector TAG v10.2.0 -->

	<decorator:head />
</head>
<body id="eac-accounts" onload="init();<decorator:getProperty property="body.onload"/>">
	<div id="wrap">
		<%@ include file="/WEB-INF/jsp/header.jsp" %>
		<div id="main">
			<decorator:body />
		</div>
		<!-- <div id="push">&nbsp;</div> -->
		
	</div>	
		<%@ include file="/WEB-INF/jsp/footer.jsp" %>
		<script src="js/core.min.js"></script>
</body>
</html>
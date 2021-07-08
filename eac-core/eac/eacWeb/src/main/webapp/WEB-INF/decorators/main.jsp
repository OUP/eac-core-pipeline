<%--
% This is the main decorator for all eac-core pages.
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html>
<html class="no-js" lang="en">
	<head>

		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		
	   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	   <meta http-equiv="Content-Script-Type" content="text/javascript"/>
		<c:if test="${sessionScope.PRODUCT ne null}">
	   		<meta name="WT.cg_n" content="${sessionScope.PRODUCT.productName}"/>
	    </c:if>
	   <title><decorator:title default="EAC" /></title>
       <!--[if (gt IE 8)]><!-->
	<link rel="stylesheet" href="css/style.css">
	<!--<![endif]-->

	<!--[if (lt IE 9)]>
			<link rel="stylesheet" href="css/style-px.min.css">
		<![endif]-->
       <script type="text/javascript" src="js/jquery-2.1.3.min.js"></script>
       <script type="text/javascript" src="js/featureDetection.min.js"></script>
       
		<!-- START OF SmartSource Data Collector TAG v10.2.0 -->
		<!-- Copyright (c) 2012 Webtrends Inc.  All rights reserved. -->
		<eac-common:settings propertyName="aws.google.analytics" var="googleAnaliticsKey"/>
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
       <eac:skinCss var="cssPath" /> 
       <c:if test="${not empty cssPath}">
          <link rel="stylesheet" href="${cssPath}" type="text/css" />
       </c:if>
       <!-- Google Tag Manager -->
			<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push(
			
			{'gtm.start': new Date().getTime(),event:'gtm.js'}
			);var f=d.getElementsByTagName(s)[0],
			j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
			'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
			})(window,document,'script','dataLayer','${googleAnaliticsKey}');</script>
		<!-- End Google Tag Manager -->
	   <decorator:head />
	   
    </head>
	<body class="stickyFooter" id="eac-core" onload="init();<decorator:getProperty property="body.onload"/>">
		<!-- Google Tag Manager (noscript) -->
			<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=${googleAnaliticsKey}"
			height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
		<!-- End Google Tag Manager (noscript) -->
		
       <div id="wrap" class="stickyFooter_page">
           <%@ include file="/WEB-INF/jsp/headerR.jsp" %>
		            <decorator:body />
       </div> 	     
       <%@ include file="/WEB-INF/jsp/footer.jsp" %>
       <script type="text/javascript" src="js/tools.js"></script>
       <script type="text/javascript" src="js/jstz.min.js"></script>
	</body>
</html>
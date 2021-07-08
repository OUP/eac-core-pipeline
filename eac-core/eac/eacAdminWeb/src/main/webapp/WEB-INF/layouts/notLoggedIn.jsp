<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>EAC Administration Console</title>
		<link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/screen.css" />" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/print.css" />" type="text/css" media="print" />
		<!--[if lt IE 8]>
		        <link rel="stylesheet" href="<c:url value="/resources/blueprint/ie.css" />" type="text/css" media="screen, projection" />
		<![endif]-->
        <link href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript">
        	var userLocale = '<c:out value="${(pageContext.request.userPrincipal.principal.locale ne null) ? pageContext.request.userPrincipal.principal.locale : \'en-uk\'}"/>';
        
        	var djConfig = {
	            locale: userLocale
	        };
	    </script>
	    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/styles/tipTip.css" />" />
		<link type="text/css" rel="stylesheet" href="<c:url value="/resources/styles/eacadmin.css" />" media="screen" />		 
	    <script type="text/javascript" src="<c:url value="/resources/jquery/jquery.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/jquery/jquery-ui.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/jquery/jcookie.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/jquery/jquery.tipTip.minified.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/jquery/jquery.shorten.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/jquery/jquery.blockUI.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/eacAdmin.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/jquery/capslock.jquery.js" />"></script>		
	</head>
	<body>
		<div id="page" class="container notLoggedIn">
			<div id="header">
				<div id="logo">
	                <div class="sub-heading">
						<a href="<c:url value="/" />">
							<img src="<c:url value="/resources/images/logo.gif"/>" alt="Oxford University Press" />
						</a>
                        <span class="sub-heading-1">EAC Administration Console</span>
	                </div>
				</div>
			</div>
			<div id="main">
				<tiles:insertAttribute name="body" />
				<div id="push">&#160;</div>
			</div>
		</div>
		<div class="container">
		<div id="footer" class="span-24">
	 		<tiles:insertAttribute name="footerTile" />
        </div>
        </div>
	</body>
</html>
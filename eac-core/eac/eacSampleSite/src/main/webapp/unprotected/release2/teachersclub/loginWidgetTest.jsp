<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.oup.eac.common.utils.EACSettings"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>

<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%
String hostPrefix = EACSettings.getProperty("deploy.eac.host"); 
String hostProtocol= EACSettings.getProperty("deploy.protocol");
String hostUrl= hostProtocol+"://"+hostPrefix;
pageContext.setAttribute("hostUrl", hostUrl);
%>
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

<c:url var="loginWidgetUrl" value="${hostUrl}/eac/loginWidget.js" >
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

<title>Teachers' Club | Oxford University Press</title>
<style type="text/css">
#eacLogoutLink
{
	font-weight: bold;
	color: black;
}
</style>
<link rel="stylesheet" type="text/css" href="http://elt.oup.com/system/css/style.css"/>
<link rel="stylesheet" type="text/css" href="http://elt.oup.com/system/css/oup_extra.css"/>
<link type="text/css" rel="stylesheet" href="/eacSampleSite/styles/teachersClubExtra.css"/>
<!--[if IE 6]>
  <link rel="stylesheet" type="text/css" href="http://elt.oup.com/system/css/ie6.css"/>
  <script type="text/javascript" src="http://elt.oup.com/system/js/iepngfix_tilebg.js"></script>
<![endif]-->
<!--[if IE 7]>
  <link rel="stylesheet" type="text/css" href="http://elt.oup.com/system/css/ie7.css"/>
<![endif]-->
<!--[if IE 8]>
  <link rel="stylesheet" type="text/css" href="http://elt.oup.com/system/css/ie8.css"/>
<![endif]-->
<link rel="shortcut icon" href="http://www.oup.com/images/favicon.ico" type="image/x-icon" />
<link rel="home" href="http://www.oup.com/" title="Home Page" />
<link rel="sitemap" href="http://www.oup.com/sitemap/" title="Site Map" />
<link rel="help" href="http://www.oup.com/site-help/" title="Site Help" />
<link rel="search" href="http://www.oup.com/search/" title="Search" />
<link rel="accessibility" href="http://www.oup.com/accessibility/" title="Accessibility" />
<link rel="canonical" href="http://elt.oup.com/teachersclub/?cc=global&amp;selLanguage=en" />
	  		<script type="text/javascript" src="${hostUrl }/eac/js/jquery-1.5.1.min.js"></script>	  		
			<script type="text/javascript" src="${loginWidgetUrl}" ></script>
			<script type="text/javascript" src="/eacSampleSite/js/eacTeachersClubLoginSupport.js" ></script>
			
		</head>
		<body id="the-top" class="js-enabled">
		<script language="JavaScript1.1" type="text/javascript" src="http://www.oup.com/scripts/common/dcs_tag.js"></script>
				<div class="jump-box">
					<ul class="offset">
						 <li><a href="#content" accesskey="s">Jump to Content</a></li>
						 <li><a href="#nav">Jump to Navigation</a></li>
						 <li><a href="#masthead_search">Jump to Search</a></li>
					</ul>
				</div>
				<div id="page" class="pic-header pic-2" style="background:url('http://elt.oup.com/promotional/161239/161283/teacher_latam_f_large.jpg') no-repeat scroll right 155px transparent;">
					<div class="clear header-space"></div>
					<div id="content">
						<!--  BEGIN CONTENT (DO NOT REMOVE THIS COMMENT) -->
	<div id="content" class="teachers-resources">
			<h1 class="main-heading page-top" style="margin-top: 20px;">Oxford Teachers' Club</h1>
			<h2 class="sub-head" style="margin-bottom:15px">Download these extra practice activities and ideas with your class when you adopt our materials.</h2>
		<div class="col-4 fl-left"><!-- left column wrapper -->
<div class="resource-menu-wrapper">
      <ul id="menu-slide-container">
        	<li id="in-page-menu-state-1" class="in-page-menu">
<div class="menu-header">
	<h3>Browse Resources</h3>
</div>
<div class="menu-body">
	<div class="column first">
		<h4>Courses For...</h4>
		<ul>
				<li><a href="http://elt.oup.com/teachersclub/courses/younglearners/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en" title="Young Learners" >Young Learners</a></li>
				<li><a href="http://elt.oup.com/teachersclub/courses/teachingteenagers/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en" title="Teenage Learners" >Teenage Learners</a></li>
				<li><a href="http://elt.oup.com/teachersclub/courses/teachingadults/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en" title="Teaching Adults" >Teaching Adults</a></li>
		</ul>
	</div>
	<div class="column middle">
		<h4>Subject</h4>
		<ul>
				<li><a href="http://elt.oup.com/teachersclub/subjects/businessenglish/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en" title="Business English and ESP" >Business English and ESP</a></li>
				<li><a href="http://elt.oup.com/teachersclub/subjects/dictionaries/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en" title="Oxford Learners' Dictionaries" >Oxford Learners' Dictionaries</a></li>
				<li><a href="http://elt.oup.com/teachersclub/subjects/exams/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en" title="Exams & Testing" >Exams & Testing</a></li>
				<li><a href="http://elt.oup.com/teachersclub/subjects/gradedreading/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en" title="Graded Reading" >Graded Reading</a></li>
				<li><a href="http://elt.oup.com/teachersclub/subjects/grammar/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en" title="Grammar and Vocabulary" >Grammar and Vocabulary</a></li>
				<li><a href="http://elt.oup.com/teachersclub/subjects/teacherdevelopment/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en" title="Teacher Development" >Teacher Development</a></li>
				<li><a href="http://elt.oup.com/teachersclub/subjects/makingdigitalsense/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en" title="Making Digital Sense" >Making Digital Sense</a></li>
		</ul>
	</div>
	<div class="column last">
		<h4>Our Titles</h4>
				<ul>
						<li><a title="New English File" href="http://elt.oup.com/teachers/englishfile/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en">New English File</a></li>
						<li><a title="Business Result" href="http://elt.oup.com/teachers/busresult/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en">Business Result</a></li>
						<li><a title="Solutions" href="http://elt.oup.com/teachers/solutions/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en">Solutions</a></li>
						<li><a title="American Headway, Second Edition" href="http://elt.oup.com/teachers/americanheadway2e/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en">American Headway, Second Edition</a></li>
						<li><a title="Smart Choice" href="http://elt.oup.com/teachers/smartchoice/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en">Smart Choice</a></li>
						<li><a title="Dominoes New Edition" href="http://elt.oup.com/teachers/dominoes/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en">Dominoes New Edition</a></li>
						<li><a title="Headway" href="http://elt.oup.com/teachers/headway/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en">Headway</a></li>
						<li><a title="Project third edition" href="http://elt.oup.com/teachers/project3rdedition/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en">Project third edition</a></li>
						<li><a title="Express Series" href="http://elt.oup.com/teachers/express/?view=Standard&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en">Express Series</a></li>
				</ul>
	</div>
	<div id="menu-pagination-forward" class="pagination-forward">
		<a id="in-page-index-link" href="/teachersclub/?atoz=true&amp;cc=global&amp;selLanguage=en&mode=hub&amp;cc=global&amp;selLanguage=en">&laquo; See all</a>
	</div>
</div>
        	</li>
        	<li id="in-page-menu-state-2" class="in-page-menu"></li>  
        	<li id="in-page-menu-state-3" class="in-page-menu"></li>
      </ul>
</div>


	
			</div><!-- ends left column -->
			<div class="col-5 fl-right" ><!-- right column wrapper -->
				<div class="teachers-club-wrapper login" id="eacLoginWrapper">				
				Loading...
				</div>
			</div>
</div>




	
</div>
			</div> <!-- end right column  -->
			
	</div>
	<a href="loginWidgetTestStart.jsp">Reconfigure</a>
			     		<!--  END CONTENT (DO NOT REMOVE THIS COMMENT) -->
			 		</div>
				</div>
				<!-- end of PAGE -->						
<script type="text/javascript">
$(document).ready(function() {

	if(typeof console !== 'undefined' && console){
     	console.log('about to call showLoginWidget(\'eacLoginWrapper\')');
		console.log('EAC_LOGIN_WIDGET',EAC_LOGIN_WIDGET);
	}
	
	EAC_LOGIN_WIDGET.showLoginWidget('eacLoginWrapper');

	if(typeof console !== 'undefined' && console){
     	console.log("isLoggedIn : ",   	EAC_LOGIN_WIDGET.isLoggedIn);
  	  	console.log("eacSessionId : ", 	EAC_LOGIN_WIDGET.eacSessionId);
	  	console.log("eacUserId : ", 	EAC_LOGIN_WIDGET.eacUserId);
	    console.log("eacUserName : ", 	EAC_LOGIN_WIDGET.eacUserName);
	    console.log("eacTitleUserName : ",  EAC_LOGIN_WIDGET.eacTitleUserName);
	    console.log("eacTitlePassword : ",  EAC_LOGIN_WIDGET.eacTitlePassword);
	    console.log("success_url : ", 	"${successUrl}");
	    console.log("error_url : ", 	"${errorUrl}");
	    console.log("lang : ", 			"${locale}");
	}	 
	 
	//configure the login widget
	
    if(EAC_LOGIN_WIDGET.isLoggedIn){
        EAC_TEACHERS_CLUB_LOGIN_SUPPORT.configureWelcome();
    } else {    
        EAC_TEACHERS_CLUB_LOGIN_SUPPORT.configureLogin(EAC_LOGIN_WIDGET.eacTitleUserName, EAC_LOGIN_WIDGET.eacTitlePassword);    
    }
});
</script>
</body>
	
</html>

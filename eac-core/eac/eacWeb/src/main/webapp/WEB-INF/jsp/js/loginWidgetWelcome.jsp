<%@ page language="java" contentType="text/javascript; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<c:url var="urlLogout" value="${urlPrefix}/logout.htm" >	
	<c:param name="url" value="${logoutUrl}" />
</c:url>
var EAC_LOGIN_WIDGET = function() { 

	/*
	 * Copyright (c) 2012 Oxford University Press  
	 */
    
	<spring:message var="hello" code="label.hello" text="?label.hello?" />
	<spring:message var="welcomeMessage" code="welcome.message" text="?welcome.message?" />
	<spring:message var="profileText" code="label.view.profile" text="?label.view.profile?" />	
    <spring:message var="logoutText" code="label.logout" text="?label.logout?" />
    <c:set var="profileLink" value="${urlPrefix}/profile.htm" />
    
    <c:set var="welcomeMessage">${fn:replace(welcomeMessage, "'", "\\'")}</c:set>
    <c:set var="profileText">${fn:replace(profileText, "'", "\\'")}</c:set>
    <c:set var="hello">${fn:replace(hello, "'", "\\'")}</c:set>
    <c:set var="webUserName">${fn:escapeXml(fn:replace(webUserName, "'", "\\'"))}</c:set>
    <c:set var="logoutText">${fn:replace(logoutText, "'", "\\'")}</c:set>
    
	function getDivContent ( containingDiv ){
		return '\
  			<div id="eacLoginWidgetContainer">\
  				<div id="eacLoginHeader"> \
  					<span id="eacLoginMessage">${hello} ${webUserName}</span> \
  				</div>\
  				<div id="eacLoginBody">${welcomeMessage}</div>\
            	<div id="eacLoginFooter">\
         	      <a id="eacProfileLink" href="${profileLink}">${profileText}</a>\
            	  <a id="eacLogoutLink" href="${urlLogout}">${logoutText}</a>\
            	</div>\
            </div>\
  		';
	};
	
	function showLogin ( containingDiv ){
		var div = document.getElementById(containingDiv);
		if(div){
			var divContent = getDivContent(containingDiv); 
  			div.innerHTML = divContent;
  		}else{
			throw 'showLoginWidget : The div named [' + containingDiv  + '] does not exist';
  		}
	};//end showLogin
	

	return {
        isLoggedIn:     true,        
		eacSessionId: 	'<c:out value="${erightsSessionKey}"/>',
		eacUserId: 		'<c:out value="${userId}"/>',
		eacUserName: 	'<c:out value="${userName}"/>',
		eacFirstName:   '<c:out value="${firstName}"/>',
		eacLastName:    '<c:out value="${lastName}"/>',
        eacTitleUserName: null,
        eacTitlePassword: null,
        eacUserType:    '<c:out value="${userType}"/>',
		showLoginWidget: function( containingDiv){
			showLogin( containingDiv)
		}
	};
}();// end EAC_LOGIN_WIDGET

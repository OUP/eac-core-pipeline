<%@ page language="java" contentType="text/javascript; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

var EAC_LOGIN_WIDGET = function() { 

	/*
	 * Copyright (c) 2012 Oxford University Press  
	 */

    <c:set var="passwordResetLink" value="${urlPrefix}/passwordReset.htm" />

    <spring:message var="titleUserName" code="label.username.title" text="?label.username.title?"/>
    <spring:message var="titlePassword" code="label.password.title" text="?label.password.title?"/>
    <spring:message var="labelLogin"    code="label.login" text="?label.login?"/>
    <spring:message var="labelUserName" code="label.username" text="?label.username?"/>
    <spring:message var="labelPassword" code="label.password" text="?label.password?"/>
    <spring:message var="loginProblemsText" code="problems.logging.in" text="?problems.logging.in?"/>
    <spring:message var="passwordResetText"  code="label.forgot.password" text="?label.forgot.password?"/>
    
    <c:set var="titleUserName">${fn:replace(titleUserName, "'", "\\'")}</c:set>
    <c:set var="titlePassword">${fn:replace(titlePassword, "'", "\\'")}</c:set>
    <c:set var="labelLogin">${fn:replace(labelLogin, "'", "\\'")}</c:set>
    <c:set var="labelUserName">${fn:replace(labelUserName, "'", "\\'")}</c:set>
    <c:set var="labelPassword">${fn:replace(labelPassword, "'", "\\'")}</c:set>
    <c:set var="loginProblemsText">${fn:replace(loginProblemsText, "'", "\\'")}</c:set>
    <c:set var="passwordResetText">${fn:replace(passwordResetText, "'", "\\'")}</c:set>

	function getDivContent ( containingDiv ){
		var successUrl ='${successUrl}';
		var errorUrl ='${errorUrl}';
		return '\
			<div id="eacLoginWidgetContainer">\
    			<div id="eacLoginHeader"> \
    				<span id="eacLoginMessage">${labelLogin}</span>\
    			</div>\
	    		<div id="eacLoginBody">\
  					<form id="eacBasicLoginForm" action="${urlPrefix}/basicLogin.htm" method="post">\
    					<div id="eacLoginUsername">\
      						<label id="eacUsernameLabel" for="eacUsername">${labelUserName}</label>\
      						<input id="eacUsername" name="username" type="text" maxlength="255"/>\
    					</div>\
    					<div id="eacLoginPassword">\
      						<label id="eacPasswordLabel" for="eacPassword">${labelPassword}</label>\
      						<input id="eacPassword" name="password" type="password" maxlength="255"/>\
    					</div>\
                        <div  id="eacLoginProblems">\
                            <ul>\
                              <li>\
                                <a id="eacLoginProblemsLink" href="http://global.oup.com/contact_us/">${loginProblemsText}</a>\
                              </li>\
                              <li>\
                                <a id="eacPasswordResetLink" id="eacPasswordResetLink" href="${passwordResetLink}">${passwordResetText}</a>\
                              </li>\
                            </ul>\
                        </div>\
    					<div id="eacLoginButton" >\
    					   <input id="eacSuccessUrl" name="success_url" type="hidden" maxlength="255" value="'+successUrl+'"/>\
   						   <input id="eacErrorUrl" name="error_url" type="hidden" maxlength="255" value="'+errorUrl+'"/>\
    					   <input id="eacLoginSubmit" type="submit" maxlength="255" value="${labelLogin}"/>\
    					</div>\
  					</form>\
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
        isLoggedIn:     false,		
		eacSessionId: 	null,
		eacUserId: 		null,
		eacUserName: 	null,
        eacFirstName:   null,
        eacLastName:    null,
		eacTitleUserName: '<c:out value="${titleUserName}"/>',
		eacTitlePassword: '<c:out value="${titlePassword}"/>',
		eacUserType:    null,
		showLoginWidget:  function( containingDiv){
            showLogin( containingDiv)
        }
	};
}();// end EAC_LOGIN_WIDGET

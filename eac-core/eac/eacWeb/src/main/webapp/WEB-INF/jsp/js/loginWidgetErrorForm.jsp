<%@ page language="java" contentType="text/javascript; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

var EAC_LOGIN_WIDGET = function() { 

	/*
	 * Copyright (c) 2012 Oxford University Press  
	 */

    <spring:message var="titleUserName" code="label.username.title" text="?label.username.title?"/>
    <spring:message var="titlePassword" code="label.password.title" text="?label.password.title?"/>
    
    <c:set var="titleUserName">${fn:replace(titleUserName, "'", "\\'")}</c:set>
    <c:set var="titlePassword">${fn:replace(titlePassword, "'", "\\'")}</c:set>

	
	function showLogin ( containingDiv ) {
		var div = document.getElementById(containingDiv);
		if(div){
			var divContent = 'An error has occurred!'; 
  			div.innerHTML = divContent;
  		}
 
  		if(typeof console !== 'undefined' && console) {
    		console.error('${errorMsg}'); 
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
		showLoginWidget:  function( containingDiv){
            showLogin( containingDiv)
        }
	};
}();// end EAC_LOGIN_WIDGET

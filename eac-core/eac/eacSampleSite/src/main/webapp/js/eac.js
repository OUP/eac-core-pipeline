function showHideLogout() {
    var loginLogout = document.getElementById("loginLogout");
    
    if (readCookie("EAC_ERIGHTS") != null) {
        loginLogout.style.display = "block";
    }
    else {
        loginLogout.style.display = "none";
    }
}

function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}

function logout(deployProtocol, deployHost, eacHost, contextPath) {	
	window.location = deployProtocol + "://"+eacHost+"/eac/logout.htm?url=http%3A%2F%2F"+deployHost+"%2F"+contextPath+"%3Flogout%3Dtrue";
}

function deleteCookie(cookieHost) {
	if (getQueryParameter("logout") == "true") {
		Delete_Cookie("EAC_ERIGHTS","/",cookieHost);
	}
}

function getQueryParameter ( parameterName ) {
	  var queryString = window.top.location.search.substring(1);
	  var parameterName = parameterName + "=";
	  if ( queryString.length > 0 ) {
	    begin = queryString.indexOf ( parameterName );
	    if ( begin != -1 ) {
	      begin += parameterName.length;
	      end = queryString.indexOf ( "&" , begin );
	        if ( end == -1 ) {
	        end = queryString.length
	      }
	      return unescape ( queryString.substring ( begin, end ) );
	    }
	  }
	  return "null";
	}

function init(cookieHost) {
	deleteCookie(cookieHost);
	showHideLogout();	
}
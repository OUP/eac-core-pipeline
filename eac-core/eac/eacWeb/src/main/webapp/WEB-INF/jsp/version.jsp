<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
      <title><spring:message code="title.version" text="?title.version?" /></title>     
		<style type="text/css">
			table.versiontable {
			    font-family: verdana,arial,sans-serif;
			    font-size:11px;
			    color:#333333;
			    border-width: 1px;
			    border-color: #666666;
			    border-collapse: collapse;
			    margin-left: 200px;
			}
            table.versiontable caption {
                border-width: 1px;
                padding: 8px;
                border-style: solid;
                border-color: #666666;
                background-color: #ffffff;
            }
            table.versiontable td {
                border-width: 1px;
                padding: 8px;
                border-style: solid;
                border-color: #666666;
                background-color: #ffffff;
            }
		</style>
		
		<script type="text/javascript" src="js/splash.js"> </script>
		<script type="text/javascript">
		var cookieEnabled;
		var flashinstalled = 0;
		var flashversion = 0;
		var navName = navigator.appName ;
		var brVer = navigator.userAgent; var brNum; var reg = new RegExp('/');
		
		if (navigator.appName == 'Microsoft Internet Explorer') {
		  verNumIE() ;
		} else {
		  verNumOt() ;
		}
		
		function verNumIE() {
		   var brVerId = brVer.indexOf('MSIE');
		   brNum = brVer.substr(brVerId,8);
		}
		
		function verNumOt() {
		   var brVerId = brVer.search(reg);
		   brNum = brVer.substring(brVerId+1);
		}
		
		function cookieEnabled() {   
		  cookieEnabled=(navigator.cookieEnabled)? true : false;    
		   // if Not IE4+ Nor NS6+
		  if (typeof navigator.cookieEnabled=="undefined" && !cookieEnabled){ 
		    document.cookie="testcookie"
		    cookieEnabled=(document.cookie.indexOf("testcookie")!=-1)? true : false
		  }
		  if (cookieEnabled) return "Yes";
		  return "No"
		}
		 
		function isJavaEnabled() {
		  return (navigator.javaEnabled() ? "Yes" : "No");
		} 
		
		function flashEnabled() {
		  var MSDetect = "false";
		  var k = new Array ("UNKNoWN","No","Yes");
		  if (navigator.plugins && navigator.plugins.length){
		    x = navigator.plugins["Shockwave Flash"];
		    if (x){
		      flashinstalled = 2;
		      if (x.description){
		        y = x.description;
		        flashversion = y.charAt(y.indexOf('.')-1);
		      }
		  }  else{
		    flashinstalled = 1;
		  }
		  if (navigator.plugins["Shockwave Flash 2.0"]){
		    flashinstalled = 2;
		    flashversion = 2;
		  }
		 } else if (navigator.mimeTypes && navigator.mimeTypes.length){
		    x = navigator.mimeTypes['application/x-shockwave-flash'];
		    if (x && x.enabledPlugin)  flashinstalled = 2;
		    else  flashinstalled = 1;
		 } else{
		  // Microsoft Internet Explorer
		  MSDetect = "true";
		}
		  // Internet Explorer Check 
		  if (MSDetect == "true") {
		    var verIE = flashInstalledIE();
		    if (verIE == 0) flashinstalled = 1;
		    else{
		      flashinstalled=2;
		      flashversion = verIE
		    }
		  }
		  var outStr = k[flashinstalled];   
		  if (flashinstalled==2) outStr = k[flashinstalled] + ",&nbsp;&nbsp;Flash Version: " + flashversion;   
		  return outStr;
		 }
		
		function flashInstalledIE() {
		  for (var i=10; i>0; i--) {
		    flashVersion = 0;
		    try{
		      var flash = new ActiveXObject("ShockwaveFlash.ShockwaveFlash." + i);
		      return i;
		    } catch(e){}
		  }        
		  return 0;
		}
		</script>      
    </head>
    <body>
      <div>
	  <table class="versiontable" onclick="splash();">
 	    <caption><b>Server Information</b></caption> 
	    <tbody>
	    <tr>
	      <td><b>Build Number : </b></td>
	      <td><spring:message code="label.version" text="?label.version?" />: <c:out value="${version}"/></td>
	    </tr>
	    </tbody>
	  </table>
	  </div>
	  <div>
	  <table class="versiontable">
 	    <caption><b>Browser Information</b></caption>
	    <tbody>
	    <tr>
	      <td><b>Platform Name : </b></td>
	      <td><script type="text/javascript">document.write(navigator.platform);</script></td>
	    </tr>
	    <tr>
	      <td><b>Browser Name : </b></td>
	      <td><script type="text/javascript">document.write(navName);</script></td>
	    </tr>
	    <tr>
	      <td><b>Browser version : </b></td>
	      <td><script type="text/javascript">document.write(brNum);</script></td>
	    </tr>
	    <tr>
	      <td><b>Cookies : </b></td>
	      <td><script type="text/javascript">document.write(cookieEnabled());</script></td>
	    </tr>
	    <tr>
	      <td><b>Java : </b></td>
	      <td><script type="text/javascript">document.write(isJavaEnabled());</script></td>
	    </tr>
	    <tr>
	      <td><b>Flash : </b></td>
	      <td><script type="text/javascript">document.write(flashEnabled());</script></td>
	    </tr>
	    <tr>
	      <td><b>Screen : </b></td>
	      <td><script type="text/javascript">
	      if (window.screen) {
	        document.write('height: ' + screen.height + '&nbsp;width: ' + screen.width + '&nbsp;available height: ' + screen.availHeight + '&nbsp;available width: ' + screen.availWidth + '&nbsp;color depth: ' + screen.colorDepth);
	      }
	      else{
	        document.write('Not Detected');
	      }
	      </script>
	      </td>
	    </tr>
	    <tr>
	      <td><b>Date Time : </b></td>
	      <td><script type="text/javascript">    
	      var dt = new Date();
	      var d  = dt.getDate();
	      var day = (d < 10) ? '0' + d : d;
	      var m = dt.getMonth() + 1;
	      var month = (m < 10) ? '0' + m : m;
	      var yy = dt.getYear();
	      var year = (yy < 1000) ? yy + 1900 : yy;
	      var hours = dt.getHours();
	      var minutes = dt.getMinutes();
	      if (minutes < 10) {
	        minutes = "0" + minutes;
	      }
	      var tz = dt.getTimezoneOffset() / 60;
	      document.write(day + "/" + month + "/" + year + " " + hours + ":" + minutes + " GMT" + tz);
	      </script>
	      </td>
	    </tr>
	    </tbody>
	  </table>
	  </div>
	  <p></p>
    </body>
</html>
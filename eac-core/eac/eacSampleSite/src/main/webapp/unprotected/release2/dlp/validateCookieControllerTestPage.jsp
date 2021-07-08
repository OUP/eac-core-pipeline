<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title>Test 'validate eac cookie controller'</title>
        <script type="text/javascript" src="/eacSampleSite/javascripts/jquery-1.3.2.min.js"></script>
        <script type="text/javascript" src="/eacSampleSite/javascripts/jquery.cookie.js"></script>
    </head>
    <body>
    <form action="/eac/validateCookie.htm" method="get">
        <br/>Success    <input type="text" name="authN_url" value="http://www.google.com" size="100" />
        <br/>Failure    <input type="text" name="noAuth_url" value="http://news.bbc.co.uk" size="100" />
        <br/>Error      <input type="text" name="error_url" value="http://www.oracle.com" size="100" />
        <br/>Cookie     <input id="cookie" type="text" name="cookie" value="cookieValue" size="100" />
        <br/>           <input id="submit" type="submit" name="submit" />
    </form>
<script type="text/javascript">
$(document).ready(function() {
    $('#submit').click(function(event){    	
    	$cookie = $('#cookie').val();    	
    	var cookieOptions = {expires: 7, path: '/'};
    	$.cookie("EAC", $cookie, cookieOptions);
    	console.log($.cookie("EAC"));
    });
});
</script>   
</body>
</html>
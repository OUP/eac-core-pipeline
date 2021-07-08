<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-control" content="public">
		
		<title>EAC Diagnostic Test</title>
		<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen, projection">
		<link rel="stylesheet" href="css/print.css" type="text/css" media="print">
		 <link rel="icon" type="image/x-icon" href="images/favicon.ico"/>
		<!--[if lt IE 8]>
		        <link rel="stylesheet" href="/eacAdmin/resources/blueprint/ie.css" type="text/css" media="screen, projection" />
		<![endif]-->
        <link href="css/jquery-ui.css" rel="stylesheet" type="text/css">
        <script type="text/javascript">
        	var userLocale = 'en-uk';
        
        	var djConfig = {
	            locale: userLocale
	        };
	    </script>
	    <link type="text/css" rel="stylesheet" href="css/tipTip.css">
		<link type="text/css" rel="stylesheet" href="css/eacadmin.css" media="screen">		 
	    <script type="text/javascript" src="js/jquery.min.js"></script>
		<script type="text/javascript" src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="js/jcookie.js"></script>
		<script type="text/javascript" src="js/jquery.tipTip.minified.js"></script>
		<script type="text/javascript" src="js/jquery.shorten.min.js"></script>
		<script type="text/javascript" src="js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="js/eacAdmin.js"></script>
		<script type="text/javascript" src="js/capslock.jquery.js"></script>		
	</head>
	<body>
		<div id="page" class="container notLoggedIn">
			<div id="header">
				<div id="logo">
	                <div class="sub-heading">
						<a href="/eacDiagnostic/">
							<img src="images/logo.gif" alt="Oxford University Press">
						</a>
                        <span class="sub-heading-1">EAC Diagnostic Test</span>
	                </div>
				</div>
			</div>
			<div id="main">
				









<div id="login" style="width: 400px;">
	<div>
		
		<div id="capsLock" class="error" style="display:none">
				<p>Caps Lock was on (last time a letter was typed)</p>
		</div>
	</div>
	
						
	<form name="f" action="LoginServlet" method="post">
		<fieldset style="padding-bottom: 0px\9; padding-left: 40px; padding-right: 40px; text-align: left">
			<legend>Login Information</legend>
			<div style="padding-top:20px">
				<div style="width: 8em; float:left; text-align: left">
					<label for="j_username">Username</label>
				</div>
				<div>
					<p><input type="text" name="j_username" style="width: 20em; font-size: 1.1em; font-family: Verdana,Arial,sans-serif" id="j_username"></p>
				</div>
			</div>
			<div>
				<div style="width: 8em; float:left; text-align: left">
					<label for="j_password">Password</label>
				</div>
				<div>
					<p><input type="password" name="j_password" style="width: 20em; font-size: 1.1em; font-family: Verdana,Arial,sans-serif" id="j_password"></p>
				</div>
			</div>
			<div>${errormessage}</div>
			<!-- <div id="resetPassword" style="float: left; padding-top: 10px">
				
				<a href="http://localhost:8080/eacAdmin/mvc/password/reset.htm">Forgotten password</a>
			</div> -->
			<div id="buttons" style="float:right; padding-right: 2px">
				<button id="submit" type="submit">Sign in</button>
			</div>				
		</fieldset>
	</form>
	<p style="padding-top: 15px\9; text-align: center">Unauthorized or improper use of this system is prohibited and constitutes an offence under the Computer Misuse Act 1990. By continuing to use this system you indicate your awareness of and consent to these Terms and conditions of use.</p>
</div>
<script type="text/javascript">

	$(function() {
		var options = {
				caps_lock_on: function() {
					$('#capsLock').show();
				},
				caps_lock_off: function() {
					$('#capsLock').hide();
				},
				caps_lock_undetermined: function() {
					$('#capsLock').hide();
				}
			};
			$("input[type='password']").capslock(options);
			
			if (window.location.href.indexOf('login_error') != -1 && $('#j_username').val()) {
				$('#j_password').focus();
			} else {
				$('#j_username').focus();
			}
	});
	
</script>
				<div id="push">&nbsp;</div>
			</div>
		</div>
		<!-- <div class="container">
		<div id="footer" class="span-24"> -->
	 		










<!-- <div id="footerTile">
	Copyright © Oxford University Press, 2016. All Rights Reserved.
</div>
<div id="appVersion">v21.88-SNAPSHOT</div>
        </div> -->
        <!-- </div> -->
	
</body></html>
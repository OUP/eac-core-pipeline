<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<html>
<head>
<title>EAC Login Widget Setup</title>
	<script type="text/javascript" src="<c:url value="/javascripts/jquery-1.5.1.min.js" />"></script>	  
	<script type="text/javascript" src="http://jzaefferer.github.com/jquery-validation/jquery.validate.js"></script>
	<script type="text/javascript" src="<c:url value="/javascripts/jquery.tipTip.minified.js" />"></script>
	<c:set var="widgetEnv" value="access.uat.oup.com"/>
	<c:if test="${param['widget_env'] == 'prod'}">
		<c:set var="widgetEnv" value="access.oup.com"/>
	</c:if>
	<script type="text/javascript" src="https://${widgetEnv}/eac/loginWidget.js?success_url=<%= java.net.URLEncoder.encode((String)pageContext.getAttribute("urlPrefix"), "UTF-8") %>%2feacSampleSite%2floginwidget%2fsetup.jsp%3flocale%3den_GB%26urlError%3d<%= java.net.URLEncoder.encode(java.net.URLEncoder.encode((String)pageContext.getAttribute("urlPrefix"), "UTF-8"), "UTF-8") %>%252feacSampleSite%252floginwidget%252fsetup.jsp%253ferror%253d1%26urlSuccess%3d<%= java.net.URLEncoder.encode(java.net.URLEncoder.encode((String)pageContext.getAttribute("urlPrefix"), "UTF-8"), "UTF-8") %>%252feacSampleSite%252floginwidget%252fsetup.jsp&error_url=<%= java.net.URLEncoder.encode((String)pageContext.getAttribute("urlPrefix"), "UTF-8") %>%2feacSampleSite%2floginwidget%2fsetup.jsp%3ferror%3d1&lang=en_GB&logout_url=<%= java.net.URLEncoder.encode((String)pageContext.getAttribute("urlPrefix"), "UTF-8") %>%2feacSampleSite%2floginwidget%2fsetup.jsp%3flocale%3den_GB%26urlError%3d<%= java.net.URLEncoder.encode(java.net.URLEncoder.encode((String)pageContext.getAttribute("urlPrefix"), "UTF-8"), "UTF-8") %>%252feacSampleSite%252floginwidget%252fsetup.jsp%253ferror%253d1%26urlSuccess%3d<%= java.net.URLEncoder.encode(java.net.URLEncoder.encode((String)pageContext.getAttribute("urlPrefix"), "UTF-8"), "UTF-8") %>%252feacSampleSite%252floginwidget%252fsetup.jsp"></script>	
	<link type="text/css" rel="stylesheet" href="<c:url value="/styles/tipTip.css" />" />
	<link rel="stylesheet" href="<c:url value="/styles/loginWidgetSetup.css" />" type="text/css" media="screen" />		
</head>
<body>
	<div id="content">
		<div id="header">
			<img alt="Oxford University Press" src="<c:url value="/images/logo.gif" />">
			<span class="heading-1">EAC Login Widget Setup</span>
		</div>
		<form id="form" action="data.jsp" method="POST">
			<fieldset>
				<legend class="heading-2">Your site info</legend>
				<div class="field">
					<div class="label">Success URL</div>
					<div class="value"><input type="text" name="urlSuccess" style="width: 540px; float: left" class="required"/>
						<img id="successUrlHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="This is where your web browser will redirect to after a successful login. This parameter is required.  As this parameter value is a URL, you should URL encode it."/>
		           		<script type="text/javascript">
		           			$("#successUrlHelp").tipTip({edgeOffset: 7});
		           		</script>
	           		</div>
				</div>
				<div class="field">
					<div class="label">Error URL</div>
					<div class="value"><input type="text" name="urlError" style="width: 540px; float: left" class="required" />
						<img id="errorUrlHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="This is where your web browser will redirect to after logging the user out. This parameter is required. As this parameter value is a URL, you should URL encode it."/>
		           		<script type="text/javascript">
		           			$("#errorUrlHelp").tipTip({edgeOffset: 7});
		           		</script>
					</div>
				</div>
				<div class="field">
					<div class="label">DIV id</div>
					<div class="value"><input type="text" name="divId" style="width: 150px; float: left" class="required" />
						<img id="divIdHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="This parameter specifies the id attribute of the element in your page (probably a DIV) that will act as a container for the login widget. This parameter is required."/>
		           		<script type="text/javascript">
		           			$("#divIdHelp").tipTip({edgeOffset: 7});
		           		</script>
					</div>
				</div>
				<div class="field">
					<div class="label">Locale</div>
					<div class="value"><input type="text" name="locale" style="width: 150px; float: left" />
						<img id="localeHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="This parameter specifies the locale (country specific language) of the text within the Login Widget. A value of ‘es_ES’ would select Spanish text.  This parameter is optional; the default language is British English (‘en_GB’)."/>
		           		<script type="text/javascript">
		           			$("#localeHelp").tipTip({edgeOffset: 7});
		           		</script>
					</div>
				</div>
				<div class="field">
					<div class="label">CC</div>
					<div class="value"><input type="text" name="cc" style="width: 150px; float: left"  />
						<img id="ccHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="This parameter allows you to specify the country code separately from the language. This parameter is optional. Using the locale parameter is preferred."/>
		           		<script type="text/javascript">
		           			$("#ccHelp").tipTip({edgeOffset: 7});
		           		</script>
					</div>
				</div>
				<div class="field">
					<div class="label">Sel Language</div>
					<div class="value"><input type="text" name="selLanguage" style="width: 150px; float: left"  />
						<img id="selLangHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="This parameter allows you to specify the language code separately from the country. This parameter is optional. Using the locale parameter is preferred."/>
		           		<script type="text/javascript">
		           			$("#selLangHelp").tipTip({edgeOffset: 7});
		           		</script>
					</div>
				</div>
				<div class="field">
					<div class="label">Environment</div>
					<input id="uat" type="radio" name="environment" value="uat" checked="checked"/><label for="uat">UAT</label>
					<input id="prod" type="radio" name="environment" value="prod"/><label for="prod">PROD</label>
				</div>
				<div id="submit" style="float: right">
					<input type="submit" value="Submit"/>
				</div>
			</fieldset>
		</form>
		<fieldset id="javascriptUrl">
			<legend class="heading-2">To load the Javascript</legend>
			<span>Add the following to the &lt;head&gt; section of your HTML page (requires JQuery):</span>
			<textarea id="url" rows="8" cols="80" readonly="readonly"></textarea>
		</fieldset>
		<fieldset id="loginWidgetJavascript">
			<legend class="heading-2">To show the login widget</legend>
			<span>Add the following Javascript to your HTML page:</span>
			<textarea id="loginWidgetUrl" rows="3" cols="80" readonly="readonly"></textarea>
		</fieldset>
		<fieldset id="loginLinksJavascript">
			<legend class="heading-2">To show the login links</legend>
			<span>An example of how to render the links in your HTML page:</span>
			<textarea id="loginLinks" rows="15" cols="80" readonly="readonly"></textarea>
		</fieldset>
		<div id="loginWidgetExample">
			<h3>Login Widget Example</h3>
			<div id="loginWidgetContainer"></div>
		</div>
		<div id="loginLinksExample">
			<h3>Login Links Example</h3>
			<ul id="loginLinksList">
			<script type="text/javascript">
				if (EAC_LOGIN_WIDGET.isLoggedIn) {
				    document.write('<li><strong>Hello ' + EAC_LOGIN_WIDGET.eacFirstName + ' ' + EAC_LOGIN_WIDGET.eacLastName + '</strong></li>');
				    document.write('<li><a href="https://${widgetEnv}/eac/profile.htm?locale=&mode=hub">View My Profile</a></li>');
				    document.write('<li class="last"><a href="https://${widgetEnv}/eac/logout.htm?url=<%= java.net.URLEncoder.encode((String)pageContext.getAttribute("urlPrefix") + "/eacSampleSite/loginwidget/setup.jsp", "UTF-8") %>&mode=hub">Logout</a></li>');
				} else {
				    document.write('<li><a href="https://${widgetEnv}/eac/login.htm?url=<%= java.net.URLEncoder.encode((String)pageContext.getAttribute("urlPrefix") + "/eacSampleSite/loginwidget/setup.jsp", "UTF-8") %>&locale=en_GB&mode=hub">Login</a></li>');
				    document.write('<li class="last"><a href="https://${widgetEnv}/eac/register.htm?prodId=8FE6FD5B-F136-4A86-A9FB-7AC3724A8298&locale=&mode=hub">Register</a></li>');
				}
			</script>
			</ul>
		</div>
		<div style="height: 50px;">&#160;</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$('input[name="urlSuccess"]').focus();
			
			$('#form').validate({
				submitHandler: function() {
					sendRequest($('#form').serialize());
					return false;
				},
				errorPlacement: function (error, element) {
					element.addClass('error');
				}
			});
			
			EAC_LOGIN_WIDGET.showLoginWidget('loginWidgetContainer');
			
			var isProdWidget = ${param['widget_env'] == 'prod'};
			if (isProdWidget) {
				$('#loginWidgetExample h3').css('color', 'red');
				$('#loginLinksExample h3').css('color', 'red');
			}
			
			var isWidgetError = ${not empty param['error']};
			if (isWidgetError) {
				$('#eacBasicLoginForm').append('<div id="loginError" style="color: red; padding-top: 10px; font-size: 0.8em">There was a problem logging in. Please try again.</div>');
			} else {
				$('#loginError').remove();
			}
			
			$('#loginWidgetContainer,#loginLinksList').dblclick(function() {
				var location = window.location.href;
				if (location.indexOf('widget_env') == -1) {
					if (location.indexOf('?') == -1) {
						location += '?';
					} else {
						location += '&';
					}
					if (isProdWidget) {
						location += 'widget_env=uat';
					} else {
						location += 'widget_env=prod';
					}
				} else {
					if (isProdWidget) {
						location = location.replace('widget_env=prod', 'widget_env=uat');
					} else {
						location = location.replace('widget_env=uat', 'widget_env=prod');
					}
					
				}
				window.location.replace(location);
			});
		});
		
		var javascriptTag = '<!-- You must include this line if you\'re not already using JQuery -->\n';
			javascriptTag += '<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script';
			javascriptTag += '>\n\n';
			javascriptTag += '<!-- Then to load the login widget Javascript you need: -->\n';
			javascriptTag += '<script type="text/javascript" src="{0}"></script';
			javascriptTag += '>';
				
		function sendRequest(queryString) {
			$.ajax( {
				url: 'data.jsp',
				data: queryString,
				dataType: 'json'
			}).done(function(data) {
				var $url = $('#url');
				$url.empty();
				$url.val(javascriptTag.replace('{0}', data.loginWidgetUrl));
				var $loginWidgetJavascript = $('#loginWidgetUrl');
				$loginWidgetJavascript.empty();
				$loginWidgetJavascript.append(data.loginWidgetJavascript);
				var $loginLinks = $('#loginLinks');
				$loginLinks.empty();
				$loginLinks.append(data.loginLinks);
			});
		}
	</script>
</body>
</html>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<c:set var="locale" value="${param['locale']}" />
<c:set var="cc" value="${param['cc']}" />
<c:set var="selLanguage" value="${param['selLanguage']}" />
<c:set var="urlError" value="${param['urlError']}" />
<c:set var="urlSuccess" value="${param['urlSuccess']}" />
<c:set var="divId" value="${param['divId']}" />

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

<c:set var="baseUrl">
	<c:choose>
		<c:when test="${param['environment'] == 'uat'}">https://access.uat.oup.com</c:when>
		<c:otherwise>https://access.oup.com</c:otherwise>
	</c:choose>
</c:set>

<c:url var="loginWidgetUrl" value="${baseUrl}/eac/loginWidget.js" >
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

{ 	"loginWidgetUrl": "${loginWidgetUrl}", 
	"loginWidgetJavascript": "$(document).ready(function() {\r\n    EAC_LOGIN_WIDGET.showLoginWidget('${divId}');\r\n});",
	"loginLinks": "if (EAC_LOGIN_WIDGET.isLoggedIn) {\r\n    document.write('&lt;li&gt;&lt;strong&gt;Hello ' + EAC_LOGIN_WIDGET.eacFirstName + ' ' + EAC_LOGIN_WIDGET.eacLastName + '&lt;/strong&gt;&lt;/li&gt;');\r\n    document.write('&lt;li&gt;&lt;a href=\"${baseUrl}/eac/profile.htm?locale=${locale}&mode=hub\"&gt;View My Profile&lt;/a&gt;&lt;/li&gt;');\r\n    document.write('&lt;li class=\"last\"&gt;&lt;a href=\"${baseUrl}/eac/logout.htm?url=${urlSuccess}&mode=hub\"&gt;Logout&lt;/a&gt;&lt;/li&gt;');\r\n} else {\r\n    document.write('&lt;li&gt;&lt;a href=\"${baseUrl}/eac/login.htm?url=${urlSuccess}&locale=${locale}&mode=hub\"&gt;Login&lt;/a&gt;&lt;/li&gt;');\r\n    document.write('&lt;li class=\"last\"&gt;&lt;a href=\"${baseUrl}/eac/register.htm?prodId=8FE6FD5B-F136-4A86-A9FB-7AC3724A8298&locale=${locale}&mode=hub\"&gt;Register&lt;/a&gt;&lt;/li&gt;');\r\n}"

}
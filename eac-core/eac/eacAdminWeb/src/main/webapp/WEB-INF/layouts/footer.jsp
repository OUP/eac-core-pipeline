<%@page import="com.oup.eac.common.utils.EACSettings"%>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<jsp:useBean id="dateTime" class="org.joda.time.LocalDateTime" />
<div id="footerTile">
	Copyright &copy; Oxford University Press, <joda:format value="${dateTime}" pattern="yyyy" dateTimeZone="UTC"/>. All Rights Reserved.
</div>
<div id="appVersion">v<%= EACSettings.getProperty(EACSettings.VERSION) %></div>
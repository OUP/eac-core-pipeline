<%@attribute name="lower" required="false" type="java.lang.String"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<spring:message var="logout" code="label.logout" text="?label.logout?" />
<c:if test="${lower eq 'true'}" >
	<c:set var="logout" value="${fn:toLowerCase(logout)}" />
</c:if>
<eac:logoutLandingUrl var="logoutLandingUrl"/>
<a href="logout.htm?url=${logoutLandingUrl}">${logout}</a>

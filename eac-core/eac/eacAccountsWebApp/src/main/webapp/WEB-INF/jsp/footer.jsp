<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@page import="java.util.*" %>
<c:set var="eacUrl">${pageContext.request.requestURL}</c:set>
<c:set var="eacWebUrlIndex">${fn:indexOf(eacUrl,'eacAccounts')}</c:set>
<c:set var="eacWebUrl">${fn:substring(eacUrl,0,eacWebUrlIndex)}eac/</c:set>

<div id="footer">
	<div id="footer-middle">
		<div id="footer_left">
			<div class="footer_inner">
				<span><spring:message code="footer.copyright" text="?footer.copyright?" arguments="<%= String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) %>"/></span>
			</div>
		</div>
		<div id="footer_right">
			<div class="footer_inner">
				<span>
	                <tags:contactUs/> |
	                <a href="${eacWebUrl}accessibility.htm"><spring:message code="footer.accessibility" text="?footer.accessibility?" /></a> |
	                <a href="https://global.oup.com/cookiepolicy"><spring:message code="footer.cookiepolicy" text="?footer.cookiepolicy?" /></a> |
	                <a href="${eacWebUrl}privacyAndLegal.htm"><spring:message code="footer.termsandcondition" text="?footer.termsandcondition?" /></a> |
				    <a href="https://global.oup.com/privacy"><spring:message code="footer.privacypolicy" text="?footer.privacypolicy?" /></a>
				</span>
			</div>
		</div>
	</div>
</div>
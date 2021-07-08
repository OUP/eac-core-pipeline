<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@ include file="/WEB-INF/jsp/cookieBanner.jsp" %>

<eacAccounts:skinSiteName var="userSiteName"/>
<eacAccounts:skinUrl var="userSiteHomeURL"/>
<c:set var="defaultSiteName"><spring:message code="default.site.name"/></c:set>
<div class="row">
	<div class="col small-12">
		<header class="header clearfix">
			<a href="${userSiteHomeURL}" class="header_logo">${defaultSiteName}</a>
			<c:if test="${userSiteName != defaultSiteName}">
				<span class="header_title">${userSiteName}</span>
			</c:if>
		</header>
	</div>
</div>
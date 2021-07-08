<%@attribute name="lower" required="false" type="java.lang.String"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<eac:skinContactUs var="contactUsURL" />
<spring:message var="contactUs" code="footer.contactus" text="?footer.contactus?" />
<c:if test="${lower eq 'true'}" >
	<c:set var="contactUs" value="${fn:toLowerCase(contactUs)}" />
</c:if>
<a href="${contactUsURL}">${contactUs}</a>
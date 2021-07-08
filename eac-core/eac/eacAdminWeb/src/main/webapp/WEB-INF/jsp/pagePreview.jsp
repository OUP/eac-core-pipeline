<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<form:form id="editRegistrationAnswersForm" method="post" commandName="registrationDto">
	<%-- <eac-common:eacform regDto="${registrationDto}" hideHelp="true"/> --%>
	<c:set var="regDto" value="${registrationDto}"></c:set>
	<%@include file="/WEB-INF/jsp/eacFormTag.jsp"%>
</form:form>

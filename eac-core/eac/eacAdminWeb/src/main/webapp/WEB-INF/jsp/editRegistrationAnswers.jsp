<%@include file="/WEB-INF/jsp/taglibs.jsp"%>

<tags:status name="registrationDto" />
<form:form id="editRegistrationAnswersForm" method="post" commandName="registrationDto">
	<fieldset>
		<div>
			<spring:message code="label.mandatory" text="?label.mandatory?" />
		</div>
	</fieldset>
	<%-- <eac-common:eacform regDto="${registrationDto}" hideHelp="true"/> --%>
		<c:set var="regDto" value="${registrationDto}"></c:set>
		<%@include file="/WEB-INF/jsp/eacFormTag.jsp"%>
</form:form>

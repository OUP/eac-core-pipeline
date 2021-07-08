<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
  <title><spring:message code="title.noactivelicence" text="?title.noactivelicence?" /></title>
  <meta name="WT.cg_s" content="No Active Licence"/>
</head>
<body class="stickyFooter">
<section class="content">
			<div class="row">
				<div class="col large-8 large-centered">
					<div class="box">
						<h1><spring:message code="title.noactivelicence" text="?title.noactivelicence?" /></h1>
						<div class="message message--error">
							<p><spring:message code="label.noactivelicence" text="?label.noactivelicence?"/> <c:if test="${not empty allExpired}"><spring:message code="label.noactivelicence1" text="?label.noactivelicence1?"/> <c:if test="${sessionScope.PRODUCT ne null && sessionScope.PRODUCT.selfRegisterable}"><a href="reregister.htm"></c:if><spring:message code="label.noactivelicence2" text="?label.noactivelicence2?"/><c:if test="${sessionScope.PRODUCT ne null && sessionScope.PRODUCT.selfRegisterable}"></a></c:if></c:if> <br/> <br/> 
								<spring:message code="label.licenceDetails" text="?label.licenceDetails?"/><br/><br/>
							</p>
							<p><c:forEach items="${licenceDtos}" var="licenceDto">
			        <eac-common:licenceDtoStatus var="licStatus" licenceDto="${licenceDto}" />
		        	<c:set var="messageCode" value="${licStatus.messageCode}" />
		        	<eac-common:licenceDescription var="licenceDescription" licenceDto="${licenceDto}" generateHtml="${false}"/>
			        ${licenceDescription} ( <spring:message code="${messageCode}" text="?${messageCode}?" />  ) <br/>
		        </c:forEach></p>
		        <p><spring:message code="label.noactivelicence3" text="?label.noactivelicence3?" argumentSeparator=";" arguments="${email}"/></p>
						</div>
					</div>
				</div>
			</div>

		</section>
<script src="js/core.min.js"></script>
</body>
</html>
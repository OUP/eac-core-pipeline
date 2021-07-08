<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div>
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title" /></h1>
	</div>
	<c:choose>
		<c:when test="${not empty statusMessageKey}">
			 <div class="success">
				<spring:message code="${statusMessageKey}"/>
			</div>
		</c:when>
		<c:otherwise>
             <div id="welcomeBanner">
                <spring:message code="info.welcome"/>
            </div>                
		</c:otherwise>
	</c:choose>
</div>

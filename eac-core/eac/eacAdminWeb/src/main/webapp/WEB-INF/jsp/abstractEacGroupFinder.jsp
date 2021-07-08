<%--  
    
    
	NOTE: This jsp is not designed to be called directly. It is used by the productFinder.tag.
		
			
 --%>
<%@page import="java.util.UUID"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:set var="mode" value="${param.mode}"/>
<c:set var="returnFieldId" value="${param.returnFieldId}"/>
<c:set var="returnNameFieldId" value="${param.returnNameFieldId}"/>
<%-- <c:set var="type" value="${param.type}"/> --%>
<c:set var="initGuid" value="${param.initGuid}"/>
<c:set var="initOnChangeId" value="${param.initOnChangeId}"/>
<c:set var="resetButtonId" value="${param.resetButtonId}"/>
<c:set var="productStates" value="${param.productStates}" />
<spring:message code="label.pleaseSelect" var="pleaseSelect" />
<spring:message code="label.clear" var="clear"/>
<% pageContext.setAttribute("id", UUID.randomUUID().toString()); %>
<div class="finder" id="finder-${id}">
	<div>
		<div class="inputWrapper"><input class="textField" type="text" disabled="disabled" value="${pleaseSelect}"/><span class="iconClear" title="${clear}">x</span></div><button type="button" class="openButton">...</button>
	</div>
	<div class="finderPopup">
		<div class="finderForm">
			<div>
				<input name="search_term" class="textField" type="text" /><a class="searchLink" href="#"><img class="searchImage" src="<c:url value="/images/magnifying_glass.png"/>"/></a>
			</div>
			<div class="searchOptions">
				<span class="searchOption"><input id="searchGroupName-${id}" type="radio" name="search_by-${id}" value="groupName" checked="checked"/><label for="searchGroupName-${id}"><spring:message code="label.groupName" /></label></span>
				<span class="searchOption"><input id="searchName-${id}" type="radio" name="search_by-${id}" value="productName"/><label for="searchName-${id}"><spring:message code="label.productName" /></label></span>
				<span class="searchOption"><input id="searchProductId-${id}" type="radio" name="search_by-${id}" value="productId"/><label for="searchProductId-${id}"><spring:message code="label.productId" /></label></span>
				<span class="searchOption"><input id="searchExternalId-${id}" type="radio" name="search_by-${id}" value="externalId"/><label for="searchExternalId-${id}"><spring:message code="label.externalId" /></label></span>
			</div>
		</div>
		<div class="finderResults"></div>
	</div>
</div>
<script type="text/javascript">

	$(function() {
		initialiseFinder($('#finder-${id}'), { 
			mode: '${mode}', 
			returnFieldId: '${returnFieldId}', 
			returnNameFieldId: '${returnNameFieldId}',
			initText: '${pleaseSelect}',
			initGuid: '${initGuid}',
			initOnChangeId: '${initOnChangeId}',
			resetButtonId: '${resetButtonId}',
			contextPath: '${pageContext.request.contextPath}',
			pleaseWaitMessage: '<spring:message code="label.pleaseWait"/>' ,
			productStates: '${productStates}' 
		});
	});
	
</script>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="productSearchTile">
	<%-- <div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.productSearch"/></h1>
	</div> --%>
	
	
         
<%-- 	<spring:hasBindErrors name="productSearchBean">
		<div class="error">
			<spring:bind path="productSearchBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors> --%>
	
	<div id="help">
		This facility allows you to search for products. 
		<br/>
		<br/>
		The following fields support partial matching:
		<ul>
			<li><spring:message code="label.productName" /></li>
			<li><spring:message code="label.externalId" /></li>
		</ul>
	</div>
	
	<div style="float:left; margin-right: 1em">
		<c:url value="/mvc/product/search" var="formUrl"/>
		<form:form modelAttribute="productSearchBean" action="" id="searchForm">
	    	<tiles:insertAttribute name="productSearchFormTile"/>
		</form:form>
	</div>
	
	<tiles:insertAttribute name="searchControlTile"/>
	<%-- <tiles:insertAttribute name="productSearchResultsTile"/> --%>
	
</div>
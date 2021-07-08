<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="productSearchTabsTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.productSearch"/></h1>
	</div>
	<c:set var="id" value="${param['id']}"/>
	
	<c:set var="statusMessageKey" value="${param['statusMessageKey']}"/>
	<c:if test="${statusMessageKey ne null}">
		 <div class="success">
			<spring:message code="${statusMessageKey}"/>
			<c:if test="${id ne null}">
				<c:out value="${id}"/>
			</c:if>
		</div>
	</c:if>
	<c:set var="errorMessageKey" value="${param['errorMessageKey']}"/>
	<c:if test="${errorMessageKey ne null}">
		 <div class="error">
			<spring:message code="${errorMessageKey}"/>
		</div>
	</c:if>

	<spring:hasBindErrors name="productSearchBean">
		<div class="error">
			<spring:bind path="productSearchBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>
	
	<div id="pagetabs">
	    <ul>
	        <li class="ui-state-hover"><a href="#pagetab-1"><spring:message code="label.product" /></a></li>
	        <li><a href="#pagetab-2"><spring:message code="tab.url" /></a></li>
	    </ul>
	    <div id="pagetab-1" class="ui-tabs-hide">
			<tiles:insertAttribute name="productSearchTile"/>
	    </div>
	    <div id="pagetab-2" class="ui-tabs-hide">
			<tiles:insertAttribute name="productMatchTile"/>
	    </div> 
	</div>	
	
	<tiles:insertAttribute name="productSearchResultsTile"/>
	
	<script type="text/javascript">
		function enableTab1() {
        	$('#searchFor').focus();
        	$('#searchResultsTile').empty();
		}
		
		function enableTab2() {
        	$('#exampleUrl').focus();
        	$('#searchResultsTile').empty();
		}
		
	    $(document).ready(function() {
	        $('#pagetabs').tabs({ cookie: { name: 'productSearch', expires: 1 } });
	        if(!$('#pagetab-1').hasClass('ui-tabs-hide')) {
	        	enableTab1();
	        } else {
	        	enableTab2();
	        }
	        $('a[href="#pagetab-1"]').click(enableTab1);
	        $('a[href="#pagetab-2"]').click(enableTab2);
	    });
	</script>
</div>
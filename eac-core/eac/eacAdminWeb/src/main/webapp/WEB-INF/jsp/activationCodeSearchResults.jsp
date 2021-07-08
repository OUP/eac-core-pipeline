<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="searchResultsTile">
    <c:if test="${not empty pageInfo}">
    <div id="totalSearchCount">
		<table class="summary">
			<tr>
			<c:if test="${pageInfo.totalItems== 0 or pageInfo.totalItems== 1 }">
				<label for="totalRecord"><spring:message code="label.totalUserFoundOne" /><c:out value="${pageInfo.totalItems}"></c:out> <spring:message code="label.totalUserFoundTwo" /></label>
			</c:if>
			<c:if test="${pageInfo.totalItems!= 0 and pageInfo.totalItems> 1}">
				<label for="totalRecord"><spring:message code="label.totalUserFoundOne" /><c:out value="${pageInfo.totalItems}"></c:out> <spring:message code="label.totalUserFoundTwoMul" /></label>
			</c:if>
			</tr>
		</table>
	</div>
	<table class="summary">
	    <thead>
	        <tr>
	            <th style="width: 15%">Code</th>
	            <th>Product</th>
	            <th>Product Group</th>
	            <th style="width: 10%">Available</th>
	            <th style="width: 10%">Action</th>
	        </tr>
	    </thead>
	    <tbody>
	    	<c:choose>
	    		<c:when test="${not empty pageInfo.items}">
				    <c:forEach var="activationCode" items="${pageInfo.items}" varStatus="status">
				        <tr>
				            <td class="role_row">${activationCode.code}</td>
				            <td class="role_row">${activationCode.productName}</td>
				             <td class="role_row">${activationCode.eacGroupName}</td>
				            <td class="role_row"><c:choose><c:when test="${activationCode.available}">Yes</c:when><c:otherwise>No</c:otherwise></c:choose></td>
				            <td class="role_row" style="text-align: right;"><spring:message code="label.view" var="view"/><c:url value="/images/view.png" var="viewUrl"/><a class="actionLink" href="#">
				            <img class="actionImg" src="${viewUrl}" title="${view}" alt="${view}" onclick="openStatus('${activationCode.code}');return false;"/></a></td>
				        </tr>  
				    </c:forEach>
			       <tiles:insertAttribute name="searchNavigationTile"/>
	    		</c:when>
	    		<c:otherwise>
	    			<tr>
	    				<td colspan="5" style="text-align: center"><spring:message code="label.noActivationCodesFound" text="No activation codes found"/></td>
	    			</tr>
	    		</c:otherwise>
	    	</c:choose>
	    </tbody>
	</table>
	</c:if>
</div>
<div id="dialog-form" title="Activation Code Status">
    <div id="item-selection"></div>
</div>
<script type="text/javascript">
	$(function() {
		
	    $( "#dialog-form" ).dialog({
	        autoOpen: false,
	        height: 280,
	        width: 450,
	        modal: true,
	        buttons: {
	            Cancel: function() {
	                $( this ).dialog( "close" );
	            }
	        },
	        open: function() {
	        	var selectionDivId = $('#item-selection');
	        	$(selectionDivId).empty();
	    		var urlVal = $(this).data('url');
	    		$.ajax({
	    	 		url: urlVal,
	    	 		type: 'GET'
	    	 	}).done(function(html) {
	    		    $(selectionDivId).html(html);
	    	 	});	
		    	$('.ui-dialog-titlebar-close').hide(); 
		    	$('.ui-widget-overlay').css('width','100%');	    		
	        }
	    });

	});
	
	function openStatus(activationCodeId) {
		var url = '<c:url value="/mvc/activationCode/search.htm?method=details&id="/>' + activationCodeId;
		$( "#dialog-form" ).data('url', url).dialog('open');		
	}
	
</script>
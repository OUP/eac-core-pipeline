<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="searchResultsTile">
  <c:if test="${not empty pageInfo}">
  
  
<div id="totalSearchCount">
					<table class="summary">
						<tr>
						<c:if test="${pageInfo.totalItems== 0 or pageInfo.totalItems== 1 }">
							<label for="totalRecord"><spring:message code="label.totalUserFoundOne" /><c:out value="${pageInfo.totalItems}"></c:out> <spring:message code="label.totalUserFoundTwo" /> </label>
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
				<th>Batch Name</th>
				<th>Product</th>
				<th>Product Group</th>
				<th>Licence Type</th>							
				<th class="createdDate">Created Date</th>
				<th>Start Date</th>
				<th>End Date</th>				
				<th style="width: 12%">Actions</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty activationCodeBatches}">
				<tr>
					<td colspan="8" style="text-align:center">No batches found.</td>
				</tr>
			</c:if>
			<c:if test="${not empty activationCodeBatches}">
      	  		<spring:message var="msg0" code="label.delete.batch" />
      	  		<spring:message var="msg1" code="label.edit.batch" />
      	  		<spring:message var="msg2" code="label.unformattedcodes" />
		 		<spring:message var="msg3" code="label.formattedcodes" />
				
				<c:url var="url0" value="/resources/images/delete.png" />
				<c:url var="url1" value="/resources/images/pencil.png" />
				<c:url var="url2" value="/resources/images/download-small.png" />
				<c:url var="url3" value="/resources/images/download-small-f.png" />
				
			
				<c:forEach var="activationCodeBatch" items="${activationCodeBatches}">
					<c:set var="activationCodeBatch" value="${activationCodeBatch}" scope="request"/>
					<tr>
						<td><c:out value="${activationCodeBatch.batchId}" escapeXml="false" /></td>
						<td><c:out value="${activationCodeBatch.activationCodeRegistrationDefinition.product.productName}"/></td>
						<td><c:out value="${activationCodeBatch.activationCodeRegistrationDefinition.eacGroup.groupName}"/></td>
						<td><c:out value="${activationCodeBatch.licenceTemplate.licenceType}"/></td>
						<td class="createdDate"><joda:format value="${activationCodeBatch.createdDate}" style="SM" dateTimeZone="UTC" locale="en_GB"/></td>
						<td><joda:format value="${activationCodeBatch.startDate}" style="S-" dateTimeZone="UTC" locale="en_GB"/></td>
						<td><joda:format value="${activationCodeBatch.endDate}" style="S-" dateTimeZone="UTC" locale="en_GB"/></td>
						<c:set var="clazz" value="summaryAction" />
						<c:set var="showDeleteBatch" value="${true}" />
						<c:set var = "actualValue" value = "${activationCodeBatch.batchId}"/>
						<c:url value="/mvc/batch/edit.htm" var="editUrl">
						  <c:param name="id" value="${actualValue}" />
						</c:url>
						<c:url value="/export" var="exportUrl">
						  <c:param name="batchId" value="${actualValue}" />
						</c:url>
						<c:url value="/exportFormatted" var="exportFormattedUrl">
						  <c:param name="batchId" value="${actualValue}" />
						</c:url>
						<c:url value="/mvc/batch/delete.htm" var="deleteUrl">
						  <c:param name="id" value="${actualValue}" />
						</c:url>
      					<c:set var = "updatedValue" value = "${fn:replace(actualValue, '&', 'amp;')}" />	
						<security:authorize ifAllGranted="ROLE_DELETE_ACTIVATION_CODE_BATCH" var="canDeleteBatch" />
						<c:if test="${canDeleteBatch}">
							<%-- <eac-common:isBatchUsed batchId="${activationCodeBatch.id}" var="batchUsed" />
							<c:set var="showDeleteBatch" value="${not batchUsed}" /> --%>
							<c:if test="${showDeleteBatch}" >
								<c:set var="clazz" value="${clazz} deleteBatch" />
							</c:if>
						</c:if>
						<td class="${clazz}">
							<c:if test="${canDeleteBatch}">
								<c:if test="${showDeleteBatch}">							
								<a class="actionLink deleteBatchAction" eacLabel="${activationCodeBatch.batchId}" href="${deleteUrl}" title="${msg0}"><img class="actionImg" alt="${msg0}" src="${url0}"/></a>
								</c:if>
							</c:if>
							<security:authorize ifAllGranted="ROLE_UPDATE_ACTIVATION_CODE_BATCH">
								
							<a class="actionLink" href="${editUrl}" title="${msg1}"><img class="actionImg" alt="${msg1}" src="${url1}"/></a>
							</security:authorize>
							
							<a class="actionLink" href="${exportUrl}"  title="${msg2}"><img class="actionImg" alt="${msg2}" src="${url2}"/></a>
							<a class="actionLink" href="${exportFormattedUrl}" title="${msg3}"><img class="actionImg" alt="${msg3}" src="${url3}"/></a>							
						</td>	
					</tr>
				</c:forEach>
			</c:if>
			<tiles:insertAttribute name="searchNavigationTile"/>
		</tbody>
	</table>
	
	<div style="float:right; min-height: 100px\9"">
		Last Search: <%= new java.util.Date().toString()%>
	</div>
	</c:if>	 
	</div>
		
	<spring:message code="confirm.title.delete.batch" text="" var="message" />
	<script type="text/javascript">
	$(document).ready(function() {
		$('#searchResultsTile').undelegate('.deleteBatchAction', 'click');<%--was getting 2 button clicks--%>	
		$('#searchResultsTile').delegate('.deleteBatchAction', 'click', eacConfirm({title:'${message}'}));
	});
	</script>

<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="bodyTile">
	<div>
		<div id="heading" class="ui-corner-top">
			<h1><spring:message code="title" /></h1>
		</div>
		
		<c:if test="${statusMessageKey ne null}">
			 <div class="success">
				<spring:message code="${statusMessageKey}"/>
			</div>
		</c:if>
	
		<fieldset>
			<legend><spring:message code="title.generated.batch.summary" /></legend>
			<div class="fieldCompact">
				<div class="fieldLabel">
					<label><spring:message code="label.batch" /></label>
				</div>
				<div class="fieldValue">
					<c:out value="${activationCodeBatchBean.activationCodeBatch.batchId}"/>	
				</div>
			</div>
			<div class="fieldCompact">
				<div class="fieldLabel">
					<label><spring:message code="label.created" /></label>
				</div>
				<div class="fieldValue">
					<joda:format value="${activationCodeBatchBean.activationCodeBatch.createdDate}" style="SM" dateTimeZone="UTC" locale="en_GB"/>	
				</div>
			</div>
			<div class="fieldCompact">
				<div class="fieldLabel">
					<label><spring:message code="label.product" /></label>
				</div>
				<div class="fieldValue" style="max-width: 60em">
					<c:out value="${activationCodeBatchBean.product.name}"/>	
				</div>
			</div>
			<div class="fieldCompact">
			<div class="fieldLabel">
					<label for="groupName"><spring:message code="label.groupName" /></label>
				</div>
				<div class="fieldValue" style="max-width: 53em;">
					<span id="groupName"><c:out value="${activationCodeBatchBean.activationCodeRegistrationDefinition.eacGroup.groupName}"/></span>	
				</div>
			</div>
			<div class="fieldCompact">
				<div class="fieldLabel">
					<label><spring:message code="label.numberoftokens" /></label>
				</div>
				<div class="fieldValue">
					<c:out value="${activationCodeBatchBean.numberOfTokens}"/>	
				</div>
			</div>
			<div class="fieldCompact">
				<div class="fieldLabel">
					<label><spring:message code="label.tokenAllowedUsages" /></label>
				</div>
				<div class="fieldValue">
					<c:out value="${activationCodeBatchBean.tokenAllowedUsages}"/>	
				</div>
			</div>
			<div class="fieldCompact" style="float: left">
				<div class="fieldLabel">
					<label><spring:message code="label.validfrom" /></label>
				</div>
				<div class="fieldValue">
					<c:choose>
						<c:when test="${activationCodeBatchBean.activationCodeBatch.startDate ne null}">
							<joda:format value="${activationCodeBatchBean.activationCodeBatch.startDate}" style="S-" dateTimeZone="UTC" />
						</c:when>
						<c:otherwise>Now</c:otherwise>
					</c:choose>	
				</div>
			</div>
			<div class="fieldCompact" style="float:left; clear: none; margin-left: 5em;">
				<div class="fieldLabelNarrow" style="width: 5em">
					<label><spring:message code="label.validto" /></label>
				</div>
				<div class="fieldValue">
					<c:choose>
						<c:when test="${activationCodeBatchBean.activationCodeBatch.endDate ne null}">
							<joda:format value="${activationCodeBatchBean.activationCodeBatch.endDate}" style="S-" dateTimeZone="UTC" />
						</c:when>
						<c:otherwise>No fixed period</c:otherwise>
					</c:choose>						
				</div>
			</div>
			<fieldset style="margin-top:3em; margin-top:0em\9">
				<legend><spring:message code="title.licence" /></legend>
				<div class="fieldCompact">
					<div class="fieldLabel">
						<label><spring:message code="label.licenceType" /></label>
					</div>
					<div class="fieldValue">
						<spring:message code="label.licenceType.${activationCodeBatchBean.licenceType}" />	
					</div>
				</div>
				<c:choose>
					<c:when test="${activationCodeBatchBean.licenceType eq 'ROLLING'}">
					    <div class="fieldCompact">
							<div class="fieldLabel">
								<label><spring:message code="label.timePeriod" /></label>
							</div>
							<div class="fieldValue">
								<c:out value="${activationCodeBatchBean.timePeriod}"/>	
							</div>
						</div>
						<div class="fieldCompact">
							<div class="fieldLabel">
								<label><spring:message code="label.rollingunittype" /></label>
							</div>
							<div class="fieldValue">
								<spring:message code="label.licence.rolling.${activationCodeBatchBean.rollingUnitType}" />
							</div>
						</div>						
						<div class="fieldCompact">
							<div class="fieldLabel">
								<label><spring:message code="label.rollingbeginon" /></label>
							</div>
							<div class="fieldValue">
								<spring:message code="label.licence.rolling.${activationCodeBatchBean.rollingBeginOn}" />				
							</div>
						</div>
					</c:when>
					<c:when test="${activationCodeBatchBean.licenceType eq 'CONCURRENT'}">
					    <div class="fieldCompact">
							<div class="fieldLabel">
								<label><spring:message code="label.totalConcurrency" /></label>
							</div>
							<div class="fieldValue">
								<c:out value="${activationCodeBatchBean.totalConcurrency}"/>	
							</div>
						</div>
						<div class="fieldCompact">
							<div class="fieldLabel">
								<label><spring:message code="label.userConcurrency" /></label>
							</div>
							<div class="fieldValue">
								<c:out value="${activationCodeBatchBean.userConcurrency}"/>	
							</div>
						</div>						
					</c:when>
					<c:when test="${activationCodeBatchBean.licenceType eq 'USAGE'}">
					    <div class="fieldCompact">
							<div class="fieldLabel">
								<label><spring:message code="label.licenceAllowedUsages" /></label>
							</div>
							<div class="fieldValue">
								<c:out value="${activationCodeBatchBean.licenceAllowedUsages}"/>	
							</div>
						</div>
					</c:when>
				</c:choose>
				<div class="fieldCompact" style="float: left">
					<div class="fieldLabel">
						<label><spring:message code="label.validfrom" /></label>
					</div>
					<div class="fieldValue">
						<c:choose>
							<c:when test="${activationCodeBatchBean.licenceStartDate ne null}">
								<joda:format value="${activationCodeBatchBean.licenceStartDate}" style="S-" dateTimeZone="UTC" />
							</c:when>
							<c:otherwise>Now</c:otherwise>
						</c:choose>	
					</div>
				</div>
				<div class="fieldCompact" style="float:left; clear: none; margin-left: 5em;">
					<div class="fieldLabelNarrow" style="width: 5em">
						<label><spring:message code="label.validto" /></label>
					</div>
					<div class="fieldValue">
						<c:choose>
							<c:when test="${activationCodeBatchBean.licenceEndDate ne null}">
								<joda:format value="${activationCodeBatchBean.licenceEndDate}" style="S-" dateTimeZone="UTC" />
							</c:when>
							<c:otherwise>No fixed period</c:otherwise>
						</c:choose>						
					</div>
				</div>				
			</fieldset>
		</fieldset>
		<div id="buttons">
        	<p>      
				<button type="button" id="export" onclick="javascript:window.location.href='<c:url value="/export" />?batchId=${activationCodeBatchBean.activationCodeBatch.batchId}'"><spring:message code="button.exportascsv" /></button>
				<button type="button" id="exportFormatted" onclick="javascript:window.location.href='<c:url value="/exportFormatted" />?batchId=${activationCodeBatchBean.activationCodeBatch.batchId}'"><spring:message code="button.exportascsvformatted" /></button>
			</p>
		</div>
	</div>
</div>

<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:set var="licenceId" value="${registration.id}-${licence.licenseId}"/>
<fieldset id="licencesTile">
	<legend><tiles:getAsString name="licenceLegend" /></legend>	
	<div class="fieldCompact">
		<div class="fieldLabelNarrow">
			<label for="licenceActive-${licenceId}"><spring:message code="label.licenceActive" /></label>
		</div>
		<div class="fieldValue">
			<span id="licenceActive-${licenceId}" class="active">
				<spring:message code="label.${licence.active}" />
			</span>
			<c:if test="${licence.active}">
				<c:url value="/images/tick.png" var="tickIcon"/>
				<img src="${tickIcon}" width="16px" height="16px" style="vertical-align: bottom; vertical-align: middle\9;"/>
			</c:if>
		</div>
	</div>					
	<div class="fieldCompact">
		<div class="fieldLabelNarrow">
			<label for="licenceExpiryDate-${licenceId}"><spring:message code="label.expiryDateAndTime" /></label>
		</div>
		<div class="fieldValue">
			<span id="licenceExpiryDate-${licenceId}"><c:choose><c:when test="${not empty licence.expiryDateAndTime}"><joda:format value="${licence.expiryDateAndTime}" pattern="dd/MM/yyyy HH:mm"  dateTimeZone="UTC"/></c:when><c:otherwise>-</c:otherwise></c:choose></span>
		</div>
	</div>					
	<div class="fieldCompact">
		<div class="fieldLabelNarrow">
			<label for="licenceExpired-${licenceId}"><spring:message code="label.licenceExpired" /></label>
		</div>
		<div class="fieldValue">
			<span id="licenceExpired-${licenceId}"><spring:message code="label.${licence.expired}" /></span>
		</div>
	</div>					
	<div class="fieldCompact">
		<div class="fieldLabelNarrow">
			<label for="licenceType-${licenceId}"><spring:message code="label.licenceType" /></label>
		</div>
		<div class="fieldValue">
			<span id="licenceType-${licenceId}"><spring:message code="label.licenceType.${licence.licenceDetail.licenceType}" /></span>
		</div>
	</div>	
	<div class="fieldCompact">
        <div class="fieldLabelNarrow">
            <label for="activationCode-${licenceId}"><spring:message code="label.activationCode" /></label>
        </div>
        <div class="fieldValue">            
            <span id="activationCode-${licenceId}"><tiles:getAsString name="actCode" ignore="true"/></span>
        </div>
    </div>  			
	<div id="advanced-${licenceId}" style="clear: both; padding-top:10px;">
		<h3><a href="#"><spring:message code="label.advanced" /></a></h3>
		<div>
			<div class="field">
				<div class="fieldLabel">
					<label for="licenceEnabled-${licenceId}"><spring:message code="label.licenceEnabled" /></label>
				</div>
				<div class="fieldValue">
					<form:checkbox id="licenceEnabled-${licenceId}" path="${licencePath}.enabled" cssClass="checkbox" disabled="true"/>
				</div>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label for="licenceStartDate-${licenceId}"><spring:message code="label.licenceStartDate" /></label>
				</div>
				<div class="fieldValue">
					<form:input id="licenceStartDate-${licenceId}" path="${licencePath}.startDate" disabled="true"/>
	              	<script type="text/javascript">
	              		$('#licenceStartDate-${licenceId}').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''});
					</script>							
				</div>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label for="licenceEndDate-${licenceId}"><spring:message code="label.licenceEndDate" /></label>
				</div>
				<div class="fieldValue">
					<form:input id="licenceEndDate-${licenceId}" path="${licencePath}.endDate" disabled="true"/>
	              	<script type="text/javascript">
	              		$('#licenceEndDate-${licenceId}').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''});
					</script>							
				</div>
			</div>
			<c:choose>
				<c:when test="${licence.licenceDetail.licenceType eq 'CONCURRENT'}">
					<div class="field">
						<div class="fieldLabel">
							<label for="licenceTotalConcurrency-${licenceId}"><spring:message code="label.licenceTotalConcurrency" />&#160;<span class="mandatory">*</span></label>
						</div>
						<div class="fieldValue">
							<form:input id="licenceTotalConcurrency-${licenceId}" path="${licencePath}.licenceDetail.totalConcurrency" disabled="true"/>
						</div>
					</div>
				    <div class="field">
						<div class="fieldLabel">
							<label for="licenceUserConcurrency-${licenceId}"><spring:message code="label.licenceUserConcurrency" />&#160;<span class="mandatory">*</span></label>
						</div>
						<div class="fieldValue">
							<form:input id="licenceUserConcurrency-${licenceId}" path="${licencePath}.licenceDetail.userConcurrency" disabled="true"/>
						</div>
					</div>
				</c:when>
				<c:when test="${licence.licenceDetail.licenceType eq 'ROLLING'}">
					<div class="field">
						<div class="fieldLabel">
							<label for="licenceTimePeriod-${licenceId}"><spring:message code="label.licence.rolling.timeperiod" />&#160;<span class="mandatory">*</span></label>
						</div>
						<div class="fieldValue">
							<form:input id="licenceTimePeriod-${licenceId}" path="${licencePath}.licenceDetail.timePeriod" disabled="true"/>
						</div>
					</div>
					<div class="field">
						<div class="fieldLabel">
							<label for="licenceRollingUnitType-${licenceId}"><spring:message code="label.licence.rolling.unittype" /></label>
						</div>
						<div class="fieldValue">
							<form:select id="licenceRollingUnitType-${licenceId}" path="${licencePath}.licenceDetail.unitType" disabled="true" >
								<c:forEach var="unitType" items="${rollingUnitTypes}">
									<spring:message code="label.licence.rolling.${unitType}" var="unitTypeLabel"/>
									<form:option value="${unitType}" label="${unitTypeLabel}"/>
								</c:forEach>
							</form:select>
						</div>
					</div>
					<div class="field">
						<div class="fieldLabel">
							<label for="licenceRollingBeginOn-${licenceId}"><spring:message code="label.licence.rolling.beginon" /></label>
						</div>
						<div class="fieldValue">
							<form:select id="licenceRollingBeginOn-${licenceId}" path="${licencePath}.licenceDetail.beginOn" disabled="true">
								<c:forEach var="beginOn" items="${rollingBeginOns}">
									<spring:message code="label.licence.rolling.${beginOn}" var="beginOnLabel"/>
									<form:option value="${beginOn}" label="${beginOnLabel}"/>
								</c:forEach>
							</form:select>	
						</div>
					</div>
				</c:when>
				<c:when test="${licence.licenceDetail.licenceType eq 'USAGE'}">
					<div class="field">
						<div class="fieldLabel">
							<label for="licenceAllowedUsages-${licenceId}"><spring:message code="label.licence.usage.allowedusages" />&#160;<span class="mandatory">*</span></label>
						</div>
						<div class="fieldValue">
							<form:input id="licenceAllowedUsages-${licenceId}" path="${licencePath}.licenceDetail.allowedUsages" disabled="true"/>
						</div>
					</div>
					<div class="field">
                        <div class="fieldLabel">
                            <label for="licenceRemainingUsages-${licenceId}"><spring:message code="label.licence.usage.remainingUsages" /></label>
                        </div>
                        <div class="fieldValue">
                            <span id="licenceRemainingUsages-${licenceId}">${licence.licenceDetail.usagesRemaining}</span>
                        </div>
                    </div>					
				</c:when>
			</c:choose>			
		</div>
	</div>	
	<script type="text/javascript"> 
	  $(function() {
	    $("#advanced-${licenceId}").accordion({ 
	    	collapsible: true, 
	    	active: false, 
	    	animated: false,
	    	autoHeight: false});
	  });
	</script>
</fieldset>		
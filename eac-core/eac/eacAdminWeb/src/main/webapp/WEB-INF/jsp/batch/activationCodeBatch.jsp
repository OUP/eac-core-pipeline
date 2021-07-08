<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="activationCodeBatchTile">
	<fieldset>
		<legend><spring:message code="title.batch" /></legend>
	   	<div class="field">
			<div class="fieldLabel">
				<label for="activationCodeBatch.batchId"><spring:message code="label.batch" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input path="activationCodeBatch.batchId" maxlength="255"  />
				<input type="hidden" name="currentBatchId" id="currentBatchId" value="${activationCodeBatchBean.activationCodeBatch.batchId}"/>
			</div>
		</div>
		<c:if test="${not activationCodeBatchBean.editMode}">
		<div class="field">
			<div class="fieldLabel">
				<label for="activationCodeFormat"><spring:message code="label.activationCodeFormat" /></label>
			</div>
			<div class="fieldValue">			
				
				<form:select id="activationCodeFormat" path="activationCodeBatch.activationCodeFormat" >
					<c:forEach var="format" items="${activationCodeFormats}">
						<spring:message code="label.activation.${format}" var="formatLabel"/>
						<form:option value="${format}" label="${formatLabel}"/>
					</c:forEach>
				</form:select>
				<script type="text/javascript">
					$('#activationCodeFormat').change(function() {
						$.ajax({
							url: window.location.href,
							type: 'POST',
							data: '_eventId=changeActivationCodeBatchFormat&' + $('#activationCodeBatchBean').serialize()
						}).done(function(html) {
							$('#activationCodeBatchFormatTile').empty().append(html);
						});
					}).live("keypress", function() {
						$('#activationCodeFormat').trigger("change");
					});
				</script>
			</div>
		</div>
		</c:if>
		
		<tiles:insertAttribute name="activationCodeBatchFormatTile"/>
		
		<div class="field">
			<div class="fieldLabel">
				<label for="numberOfTokens"><spring:message code="label.numberoftokens" />
				<c:if test="${not activationCodeBatchBean.editMode}">
				&#160;<span class="mandatory">*</span>
				</c:if>
				</label>
			</div>
			<div class="fieldValue">
				<c:choose>
					<c:when test="${activationCodeBatchBean.editMode}">
						<eac-common:getBatchSize batchId="${activationCodeBatchBean.activationCodeBatch.batchId}" var="batchSize"/>${batchSize}
					</c:when>
					<c:otherwise>
						<form:input path="numberOfTokens" />
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<c:if test="${not activationCodeBatchBean.editMode}">
		<div class="field">
			<div class="fieldLabel">
				<label for="tokenAllowedUsages"><spring:message code="label.tokenAllowedUsages" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input path="tokenAllowedUsages" />
			</div>
		</div>
		</c:if>
		<div class="field">
			<div class="fieldLabel">
				<label for="batchStartDate"><spring:message code="label.batch.startdate" /></label>
			</div>
			<div class="fieldValue">
				<c:choose>

					<c:when test="${activationCodeBatchBean.getAddedInPool() == 'ALL'}">

						<form:input id="batchStartDate"
							path="activationCodeBatch.startDate" disabled="true" />
					</c:when>
					<c:otherwise>
						<form:input id="batchStartDate"
							path="activationCodeBatch.startDate" />
					</c:otherwise>
				</c:choose>
			</div>
			<spring:message code="info.batch.startdate" var="batchStartDateHelpText"/>
			<img id="batchStartDateHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="${batchStartDateHelpText}"/>
            <script type="text/javascript">
				$("#batchStartDateHelp").tipTip({edgeOffset: 7});
				$('#batchStartDate').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
			</script>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="batchEndDate"><spring:message code="label.batch.enddate" /></label>
			</div>
			<div class="fieldValue">
				<c:choose>

					<c:when test="${activationCodeBatchBean.getAddedInPool() == 'ALL'}">

					<form:input id="batchEndDate" path="activationCodeBatch.endDate"
							disabled="true" />
					</c:when>
					<c:otherwise>
						<form:input id="batchEndDate" path="activationCodeBatch.endDate" />
					</c:otherwise>
				</c:choose>
			</div>
			<spring:message code="info.batch.enddate" var="batchEndDateHelpText"/>
			<img id="batchEndDateHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="${batchEndDateHelpText}"/>
            <script type="text/javascript">
				$("#batchEndDateHelp").tipTip({edgeOffset: 7});
				$('#batchEndDate').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
			</script>
		</div>
	</fieldset>
</div>		
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div id="productAndLicenceTile">
	<fieldset>
		<legend><spring:message code="title.product" /></legend>
		
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.product" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue" style="max-width: 60em" id="productDiv">
				<c:set var="acbProdId" value="" />				
				<!-- <c:if test="${activationCodeBatchBean.activationCodeRegistrationDefinition ne null}"> -->
					<c:if test="${activationCodeBatchBean.product ne null}">
						<c:set var="acbProdId" value="${activationCodeBatchBean.product.productId}" />
						
					</c:if>						
				<!-- </c:if>-->
				<tags:productFinder returnFieldId="productId" type="ACTIVATION_CODE_REGISTRATION" initGuid="${acbProdId}" productStates="SUSPENDED, ACTIVE"/> <%-- we can only create batches of tokens for products where the state is ACTIVE or SUSPENDED --%>
				<input type="hidden" name="productId" id="productId" value="${acbProdId}"/>
				<script type="text/javascript">
					$('#productId').change(function() {
						$.ajax({
			    		    url: '<c:url value="/mvc/batch/selectProduct.htm"/>',
			    		    type: 'GET',
			    		    data: 'productId=' + $(this).val()
						}).done(function(html) {
							if($('#productId').val() != ""){
								$('#groupDiv').find('.iconClear').click();
								$('#activationCodeRegistrationDefinitionTile').show();
								$('#eacGroupActivationCodeRegistrationDefinitionTile').hide();
							}
							$('#activationCodeRegistrationDefinitionTile').empty().append(html);
							return false;
						});
					});
				</script>
			</div>
			<c:if test="${activationCodeBatchBean.editMode}">
				<spring:message code="info.edit.batch" var="editBatchHelp"/>
				<img id="editBatchHelp1" class="infoIcon" src="<c:url value="/images/information.png" />" title="${editBatchHelp}"/>
			</c:if>			
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.product.group" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue" style="max-width: 60em" id="groupDiv">
				<c:set var="acbEacGroupId" value="" />
				<c:if test="${activationCodeBatchBean.activationCodeRegistrationDefinition ne null}">
					<c:if test="${activationCodeBatchBean.activationCodeRegistrationDefinition.eacGroup ne null}">
						<c:set var="acbEacGroupId" value="${activationCodeBatchBean.activationCodeRegistrationDefinition.eacGroup.id}" />
					</c:if>					
				</c:if>
				<tags:eacGroupFinder returnFieldId="eacGroupId" initGuid="${acbEacGroupId}"/>
				<input type="hidden" name="eacGroupId" id="eacGroupId" value="${acbEacGroupId}"/>
				<script type="text/javascript">					
					$('#eacGroupId').change(function() {
						$.ajax({
			    		    url: '<c:url value="/mvc/batch/selectEacGroup.htm"/>',
			    		    type: 'GET',
			    		    data: 'eacGroupId=' + $(this).val()
						}).done(function(html) {
							if($('#eacGroupId').val() != ""){
								$('#productDiv').find('.iconClear').click();
								$('#activationCodeRegistrationDefinitionTile').hide();
								$('#eacGroupActivationCodeRegistrationDefinitionTile').show();
							}
							$('#eacGroupActivationCodeRegistrationDefinitionTile').empty().append(html);
							return false;
						});
					});
				</script>
			</div>
			<c:if test="${activationCodeBatchBean.editMode}">
				<spring:message code="info.edit.batch" var="editBatchHelp"/>
				<img id="editBatchHelp1" class="infoIcon" src="<c:url value="/images/information.png" />" title="${editBatchHelp}"/>
			</c:if>			
		</div>
		<tiles:insertAttribute name="activationCodeRegistrationDefinitionTile"/>
		<tiles:insertAttribute name="eacGroupActivationCodeRegistrationDefinitionTile"/>
		<script type="text/javascript">
			$('#activationCodeRegistrationDefinitionTile').hide();
			$('#eacGroupActivationCodeRegistrationDefinitionTile').hide();
		</script>
	</fieldset>
	<fieldset>
		<legend><spring:message code="title.licence" /></legend>
		<c:if test="${activationCodeBatchBean.editMode}">
			<spring:message code="info.edit.batch" var="editBatchHelp"/>
			<img id="editBatchHelp2" class="infoIcon" style="margin-top: 10px\9" src="<c:url value="/images/information.png" />" title="${editBatchHelp}"/>
		</c:if>
		<div class="field">
			<div class="fieldLabel">
				<label for="licenceType"><spring:message code="label.licenceType" /></label>
			</div>
			<div class="fieldValue">
				<c:choose>
					<c:when test="${activationCodeBatchBean.editMode}">
						<spring:message code="label.licenceType.${activationCodeBatchBean.licenceType}" />						
					</c:when>
					<c:otherwise>
						<form:select path="licenceType" >
							<c:forEach items="${licenceTypes}" var="licenceType">
								<spring:message code="label.licenceType.${licenceType}" var="licenceTypeLabel"/>
								<form:option value="${licenceType}" label="${licenceTypeLabel}"/>
							</c:forEach>
						</form:select>
					</c:otherwise>
				</c:choose>
			</div>
			
			<spring:message code="info.licence.concurrent" var="concurrentLicenceHelp"/>
				<spring:message code="info.licence.usage" var="usageLicenceHelp"/>
				<spring:message code="info.licence.rolling" var="rollingLicenceHelp"/>
				<c:set var="title" value="${concurrentLicenceHelp}<br/><br/>${usageLicenceHelp}<br/><br/>${rollingLicenceHelp}"/>
				
				<c:choose>
				<c:when test="${activationCodeBatchBean.editMode}">
					<c:choose>
						<c:when test="${activationCodeBatchBean.licenceType eq 'ROLLING' }"><c:set var="title" value="${rollingLicenceHelp}" /></c:when>
						<c:when test="${activationCodeBatchBean.licenceType eq 'USAGE' }"><c:set var="title" value="${usageLicenceHelp}" /></c:when>
						<c:when test="${activationCodeBatchBean.licenceType eq 'CONCURRENT' }"><c:set var="title" value="${concurrentLicenceHelp}" /></c:when>
					</c:choose>
				</c:when>
				<c:otherwise>	
					<c:set var="title" value="${concurrentLicenceHelp}<br/><br/>${usageLicenceHelp}<br/><br/>${rollingLicenceHelp}" />
				</c:otherwise>
				</c:choose>
			<img id="licenceTypeHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="${title}"/>
			<script type="text/javascript">
				$("#licenceTypeHelp").tipTip({edgeOffset: 7, maxWidth: '400px'});
			</script>
		</div>	
		<% request.setAttribute("licenceTemplateWrapped", true); %>
		<tiles:insertAttribute name="licenceTemplateTile" />	    
	</fieldset>	
	<script type="text/javascript">
		$(function() {
			$("#editBatchHelp1").tipTip({edgeOffset: 7});
			$("#editBatchHelp2").tipTip({edgeOffset: 7});
		});
		
		
		
		
		
	</script>
</div>

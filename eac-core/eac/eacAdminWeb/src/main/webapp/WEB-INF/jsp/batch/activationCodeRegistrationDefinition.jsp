<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="activationCodeRegistrationDefinitionTile" style="clear:both">
	
	<fieldset>
		<legend><spring:message code="title.product.summary" /></legend>
		<div>
			<div class="field" style="float:left; min-height: 3em">
				<div class="fieldLabel">
					<label for="productName"><spring:message code="label.product" /></label>
				</div>
				<div class="fieldValue" style="max-width: 53em;">
					<span id="productName"><c:out value="${activationCodeBatchBean.product.name}"/></span>	
				</div>
			</div>
		</div>
		<div>
			 <div class="fieldCompact">
				<div class="fieldLabel">
					<label for="divisionType"><spring:message code="label.division" /></label>
				</div>
				<div class="fieldValue">
					<span id="divisionType"><c:out value="${activationCodeBatchBean.product.division.divisionType}"/></span>	
				</div>
			</div> 
			<div class="fieldCompact">
				<div class="fieldLabel">
					<label for="activation"><spring:message code="label.activation" /></label>
				</div>
				<div class="fieldValue">
					<span id="activation"><spring:message code="label.registration.activation.${activationCodeBatchBean.product.activationStrategy}" text=""/></span>	
				</div>
			</div>
	        <div class="fieldCompact">
	            <div class="fieldLabel">
	                <label for="extId"><spring:message code="label.extId" /></label>
	            </div>
	            <div class="fieldValue" style="max-width: 100%">
	                <span id="extId">
	                	<c:choose>
	                		<c:when test="${not empty activationCodeBatchBean.externalIdsForDisplay}">
			                	<c:forEach items="${activationCodeBatchBean.externalIdsForDisplay}" var="extId" varStatus="status">
			                		<c:out value="${extId}"/>
			                		<c:if test="${not status.last}">
			                			<br/>
			                		</c:if>
			                	</c:forEach>
	                		</c:when>
	                		<c:when test="${not empty activationCodeBatchBean.activationCodeRegistrationDefinition}">-</c:when>
	                	</c:choose>
	                </span>       
	            </div>
	        </div>
        </div>
	</fieldset>
</div>
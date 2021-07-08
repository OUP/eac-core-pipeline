<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="activationCodeBatchFormatTile">
	<c:if test="${activationCodeBatchBean.activationCodeBatch.activationCodeFormat.prefixed}">	    
		<div>
			<div class="span-3">
				<label for="activationCodeBatchBean.activationCodeBatch.codePrefix"><spring:message code="label.prefix" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="span-7">
				<p>
					<spring:bind path="activationCodeBatchBean.activationCodeBatch.codePrefix">
				   		<input type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/>
					</spring:bind>
				</p>
			</div>
		</div>
	</c:if>	
</div>
<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<form:form modelAttribute="addRegistrationBean">
<form:hidden path="isAdmin"  id="isAdmin" />
<fieldset>
	<legend><spring:message code="title.registration" /></legend>
	<fieldset>
		<legend><spring:message code="title.summary" /></legend>
		<div class="fieldCompact">
			<div class="fieldLabel">
				<label for="activation"><spring:message code="label.registration.activation" /></label>
			</div>
			<div class="fieldValue">
				<span id="activation"><spring:message code="label.registration.activation.${addRegistrationBean.product.activationStrategy}" text=""/></span>
			</div>
		</div>
		<div class="fieldCompact">
			<div class="fieldLabel">
				<label for="page"><spring:message code="label.page" /></label>
			</div>
			<div class="fieldValue">
				<span id="page">
					<c:choose>
						<c:when test="${not empty addRegistrationBean.productRegistrationDefinition.pageDefinition.name}">
							<c:out value="${addRegistrationBean.productRegistrationDefinition.pageDefinition.name}"/>
						</c:when>
						<c:otherwise>-</c:otherwise>
					</c:choose>
				</span>
			</div>
		</div>
		<div class="fieldCompact">
			<div class="fieldLabel">
				<label for="linkedProducts"><spring:message code="label.linkedProducts" /></label>
			</div>
			<div class="fieldValue" style="width: 22em">
				<span id="linkedProducts">
					<c:choose>
						<c:when test="${not empty addRegistrationBean.product.linkedProducts}">
							<c:forEach var="linkedProduct" items="${addRegistrationBean.product.linkedProducts}" varStatus="status">
								<c:out value="${linkedProduct.name}"/><c:if test="${not status.last}">,<br/></c:if>
							</c:forEach>
						</c:when>
						<c:otherwise>-</c:otherwise>
					</c:choose>
				</span>
			</div>
		</div>
	</fieldset>
		<c:set var="registrationType" value="${addRegistrationBean.product.activationStrategy}"/>
		<c:choose>
			<c:when test="${registrationType == 'SelfRegistrationActivation'}">
				<fieldset>
					<legend><spring:message code="title.registration.state" /></legend>
					<div class="fieldCompact" style="margin-bottom: 5px">
						<div class="fieldLabel">
							<label for="activate"><spring:message code="label.registration.activate" /></label>
						</div>
						<div class="fieldValue">
							<form:checkbox id="activate" path="activate" cssClass="checkbox"/>
							<!-- EAC 300 -->
							<script type="text/javascript">
							 $("#activate").click(function() {
								if ($(this).attr('checked') == 'checked'){
									$("#sendEmail").attr('checked',true);
									 $("#sendEmail").attr('disabled',false);
								}else{
									$("#sendEmail").attr('checked',false);
									 $("#sendEmail").attr('disabled',true);
								}
							 });
							
							</script>
							<!-- EAC 300 -->
						</div>
						<spring:message code="info.registration.activate" var="activateHelp"/>
						<img id="activateHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="${activateHelp}"/>
						<script type="text/javascript">
							$(function() {
								$("#activateHelp").tipTip({edgeOffset: 7});
							});
						</script>
					</div>
					<div id="fieldCompact">
						<div class="fieldLabel">
							<label for="sendEmail"><spring:message code="label.registration.sendEmail" /></label>
						</div>
						<div class="fieldValue">
							<form:checkbox id="sendEmail" path="sendEmail" cssClass="checkbox"/>
						</div>
					</div>	
				</fieldset>
			</c:when>
			<c:when test="${registrationType == 'ValidatedRegistrationActivation'}">
				<fieldset>
					<legend><spring:message code="title.registration.state" /></legend>
					<div class="fieldCompact" style="margin-bottom: 5px">
						<div class="fieldLabel">
							<label for="validate"><spring:message code="label.registration.validate" /></label>
						</div>
						<div class="fieldValue">
							<form:checkbox id="validate" path="validate" cssClass="checkbox"/>
							<!-- EAC 300 -->
							<script type="text/javascript">
							 $("#validate").click(function() {
								if ($(this).attr('checked') == 'checked'){
									$("#sendEmail").attr('checked',true);
									 $("#sendEmail").attr('disabled',false);
								}else{
									$("#sendEmail").attr('checked',false);
									 $("#sendEmail").attr('disabled',true);
								}
							 });
							
							</script>
							<!-- EAC 300 -->
						</div>
						<spring:message code="info.registration.validated" var="validateHelp"/>
						<img id="validateHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="${validateHelp}"/>
						<script type="text/javascript">
							$(function() {
								$("#validateHelp").tipTip({edgeOffset: 7});
							});
						</script>
					</div>
					<div id="fieldCompact">
						<div class="fieldLabel">
							<label for="sendEmail"><spring:message code="label.registration.sendEmail" /></label>
						</div>
						<div class="fieldValue">
							<form:checkbox id="sendEmail" path="sendEmail" cssClass="checkbox"/>
						</div>
					</div>	
				</fieldset>
			</c:when>
		</c:choose>
	<fieldset>
		<legend><spring:message code="title.licence" /></legend>
		<div class="fieldCompact">
			<div class="fieldLabel">
				<label for="startDate"><spring:message code="label.licenceType" /></label>
			</div>
			<div class="fieldValue">
				<span id="startDate"><c:choose><c:when test="${not empty addRegistrationBean.product.licenceDetail.licenceDetail.licenceType}"><spring:message code="label.licenceType.${addRegistrationBean.product.licenceDetail.licenceDetail.licenceType}" /></c:when><c:otherwise>-</c:otherwise></c:choose></span>
			</div>
		</div>
		<div class="fieldCompact">
			<div class="fieldLabel">
				<label for="startDate"><spring:message code="label.licenceStartDate" /></label>
			</div>
			<div class="fieldValue">
				<span id="startDate"><c:choose><c:when test="${not empty addRegistrationBean.product.licenceDetail.licenceDetail.startDate}"><joda:format value="${addRegistrationBean.product.licenceDetail.licenceDetail.startDate}" pattern="dd/MM/yyyy" dateTimeZone="UTC"/></c:when><c:otherwise>-</c:otherwise></c:choose></span>
			</div>
		</div>
		<div class="fieldCompact">
			<div class="fieldLabel">
				<label for="endDate"><spring:message code="label.licenceEndDate" /></label>
			</div>
			<div class="fieldValue">
				<span id="endDate"><c:choose><c:when test="${not empty addRegistrationBean.product.licenceDetail.licenceDetail.endDate}"><joda:format value="${addRegistrationBean.product.licenceDetail.licenceDetail.endDate}" pattern="dd/MM/yyyy" dateTimeZone="UTC"/></c:when><c:otherwise>-</c:otherwise></c:choose></span>
			</div>
		</div>
		<c:choose>
			<c:when test="${addRegistrationBean.product.licenceDetail.licenceDetail.licenceType == 'CONCURRENT'}">
				<div class="fieldCompact">
					<div class="fieldLabel">
						<label for="totalConcurrency"><spring:message code="label.licenceTotalConcurrency" /></label>
					</div>
					<div class="fieldValue">
						<span id="totalConcurrency"><c:choose><c:when test="${not empty addRegistrationBean.product.licenceDetail.licenceDetail.totalConcurrency}"><c:out value="${addRegistrationBean.product.licenceDetail.licenceDetail.totalConcurrency}"/></c:when><c:otherwise>-</c:otherwise></c:choose></span>
					</div>
				</div>
				<div class="fieldCompact">
					<div class="fieldLabel">
						<label for="userConcurrency"><spring:message code="label.licenceUserConcurrency" /></label>
					</div>
					<div class="fieldValue">
						<span id="userConcurrency"><c:choose><c:when test="${not empty addRegistrationBean.product.licenceDetail.licenceDetail.userConcurrency}"><c:out value="${addRegistrationBean.product.licenceDetail.licenceDetail.userConcurrency}"/></c:when><c:otherwise>-</c:otherwise></c:choose></span>
					</div>
				</div>
			</c:when>
			<c:when test="${addRegistrationBean.product.licenceDetail.licenceDetail.licenceType == 'ROLLING'}">
				<div class="fieldCompact">
					<div class="fieldLabel">
						<label for="timePeriod"><spring:message code="label.licence.rolling.timeperiod" /></label>
					</div>
					<div class="fieldValue">
						<span id="timePeriod"><c:choose><c:when test="${not empty addRegistrationBean.product.licenceDetail.licenceDetail.timePeriod}"><c:out value="${addRegistrationBean.product.licenceDetail.licenceDetail.timePeriod}"/></c:when><c:otherwise>-</c:otherwise></c:choose></span>
					</div>
				</div>
				<div class="fieldCompact">
					<div class="fieldLabel">
						<label for="unitType"><spring:message code="label.licence.rolling.unittype" /></label>
					</div>
					<div class="fieldValue">
						<span id="unitType"><c:choose><c:when test="${not empty addRegistrationBean.product.licenceDetail.licenceDetail.unitType}"><spring:message code="label.licence.rolling.${addRegistrationBean.product.licenceDetail.licenceDetail.unitType}" /></c:when><c:otherwise>-</c:otherwise></c:choose></span>
					</div>
				</div>
				<div class="fieldCompact">
					<div class="fieldLabel">
						<label for="beginOn"><spring:message code="label.licence.rolling.beginon" /></label>
					</div>
					<div class="fieldValue">
						<span id="beginOn"><c:choose><c:when test="${not empty addRegistrationBean.product.licenceDetail.licenceDetail.beginOn}"><spring:message code="label.licence.rolling.${addRegistrationBean.product.licenceDetail.licenceDetail.beginOn}" /></c:when><c:otherwise>-</c:otherwise></c:choose></span>
					</div>
				</div>
			</c:when>
			<c:when test="${addRegistrationBean.product.licenceDetail.licenceDetail.licenceType == 'USAGE'}">
				<div class="fieldCompact">
					<div class="fieldLabel">
						<label for="allowedUsages"><spring:message code="label.licence.usage.allowedusages" /></label>
					</div>
					<div class="fieldValue">
						<span id="allowedUsages"><c:choose><c:when test="${not empty addRegistrationBean.product.licenceDetail.licenceDetail.allowedUsages}"><c:out value="${addRegistrationBean.product.licenceDetail.licenceDetail.allowedUsages}"/></c:when><c:otherwise>-</c:otherwise></c:choose></span>
					</div>
				</div>
			</c:when>
		</c:choose>
	</fieldset>
</fieldset>
<input type="hidden" name="product_id" value="${addRegistrationBean.product.productId}"/>
</form:form>
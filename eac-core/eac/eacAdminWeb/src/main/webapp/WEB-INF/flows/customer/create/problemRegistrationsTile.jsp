<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="problemRegistrationsTile" style="min-height: 7em;">
	<c:if test="${empty customerBean.problemRegistrations && empty customerBean.problemtEntitlementGroups}">
		<div class="error">
			<spring:message code="label.registrations.noProblemRegistrations" text="?label.registrations.noProblemRegistrations?"/>
		</div>
	</c:if>
	<c:forEach var="entitlementGroup" items="${customerBean.problemtEntitlementGroups}" varStatus="egStatus">
		<c:set var="registration" value="${entitlementGroup.entitlement.registration}" scope="request"/>
		<c:set var="licence" value="${entitlementGroup.entitlement.licence}" scope="request"/>
		<c:set var="licencePath" value="problemtEntitlementGroups[${egStatus.index}].entitlement.licence" scope="request"/>
		
		<%-- this code assumes there is only 1 product associated with a licence. Can be more than 1 product per licence in Atypon. --%>
		<c:set var="emptyProducts" value="${empty entitlementGroup.entitlement.productList}" />
		<c:choose>
			<c:when test="${emptyProducts}">
				<c:set var="productId">${entitlementGroup.entitlement.licence.productIds[0]}</c:set><%--productId will now be a string --%>
				<spring:message var="productName" code="label.product.in.atypon.but.not.eac" text="?label.product.in.atypon.but.not.eac?" arguments="${productId}" />
			</c:when>
			<c:otherwise>
				<c:set var="productName" value="${entitlementGroup.entitlement.productList[0].product.productName}"/>
			</c:otherwise>
		</c:choose>	
			<fieldset><%-- START PRODUCT --%>
				<legend><span style="max-width: 50em; display: block" class="shorten"><c:out value="${productName}"/></span></legend>
				<c:choose>
					<c:when test="${empty registration}">
						<div class="error">
						<c:set var="erightsLicenceId">${entitlementGroup.entitlement.licence.erightsId}</c:set><%-- erightsLicenceId id will now be string --%>
						<spring:message code="label.extra.licence.in.atypon" text="?label.extra.licence.in.atypon?" arguments="${erightsLicenceId}" />
						</div>
						<div id="removeLicence" style="float: right;">
                        			<a class="actionLink removeLicenceLink" eacLabel="${erightsLicenceId}" href="${flowExecutionUrl}&amp;_eventId=removeLicence&amp;eRightsLicenceID=${erightsLicenceId}"><button type="button" id="remove"><spring:message code="button.remove.licence"/></button></a>
                        		</div>
					</c:when>
					<c:otherwise>
					<c:if test="${entitlementGroup.orphan}">
						<div class="error">
						<spring:message code="label.parent.registration.problem" text="?label.parent.registration.problem?" arguments="${registration.id}" />
						</div>
					</c:if>
					<tiles:insertAttribute name="registrationDetailsTile" />
					</c:otherwise>
				</c:choose>				
				
				
				<c:choose> 
				    <c:when test="${not empty entitlementGroup.entitlement.activationCode}">
    				    <c:set var="ac">${entitlementGroup.entitlement.activationCode}</c:set>
	       			</c:when>
                    <c:otherwise>
				        <c:set var="ac">-</c:set>
				    </c:otherwise>
				</c:choose>
				
				<spring:message code="title.licence" var="licenceLegend"/>											
				<tiles:insertAttribute name="licencesTile">
					<tiles:putAttribute name="licenceLegend" value="${licenceLegend}"/>
					<tiles:putAttribute name="actCode" value="${ac}"/>  
				</tiles:insertAttribute>				
				<c:forEach var="linkedEntitlement" items="${entitlementGroup.linkedEntitlements}" varStatus="leStatus">
					<c:forEach var="linkedProductDetails" items="${linkedEntitlement.productList}" varStatus="lpdStatus">
						<c:set var="licence" value="${linkedEntitlement.licence}" scope="request"/>
						<c:set var="licencePath" value="problemtEntitlementGroups[${egStatus.index}].linkedEntitlements[${leStatus.index}].licence" scope="request"/>
						<c:choose> 
		                    <c:when test="${not empty linkedEntitlement.activationCode}">
		                        <c:set var="ac">${linkedEntitlement.activationCode}</c:set>
		                    </c:when>
		                    <c:otherwise>
		                        <c:set var="ac">-</c:set>
		                    </c:otherwise>
		                </c:choose>						
						<tiles:insertAttribute name="licencesTile">
							<tiles:putAttribute name="licenceLegend" value="${linked}&#160;-&#160;${linkedProductDetails.product.productName}"/>
							<tiles:putAttribute name="actCode" value="${ac}"/>
						</tiles:insertAttribute>						
					</c:forEach>
				</c:forEach>
				<c:forEach var="linkedRegistration" items="${entitlementGroup.entitlement.registration.linkedRegistrations}" varStatus="lrStatus" >						
						<c:if test="${not empty linkedRegistration.erightsLicenceId}">							
							<c:if test="${not fn:contains(customerBean.matchedErightsLicenceIds, linkedRegistration.erightsLicenceId) }">
								<fieldset>
									<legend>${linked}&#160;-&#160;<c:out value="${linkedRegistration.linkedProduct.productName}"/></legend>
									<div class="error">
									<c:set var="problemLinkedErightsId">${linkedRegistration.erightsLicenceId}</c:set><%-- problemLinkedErightsId is now a string --%>
									<spring:message code="label.linked.licence.not.in.atypon" text="?label.linked.licence.not.in.atypon?" arguments="${problemLinkedErightsId}" />
									</div>
								</fieldset>
							</c:if>
						</c:if>
				</c:forEach>
			</fieldset><%--END PRODUCT--%>
			<script type="text/javascript">
				$(function() {
					$('#regSendEmail-${registration.id}-div').hide();
				});
			</script>		
		</c:forEach>
	<c:if test="${not empty customerBean.problemRegistrations}">
		<div class="error">
			<spring:message code="label.problem.registrations.error" text="?label.problem.registrations.error?" />
		</div>
	</c:if>
	
	<c:set var="matchedErightsLicenceIds" value="${customerBean.matchedErightsLicenceIds}"/>
	<c:forEach var="registration" items="${customerBean.problemRegistrations}">
		<fieldset>
			<legend><span style="max-width: 50em; display: block" class="shorten"><c:out value="${registration.registrationDefinition.product.productName}"/></span></legend>
			<div class="fieldCompact">
				<div class="fieldLabelNarrow">
					<label for="regId-${registration.id}"><spring:message code="label.registration.id" text="?label.registration.id?" /></label>
				</div>
				<div class="fieldValue">
					<span id="regId-${registration.id}">${registration.id}</span>
				</div>
			</div>
			<c:set var="missingErightsId" value="${not fn:contains(matchedErightsLicenceIds, registration.erightsLicenceId)}" />
			<c:if test="${missingErightsId}">
				<div class="error">
					<c:set var="erightsLicenceId1">${registration.erightsLicenceId}</c:set><%--erightsLicenceId will now be a string --%>
					<spring:message code="label.atypon.is.missing.licence" text="?label.atypon.is.missing.licence?" arguments="${erightsLicenceId1}" />
				</div>
			</c:if>
			<div class="fieldCompact">
				<div class="fieldLabelNarrow">
					<label for="regType-${registration.id}"><spring:message code="label.registration.type" text="?label.registration.type?" /></label>
				</div>
				<div class="fieldValue" style="margin-bottom:5px">
					<c:set var="registrationType" value="${registration.registrationType}"/>
					<span id="regType-${registration.id}"><spring:message code="label.registration.type.${registrationType}" text="?label.registration.type.${registrationType}?"/></span>
				</div>
			</div>
			<div class="fieldCompact">
				<div class="fieldLabelNarrow">
					<label for="regCreatedDate-${registration.id}"><spring:message code="label.registration.created" text="?label.registration.created?"/></label>
				</div>
				<div class="fieldValue">
					<span id="regCreatedDate-${registration.id}"><joda:format value="${registration.createdDate}" pattern="dd/MM/yyyy" dateTimeZone="UTC" /></span>
				</div>
			</div>
			<div class="fieldCompact">
				<div class="fieldLabelNarrow">
					<label for="regUpdatedDate-${registration.id}"><spring:message code="label.registration.updated" text="?label.registration.updated?"/></label>
				</div>
				<div class="fieldValue">
					<span id="regUpdatedDate-${registration.id}"><joda:format value="${registration.updatedDate}" pattern="dd/MM/yyyy" dateTimeZone="UTC" /></span>
				</div>
			</div>
			<fieldset>
				<legend id="legend-${registration.id}"><spring:message code="title.registration.stateFinalised" text="?title.registration.stateFinalised?"/></legend>
				<div class="fieldCompact">
					<div class="fieldLabelNarrow">
						<label for="regCompleted-${registration.id}"><spring:message code="label.registration.completed" text="?label.registration.completed?" /></label>
					</div>
					<div class="fieldValue">
						<span id="regCompleted-${registration.id}"><spring:message code="label.${registration.completed}" text="?label.${registration.completed}?"/></span>
					</div>
				</div>
			</fieldset>
			<spring:message code="label.linked" text="?label.linked?" var="linked" />
			<c:forEach var="linkedRegistration" items="${registration.linkedRegistrations}">
				<fieldset>
					<legend id="legend-${registration.id}">${linked}&#160;-&#160;${linkedRegistration.linkedProduct.productName}</legend>
					<c:set var="LinkedRegId1" value="linkedRegId1-${linkedRegistration.id}"/>
					<c:set var="LinkedRegId2" value="linkedRegId2-${linkedRegistration.id}"/>					
					<c:set var="missingErightsId" value="${not fn:contains(matchedErightsLicenceIds, linkedRegistration.erightsLicenceId)}" />
					<c:if test="${missingErightsId}">
						<div class="error">
							<c:set var="erightsLicenceId2">${linkedRegistration.erightsLicenceId}</c:set><%--erightsLicenceId will now be a string --%>
							<spring:message code="label.atypon.is.missing.licence" text="?label.atypon.is.missing.licence?" arguments="${erightsLicenceId2}" />
						</div>
					</c:if>
					<div class="fieldCompact">
						<div class="fieldLabel">
							<label for="${linkedRegId1}"><spring:message code="label.linked.registration.id" text="?label.linked.registration.id?" /></label>
						</div>
						<div class="fieldValue">
							<span id="${linkedRegId1}">${linkedRegistration.id}</span>
						</div>
					</div>
					<div class="fieldCompact">
						<div class="fieldLabel">
							<label for="${linkedRegId2}"><spring:message code="label.activation.method" text="?label.activation.method?" /></label>
						</div>
						<div class="fieldValue">
							<span id="${linkedRegId2}">${linkedRegistration.linkedProduct.activationMethod}</span>
						</div>
					</div>
				</fieldset>
			</c:forEach>
		</fieldset>
	</c:forEach> 
</div>
<spring:message code="confirm.title.remove.licence" text="" var="message" />
	<script type="text/javascript">
	$(document).ready(function() {
		$('#problemRegistrationsTile').undelegate('a.removeLicenceLink', 'click');<%--was getting 2 button clicks--%>
		$('#problemRegistrationsTile').delegate('a.removeLicenceLink', 'click', eacConfirm({title:'${message}', width: 700}));
	});
</script>

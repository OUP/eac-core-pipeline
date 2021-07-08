<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<spring:message code="label.edit" var="edit"/>
<div id="productDefinitionTile">
	<fieldset>
		<c:if test="${productBean.editMode}">
			<div class="field">
				<div class="fieldLabel">
					<label for="productId"><spring:message code="label.id" /><c:if test="${not productBean.editMode and productBean.systemAdmin}">&#160;<span class="mandatory">*</span></c:if></label>
				</div>
				<div class="fieldValue">
					<c:choose>
						<c:when test="${productBean.editMode or not productBean.systemAdmin}">
							<c:out value="${productBean.productId}"/>
							<form:hidden id="productId" path="productId" />
						</c:when>
						<c:otherwise>
							<form:input id="productId" path="productId" />
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</c:if>	
		<div class="field">
			<div class="fieldLabel">
				<label for="name"><spring:message code="label.name" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input id="productName" path="productName" cssClass="wide"/>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="division"><spring:message code="label.division" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:select path="divisionId" >
					<c:if test="${not productBean.editMode}">
						<spring:message code="label.pleaseSelect" var="pleaseSelect"/>
						<form:option value="" label="${pleaseSelect}"/>
					</c:if>
					<c:forEach var="division" items="${productBean.divisions}">
						<form:option value="${division.erightsId}" label="${division.divisionType}"/>
					</c:forEach>
				</form:select>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="productState"><spring:message code="label.productState" /></label>
			</div>
			<div class="fieldValue">
				<spring:message code="info.productHelp" var="productStateHelp" />
				<form:select path="state" >
					<c:forEach var="state" items="${productBean.states}">
						<form:option value="${state}" label="${state}"/>
					</c:forEach>
				</form:select>
			</div>
			<img id="productStateHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${productStateHelp}" />
			<script type="text/javascript">
				$("#productStateHelp").tipTip({edgeOffset: 7});
			</script>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="registrationType"><spring:message code="label.registrationType" /></label>
			</div>
			<div class="fieldValue">
				<form:select path="registrationType" >
					<c:forEach var="registerableType" items="${productBean.registerableTypes}">
						<spring:message code="label.product.registerableType.${registerableType}" var="registerableTypeLabel"/>
						<form:option value="${registerableType}" label="${registerableTypeLabel}"/>
					</c:forEach>
				</form:select>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="homePage"><spring:message code="label.homePage" /></label>
			</div>
			<div class="fieldValue" style="max-width: 50em">
				<form:input id="homePage" path="homePage" class="wide"/>
			</div>
			<spring:message code="info.homePage" var="homePageHelp" />
			<img id="homePageHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${homePageHelp}"/>
			<script type="text/javascript">
				$("#homePageHelp").tipTip({edgeOffset: 7, maxWidth: '400px'});
			</script>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="landingPage"><spring:message code="label.landingPage" /></label>
			</div>
			<div class="fieldValue" style="max-width: 50em">
				<form:input id="landingPage" path="landingPage" class="wide"/>
			</div>
			<spring:message code="info.landingPage" var="landingPageHelp" />
			<img id="landingPageHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${landingPageHelp}"/>
			<script type="text/javascript">
				$("#landingPageHelp").tipTip({edgeOffset: 7});
			</script>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="email"><spring:message code="label.emailAddress" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue" style="max-width: 30em">
				<form:input id="email" path="email"/>
			</div>
			<spring:message code="info.product.emailAddress" var="emailHelp" />
			<img id="emailHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${emailHelp}"/>
			<script type="text/javascript">
				$("#emailHelp").tipTip({edgeOffset: 7});
			</script>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="sla"><spring:message code="label.sla" /></label>
			</div>
			<div class="fieldValue" style="max-width: 30em">
				<form:input id="sla" path="sla"/>
			</div>
			<spring:message code="info.sla" var="slaHelp" />
			<img id="slaHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${slaHelp}"/>
			<script type="text/javascript">
				$("#slaHelp").tipTip({edgeOffset: 7});
			</script>
		</div>
	</fieldset>
</div>
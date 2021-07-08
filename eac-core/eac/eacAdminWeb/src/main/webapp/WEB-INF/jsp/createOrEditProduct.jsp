<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<div id="createOrEditProductTabsTile">
	<div id="heading" class="ui-corner-top">
		<c:choose>
			<c:when test="${productBean.editMode}">
				<spring:message code="title.editProduct" var="title" />
			</c:when>
			<c:otherwise>
				<spring:message code="title.createProduct" var="title" />
			</c:otherwise>
		</c:choose>
		<h1>${title}</h1>
	</div>

	<spring:hasBindErrors name="productBean">
		<div class="error">
			<spring:bind path="productBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}" /></span>
					<br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>

	<form:form modelAttribute="productBean" method="POST">
		<div id="pagetabs">
			<ul>
				<li class="ui-state-hover"><a href="#pagetab-1"><spring:message
							code="tab.definition" /></a></li>
				<li><a href="#pagetab-2"><spring:message
							code="tab.registration" /></a></li>
				<li><a href="#pagetab-3"><spring:message
							code="tab.associations" /></a></li>
				<li><a href="#pagetab-4"><spring:message
							code="tab.externalIds" /></a></li>
				<li><a href="#pagetab-5"><spring:message code="tab.urls" /></a></li>
				<li><a href="#pagetab-6"><spring:message code="tab.platformUse" /></a></li>
				
			</ul>
			<div id="pagetab-1" class="ui-tabs-hide">
				<tiles:insertAttribute name="productDefinitionTile" />
			</div>
			<div id="pagetab-2" class="ui-tabs-hide">
				<tiles:insertAttribute name="productRegistrationTile" />
			</div>
			<div id="pagetab-3" class="ui-tabs-hide">
				<tiles:insertAttribute name="productAssociationsTile" />
			</div>
			<div id="pagetab-4" class="ui-tabs-hide">
				<tiles:insertAttribute name="productExternalIdsTile" />
			</div>
			<div id="pagetab-5" class="ui-tabs-hide">
				<tiles:insertAttribute name="productUrlsTile" />
			</div>
			<div id="pagetab-6" class="ui-tabs-hide">
				<tiles:insertAttribute name="productUsedByPlatformTile" />
			</div>
		</div>
		<div id="buttons">
			<p>
				<c:choose>
					<c:when test="${productBean.editMode}">
						<button type="submit" id="edit">
							<spring:message code="button.edit" />
						</button>
						<button type="button" id="cancel">
							<spring:message code="button.cancel" />
						</button>
					</c:when>
					<c:otherwise>
						<button type="submit" id="save">
							<spring:message code="button.save" />
						</button>
					</c:otherwise>
				</c:choose>
			</p>
		</div>
	</form:form>

	<script type="text/javascript">
		$(function() {
			$('#pagetabs').tabs({ cookie: { name: 'createOrEditCustomer', expires: 1 }});
			$('#cancel').click(function() {
				window.location.replace('<c:url value="/mvc/product/search.htm"/>');
			});
		});
	</script>
</div>
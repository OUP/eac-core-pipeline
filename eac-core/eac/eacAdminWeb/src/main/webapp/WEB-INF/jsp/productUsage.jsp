<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="productUrlsTile">
	<div id="productUsage">
		<fieldset>
			 <legend><spring:message code="label.asset.usage.products.registered" /></legend> 
			<div id="messagesPanel" style="max-height: 250px; overflow-x: hidden; overflow-y: auto; margin-bottom: 15px">
				<table class="summary" style="margin-bottom: 0px">
					<thead>
						<tr>
							<th><spring:message code="label.product.id" /></th>
							<th><spring:message code="label.registration.definition.type" /></th>
							<th><spring:message code="label.registerable.type" /></th>						
							<th><spring:message code="label.activation" /></th>
						</tr>
					</thead>
					<tbody>
						<c:set var="productCount" value="${0}" />
						<c:forEach var="pd" items="${productBean.productData}" >
							<c:set var="productCount" value="${productCount+1}" />
							<tr>
								<td>${pd.productId}</td>
								<td><spring:message code="label.registration.definition.type.${pd.regDefType}" /></td>
								<td><spring:message code="label.product.registerableType.${pd.registerableType}" /></td>
								<td><spring:message code="label.registration.activation.${pd.registrationActivation.name}" /></td>
							</tr>
						</c:forEach>
						<c:if test="${productCount eq 0}">
							<tr><td id="noMessages" colspan="4" style="text-align:center"><spring:message code="label.no.direct.products" /></td></tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</fieldset>
		<fieldset>
			 <legend><spring:message code="label.asset.usage.products.linked.indirect" /></legend> 
			<div id="messagesPanel" style="max-height: 250px; overflow-x: hidden; overflow-y: auto; margin-bottom: 15px">
				<table class="summary" style="margin-bottom: 0px">
					<thead>
						<tr>
							<th><spring:message code="label.product.id.registered" /></th>
							<th><spring:message code="label.product.id.linked" /></th>						
							<th><spring:message code="label.product.name" /></th>
						</tr>
					</thead>
					<tbody>
						<c:set var="linkedCount" value="${0}" />
						<c:forEach var="linkedProduct" items="${productBean.indirectLinkedProducts}" >
							<c:set var="linkedCount" value="${linkedCount + 1}" />
							<tr>
								<td>${linkedProduct.linkedProduct.productId}</td>
								<td>${linkedProduct.linkedProduct.productId}</td>
								<td>${linkedProduct.linkedProduct.name}</td>									
							</tr>
						</c:forEach>
						<c:if test="${linkedCount eq 0}">
							<tr><td id="noMessages" colspan="4" style="text-align:center"><spring:message code="label.no.linked.products" /></td></tr>
						</c:if>
				</table>
			</div>
		</fieldset>
		<fieldset>
			 <legend><spring:message code="label.asset.usage.products.linked.direct" /></legend> 
			<div id="messagesPanel" style="max-height: 250px; overflow-x: hidden; overflow-y: auto; margin-bottom: 15px">
				<table class="summary" style="margin-bottom: 0px">
					<thead>
						<tr>
							<th><spring:message code="label.product.id.linked" /></th>
							<th><spring:message code="label.product.id.registered"   /></th>
							<th><spring:message code="label.product.name" /></th>
						</tr>
					</thead>
					<tbody>
						<c:set var="linkedCount" value="${0}" />
						<c:forEach var="linkedProduct" items="${productBean.directLinkedProducts}" >
							<c:set var="linkedCount" value="${linkedCount + 1}" />
							<tr>
								<td>${linkedProduct.productId}</td>
								<td>${linkedProduct.productId}</td>
								<td>${linkedProduct.name}</td>
							</tr>
						</c:forEach>
						<c:if test="${linkedCount eq 0}">
							<tr><td id="noMessages" colspan="3" style="text-align:center"><spring:message code="label.no.linked.products" /></td></tr>
						</c:if>
				</table>
			</div>
		</fieldset>
	</div>
</div>

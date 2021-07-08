<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:choose>
	<c:when test="${not empty results}">
		<c:forEach items="${results}" var="product" varStatus="status">
			<a href="#">
			<div id="${product.productId}" title="${product.productName}" class="product result">
				<div class=productName>
					<span class="shorten"><c:out value="${product.productName}"/></span>
					${product.productId}
				</div>
				<div>
					<div class="divisionName"><c:out value="${product.divisionType}"/></div>
					<div class="registerableType"><spring:message code="label.product.registerableType.${product.registerableType}" text=""/></div>
					<div class="activation"><spring:message code="label.registration.activation.${product.activationStrategy}" text="" /></div>
				</div>
			</div>
			</a>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<div style="text-align: center; padding-top: 50px">
			<spring:message code="label.noResults" />
		</div>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
	$(function() {
		$('.shorten').shorten();
	});
</script>
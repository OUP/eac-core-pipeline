<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="externalSystemIdTypeTile">
	<div class="field">
		<div class="fieldLabelNarrow">
			<label for="externalSystemIdType"><spring:message code="label.externalIds.externalSystemIdType" /></label>
		</div>
		<div class="fieldValue">
			<spring:bind path="customerBean.externalSystemIdType">
				<select name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" style="width: auto">
					<c:forEach var="externalSystemIdType" items="${externalSystemIdTypes}">
						<option value="${externalSystemIdType.id}" <c:if test="${status.value == externalSystemIdType}">selected="selected"</c:if>><c:out value="${externalSystemIdType.name}"/></option>
					</c:forEach>
				</select>
			</spring:bind>	
		</div>
	</div>
</div>
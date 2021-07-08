<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<form:form modelAttribute="productSearchBean" action="" id="productMatchForm">
	<div id="productMatchFormTile">
		<fieldset style="width: 50em">
			<legend><spring:message code="title.matchcriteria" /></legend>
			<div class="field">
				<div class="fieldLabelNarrow">
					<label for="exampleUrl"><spring:message code="label.exampleUrl"/></label>
				</div>
				<div class="fieldValue">
					<form:input id="exampleUrl" path="exampleUrl" cssClass="wide"/>
				</div>
			</div>	
		</fieldset>
	</div>
</form:form>

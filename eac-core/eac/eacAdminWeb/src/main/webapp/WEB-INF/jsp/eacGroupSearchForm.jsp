<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="eacGroupSearchFormTile">
	<fieldset>
		<legend><spring:message code="title.searchcriteria" /></legend>
		<div class="field" style="margin-bottom: 0px">
			<div class="fieldLabel">
				<label for="searchTerm"><spring:message code="label.searchFor"/></label>
			</div>
			<div class="fieldValue">
				<form:input id="searchFor" path="searchTerm" />
			</div>
		</div>	
		<div class="field">
			<div class="fieldLabel">&#160;</div>
			<span class="searchOption"><form:radiobutton id="searchGroupName" path="searchBy" value="groupName" checked="checked"/><label for="searchGroupName"><spring:message code="label.groupName" /></span></label>
			<span class="searchOption"><form:radiobutton id="searchProductName" path="searchBy" value="productName"/><label for="searchProductName"><spring:message code="label.productName" /></span></label>
			<span class="searchOption"><form:radiobutton id="searchProductId" path="searchBy" value="productId"/><label for="searchProductId"><spring:message code="label.productId" /></span></label>
			<span class="searchOption"><form:radiobutton id="searchExternalId" path="searchBy" value="externalId"/><label for="searchExternalId"><spring:message code="label.externalId" /></span></label>
		</div>
		
		<div class="field">
			<div class="fieldLabel">
				<label for="resultsPerPage"><spring:message code="label.resultsperpage" /></label>
			</div>
			<div class="fieldValue">
				<form:select id="resultsPerPage" path="resultsPerPage" cssStyle="width: auto">
					<form:option value="5">5</form:option>
					<form:option value="10">10</form:option>
					<form:option value="15">15</form:option>
					<form:option value="20">20</form:option>
				</form:select>
			</div>
		</div>
	</fieldset>
	<script type="text/javascript">
		$(function() {
			$('#searchFor').focus();
		});
	</script>
</div>
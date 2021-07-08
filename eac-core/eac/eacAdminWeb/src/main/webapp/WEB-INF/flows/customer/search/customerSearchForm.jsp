<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="customerSearchFormTile">
	<fieldset>
		<legend><spring:message code="title.searchcriteria" /></legend>
		<div class="field">
			<div class="fieldLabel">
				<label for="username"><spring:message code="label.username" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="username" path="username"/>
			</div>
		</div>	
		<div class="field">
			<div class="fieldLabel">
				<label for="email"><spring:message code="label.email" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="email" path="email"/>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="firstName"><spring:message code="label.firstName" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="firstName" path="firstName"/>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="familyName"><spring:message code="label.familyName" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="familyName" path="familyName"/>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="externalId"><spring:message code="label.externalId" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="externalId" path="externalId"/>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="createdDateFrom"><spring:message code="label.createdDateFrom" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="createdDateFrom" path="createdDateFrom"/>
				<script type="text/javascript">
					$('#createdDateFrom').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
				</script>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="createdDateTo"><spring:message code="label.createdDateTo" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="createdDateTo" path="createdDateTo"/>
				<script type="text/javascript">
					$('#createdDateTo').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
				</script>
			</div>
		</div>				
		<fieldset style="clear:both; padding-top: 0px\9; margin-bottom: 20px\9;">
			<legend><spring:message code="title.registrationData" /></legend>
			<div class="field">
				<div class="fieldLabelNarrow">
					<label for="registrationData"><spring:message code="label.value" /></label>
				</div>
				<div class="fieldValue">
					<form:input path="registrationData" cssStyle="width:30em; margin-bottom: 5px"/>
					<spring:message code="info.separateFields" />
				</div>
			</div>	
		</fieldset>
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
			$('#username').focus();
			$( "#searchResultsTile" ).hide();
		});
	</script>
</div>
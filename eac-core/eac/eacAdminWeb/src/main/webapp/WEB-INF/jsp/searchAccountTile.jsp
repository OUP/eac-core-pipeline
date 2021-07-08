<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="customerSearchFormTile">
	<fieldset>
		<legend><spring:message code="title.searchcriteria" /></legend>
		<input type="hidden" name="search" value="1"/>
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
					<label for="resultsPerPage"><spring:message code="label.role" /></label>
				</div>
				<div class="fieldValue">
							<select id="selectedRole" name="selectedRole">
								<option value="pleaseSelect">${pleaseSelect}</option>
									<c:forEach var="systemRoles" items="${accountBean.roles}">
										<c:set var="selected" value=""/>
										<option value="${systemRoles.id}" ${selected}><c:out value="${systemRoles.name}"/></option>
									</c:forEach>
								<option value=""><spring:message code="label.newEntry" htmlEscape="true"/></option>
						</select>
				</div>
		
			</div>	
			<div class="field">
				<div class="fieldLabel">
					<label for="resultsPerPage"><spring:message code="label.divisions" /></label>
				</div>
				<div class="fieldValue">
							<select id="selectDivisions" name="selectedDivision">
								<option value="pleaseSelect">${pleaseSelect}</option>
									<c:forEach var="systemDivision" items="${accountBean.division}">
										<c:set var="selected" value=""/>
										<option value="${systemDivision.erightsId}" ${selected}><c:out value="${systemDivision.divisionType}"/></option>
									</c:forEach>
								<option value=""><spring:message code="label.newEntry" htmlEscape="true"/></option>
						</select>
				</div>
		
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
			$('#username').focus();
		});
	</script>
</div>
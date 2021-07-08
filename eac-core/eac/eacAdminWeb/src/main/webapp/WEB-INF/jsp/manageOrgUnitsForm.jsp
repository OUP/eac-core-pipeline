<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<c:url value="/mvc/orgunits/manage.htm" var="formUrl"/>
<c:url value="/images/add.png" var="addIconUrl"/>
<form:form modelAttribute="orgUnitsBean" action="${formUrl}">
<fieldset>
	<spring:message var="txtOrgUnitsLong" code="label.org.unit.long"  text="?label.org.units.long?"/>
	<spring:message var="txtOrgUnits"     code="label.org.unit.short" text="?label.org.units.short?"/>
	<spring:message var="txtOrgUnitLong"  code="label.org.unit.long"  text="?label.org.unit.long?"/>
	<spring:message var="txtOrgUnit"      code="label.org.unit.short" text="?label.org.unit.short?"/>
	<spring:message var="txtAddOrgUnit"      code="label.add.org.unit"   text="?label.add.org.unit?"/>
	
	<legend>${txtOrgUnitsLong}</legend>		
		<div id="orgUnitWrapper"  style="max-height: 620px; overflow-x: hidden; overflow-y: auto; margin-bottom: 15px">
		<c:forEach var="orgUnit" items="${orgUnitsBean.newOrgUnits}" varStatus="status">
		<fieldset class="newOrgUnit">	
			<div class="field orgUnitField">
				<div class="fieldLabelNarrow">
					<label>${txtOrgUnit}&#160;<span class="mandatory">*</span></label>
				</div>				
				<form:input  path="newOrgUnits[${status.index}].divisionType" maxlength="255"/>
				<form:errors path="newOrgUnits[${status.index}].divisionType" cssClass="orgUnitError" />
				<div class="removeNewOrgUnit" style="float: right; cursor: pointer"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
			</div>
		</fieldset>
		</c:forEach>		
		
		<c:forEach var="orgUnit" items="${orgUnitsBean.orgUnits}" varStatus="status">
			<c:set var="deleted" value="${false}" />
			<c:forEach var="removedIdx" items ="${orgUnitsBean.orgUnitIndexesToRemove}">				
				<c:if test="${not deleted}">
					<c:if test="${status.index eq removedIdx}">
						<c:set var="deleted" value="${true}" />
					</c:if> 
				</c:if>
			</c:forEach>
			<c:if test="${not deleted}">
				<fieldset class="orgUnit" id="${status.index}">
					<div class="field orgUnitField">
						<div class="fieldLabelNarrow">
							<label for="">${txtOrgUnit}&#160;<span class="mandatory">*</span></label>
						</div>				
						<form:input  path="orgUnits[${status.index}].divisionType" maxlength="255"/>
						<form:errors path="orgUnits[${status.index}].divisionType" cssClass="orgUnitError" />
						<eac-common:isOrgUnitUsed var="isOrgUnitUsed" orgUnitId="${orgUnit.erightsId}" />
						<c:if test="${not isOrgUnitUsed}">
							<div class="removeOrgUnit" style="float: right; cursor: pointer"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
						</c:if>
					</div>
				</fieldset>
			</c:if>
		</c:forEach>
		</div>
		<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a href="#" id="addNewOrgUnitLink">${txtAddOrgUnit}</a>
</fieldset>
      <div id="buttons">
	   	<p>                 
      		<button type="submit" id="save"  ><spring:message code="button.save"   /></button>
      		<button type="button" id="cancel"><spring:message code="button.cancel"  /></button>
        </p>
      </div>
<spring:message code="label.remove" var="labelRemove"/>

	<div id="newOrgUnitlPrototype">
		<fieldset class="newOrgUnit">
			<div class="field orgUnitField">
				<div class="fieldLabelNarrow">
					<label>${txtOrgUnit}&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<input type="text" name="newOrgUnits[%idx%].divisionType" disabled="disabled" maxlength="255" />
				</div>
				<div class="removeNewOrgUnit" style="float: right; cursor: pointer"><img class="actionImg" src="<c:url value="/images/delete.png"/>" width="16" height="16" alt="${labelRemove}" title="${labelRemove}"/></div>
			</div>
		</fieldset>
	</div>	
	<form:hidden id="orgUnitIndexesToRemove" path="orgUnitIndexesToRemove" />
</form:form>

<script type="text/javascript">
$(function() {
	
	$('#cancel').click(function() {		
		window.location.replace('<c:url value="/home"/>');
	});
	
	var $newOrgUnit = $('#newOrgUnitlPrototype');
	$newOrgUnit.hide();
	
	$('#addNewOrgUnitLink').click(function() {
		var $orgUnitWrapper = $('#orgUnitWrapper');
		$clonedOrgUnit = $newOrgUnit.clone();
		$clonedOrgUnit.removeAttr('id');
		$clonedOrgUnit.find('input').each(function() {
			$(this).removeAttr('disabled');
		});
		$orgUnitWrapper.prepend($clonedOrgUnit);
		$clonedOrgUnit.show();
		$clonedOrgUnit.find('input:first').focus();
		$orgUnitWrapper.scrollTop(0);
		return false;
	});
	
	$('.removeOrgUnit').click(function() {
		var $fieldset = $(this).closest('fieldset');
		var $orgUnitIndexesToRemove = $('#orgUnitIndexesToRemove');
		var orgUnitIndexesToRemove = $orgUnitIndexesToRemove.val();
		if (orgUnitIndexesToRemove != "") {
			orgUnitIndexesToRemove += ",";
		}
		orgUnitIndexesToRemove += $fieldset.attr('id');
		$orgUnitIndexesToRemove.val(orgUnitIndexesToRemove);
		$fieldset.hide();
	});
	
	$('#orgUnitWrapper').delegate('.removeNewOrgUnit', 'click', function() {
		$(this).closest('fieldset').remove();
	});

	
	$('form').submit(function() {
		var newOrgUnitCount = 0;
		$('#orgUnitWrapper .newOrgUnit').each(function() {			
			$(this).find('input').each(function() {									
				$(this).attr('name', 'newOrgUnits['+newOrgUnitCount+'].divisionType');
			});
			newOrgUnitCount++;
		});
	});

});
</script>


<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<fieldset>
	<legend><spring:message code="label.divisions"/></legend>
	<c:forEach var="divisionAccess" items="${accountBean.divisionAccessList}" varStatus="status">
		<div class="field checkboxField">
			<form:checkbox id="${divisionAccess.division.id}" path="divisionAccessList[${status.index}].access" cssClass="checkbox" cssStyle="vertical-align: 1px\9;"/>
			<label for="${divisionAccess.division.id}"><c:out value="${divisionAccess.division.divisionType}"/></label>
		</div>	
	</c:forEach>
</fieldset>
<fieldset>
	<legend><spring:message code="label.roles"/></legend>
	<c:forEach var="roleAccess" items="${accountBean.roleAccessList}" varStatus="status">
		<div class="field checkboxField">
			<form:checkbox id="${roleAccess.role.id}" path="roleAccessList[${status.index}].access" cssClass="checkbox role" cssStyle="vertical-align: 1px\9;"/>
			<label for="${roleAccess.role.id}"><c:out value="${roleAccess.role.name}"/></label>
		</div>	
	</c:forEach>	
</fieldset>
<fieldset>
	<legend><spring:message code="label.permissions"/></legend>
	<spring:message code="info.permissions" var="permissionsHelp" />
	<img id="permissionsHelp" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${permissionsHelp}" style="margin-left: 5px; padding-top: 0px"/>
	<script type="text/javascript">
		$("#permissionsHelp").tipTip({edgeOffset: 7});
	</script>		
	<div id="selectedPermissions" style="margin-top: 10px; max-height: 150px; overflow-x: hidden; overflow-y: auto"></div>
</fieldset>
<script type="text/javascript">

	$(function() {
		var rolePermissionsMap = {};
		<c:forEach var="role" items="${accountBean.roles}">
		var roleId = '${role.id}';
			var permissions = rolePermissionsMap[roleId];
			if (!permissions) {
				permissions = [];
				rolePermissionsMap[roleId] = permissions;
			}
			<c:forEach var="permission" items="${role.permissions}">
				permissions.push('${permission.name}');
			</c:forEach>
		</c:forEach>
		
		function displayPermissions() {
			var selectedPermissions = [];
			$('input:checked.role').each(function() {
				var roleId = $(this).attr('id');
				var rolePermissions = rolePermissionsMap[roleId];
				for (var i = 0; i < rolePermissions.length; i++) {
					var rolePermission = rolePermissions[i];
					if (jQuery.inArray(rolePermission, selectedPermissions) == -1) {
						selectedPermissions.push(rolePermission);
					}
				}
			});
			var $selectedPermissions = $('#selectedPermissions');
			$selectedPermissions.empty();
			if (selectedPermissions.length > 0) {
				selectedPermissions.sort();
				$selectedPermissions.append(selectedPermissions.join('<br/>'));
			} else {
				$selectedPermissions.append('<spring:message code="label.userNoPermissions"/>');
			}
		}
		
		$('input.role').click(function() {
			displayPermissions();
		});
		
		displayPermissions();
		
	});

</script>

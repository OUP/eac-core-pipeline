<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div>
	<div id="heading" class="ui-corner-top">
	   <h1><spring:message code="label.rolesandpermissions" /></h1>
	</div>
	<spring:hasBindErrors name="roleCriteria">
		<div class="error">
			<spring:bind path="roleCriteria.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>	
    <c:if test="${successMsg ne null}">
    <div class="success">
        <spring:message code="${successMsg}"/>
    </div>
    </c:if> 
	<form:form action="" id="roleCriteria" modelAttribute="roleCriteria" >
    <fieldset>
        <legend><spring:message code="label.roles"/></legend>
        <div class="field">
            <div class="fieldLabel">
                <label for="role"><spring:message code="label.selectrole"/></label>
            </div>
            <div class="fieldValue">
                <form:select id="roleId" path="roleId" >
                    <c:forEach var="role" items="${roles}">
                        <form:option value="${role.id}" label="${role.name}"/>
                    </c:forEach>
                    <form:option value="new" label="<new>"/>
                </form:select> 
            </div>
        </div> 
    </fieldset>
	<c:if test="${not empty roleCriteria.permissionSelections}">
	<fieldset>
	    <legend><spring:message code="label.roleSummary" text="Role Summary"/></legend>
	    <div>
	        <div class="field" style="padding-bottom: 10px;">
                <div class="fieldLabel">
                    <label for="role"><spring:message code="label.name"/></label>
                </div>
                <div class="fieldValue">
                    <form:input id="roleName" path="roleName" cssErrorClass="fieldError"/>
                </div>
            </div>
		    <fieldset>
		        <legend><spring:message code="title.permissions" /></legend>
	            <div id="permissions" style="max-height: 500px; overflow: auto; width: 680px;">
			        <table style="float: left; width: 600px;">
			            <c:forEach var="permissionSelection" items="${roleCriteria.permissionSelections}" varStatus="status">
			                <tr>
			                    <td class="role_row" style="width: 30px;"><form:checkbox  cssStyle="display: inline;float: left;position: static;" id="${permissionSelection.permission.id}" path="permissionSelections[${status.index}].access"/>
			                                                              <form:hidden path="permissionSelections[${status.index}].permission.id"/>
			                                                              <form:hidden path="permissionSelections[${status.index}].permission.name"/> </td>
			                    <td class="role_row" style="width: 500px;"><label for="${permissionSelection.permission.id}" style="font-weight: normal"><c:out value="${permissionSelection.permission.name}"/></label></td>
			                </tr>  
			            </c:forEach>
			        </table>
			        <div>
			        <input type="hidden" name="deletable" id="deletable" value="${roleCriteria.deletable}"/>
			        </div>
		        </div>
		    </fieldset>
	    </div> 
	</fieldset>
	</c:if>
	<div id="buttons">
        <button type="button" id="delete" eacLabel="" style="<c:if test="${!roleCriteria.deletable}">display: none;</c:if>"><spring:message code="button.delete" /></button>
        <button type="button" id="save""><spring:message code="button.save" /></button>
        <input type="hidden" id="method" name="method" value="save"/>
        <spring:message code="confirm.title.deleterole" var="confirmDelete" text="" />
	</div>
	</form:form> 
</div>
<script type="text/javascript">
 
 $(function() {
    $('#roleId').change(function() {
	   $('.success').remove();
	   $('.error').remove();
       $('#roleNameErrors').remove();
       $('#roleName').removeClass('fieldError');
       
       var url = '<c:url value="/mvc/roles/manage.htm"/>';
       if($(this).val() == 'new') {
    	   url += '?method=allpermissions';
    	   $('#roleName').val('');
       } else  {
    	   url += '?method=permissions&id=' + $(this).val();
    	   $('#roleName').val($(this).find("option:selected").text());
    	   $('#delete').attr('eacLabel', $(this).find("option:selected").text());
       }
       $.ajax({
           url: url,
           type: 'GET'
       }).done(function(html) {
           $('#permissions').empty().html(html);
           if($('#deletable').val() == 'true') {
                $('#delete').show();
           } else {
                $('#delete').hide();
           }
       });  
    }).keypress(function() {
		$(this).trigger('change');
	});
    
    $('#save').click(function() {
        $('#method').val('save');
        $('#roleCriteria').submit();
    });    
    
    var callback = function(){
    	$('#method').val('delete');
    	$('#roleCriteria').submit();
    };
    var clickHandler = eacConfirm({callbackYes:callback, title:'${confirmDelete}'});
    $('#delete').click(clickHandler);
    
    $('#delete').attr('eacLabel', $('#roleId').find("option:selected").text());
 });    
</script>
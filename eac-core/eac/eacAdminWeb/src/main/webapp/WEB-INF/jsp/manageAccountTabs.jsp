<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<div id="manageAccountTabsTile">
<c:url value="/mvc/account/manage.htm" var="formUrl"/>
	<spring:hasBindErrors name="accountBean">
		<div class="error">
			<spring:bind path="accountBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>	
	
	<form:form modelAttribute="accountBean" method="POST">
			<div id="pagetabs" style="margin-top: 20px\9;">
			    <ul>
			        <li class="ui-state-hover"><a href="#pagetab-1"><spring:message code="tab.user"/></a></li>
			        <li><a href="#pagetab-2"><spring:message code="tab.permission"/></a></li>
			    </ul>
			    <div id="pagetab-1" class="ui-tabs-hide">
					<tiles:insertAttribute name="manageAccountUserTile"/>
			    </div>
			    <div id="pagetab-2" class="ui-tabs-hide">
					<tiles:insertAttribute name="manageAccountPermissionTile"/>
			    </div>
			</div>	
        <div id="buttons">
	    	<p>                 
	    		<button type="button" eacLabel="${accountBean.selectedAdminUser.username}" id="delete"><spring:message code="button.delete"/></button>
	    		<button type="submit" id="save"><spring:message code="button.save" /></button>
	        </p>
	    </div>
 		 <input type="hidden" name="id" id="id" value="${accountBean.selectedAdminUser.id}"/>
	</form:form>		
	<script type="text/javascript">
	$(function() {
		$('#pagetabs').tabs({ cookie: { name: 'manageAccount', expires: 1 } });

		var myAccount = ${accountBean.myAccount};
		var submitForm = function() {
			var $form = $('form');
			$form.append('<input type="hidden" name="delete" value="1" />');
			$form.submit();
		};
		
		
		
		var clickHandler = eacConfirm({callbackYes:submitForm, title:'${confirmDelete}'});
		$('#delete').click(clickHandler);
		
	});
	</script>
</script>
</div>
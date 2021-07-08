<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<spring:message code="label.pleaseSelect" var="pleaseSelect"/>
<div id="manageAccountTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.manageAccounts"/></h1>
	</div>

	<c:if test="${not empty statusMessageKey}">
		 <div class="success">
			<spring:message code="${statusMessageKey}"/>
		</div>
	</c:if>
	
	<spring:hasBindErrors name="accountBean">
		<div class="error">
			<spring:bind path="accountBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>	
	<c:url value="/mvc/account/search" var="formUrl"/>
	<form:form modelAttribute="accountBean"  id="searchForm">
	<fieldset>
		<legend><spring:message code="label.adminAccount"/></legend>
		<div class="field" style="min-height: 32px">
			<div class="fieldLabel">
				<label><spring:message code="label.selectUser"/></label>
			</div>
			<div id=searchAccountTile>
			<tiles:insertAttribute name="searchAccountTile"/>
			</div>
			<div>
			<tiles:insertAttribute name="searchControlTile"/>
			<div>
			 	<button type="button" id="createAccount" >Create</button>
			</div>
			</div>
			
			<div id="ownAccountWarning" style="display: none">
				<img id="receptionist" src="<c:url value="/images/receptionist.png"/>" style="float: left; vertical-align: middle; margin-left: 50px;"/>
				<span style="width: 200px; margin-left: 10px; display: block; float: left; font-weight: bold"><spring:message code="info.accountLockoutWarning"/></span>
			</div>			
		</div>
	</fieldset>
	
		<tiles:insertAttribute name="searchResultsAccountTile"/>
	

	</form:form>
	
	
	<script type="text/javascript">
	
	
	
	
	
		$(function() {
			var $selectedAdminUser = $('#selectedAdminUser');
			var $createAccount = $('#createAccount')
			var ownAccountWarning = $('#ownAccountWarning');
			
			$createAccount.click(function(){
				window.location.href = "/eacAdmin/mvc/account/manage.htm?id=&refreshSession=Y";
			});
			
			$selectedAdminUser.click(function() {
				$.ajax({
					type: 'GET',
					url: '<c:url value="/mvc/account/manage.htm"/>',
					data: 'id=' + $(this).val()
				}).done(function(html) {
					$('#searchResultsAccountTile').empty().append(html);
				});
				$('.success').remove();
				$('.error').remove();
				if ($(this).val() == '${accountBean.myGuid}') {
					$('#ownAccountWarning').show();
				} else {
					$('#ownAccountWarning').hide();
				}
			}).keypress(function() {
				$(this).trigger('change');
			});
		});
	</script>
</div>
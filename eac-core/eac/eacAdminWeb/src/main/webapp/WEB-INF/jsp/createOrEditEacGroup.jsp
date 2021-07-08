<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<div id="createOrEditEacGroup">

	<div id="heading" class="ui-corner-top">
		<c:choose>
			<c:when test="${eacGroupsBean.editMode}">
				<spring:message code="title.editEacGroup" var="title" />
			</c:when>
			<c:otherwise>
				<spring:message code="title.createEacGroup" var="title" />
			</c:otherwise>
		</c:choose>
		<h1>${title}</h1>
	</div>
	
	<%-- <c:choose>
		<c:when test="${!eacGroupsBean.editable}">
			<div class="error" id="notEditableError">
				<spring:message code="error.eacGroupNameIsNotEditable" />
			</div>
		</c:when>
	</c:choose> --%>
	
	<div class="error" id="errorDiv">
		<spring:message code="error.emptyEacGroupName" />
	</div>
	
	<spring:hasBindErrors name="eacGroupsBean">
		<div class="error">
			<spring:bind path="eacGroupsBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}" /></span>
					<br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>

	<form:form modelAttribute="eacGroupsBean" method="POST">
		<fieldset
			style="clear: both; padding-top: 0px\9; margin-bottom: 20px\9; width: 96%;">
			<legend>
				<spring:message code="label.eac.group.info" />
			</legend>
				
			<div id="groupNameDiv">
				
					<c:choose>
					
						<c:when test="${eacGroupsBean.editMode}">
						<div class="field">
							<div class="fieldLabel">
								<label for="id"><spring:message code="label.id" />&#160;<span class="mandatory">*</span></label>
							</div>
							<div class="fieldValue">
								<c:out value="${eacGroupsBean.groupId}" />
								<form:hidden id="groupid" path="groupId" />
							</div>
						</div>	
						</c:when>
						<c:otherwise>
							<form:hidden id="groupid" path="groupId" />
						</c:otherwise>
						
					</c:choose>
				
				<div class="field">
					<div class="fieldLabel">
						<label for="groupName"><spring:message code="label.groupName" />&#160;<span class="mandatory">*</span></label>
					</div>
					<div class="fieldValue">
						<form:input id="groupName" path="groupName" />
					</div>
				</div>
				 <%-- <security:authorize ifAllGranted="ROLE_CHANGE_ISEDITABLE_PRODUCT_GROUP"> 
				<div class="field">
					<div class="fieldLabel">
						<label for="editable"><spring:message code="label.isEditable" /></label>
					</div>
					<div class="fieldValue">
						<form:checkbox id="editable" path="editable" cssClass="checkbox"/>
					</div>
					<security:authorize ifAllGranted="ROLE_CHANGE_ISEDITABLE_PRODUCT_GROUP">
					<c:choose>
						<c:when test="${eacGroupsBean.editMode}">
							<div id="buttons">
                				<p><button type="button" id="updateIsEditable"><spring:message code="button.update.isEditable" /></button></p>
            				</div>
						</c:when>
					</c:choose>
					</security:authorize>	 
				</div>
				 </security:authorize> --%>
			</div>
		</fieldset>
		<fieldset style="clear: both; padding-top: 0px\9; margin-bottom: 20px\9; width: 96%;">
			<div>
				<div style="height: 500px; width: 350px; float: left;">
					<fieldset style="clear: both; padding-top: 0px\9; margin-bottom: 20px\9; width: 90%;">
						<legend>
							<spring:message code="label.eac.group.product.search" />
						</legend>
						<div class="field" style="margin-bottom: 0px">
							<div class="fieldLabel" style="width: 90px; padding-left: 6px;">
								<label for="searchTerm"><spring:message
										code="label.searchFor" /></label>
							</div>
							<div class="fieldValue">
								<input type="text" id="searchFor" style="width: 180px;" onkeypress="searchKeyPress(event);"/>
							</div>
						</div>
						<div class="field">
							<div class="fieldLabel"></div>
							<span class="searchOption">
								<input type="radio"	name="searchBy" id="searchProductName" value="productName" checked="checked" />
								<label for="searchProductName">
									<spring:message	code="label.productName" />
								</label>
							</span> 
							<span class="searchOption">
								<input type="radio" name="searchBy" id="searchProductId" value="productId" />
								<label for="searchProductId">
									<spring:message	code="label.productId" />
								</label>
							</span>
							<span class="searchOption">
								<input type="radio" name="searchBy" id="searchExternalId" value="externalId" />
								<label for="searchExternalId">
									<spring:message	code="label.externalId" />
								</label>
							</span>
						</div>

						<button type="button" id="searchProduct" onclick="myFunction()">
							<spring:message code="button.search" />
						</button>
						
						<fieldset style="clear: both; padding-top: 0px\9; margin-bottom: 20px\9; width: 91%;">

							<div id="searchResults" style="height: 250px;">
								<table class="summary" id="productTable">


								</table>
							</div>

						</fieldset>
						
						<button type="button" id="add" onClick="addFunction()">Add Selected</button>
					</fieldset>
				</div>
				<!-- test -->
				<div style="height: 500px; width: 350px; float: left;">
					<fieldset style="clear: both; padding-top: 0px\9; margin-bottom: 20px\9; width: 96%;">
						<legend>
							<spring:message code="label.eac.group.mapping" />
						</legend>
						<div id="mapping" style="height: 425px;">
							<fieldset style="clear: both; padding-top: 0px\9; margin-bottom: 20px\9; width: 92%;">
								
								<div id="productsToAdd" style="height: 337px;">
									<table class="summary" id="groupingTable">
										<script type="text/javascript">
											var existingKey = new Array();
											var existingValue = new Array();
											var cnt=0;
										</script>
										<c:choose>
											<c:when test="${eacGroupsBean.editMode}">
												<c:forEach var="existProd" items="${eacGroupsBean.existingProducts}" >
													<script type="text/javascript">
														existingKey[cnt]="${existProd.key}";
														existingValue[cnt]="${existProd.value}";
														cnt++;
													</script>
												</c:forEach>
											</c:when>
										</c:choose>
										
									</table>
								</div>

							</fieldset>

							<button type="button" id="remove" onClick="removeFunction()">Remove	Selected</button>

						</div>

					</fieldset>
				</div>

			</div>
			<div id="buttons">
				<p>
					<c:choose>
						<c:when test="${eacGroupsBean.editMode}">
							<%-- <c:set var="canUpdate" value="${false}" />
							<security:authorize ifAllGranted="ROLE_CHANGE_ISEDITABLE_PRODUCT_GROUP">
								<c:set var="canUpdate" value="${true}" />
							</security:authorize> 
						
						 <c:if test="${canUpdate || eacGroupsBean.editable}">--%>
						 <%--  <c:if test="${eacGroupsBean.editable}"> --%>
						 <%-- 	<button type="submit" id="edit" onclick="emptyNameCheck(event)"><spring:message code="button.edit" /></button> --%>
						 <%-- </c:if> --%>
							<button type="button" id="cancel"><spring:message code="button.cancel" /></button>
						</c:when>
						<c:otherwise>
							<button type="submit" id="save" onclick="emptyNameCheck(event)"><spring:message code="button.save" /></button>
						</c:otherwise>
					</c:choose>
				</p>
			</div>
		</fieldset>

	</form:form>

	<script type="text/javascript">
	$(function() {
		$('#errorDiv').hide();
		
		$('#cancel').click(function() {
			window.location.replace('<c:url value="/mvc/eacGroups/search.htm"/>');
		});
	});
		
		function searchKeyPress(e)
		{
			if (typeof e == 'undefined' && window.event) { e = window.event; }
			if (e.keyCode == 13) {
				e.preventDefault();
				document.getElementById('searchProduct').click();
			}
			return false;
		}
	
		for (var i in existingKey) {
			addToGrouping(existingKey[i], existingValue[i]);
		}

		var productToAdd = new Array();
		var productToAddName = new Array();
		var productToRemove = new Array();

		function myFunction() {

			var div = document.getElementById("searchResults");
			var table = document.getElementById("productTable");
			table.innerHTML = "";
			var searchTerm = document.getElementById("searchFor").value;
			var searchBy = "";
			if (document.getElementById("searchProductName").checked)
				searchBy = "productName";
			else if (document.getElementById("searchProductId").checked)
				searchBy = "productId";
			else
				searchBy = "externalId";
			$.ajax({
				url : '<c:url value="/mvc/eacGroups/searchProductsForGrouping.htm"/>',
				type : 'GET',
				data : 'id=${eacGroupsBean.groupId}&searchTerm=' + searchTerm + '&searchBy=' + searchBy
			}).done(function(response) {
				div.setAttribute("style", "height:250px;overflow-y:scroll;");

				$.each(response, function(key, value) 
				{
					var tr = document.createElement("tr");
					tr.setAttribute("style", "border:1px solid #dedede;");
					var tdName = document.createElement("td");
					var tdCheckbox = document.createElement("input");
					tdCheckbox.setAttribute("type", "checkbox");
					tdCheckbox.setAttribute("name", "CB");
					tdCheckbox.setAttribute("value", value);
					tdCheckbox.setAttribute("id", key);
					var label = document.createElement("label");
					label.setAttribute("for", key);
					label.setAttribute("id", "label" + key);
					label.appendChild(document.createTextNode(value));
					tdName.appendChild(label);
					tr.appendChild(tdCheckbox);
					tr.appendChild(tdName);
					table.appendChild(tr);

				});

			});

		}

		function addToGrouping(id, name) 
		{
			var newID = "added" + id;
			var output = document.getElementById("groupingTable");
			var div = document.getElementById("productsToAdd");
			div.setAttribute("style", "height:337px;overflow-y:scroll;");

			if (document.getElementById(newID))
				return;

			var tr = document.createElement("tr");
			tr.setAttribute("style", "border:1px solid #dedede;");
			var tdName = document.createElement("td");
			var tdCheckbox = document.createElement("input");
			tdCheckbox.setAttribute("type", "checkbox");
			tdCheckbox.setAttribute("name", "CB2");
			tdCheckbox.setAttribute("value", name);
			tdCheckbox.setAttribute("id", newID);
			var label = document.createElement("label");
			label.setAttribute("for", name);
			label.setAttribute("id", "label" + newID);
			label.appendChild(document.createTextNode(name));
			tdName.appendChild(label);
			tr.appendChild(tdCheckbox);
			tr.appendChild(tdName);
			output.appendChild(tr);

		}

		function addFunction() 
		{

			for (i = 0; i < document.getElementsByName("CB").length; i++) 
			{
				productID = document.getElementsByName("CB")[i];
				if (productID.checked) 
				{
					if (productToAdd.indexOf(productID.id) < 0){
						
						if(productToRemove.indexOf(productID.id) >= 0){
							var index=productToRemove.indexOf(productID.id);
							productToRemove.splice(index, 1);
						}else{
							productToAddName.push(productID.value);
							productToAdd.push(productID.id);
						}
						
						
					addToGrouping(productID.id, productID.value);
					}
				}
			}
		}

		function removeFunction() 
		{
			var productGrpID = document.getElementsByName("CB2");
			for (i = 0; i < productGrpID.length;) 
			{
				if (productGrpID[i].checked) 
				{
					if (productToAdd.indexOf(productGrpID[i].id.substring(5)) >= 0) 
					{
						var index=productToAdd.indexOf(productGrpID[i].id.substring(5));
						productToAdd.splice(index, 1);
						productToAddName.splice(index, 1);
					} 
					else 
					{
						productToRemove.push(productGrpID[i].id.substring(5));
					}

					var element = document.getElementById(productGrpID[i].id);
					var parent = element.parentNode;
					parent.removeChild(element);
					parent.parentNode.removeChild(parent);
				} 
				else 
				{
					i++;
					continue;
				}
			}
		}
		
		function emptyNameCheck(e){
			if($('#groupName').val() == ""){
				$('#errorDiv').show();
				window.scrollTo(0,0);
				if (typeof e == 'undefined' && window.event) { e = window.event; }
				e.preventDefault();
				return false;
			}
		}	
		
		$('#eacGroupsBean').submit(	function() 
		{	
				  	var $this = $(this);
					$this.append('<input type="hidden" name="productIdsToAdd" value="' + productToAdd + '" />');
					$this.append('<input type="hidden" name="productIdsToRemove" value="' + productToRemove + '" />');
		});
		
		$('button[id="updateIsEditable"]').click(function(){
			document.forms[0].action="/eacAdmin/mvc/eacGroups/updateIsEditable.htm?id=${eacGroupsBean.groupId}";
			document.forms[0].method="post";
			document.forms[0].submit();	 
		});
		
		var $isEditableCbx = $('#editable');
		$isEditableCbx.click(function() {
			if ($(this).prop('checked')) {
				$('#edit').show();
			} else {
				$('#edit').hide();
			}
		});
	</script>
</div>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<form:form modelAttribute="activationCodeSearchBean" action="" id="searchForm">
<div id="productSearchFormTile">
	<fieldset>
		<legend><spring:message code="label.search.activationcodes" /></legend>
        <div class="field">
            <div class="fieldLabel">
                <label for="code"><spring:message code="label.activationCode" /></label>
            </div>
            <div class="fieldValue">
                <form:input path="code" id="code"/>
            </div>
        </div> 
        <div class="field">
            <div class="fieldLabel">
                <label><spring:message code="label.product" /></label>
            </div>
            <div class="fieldValue" style="max-width: 60em" id="productDiv">
                <tags:productFinder returnFieldId="reportProductId" initGuid="${activationCodeReportCriteria.productId}" resetButtonId="reset"/>
                <form:hidden path="productId" id="reportProductId"/>
                <input type="hidden" id="method" name="method" value="search"/>
                <script type="text/javascript">
				$('#reportProductId').change(function() {
					if($('#reportProductId').val() != ""){
						$('#groupDiv').find('.iconClear').click();
					}
				});	
				</script>
            </div>
        </div>
         <div class="field">
            <div class="fieldLabel">
                <label><spring:message code="label.product.group" /></label>
            </div>
            <div class="fieldValue" style="max-width: 60em" id="groupDiv">
                <tags:eacGroupFinder returnFieldId="eacGroupId" resetButtonId="reset"/>
                <form:hidden path="eacGroupId" id="eacGroupId"/>
                <script type="text/javascript">
				$('#eacGroupId').change(function() {
					if($('#eacGroupId').val() != ""){
						$('#productDiv').find('.iconClear').click();
					}
				});		
				</script>
            </div>
        </div>
        <div class="field">
            <div class="fieldLabel">
                <label><spring:message code="label.state" /></label>
            </div>
            <div class="fieldValue">
                <span class="searchOption"><form:radiobutton id="activationCodeStateAvailable" path="activationCodeState" value="AVAILABLE"/><label for="activationCodeStateAvailable"><spring:message code="label.available" /></span></label>
                <span class="searchOption"><form:radiobutton id="activationCodeStateUsed" path="activationCodeState" value="USED"/><label for="activationCodeStateUsed"><spring:message code="label.unavailable" /></span></label>
                <span class="searchOption"><form:radiobutton id="activationCodeStateAll" path="activationCodeState" value="ALL"/><label for="activationCodeStateAll"><spring:message code="label.all" /></span></label>
            </div>
        </div>
	</fieldset>
</div>
</form:form>
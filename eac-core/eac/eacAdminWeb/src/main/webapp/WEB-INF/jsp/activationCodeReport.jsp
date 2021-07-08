<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div>
	<div id="heading" class="ui-corner-top">
	   <h1><spring:message code="title.activationcodesearch" /></h1>
	</div>
	<c:if test="${successMsg ne null}">
	     <div class="success">
	        <spring:message code="${successMsg}"/>
	    </div>
	</c:if>	
    <fieldset>
        <legend><spring:message code="label.search.activationcodes"/></legend>
        <form:form modelAttribute="activationCodeCriteria" action="" id="reportCriteria">
        <div class="field">
            <div class="fieldLabel">
                <label for="code"><spring:message code="label.activationCode" text="Activation Code"/></label>
            </div>
            <div class="fieldValue">
                <form:input path="code" id="code"/>
            </div>
        </div> 
        <div class="field">
            <div class="fieldLabel">
                <label for="product"><spring:message code="label.product" /></label>
            </div>
            <div class="fieldValue" style="max-width: 60em">
                <tags:productFinder returnFieldId="reportProductId" initGuid="${activationCodeReportCriteria.productId}" resetButtonId="reset"/>
                <form:hidden path="productId" id="reportProductId"/>
                <input type="hidden" id="method" name="method" value="search"/>
            </div>
        </div>
        <div class="field">
            <div class="fieldLabel">
                <label for="product"><spring:message code="label.state" /></label>
            </div>
            <div class="fieldValue">
	            <span class="searchOption"><form:radiobutton id="activationCodeStateAvailable" path="activationCodeState" value="AVAILABLE"/><label for="searchProductName"><spring:message code="label.available" /></span></label>
	            <span class="searchOption"><form:radiobutton id="activationCodeStateUsed" path="activationCodeState" value="USED"/><label for="searchProductId"><spring:message code="label.used" /></span></label>
	            <span class="searchOption"><form:radiobutton id="activationCodeStateAll" path="activationCodeState" value="ALL"/><label for="searchExternalId"><spring:message code="label.all" /></span></label>
	        </div>
        </div>
        </form:form>
	    <div id="buttons">
	        <button type="button" id="report"><spring:message code="button.search" /></button>
	        <button type="button" id="reset"><spring:message code="button.reset" /></button>
	        <script type="text/javascript">
	        
	            $('#reset').click(function() {
	                $('#reportProductId').val('');
	                $('#"code"').val('');
	            });       
	                
	            $('#report').click(function() {
	                $('#report').attr('disabled', 'disabled');
	                $('#reportDetails').empty().html('<div style="text-align:center; margin-top: 40px;"><img src="<c:url value="/images/progress-indicator-large.gif"/>"/></div>');
	                $.ajax({
	                    url: window.location.href,
	                    type: 'POST',
	                    data: $('#reportCriteria').serialize()
	                }).done(function(html) {
	                    $('#report').removeAttr('disabled');
	                    setTimeout(function() {
	                        $('#reportDetails').empty().html(html);
	                    }, 500);
	                });
	            });
	
	        </script>
	    </div>
    </fieldset>
</div>
<div id="reportDetails">

</div>
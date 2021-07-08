<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div>
	<div id="heading" class="ui-corner-top">
	   <h1><spring:message code="title.activationcodereport" /></h1>
	</div>
	<c:if test="${successMsg ne null}">
	     <div class="success">
	        <spring:message code="${successMsg}"/>
	    </div>
	</c:if>	
    <fieldset>
        <legend><spring:message code="label.report.registrations"/></legend>
        <form:form modelAttribute="activationCodeReportCriteria" action="" id="reportCriteria">
        <div class="field">
            <div class="fieldLabel">
            	<label for="division"><spring:message code="label.division"/></label>
            </div>
            <div class="fieldValue">
                <form:select id="division" path="divisionId" >
                    <form:option value="" label=""/>
                    <c:forEach var="division" items="${availableDivisions}">
                        <form:option value="${division.erightsId}" label="${division.divisionType}"/>
                    </c:forEach>
                </form:select>  
            </div>
        </div>
        <div class="field">
			<div class="fieldLabel">
				<label for="platform"><spring:message code="label.platform.code"/></label>
			</div>
			<div class="fieldValue">
				<form:select id="platform" path="platformCode" >
					<form:option value="" label=""/>
					<c:forEach var="platform" items="${availablePlatforms}">
						<form:option value="${platform.code}" label="${platform.code}"/>
					</c:forEach>
				</form:select>	
			</div>
		</div>
        <div class="field">
            <div class="fieldLabel">
                <label for="product"><spring:message code="label.product" /></label>
            </div>
            <!-- Rishit: start added for product group -->
            <div class="fieldValue" style="max-width: 60em" id="productDiv">
                <tags:productFinder returnFieldId="reportProductId" initGuid="${activationCodeReportCriteria.productId}" resetButtonId="reset"/>
                <form:hidden path="productId" id="reportProductId"/>
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
        <!-- Rishit: end added for product group -->
        <div class="field">
            <div class="fieldLabel">
                <label for="customer">Batch Id</label>
            </div>
            <div class="fieldValue">
                <form:input path="batchId" id="batchId"/>
            </div>
        </div>
        <div class="field">
            <div class="fieldLabel">
                <label for="maxResults"><spring:message code="label.maxresults" /></label>
            </div>
            <div class="fieldValue">
                <form:select id="maxResults" path="maxResults" cssStyle="width:auto">
                    <form:option value="100">100</form:option>
                    <form:option value="1000">1000</form:option>
                    <form:option value="5000">5000</form:option>
                    <form:option value="10000">10000</form:option>
                    <security:authorize ifAllGranted="ROLE_EXTRACT_LARGER_REPORT">
                        <form:option value="15000">15000</form:option>
                        <form:option value="20000">20000</form:option>
                    </security:authorize>
                </form:select>
                <input type="hidden" id="eventId" name="_eventId" value="report"/>
            </div>
        </div>
        </form:form>
    </fieldset>
    <div id="buttons">
        <button type="button" id="report"><spring:message code="button.report" /></button>
        <button type="button" id="reset"><spring:message code="button.reset" /></button>
        <script type="text/javascript">
        
	        $('#reset').click(function() {
	        	$('#reportProductId').val('');
	        	$('#batchId').val('');
	        	$('#maxResults').val(100);
	        	$('#reportDetails').empty();
	        	$('#division').val('');
	        	$('#platform').val('');
	        });       
                
            $('#report').click(function() {
            	$('#report').attr('disabled', 'disabled');
            	$('#reportDetails').empty().html('<div style="text-align:center; margin-top: 40px;"><img src="<c:url value="/images/progress-indicator-large.gif"/>"/></div>');
                $('#eventId').val('report');
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
</div>
<div id="reportDetails">

</div>
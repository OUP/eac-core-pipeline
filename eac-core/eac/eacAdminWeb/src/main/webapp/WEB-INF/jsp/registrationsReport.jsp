<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div>
	<div id="heading" class="ui-corner-top">
	   <h1><spring:message code="title.report" /></h1>
	</div>
	<c:if test="${successMsg ne null}">
	     <div class="success">
	        <spring:message code="${successMsg}"/>
	    </div>
	</c:if>	
	<div id="help">
		Note: Atleast one filter option is mandatory other than Registration State and Registration Type.
		<br/>
		<br/>
		<%-- The following field support partial matching:
		 <ul>
			<li><spring:message code="label.batchname" /></li>
			<li><spring:message code="label.activationcode" /></li>
		</ul> --%>
	</div>
    <fieldset>
        <legend><spring:message code="label.report.registrations"/></legend>
        <form:form modelAttribute="reportCriteria" action="" id="reportCriteria">
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
            <div class="fieldValue" style="max-width: 60em">
                <tags:productFinder returnFieldId="reportProductId" initGuid="${reportCriteria.productId}" resetButtonId="reset"/>
                <form:hidden path="productId" id="reportProductId"/>
            </div>
        </div>
        <div class="field">
            <div class="fieldLabel">
                <label for="customer"><spring:message code="label.customer" /></label>
            </div>
            <div class="fieldValue">
                <form:input path="customerUsername" id="customer"/>
            </div>
            <spring:message code="info.report.username" var="usernameHelp"/>
            <img id="usernameHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="${usernameHelp}"/>
            <script type="text/javascript">
                $(function() {
                    $("#usernameHelp").tipTip({edgeOffset: 7});
                });
            </script>
        </div>
        <div class="field">
            <div class="fieldLabel">
                <label for="externalProductId"><spring:message code="label.externalSystem" /></label>
            </div>
            <div class="fieldValue">
                <form:select id="externalProductId" path="externalProductId" >
                    <form:option value=""><spring:message code="label.noneSelected" /></form:option>
                    <c:forEach var="externalSystem" items="${externalSystems}">
                        <form:option value="${externalSystem.name}"><c:out value="${externalSystem.name}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>            
        </div>
        <c:set var="selectedSystemType" value="${reportCriteria.productExternalSystemIdType}" />
        
        <div class="field">
            <div class="fieldLabel">
                <label for="productExternalSystemIdType"><spring:message code="label.externalIds.externalSystemType" /></label>
            </div>
            <div class="fieldValue">
                 <form:select id="productExternalSystemIdType" path="productExternalSystemIdType" style="width:auto">
                    <form:option value=""><spring:message code="label.noneSelected" /></form:option>
                </form:select>
            </div>            
        </div>
        <div class="field">
            <div class="fieldLabel">
                <label for="Locale"><spring:message code="label.locale" /></label>
            </div>
            <div class="fieldValue">
                <form:select id="Locale" path="locale" >
                    <form:option value=""><spring:message code="label.noneSelected" /></form:option>
                    <c:forEach var="localeItem" items="${locales}">
                        <form:option value="${localeItem}"><c:out value="${localeItem.displayName}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>
        <div class="field">
            <div class="fieldLabel">
                <label for="customerTimeZone"><spring:message code="label.timeZone" /></label>
            </div>
            <div class="fieldValue">
                <form:select id="customerTimeZone" path="timeZone" >
                    <form:option value=""><spring:message code="label.noneSelected" /></form:option>
                    <c:forEach var="timeZoneId" items="${timezones}">
                        <form:option value="${timeZoneId}"><c:out value="${timeZoneId}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>        
        <div class="field">
            <div class="fieldLabel">
                <label for="customerEmailVerified"><spring:message code="label.emailVerification" /></label>
            </div>
            <div class="fieldValue">
                <form:select id="customerEmailVerified" path="eMailVarificationState">
                 <form:option value=""><spring:message code="label.noneSelected" /></form:option>
                    <c:forEach var="emailVerificationState" items="${emailVerificationStates}">
                        <spring:message code="label.email.verification.${emailVerificationState}" var="emailVerificationStateLabel"/>
                        <form:option value="${emailVerificationState}" label="${emailVerificationStateLabel}"/>
                    </c:forEach>
                </form:select>
            </div>
        </div>
        <div class="field">
            <div class="fieldLabel">
                <label><spring:message code="label.registrationState" /></label>
            </div>
            <div class="fieldValue" style="max-width:36em">
                <form:checkbox id="pending" path="pending"/> <label for="pending">Pending</label> 
                <form:checkbox id="activated" path="activated"/> <label for="activated">Activated</label> 
                <form:checkbox id="denied" path="denied"/> <label for="denied">Denied</label>
                <form:checkbox id="expired" path="expired"/> <label for="expired">Expired</label>
                <form:checkbox id="disabled" path="disabled"/> <label for="disabled">Disabled</label>
            </div>
        </div>
        <div class="field">
            <div class="fieldLabel">
                <label><spring:message code="label.registrationType" /></label>
            </div>
            <div class="fieldValue">
                <form:radiobutton id="registrationSelectionActivationCode" path="registrationSelectionType" value="ACTIVATION_CODE" />  <label for="registrationSelectionActivationCode">Activation Code</label> 
                <form:radiobutton id="registrationSelectionProduct" path="registrationSelectionType" value="PRODUCT" />  <label for="registrationSelectionProduct">Product</label> 
                <form:radiobutton id="registrationSelectionAll" path="registrationSelectionType" value="ALL" />  <label for="registrationSelectionAll">All</label> 
            </div>
        </div>
        <fieldset style="clear:both; padding-top: 0px\9; margin-bottom: 20px\9; width: 67%;">
            <legend><spring:message code="label.customer.last.login.date" /></legend>
        <div class="field">
            <div class="fieldLabel" style="width: 75px; padding-left:30px;">
                <label for="lastLoginFromDate"><spring:message code="label.from.date" /></label>
            </div>
            <div class="fieldValue">
                <form:input id="lastLoginFromDate" path="lastLoginFromDate" style="width:90px;"/>
            </div>
            <script type="text/javascript">
                $('#lastLoginFromDate').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
            </script>
    
            <div class="fieldLabel" style="width: 60px; padding-left:55px;">
                <label for="lastLoginToDate"><spring:message code="label.to.date" /></label>
            </div>
            <div class="fieldValue">
                <form:input id="lastLoginToDate" path="lastLoginToDate" style="width:90px;"/>
            </div>
            <script type="text/javascript">
                $('#lastLoginToDate').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
            </script>
        </div>
        </fieldset>
        
        <fieldset style="clear:both; padding-top: 0px\9; margin-bottom: 20px\9; width: 67%;">
            <legend><spring:message code="label.customer.created.date" /></legend>
        <div class="field">
            <div class="fieldLabel" style="width: 75px; padding-left:30px;">
                <label for="customerCreatedFromDate"><spring:message code="label.from.date" /></label>
            </div>
            <div class="fieldValue">
                <form:input id="customerCreatedFromDate" path="customerCreatedFromDate" style="width:90px;"/>
            </div>
            <script type="text/javascript">
                $('#customerCreatedFromDate').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
            </script>
    
            <div class="fieldLabel" style="width: 60px; padding-left:55px;">
                <label for="customerCreatedToDate"><spring:message code="label.to.date" /></label>
            </div>
            <div class="fieldValue">
                <form:input id="customerCreatedToDate" path="customerCreatedToDate" style="width:90px;"/>
            </div>
            <script type="text/javascript">
                $('#customerCreatedToDate').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
            </script>
        </div>
        </fieldset>
        
        <fieldset style="clear:both; padding-top: 0px\9; margin-bottom: 20px\9; width: 67%;">
            <legend><spring:message code="label.registration.created.date" /></legend>
        <div class="field">
            <div class="fieldLabel" style="width: 75px; padding-left:30px;">
                <label for="RegistrationCreatedFromDate"><spring:message code="label.from.date" /></label>
            </div>
            <div class="fieldValue">
                <form:input id="RegistrationCreatedFromDate" path="RegistrationCreatedFromDate" style="width:90px;"/>
            </div>
            <script type="text/javascript">
                $('#RegistrationCreatedFromDate').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
            </script>
    
            <div class="fieldLabel" style="width: 60px; padding-left:55px;">
                <label for="RegistrationCreatedToDate"><spring:message code="label.to.date" /></label>
            </div>
            <div class="fieldValue">
                <form:input id="RegistrationCreatedToDate" path="RegistrationCreatedToDate" style="width:90px;"/>
            </div>
            <script type="text/javascript">
                $('#RegistrationCreatedToDate').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
            </script>
        </div>
        </fieldset>
        
        <fieldset style="clear:both; padding-top: 0px\9; margin-bottom: 20px\9; width: 67%;">
            <legend><spring:message code="label.registration.updated.date" /></legend>
        <div class="field">
            <div class="fieldLabel" style="width: 75px; padding-left:30px;">
                <label for="fromDate"><spring:message code="label.from.date" /></label>
            </div>
            <div class="fieldValue">
                <form:input id="fromDate" path="fromDate" style="width:90px;"/>
            </div>
            <script type="text/javascript">
                $('#fromDate').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
            </script>
    
            <div class="fieldLabel" style="width: 60px; padding-left:55px;">
                <label for="toDate"><spring:message code="label.to.date" /></label>
            </div>
            <div class="fieldValue">
                <form:input id="toDate" path="toDate" style="width:90px;"/>
            </div>
            <script type="text/javascript">
                $('#toDate').datepicker({ dateFormat: 'dd/mm/yy', showAnim: ''}).attr('readOnly','readOnly');
            </script>
        </div>
        </fieldset>
           
         <!-- 
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
         -->
         <input type="hidden" id="eventId" name="_eventId" value="report"/>
         <input type="hidden" id="startIndex" name="startIndex" value="0"/>
         <input type="hidden" id="maxResults" name="maxResults" value="0"/>
        </form:form>
    </fieldset>
    <div id="buttons">
        <button type="button" id="report"><spring:message code="button.report" /></button>
        <button type="button" id="reset"><spring:message code="button.reset" /></button>
        <script type="text/javascript">
        
       	$(document).ready(function(){
       		
       		var $selectedSystemType = '${selectedSystemType}';
			
	        $('#externalProductId').change(function() {
				var externalSys = $(this).find('option:selected').val();	        	
	        	/*  $("#productExternalSystemIdType option"); */
				var option_array = document.getElementById("productExternalSystemIdType").options;
				while(option_array[1]){
					option_array.remove(1);
				}
        		if(externalSys != ''){
	        		$('#productExternalSystemIdType').removeAttr('disabled');
	        		$.ajax({
						url: '<c:url value="/mvc/reports/registrations/changeExternalSystem.htm"/>',
						type: 'GET',
						data: "_eventId=changeExternalSystem&externalSystem=" + externalSys
					}).done(function(response) {
						$.each(response, function(index, value){
							$('#productExternalSystemIdType').append('<option value="' + value + '">' + value + '</option');
						});
						$('#productExternalSystemIdType option[value='+$selectedSystemType+']').prop('selected',true);
						$selectedSystemType ='';
					});
	        	}else{
					$('#productExternalSystemIdType').attr('disabled', 'disabled');
	        	}
	        });
	        $('#externalProductId').change();
       	});
       	
       	//$('#productExternalSystemIdType').attr('disabled', 'disabled');
       	
	        $('#reset').click(function() {
	        	$('#division').val('');
	        	$('#platform').val('');
	        	$('#reportProductId').val('');
	        	$('#customer').val('');
	        	$('#Locale').val('');
	        	$('#customerTimeZone').val('');
	        	$('#customerEmailVerified').val('');
	        	$('#externalProductId').val('');
	        	
	        	var option_array = document.getElementById("productExternalSystemIdType").options;
				while(option_array[1]){
					option_array.remove(1);
				}
				$('#productExternalSystemIdType').attr('disabled', 'disabled');
	            $('#lastLoginFromDate').val('');
	            $('#lastLoginToDate').val('');
	                
	            $('#customerCreatedFromDate').val('');
	            $('#customerCreatedToDate').val('');
	                
	            $('#RegistrationCreatedFromDate').val('');
	            $('#RegistrationCreatedToDate').val('');

	        	$('#fromDate').val('');
	        	$('#toDate').val('');
	        	//$('#maxResults').val(100);
	        	$('#pending').attr('checked', true);
	        	$('#activated').attr('checked', true);
	        	$('#denied').attr('checked', true);
	        	$('#expired').attr('checked', true);
	        	$('#disabled').attr('checked', true);
	        	$('#registrationSelectionActivationCode').attr('checked', false);
	        	$('#registrationSelectionProduct').attr('checked', false);
	        	$('#registrationSelectionAll').attr('checked', true);
	        	$('#reportDetails').empty();
	        	$('.success').hide();
	        });
                
	        
	        
            $('#report').click(function() {
            	if(!$('#pending').prop('checked') && !$('#activated').prop('checked') && !$('#denied').prop('checked') && !$('#expired').prop('checked') && !$('#disabled').prop('checked')) {
            		alert('Please choose at least one or more states of pending, activated, denied, expired and disabled.');
            		return;
            	}
            	if(($('#division').val()=="") && ($('#reportProductId').val()=="") && ($('#customer').val()=="") && 
                    	($('#Locale').val()=="") && ($('#customerTimeZone').val()=="") && ($('#customerEmailVerified').val()=="") && ($('#lastLoginToDate').val()=="") 
                    	&& ($('#lastLoginFromDate').val()=="") && ($('#externalProductId').val()=="") && ($('#customerCreatedFromDate').val()=="")&& ($('#customerCreatedToDate').val()=="")
                    	&& ($('#RegistrationCreatedFromDate').val()=="")&& ($('#RegistrationCreatedToDate').val()=="") && ($('#fromDate').val()=="")&& ($('#toDate').val()=="") && ($('#platform').val()==""))	{
            		$('.success').remove();
 					$('.error').remove();
            		$("<div class='error'><span><spring:message code='registration.report.code.error' text='Please select atleast one filter option other than Registration State and Registration Type'/></span><br></div>").insertAfter("#heading");
                }

            	 else {
            		$('.success').remove();
 					$('.error').remove();
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
            	}
            });

        </script>
    </div>
</div>
<div id="reportDetails">

</div>
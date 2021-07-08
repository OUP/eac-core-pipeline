<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="customerTile">
	<fieldset>
        <%-- <c:if test="${editMode}">
	        <div class="field">
	            <div class="fieldLabel">
	                <label><spring:message code="label.id" /></label>
	            </div>
	            <div class="fieldValue"> 	                
	                <c:choose>
	                    <c:when test="${customerbean.customer.id != 0 }">${customerBean.customer.id}</c:when>
	                    <c:otherwise><spring:message code="label.customerNotFound" /></c:otherwise>
	                </c:choose>
	            </div>
	        </div>
        </c:if> --%>
		<div class="field">
			<div class="fieldLabel">
				<label for="customerType"><spring:message code="label.type" /></label>
			</div>
			<div class="fieldValue">
				<form:select id="customerType" path="customer.customerType" >
					<c:forEach var="customerType" items="${customerTypes}">
						<spring:message code="label.customerType.${customerType}" var="customerTypeLabel"/>
						<form:option value="${customerType}" label="${customerTypeLabel}"/>
					</c:forEach>
				</form:select>
			</div>
			<div id="userConcurrencyField">
                <div class="fieldLabel" style="width: 45px; padding-left:45px;">
                    <label for="concurrencyValue"><spring:message code="label.concurrencyValue" /></label>
                </div>
                <div class="fieldValue">
                	<c:set var="canSetUserConcurrency" value="${false}" />
                	<form:input id="concurrencyValue" path="concurrencyValue" disabled="true" maxlength="9" style="width:70px;"/>
                	<security:authorize ifAllGranted="ROLE_CAN_SET_USER_CONCURRENCY">
                     	<c:set var="canSetUserConcurrency" value="${true}" />
                    </security:authorize>
					<%-- <input type="hidden" value="${canUpdate}" id="canSetUserConcurrency"/> --%>
                </div>
            </div>
		</div>
		<form:hidden path="defaultConcurrencyValue" id="defaultConcurrencyValue" />
		<div class="field">
			<div class="fieldLabel">
				<label for="customerFirstName"><spring:message code="label.firstName" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="customerFirstName" path="customer.firstName" maxlength="255" />
			</div>
		</div> 
		<div class="field">
			<div class="fieldLabel">
				<label for="customerFamilyName"><spring:message code="label.familyName" /></label>
			</div>
			<div class="fieldValue">
				<form:input id="customerFamilyName" path="customer.familyName" maxlength="255" />
			</div>
		</div> 
		<div class="field">
			<div class="fieldLabel">
				<label for="customerEmailAddress"><spring:message code="label.emailAddress" />&#160;<span class="mandatory">*</span></label>
			</div>
			<div class="fieldValue">
				<form:input id="customerEmailAddress" path="customer.emailAddress" maxlength="255" />
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="customerLocale"><spring:message code="label.locale" /></label>
			</div>
			<div class="fieldValue">
				<form:select id="customerLocale" path="customer.locale" >
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
				<form:select id="customerTimeZone" path="customer.timeZone" >
					<form:option value=""><spring:message code="label.noneSelected" /></form:option>
					<c:forEach var="timeZoneId" items="${timezones}">
						<form:option value="${timeZoneId}"><c:out value="${timeZoneId}"/></form:option>
					</c:forEach>
				</form:select>
			</div>
		</div>
		<c:if test="${editMode}">
			<div class="field">
				<div class="fieldLabel">
					<label for="customerLocked"><spring:message code="label.locked" /></label>
				</div>
				<div class="fieldValue">
					<form:checkbox id="customerLocked" path="customer.locked" cssClass="checkbox"/>
				</div>
			</div>
		</c:if>
		<div class="field">
			<div class="fieldLabel">
				<label for="customerResetPassword"><spring:message code="label.resetPassword" /></label>
			</div>
			<div class="fieldValue">
				<form:checkbox id="customerResetPassword" path="customer.resetPassword" cssClass="checkbox"/>
			</div>
		</div>
		<div class="field">
			<div class="fieldLabel">
				<label for="customerEmailVerified"><spring:message code="label.registration.validated" /></label>
			</div>
			<div class="fieldValue">
                <form:select id="customerEmailVerified" path="customer.emailVerificationState" cssClass="customerSelect">
                    <c:forEach var="emailVerificationState" items="${emailVerificationStates}">
                        <spring:message code="label.email.verification.${emailVerificationState}" var="emailVerificationStateLabel"/>
                        <form:option value="${emailVerificationState}" label="${emailVerificationStateLabel}"/>
                    </c:forEach>
                </form:select>
			</div>
		</div>
		<c:if test="${editMode}">
			<div class="field">
				<div class="fieldLabel">
					<label for="customerLastLoginDateTime"><spring:message code="label.lastLoginDateTime" /></label>
				</div>
				<div class="fieldValue">
					<span id="customerLastLoginDateTime"><joda:format value="${customerBean.customer.lastLoginDateTime}" style="SM" dateTimeZone="UTC" /></span>
				</div>
			</div>
			<div class="field">
				<div class="fieldLabel">
					<label for="customerFailedAttempts"><spring:message code="label.failedAttempts" /></label>
				</div>
				<div class="fieldValue">
					<span id="customerFailedAttempts"><c:out value="${customerBean.customer.failedAttempts}"/></span>
				</div>
			</div>
			<div class="field">
                <div class="fieldLabel">                
                    <label for="loggedInSessions" id=lblLoggedInSessions><spring:message code="label.logged.in.session" /></label>
                </div>
                <div class="fieldValue" style="width: 51em; max-width:52em;">
                    <span id="loggedInSessions">
                        <c:forEach var="listItems" items="${customerBean.customer.sessions}">
                        <div style="margin-bottom: 30px;">
                        	<div style="max-width:39em; float: left;">
                        		<c:out value="${listItems}" />
                        	</div>
                        	<security:authorize ifAllGranted="ROLE_KILL_USER_SESSION">  
                        		<div id="killUserSession" class="killUserSessionClass">
                        			<a class="actionLink killSessionLink" href="${flowExecutionUrl}&amp;_eventId=killUserSession&amp;sessionId=${listItems}"><button type="button" id="delete"><spring:message code="button.kill.user.session"/></button></a>
                        		</div>
                        	</security:authorize>
                        </br></br>
                        </div>
                        </c:forEach>
                    </span>
                </div>               
            </div>
            <%--  <security:authorize ifAllGranted="ROLE_KILL_USER_SESSION">
                <div id="buttons">
                    <p><button type="button" id="killUserSession" name="_eventId_killUserSession"><spring:message code="button.kill.user.session" /></button></p>
                    <a class="actionLink deleteProductLink" href="<c:url value="/mvc/customer/killSession.htm" />?id=${flowExecutionKey}"><button type="button" id="delete"><spring:message code="button.delete"/></button></a>
                </div>            
            </security:authorize>  --%>
             
		</c:if>
	</fieldset>	
</div>
  <spring:message code="confirm.title.kill.session" text="" var="message" />
	<script type="text/javascript">
	$(document).ready(function() {
		$('#customerTile').undelegate('a.killSessionLink', 'click');/* was getting 2 button clicks */
		$('#customerTile').delegate('a.killSessionLink', 'click', eacConfirm({title:'${message}', width: 700}));
	});
	</script>
<script type="text/javascript">
	$(function() {
		// This has been changed to link
		/* $('button[id="killUserSession"]').click(function(){
			$(this).prop('type', 'submit');
			$('this .default').click();			 
		}); */
		var $canSetUserConcurrency = '${canSetUserConcurrency}';
		
		var $defConcurrencyVal = $('#defaultConcurrencyValue').val();
		
		$('#customerType').change(function() {
			if ($(this).val() == 'SHARED') {
				$('#customerResetPassword').removeAttr('checked');
				$('#customerResetPassword').attr('disabled', 'disabled');
				if ($('#generatePassword:checked').length > 0) {
					$('#generatePassword').removeAttr('checked');
					//disableChangeUsernamePass(false);
				}
				$('#generatePassword').attr('disabled', 'disabled');				
	            $('#lblLoggedInSessions').text('<spring:message code="label.logged.in.sessions" />');
	            $('#userConcurrencyField').hide();
			} else if($(this).val() == 'SELF_SERVICE' || $(this).val() == 'SPECIFIC_CONCURRENCY'){
				$('#customerResetPassword').removeAttr('disabled');
				$('#generatePassword').removeAttr('disabled');				
	            $('#lblLoggedInSessions').text('<spring:message code="label.logged.in.session" />');
	            $('#concurrencyValue').attr('disabled', 'disabled');
	            $('#concurrencyValue').val($defConcurrencyVal);
	            /* $('#userConcurrencyField').hide(); */
	            $('#userConcurrencyField').show();
	            if($('#customerType').val() == 'SPECIFIC_CONCURRENCY'){
	            	if($canSetUserConcurrency == 'true'){
	            		$('#concurrencyValue').removeAttr('disabled');
	            	}
                }               
			}			
		});
		
		
		if($('#customerType').val() == 'SELF_SERVICE'  || $('#customerType').val() == 'SPECIFIC_CONCURRENCY'){
			$('#lblLoggedInSessions').text('<spring:message code="label.logged.in.session" />');
			$('.killUserSessionClass').show();
			//$('#concurrencyValue').attr('disabled', 'disabled');
		   /*  $('#userConcurrencyField').hide(); */
		    if($('#customerType').val() == 'SPECIFIC_CONCURRENCY'){
               // $('#userConcurrencyField').show();
		    	if($canSetUserConcurrency == 'true'){
            		$('#concurrencyValue').removeAttr('disabled');
            	}
				if($('#concurrencyValue').val() == "" || $('#concurrencyValue').val() == "-1" ){
                    $('#concurrencyValue').val($defConcurrencyVal);
                }
               // $('.killUserSessionClass').hide();
            }
        }else if($('#customerType').val() == 'SHARED') {
        	$('#customerResetPassword').attr('disabled', 'disabled');
            $('#generatePassword').attr('disabled', 'disabled');        
            $('#lblLoggedInSessions').text('<spring:message code="label.logged.in.sessions" />');
        	$('.killUserSessionClass').hide();
            $('#userConcurrencyField').hide();            
        } 
    
	    if("${customerBean.customer.sessions[0]}" == '-'){
	        $('.killUserSessionClass').hide();
        }
	    
	   
	    /* if("${fn:length(customerBean.customer.sessions)}" > 1 ){

            $('#killUserSession').hide();
        } */
	    	
		
		var editMode = ${editMode};
		if (!editMode) {
	       	var $email    = $('#customerEmailAddress');
			var $username = $('#customerUsername');
	    	var usernameModified = false;
	    	
			$email.keyup(function() {
				if (!usernameModified) {
					$username.val($email.val());
				}
			});
			
			$email.change(function() {
				if (!usernameModified) {
					$username.val($email.val());
				}
			});
			
			$username.keyup(function() {
	    		if ($username.val() != '') {
	    			usernameModified = true;
	    		} else {
	    			usernameModified = false;
	    		}
			});   
		}
	});
	
</script>

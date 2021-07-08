<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<input type="hidden" name="checkboxClicked" id="checkboxClicked" value="false"/>
    <div class="fieldCompact">
    	<div class="fieldLabelNarrow">
    		<label for="regCreatedDate-${registration.id}"><spring:message code="label.registration.created" /></label>
    	</div>
    	<div class="fieldValue">
    		<span id="regCreatedDate-${registration.id}"><joda:format value="${registration.createdDate}" pattern="dd/MM/yyyy" dateTimeZone="UTC" /></span>
    	</div>
    </div>
    <div class="fieldCompact">
    	<div class="fieldLabelNarrow">
    		<label for="regUpdatedDate-${registration.id}"><spring:message code="label.registration.updated" /></label>
    	</div>
    	<div class="fieldValue">
    		<span id="regUpdatedDate-${registration.id}"><joda:format value="${registration.updatedDate}" pattern="dd/MM/yyyy" dateTimeZone="UTC" /></span>
    	</div>
    </div>
    <div class="fieldCompact">
    	<div class="fieldLabelNarrow">
    		<label for="regType-${registration.id}"><spring:message code="label.registration.type" /></label>
    	</div>
    	<div class="fieldValue" style="margin-bottom:5px">
    		<c:set var="registrationType" value="${registration.registrationType}"/>    		
    		<span id="regType-${registration.id}"><spring:message code="label.registration.type.${registrationType}" /></span>
    	</div>
    </div>
    <div class="editAnswersPopupContainer">
    	<div style="margin-top: 15px; margin-bottom:15px; margin-bottom:0px\9">
    		<c:if test="${not empty registration.registrationDefinition.pageDefinition}">
    			<img src="${editIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a id="${registration.id}" class="editAnswersLink" href="#"><spring:message code="label.registration.editAnswers" /></a>
    		</c:if>
    	</div>
    	<div id="${registration.id}" class="editAnswersPopup" title="${editRegistrationAnswersTitle}"></div>
    </div>
    <fieldset>
    	<legend id="legend-${registration.id}"><spring:message code="title.registration.stateFinalised" /></legend>
    	<div class="fieldCompact">
    		<div class="fieldLabelNarrow">
    			<label for="regCompleted-${registration.id}"><spring:message code="label.registration.completed" /></label>
    		</div>
    		<div class="fieldValue">
    			<span id="regCompleted-${registration.id}"><spring:message code="label.${registration.completed}" /></span>
    			
    		</div>
    	</div>    	
    	<div>
    		<c:set var="registrationType" value="${registration.registrationDefinition.registrationActivation.name}"/>
    		<c:choose>
    			<c:when test="${registrationType == 'SelfRegistrationActivation'}">    	
        <c:choose>
        	<c:when test="${registration.activated}">
        		<div class="fieldCompact">
        			<div class="fieldLabelNarrow">
            <label for="regActivated-${registration.id}"><spring:message code="label.registration.activated" /></label>
        			</div>
        			<div class="fieldValue">
            <span id="regActivated-${registration.id}"><spring:message code="label.${registration.activated}" /></span>
        			</div>
        		</div>
        	</c:when>
        	<c:otherwise>
        		<div class="fieldCompact">
        			<div class="fieldLabelNarrow">
            <label for="regActivate-${registration.id}"><spring:message code="label.registration.activate" /></label>
        			</div>		
        			<div class="fieldValue">        
            <form:checkbox id="regActivate-${registration.id}" path="registrationStateMap[${registration.id}].activate" cssClass="checkbox"/>
            <form:hidden id="regActivate-${registration.id}-allowDenySelected" path="registrationStateMap[${registration.id}].allowDenySelected"/>
        			</div>
        			<spring:message code="info.registration.activate" var="regActivateHelp" />
        			<img id="regActivate-${registration.id}-help" class="infoIcon" src="<c:url value="/images/information.png" />" title="${regActivateHelp}"/>        			
        			<script type="text/javascript">
            $("#regActivate-${registration.id}").click(function() {
            	if ($(this).attr('checked') == 'checked') {
            		$('#regSendEmail-${registration.id}-div').show();
            		$('#regSendEmail-${registration.id}').attr('checked',true);
            		$('#regActivate-${registration.id}-allowDenySelected').val("Y");
            	} else {
            		$('#regSendEmail-${registration.id}-div').hide();
            		$('#regSendEmail-${registration.id}').attr('checked',false);
            		$('#regActivate-${registration.id}-allowDenySelected').val("");
            	}
            });
            $("#regActivate-${registration.id}-help").tipTip({edgeOffset: 7});
            $(function() {
            	$('#legend-${registration.id}').text('<spring:message code="title.registration.stateAwaitingActivation" />');
            });              
        			</script>
        		</div>
        	</c:otherwise>
        </c:choose>
    			</c:when>
    			<c:when test="${registrationType == 'ValidatedRegistrationActivation'}">
        <c:choose>
        	<c:when test="${registration.activated or registration.denied}">
        		<div class="fieldCompact">
        			<div class="fieldLabelNarrow">
            <label for="regDenied-${registration.id}"><spring:message code="label.registration.denied" /></label>
        			</div>			
        			<div class="fieldValue">
            <span id="regDenied-${registration.id}"><spring:message code="label.${registration.denied}" /></span>
             <!-- CR15 -->
            <label for="regDeniedAllow-${registration.id}">
													<c:if test="${registration.denied}">
														<spring:message code="label.registration.denyToggle" />
														<form:checkbox id="regDeniedAllow-${registration.id}"  path="registrationStateMap[${registration.id}].validate"  cssClass="checkbox" value="null"/>
														<form:hidden id="regDeniedAllowChckVal-${registration.id}" path="registrationStateMap[${registration.id}].allowDenySelected"/>
													</c:if>
													<c:if test="${not registration.denied}">
														<spring:message code="label.registration.allowToggle" />
														<form:checkbox id="regDeniedDeny-${registration.id}"  path="registrationStateMap[${registration.id}].validate"  cssClass="checkbox" value="null" />
														<form:hidden id="regDeniedDenyChckVal-${registration.id}" path="registrationStateMap[${registration.id}].allowDenySelected"/>
													</c:if>
            </label>
           						 <script type="text/javascript">
												$("#regDeniedAllow-${registration.id}").click(function() {
													if ( $("#regDeniedAllow-${registration.id}").is(':checked')) {
														$("#regDeniedAllow-${registration.id}").val(true);
														$("#regDeniedAllowChckVal-${registration.id}").val("Y");
														$("#checkboxClicked").val("true");
														$('#regSendEmail-${registration.id}').attr('checked',true);
														$('#regSendEmail-${registration.id}-regToggle').show();
													}else{
														$("#regDeniedAllowChckVal-${registration.id}").val("");
														$("#checkboxClicked").val("false");
														$('#regSendEmail-${registration.id}-regToggle').hide();
														$('#regSendEmail-${registration.id}').attr('checked',false);
													}
												});
												$("#regDeniedAllow-${registration.id}").val(null);
												$("#regDeniedDeny-${registration.id}").click(function() {
													if ( $("#regDeniedDeny-${registration.id}").is(':checked')) {
														 $("#regDeniedDenyChckVal-${registration.id}").val("Y");
														 $("#checkboxClicked").val("true");
														 $("#regDeniedDeny-${registration.id}").val(false);
														 $('#regSendEmail-${registration.id}').attr('checked',true);
														 $('#regSendEmail-${registration.id}-regToggle').show();
													}else{
														$("#regDeniedDenyChckVal-${registration.id}").val("");
														$("#checkboxClicked").val("false");
														$('#regSendEmail-${registration.id}-regToggle').hide();
														$('#regSendEmail-${registration.id}').attr('checked',false);
													}
												});
												$("#regDeniedDeny-${registration.id}").val(null);
										</script>
            <!--CR15  -->
        			</div>	
        		</div>
        	</c:when>
        	<c:otherwise>
        		<div class="fieldCompact">
        			<div class="fieldLabelNarrow">
            <label for="regValidated-${registration.id}"><spring:message code="label.registration.validate" /></label>
        			</div>			
        			<div class="fieldValue">
            <form:radiobutton id="regValidated-${registration.id}-1" class="radio" path="registrationStateMap[${registration.id}].validate" value="true"/><label for="regValidated-${registration.id}-1" style="font-weight: normal; display: inline; vertical-align: middle"><spring:message code="label.registration.allow" /></label>&#160;
            <form:hidden id="regValidated-${registration.id}-1-allowDenySelected" path="registrationStateMap[${registration.id}].allowDenySelected"/>
            <form:radiobutton id="regValidated-${registration.id}-2" class="radio" path="registrationStateMap[${registration.id}].validate" value="false"/><label for="regValidated-${registration.id}-2" style="font-weight: normal; display: inline; vertical-align: middle"><spring:message code="label.registration.deny" /></label>
            <form:hidden id="regValidated-${registration.id}-2-allowDenySelected" path="registrationStateMap[${registration.id}].allowDenySelected"/>
        			</div>		
        			<spring:message code="info.registration.validated" var="regValidatedHelp" />
        			<img id="regValidated-${registration.id}-help" class="infoIcon" src="<c:url value="/images/information.png"/>" title="${regValidatedHelp}"/>
        			<script type="text/javascript">
            $("#regValidated-${registration.id}-1").click(function() {
            	
            	if ($(this).attr('checked') == 'checked') {
            		$("#checkboxClicked").val("true");
            		$('#regSendEmail-${registration.id}-div').show();
            		$('#regSendEmail-${registration.id}').attr('checked',true);
            		$("#regValidated-${registration.id}-1-allowDenySelected").val("Y");
            	}else{
            		$("#regValidated-${registration.id}-1-allowDenySelected").val("");
            	}
            });
            $("#regValidated-${registration.id}-2").click(function() {
            
            	if ($(this).attr('checked') == 'checked') {
            		$("#checkboxClicked").val("true");
            		$('#regSendEmail-${registration.id}-div').show();
            		$('#regSendEmail-${registration.id}').attr('checked',true);
            		$('#regValidated-${registration.id}-2-allowDenySelected').val("Y");
            	}else{
            		$("#regValidated-${registration.id}-2-allowDenySelected").val("");
            	}
            });
            $("#regValidated-${registration.id}-help").tipTip({edgeOffset: 7});
            $(function() {
            	$('#legend-${registration.id}').text('<spring:message code="title.registration.stateAwaitingValidation" />');
            });	
        			</script>
        		</div>    	
        	</c:otherwise>
        </c:choose>
    			</c:when>
    		</c:choose>    		
    	</div>	
    	<c:if test="${(registrationType == 'SelfRegistrationActivation' and not registration.activated) or (registrationType == 'ValidatedRegistrationActivation' and (not registration.activated and not registration.denied))}">
    		<div id="regSendEmail-${registration.id}-div" class="fieldCompact">
    			<div class="fieldLabelNarrow">
        <label for="regSendEmail-${registration.id}"><spring:message code="label.registration.sendEmail" /></label>
    			</div>
    			<div class="fieldValue">
        <form:checkbox id="regSendEmail-${registration.id}" cssClass="checkbox" path="registrationStateMap[${registration.id}].sendEmail"/>
    			</div>
    		</div>	
    	</c:if>
    	<!-- CR15 -->
    	
    	<c:if test="${registrationType == 'ValidatedRegistrationActivation' and (registration.activated or registration.denied)}">
    		<div id="regSendEmail-${registration.id}-regToggle" class="fieldCompact" style="display: none;">
							<div class="fieldLabelNarrow">
								<label for="regSendEmail-${registration.id}"><spring:message code="label.registration.sendEmail" /></label>
							</div>
							<div class="fieldValue">
								<form:checkbox id="regSendEmail-${registration.id}" cssClass="checkbox" path="registrationStateMap[${registration.id}].sendEmail"/>
							</div>
					</div>	
			</c:if>
    		<!-- CR15 -->
    </fieldset> 
    <script>
	     var registrationIDD="${registration.id}";
    	 $('#regSendEmail-${registration.id}').attr('checked',false);
	</script>

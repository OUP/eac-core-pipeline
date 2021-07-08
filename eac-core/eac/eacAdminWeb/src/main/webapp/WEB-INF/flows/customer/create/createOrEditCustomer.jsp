<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="bodyTile">
	
	<script type="text/javascript">
	
	    $(document).ready(function() {
	        $( "#pagetabs" ).tabs({ cookie: { name: 'createOrEditCustomer', expires: 1 } });
	        
	        //Hide buttons after form has been submitted (setting disabled does not work)
	        $('form').submit(function(){
	            $('button[type=submit]', this).attr('style', 'display:none');
	        });

	        if($('a[href="#pagetab-3"]').css("cursor") == 'text') { 
	        	$("#buttons").hide();
	        }

	        // change update button event on tab change  
	        if($('a[href="#pagetab-1"]').css("cursor") == 'text') { 
	        	$("#edit").attr('name', '_eventId_editCustomer');
	        } else if ($('a[href="#pagetab-2"]').css("cursor") == 'text') { 
	        	$("#edit").attr('name', '_eventId_editCredentials');
	        } else if ($('a[href="#pagetab-4"]').css("cursor") == 'text') { 
	        	$("#edit").attr('name', '_eventId_editExternalIds');
	        }
        	
	        $('a[href="#pagetab-1"]').click(function(){
				$("#edit").attr('name', '_eventId_editCustomer');
				$("#buttons").show();
		        });
	        
	        $('a[href="#pagetab-2"]').click(function(){
				$("#edit").attr('name', '_eventId_editCredentials');
				$("#buttons").show();
		        });

	        $('a[href="#pagetab-4"]').click(function(){
				$("#edit").attr('name', '_eventId_editExternalIds');
				$("#buttons").show();
		        });

	        $('a[href="#pagetab-3"]').click(function(){
				$("#buttons").hide();
		        });
	    });
	    
	</script>
	
	<div style="margin-bottom: 8em\9">
		<div id="heading" class="ui-corner-top">
			<h1>
				<c:choose>
					<c:when test="${editMode}">
						<spring:message code="title.edit" />					
					</c:when>
					<c:otherwise>
						<spring:message code="title.create" />
					</c:otherwise>
				</c:choose>
			</h1>
		</div>

		<tiles:insertAttribute name="statusTile"/>
		
		<div>
			<form:form modelAttribute="customerBean" action="${flowExecutionUrl}" id="customerBean">
		        <div id="pagetabs">
				    <ul>
				        <li class="ui-state-hover"><a href="#pagetab-1"><spring:message code="tab.customer" /></a></li>
				        <li><a href="#pagetab-2"><spring:message code="tab.credentials" /></a></li>
				        <c:if test="${editMode}">
					    	<li><a href="#pagetab-3"><spring:message code="tab.registrations" /></a></li>
				    	</c:if>				  
				    	<li><a href="#pagetab-4"><spring:message code="tab.externalIds" /></a></li>
						<c:if test="${fn:length(customerBean.problemRegistrations) > 0 || fn:length(customerBean.problemtEntitlementGroups) > 0 }">
				    		<li><a href="#pagetab-5"><spring:message code="tab.problem.registrations" text="?tab.problem.registrations?"/></a></li>
				    	</c:if>
				    </ul>
				    <div id="pagetab-1" class="ui-tabs-hide">
    	    			<tiles:insertAttribute name="customerTile"/>
				    </div>
				    <div id="pagetab-2" class="ui-tabs-hide">
		                <tiles:insertAttribute name="credentialsTile"/>
				    </div>
				    <c:if test="${editMode}">
					    <div id="pagetab-3" class="ui-tabs-hide">
			                <tiles:insertAttribute name="registrationsTile"/>
					    </div>
				    </c:if>
				    <div id="pagetab-4" class="ui-tabs-hide">
		                <tiles:insertAttribute name="externalIdsTile"/>
				    </div>
				    <c:if test="${fn:length(customerBean.problemRegistrations) > 0 || fn:length(customerBean.problemtEntitlementGroups) > 0 }">
					    <div id="pagetab-5" class="ui-tabs-hide">
			                <tiles:insertAttribute name="problemRegistrationsTile"/>
					    </div>
				    </c:if>
		        </div>		    
		        <div id="buttons">
		        	<p>                 
			        	<c:choose>
			        		<c:when test="${editMode}">
			        			<button type="submit" id="edit" name="_eventId_editCustomer"><spring:message code="button.edit" /></button>
			        			<button type="submit" id="cancel" name="_eventId_cancel"><spring:message code="button.cancel" /></button>
			        		</c:when>
			        		<c:otherwise>
				        		<button type="submit" id="save" name="_eventId_save"><spring:message code="button.save" /></button>
			        		</c:otherwise>
			        	</c:choose>
		            </p>
		        </div>
	        </form:form>
		</div>
	</div>
</div>

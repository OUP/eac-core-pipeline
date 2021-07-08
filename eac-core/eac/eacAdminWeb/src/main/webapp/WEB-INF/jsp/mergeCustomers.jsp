<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:url value="/mvc/mergeCustomer/searchCustomers.htm" var="formUrl"/>
<c:url value="/mvc/mergeCustomer/merge.htm" var="mergeUrl"/>
<spring:message code="confirm.title.merge.customer" var="confirmMerge" text="" />
<div id="mergeCustomersTile">
	<div id="heading" class="ui-corner-top">
	   <h1><spring:message code="title.merge.customers" /></h1>
	</div>
	<c:set var="statusMessageKey" value="${param['statusMessageKey']}"/>
	<c:if test="${statusMessageKey ne null}">
		 <div class="success">
			<spring:message code="${statusMessageKey}"/>
		</div>
	</c:if>
	<c:set var="errorMessageKey" value="${param['errorMessageKey']}"/>
	<c:if test="${errorMessageKey ne null}">
		 <div class="error">
			<spring:message code="${errorMessageKey}"/>
		</div>
	</c:if>
	
	<div id="help">
	This facility provides you to search customers by email id for merging customer
	<ul>
		<li>Please select one master user</li>
		<li>All other users data and information is merged into master user</li>
	</ul>
	</div>
	
	 <fieldset>
        <legend><spring:message code="label.searchcustomersbyemail"/></legend>
			<form:form method="POST" action="${formUrl}" modelAttribute="mergeCustomerForm" id="mergeCustomerForm" enctype="multipart/form-data">
			<table>
			<tr>
			<td>
      		<div class="field">
				<div class="fieldLabel" style="width: 180px">
					<label for="emailId"><spring:message code="label.email" /></label>&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
					<form:input id="emailId" name="emailId" path="emailId"/>
				</div>
			</td>	
			<td>	 <div id="buttons">
					<button type="button" id="search">
						<spring:message code="button.search" />
					</button>
					<button type="button" id="reset">
						<spring:message code="button.reset" />
					</button>
				 </div> 
			</td>	 
			</div>
			</tr>
			</table>
		
       </form:form>
       <div id="mergefun">
     
       	<tiles:insertAttribute name="searchResultsMergeCustomersTile"/>
      
		</div>
    </fieldset>
    
    <script type="text/javascript">
    window.onload = function(e){ 
        $('#mergefun').hide(); 
       }
    $(function() {
		$('#emailId').focus();
	});
    $('input[type=radio][name=customerId]').on('change', function() {
        var str=($(this).val()).split(":");
        
    	$('#customerIdSelected').val(str[0]);
    	$('#customerNameSelected').val(str[1]);
     });
   
		$('#reset').click(function() {
			$('#emailId').val('');
			$('#searchResultsMergeCustomersTile').empty();
		});
		$('#cancel').click(function() {
			$('#searchResultsMergeCustomersTile').empty();
			//$('#buttons').show();
		});
		$('#search').click(function() {
			
			window.localStorage.setItem('emailId_val',  $('#emailId').val());
			var email=$('#emailId').val();
			var done = false;
			
			$.ajax({
				type: 'POST',
				url: '${formUrl}',
				data: 'emailId=' +email
			}).done(function(html) {
				$('#mergeCustomersTile').empty();
				$('#mergeCustomersTile').append(html);
				$('#emailId').val(window.localStorage.getItem('emailId_val'));
				//$('#buttons').hide();
				$('#mergefun').show();
				done=true;
			});
      		
      		
      		 setTimeout(function() {
      			if (!done) {
		      		$('#mergeCustomersTile').empty();
		      		$('#mergeCustomersTile').append('<div class="progressMessageContainer"><h2 class="progressMessage"><spring:message code="label.pleaseWait"/></h2><img src="<c:url value="/images/progress-indicator-large.gif"/>"/></div>');
      			}
      		}, 500); 
		});
		
		$('#merge').click(function(e){

			if (!($('input[name=customerId]:checked').length>0)) {
				 // no radio button was checked
	            alert("Please select one customer")
	            return false; // stop whatever action would normally happen
	       }
			var message1 = '<spring:message code="button.confirmSave" />';
			var message2 = $('#customerNameSelected').val();   
			var $contents = $('<div class="confirmDialog">\
					<div class="confirmDialogLine"><span class="confirmDialogLabel"></span></div>\
					<div class="confirmDialogLine">'+message2+'</div>\
					<div class="confirmDialogLine">'+message1+'</div>\
					</div>'); 
			var clickHandler = eacConfirm({callbackYes:submitForm,contents:$contents,title:'${confirmMerge}'});
			clickHandler(e);
									
		});
		
		var submitForm = function() {

			var email=window.localStorage.getItem('emailId_val');
			var customerId=$('#customerIdSelected').val();
			var done = false;

			$.ajax({
				type: 'POST',
				url: '${mergeUrl}',
				data: 'emailId=' +email+'&customerId='+customerId
			}).done(function(html) {				
				done=true;
			});
      		
      		
      		setTimeout(function() {
      			if (!done) {
		      		$('#searchResultsMergeCustomersTile').empty();
		      		$('#searchResultsMergeCustomersTile').append('<div class="progressMessageContainer"><h2 class="progressMessage"><spring:message code="label.pleaseWait"/></h2><img src="<c:url value="/images/progress-indicator-large.gif"/>"/></div>');
      			}
      		}, 500);
		};					
		
		$(document).keypress(function(e){
		    if (e.which == 13){
		        $("#search").click();
		    }
		});
	</script>
</div>	
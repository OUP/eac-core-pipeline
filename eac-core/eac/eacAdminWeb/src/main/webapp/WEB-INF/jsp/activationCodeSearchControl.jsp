<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="searchControlTile">	
   	<p>                 
       	<button type="button" id="search"><spring:message code="button.search" /></button>
		<button type="button" id="clear"><spring:message code="button.reset" /></button>
   	</p>
	<script type="text/javascript">
		function sendAjaxRequest(event, params) {
			if (params == null) params = {};
			if (params.form == null) params.form = 'searchForm';
			if (params.sendFormData == null) params.sendFormData = true;
      		var postParams = '_eventId=' + event + '&ajaxSource=' + event;
			var fragments = '${fragments}';
			if (fragments != '') {
				postParams += '&fragments=' + fragments;
			}
      		if (params.sendFormData) {
      			postParams += '&' + $('#' + params.form).serialize();
      		}
   		  	var done = false;
      		$.ajax({
   		  		url: window.location.href,
   		  		type: 'POST',
   		  		data: postParams
   		  	}).done(function(html) {
    			$('#searchResultsTile').empty();
    			$('#searchResultsTile').append(html);
    			done = true;
   		  	});
      		
      		setTimeout(function() {
      			if (!done) {
		      		$('#searchResultsTile').empty();
		      		$('#searchResultsTile').append('<div class="progressMessageContainer"><h2 class="progressMessage"><spring:message code="label.pleaseWait"/></h2><img src="<c:url value="/images/progress-indicator-large.gif"/>"/></div>');
      			}
      		}, 500);
		}
			
		$('#search').click(function() {
			if($("#code").length >0){
				if(($('#code').val()!="" || $('#reportProductId').val() != "" || $('#eacGroupId').val() != "")) {
					$('.success').remove();
					$('.error').remove();
					sendAjaxRequest('search');
				}
				else{
					sendAjaxRequest('reset');
					$('.success').remove();
					$('.error').remove();
					$("<div class='error'><span><spring:message code='seacrhactivation.code.error' text='Please select atleast one filter option from Activation Code, Product or Product Group.'/></span><br></div>").insertAfter("#heading");
				}
			}
			if($("#batchName").length >0){
				 if((($('#batchName').val()!="") || $('#productId').val() != "") || ($('#eacGroupId').val() != "") || ($('#activationCode').val() != "")) {
					$('.success').remove();
					$('.error').remove();
					sendAjaxRequest('search');
				}
				else{
					sendAjaxRequest('reset');
					$('.success').remove();
					$('.error').remove();
					$("<div class='error'><span><spring:message code='seacrhactivation.code.error' text='Please select atleast one filter option from Batch Name, Product, Product Group or Activation Code.'/></span><br></div>").insertAfter("#heading");
				}
			}
		});

		$('#searchForm').keypress(function(e) {
			if (e.keyCode == 13) {
				if($("#code").length >0){
					if(($('#code').val()!="" || $('#reportProductId').val() != "" || $('#eacGroupId').val() != "")) {
						$('.success').remove();
						$('.error').remove();
						sendAjaxRequest('search');
						return false;
					}
					else{
						sendAjaxRequest('reset');
						$('.success').remove();
						$('.error').remove();
							$("<div class='error'><span><spring:message code='seacrhactivation.code.error' text='Please select atleast one filter option from Activation Code, Product or Product Group.'/></span><br></div>").insertAfter("#heading");
					}
				}
				if($("#batchName").length >0){
					 if((($('#batchName').val()!="") || $('#productId').val() != "") || ($('#eacGroupId').val() != "") || ($('#activationCode').val() != "")) {
						$('.success').remove();
						$('.error').remove();
						sendAjaxRequest('search');
						return false;
					}
					else
						{
						sendAjaxRequest('reset');
						$('.success').remove();
						$('.error').remove();
						$("<div class='error'><span><spring:message code='seacrhactivation.code.error' text='Please select atleast one filter option from Batch Name, Product, Product Group or Activation Code.'/></span><br></div>").insertAfter("#heading");
					}
				}
				//sendAjaxRequest('search');
				//return false;
			}
		});  
		
	/* 	$('#searchForm').keypress(function(e) {
			if (e.keyCode == 13) {
				sendAjaxRequest('search');
				return false;
			}
		}); */
		
		$('#clear').click(function() {		
			$('#searchForm')[0].reset();
			$('.success').remove();
			$('.error').remove();
			sendAjaxRequest('reset');
		});
		
	</script>
</div>
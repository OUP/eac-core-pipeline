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
    			$('#searchResultsTile').show();
    			done = true;
   		  	});
      		
      		setTimeout(function() {
      			if (!done) {
		      		$('#searchResultsTile').empty();
		      		$('#searchResultsTile').append('<div class="progressMessageContainer"><h2 class="progressMessage"><spring:message code="label.pleaseWait"/></h2><img src="<c:url value="/images/progress-indicator-large.gif"/>"/></div>');
      			}
      		}, 500);
		}
		$('#search').click(function(event) {
			var formAction = document.getElementById('searchForm').action;
			if(formAction.indexOf("customer")!=-1){
			 if((($('#username').val()!="") || ($('#email').val() != "") || ($('#firstName').val() != "") ||
					  ($('#familyName').val() != "") || ($('#externalId').val() != "") || ($('#createdDateFrom').val() != "")
					 || ($('#createdDateTo').val() != "")) && ($('#registrationData').val() != "") ) {
					$('.success').remove();
					$('.error').remove();
					$("<div class='error'><span><spring:message code='seacrhactivation.code.error' text='Either Username, Email, First Name, Family Name, External Id, Created Date From, Created Date To can be searched or Registration Data can be searched.'/></span><br></div>").insertAfter("#heading");
			}else{
					/* sendAjaxRequest('reset');*/
					$('.success').remove();
					$('.error').remove();
					sendAjaxRequest('search');
				}
			}else{
				/* sendAjaxRequest('reset'); */
				$('.success').remove();
				$('.error').remove();
				sendAjaxRequest('search');
			}
		});
		
		$('#searchForm').keypress(function(e) {
			if (e.keyCode == 13) {
				sendAjaxRequest('search');
				return false;
			}
		});
		
		$('#clear').click(function() {			
			$('#searchForm')[0].reset();
			sendAjaxRequest('reset');
			sendAjaxRequest('search');
		});
		
	</script>
</div>
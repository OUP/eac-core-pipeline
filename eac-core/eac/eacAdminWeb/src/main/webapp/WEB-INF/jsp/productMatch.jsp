<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
	
<div id="help" style="margin-bottom: 2em; margin-bottom: 0\9;">
	This facility allows you to search for Products by matching on the URL of a corresponding eRights product. 
</div>
	
<div style="float:left; margin-right: 1em">
   	<tiles:insertAttribute name="productMatchFormTile"/>
</div>

<button type="button" id="match" style="margin-top: 29px\9;"><spring:message code="button.match"/></button>

<script type="text/javascript">
	$('#match').click(function() {
		if ($('#exampleUrl').val() != '') {
			performMatch();
		}
	});
	
	$('#productMatchForm').keypress(function(e) {
		if (e.keyCode == 13) {
			performMatch();
			return false;
		}
	});
	
	function performMatch() {
		if (isValidUrl($('#exampleUrl').val())) {
			sendAjaxRequest('match', {form: 'productMatchForm'});
		} else {
			alert('<spring:message code="error.malformedUrl" />');
		}
	}
	
	function isValidUrl(url) {
		var urlRegex = /(http|ftp|https):\/\/[\w-]+(\.[\w-]+)+([\w.,@?^=%&amp;:\/~+#-]*[\w@?^=%&amp;\/~+#-])?/;
		return url.match(urlRegex);
	}
</script>


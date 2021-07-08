<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<c:url value="/mvc/BulkCreateActivationCodes/Activationcodeupload.htm" var="formUrl"/>
<div>
	<div id="heading" class="ui-corner-top">
	   <h1><spring:message code="title.bulkCreateActivationCode" /></h1>
	</div>
	<c:set var="errorStatus" value="${param['errorStatus']}"/>
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
			<c:if test="${errorStatus ne null}">
				<c:out value="${errorStatus}"/>
			</c:if>
		</div>
	</c:if>
	
	<div id="help">
	Bulk Create Activation Codes file must follow below requirements:
	<ul>
		<li>File should be in MS-Excel 97-2003 .xls format</li>
		<li>File should contain two sheets.</li>
	</ul>
	</div>
	
	
	
    <fieldset>
        <legend><spring:message code="label.bulkCreate.activationCode.file"/></legend>
			<form:form method="POST" action="${formUrl}" modelAttribute="uploadForm" enctype="multipart/form-data">
      		<div class="field">
				<div class="fieldLabel" style="width: 180px">
					<label for="fileToUpload"><spring:message code="label.select.file.upload"  />&#160;<span class="mandatory">*</span></label>
				</div>
				<div class="fieldValue">
				<p class="Uploadform">
					<input type="text" id="filePath"  style="float: left; width: 15em; height: 18px;"/>
						<label class="add-photo-btn"><spring:message code="button.selectFile" /><span><input type="file" id="myfile" name="myfile" /></span></label>
				</p>
				</div>
		</div>
		<div id="buttons">
			<button type="submit" id="upload"><spring:message code="button.upload" /></button>
			<button type="button" id="reset"><spring:message code="button.reset" /></button>
		</div>
       </form:form>
    </fieldset>

	<script type="text/javascript">
        
		$('#myfile').change(function(){
			$('#filePath').val($(this).val());
		});
        
		$('#reset').click(function() {
			$('#myfile').val('');
			$('#filePath').val('');
		});
	</script>
</div>

<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<fieldset>
    <legend><spring:message code="title.results" /></legend>
    <div>
        <p><strong><c:out value="${reportSize}"/> results found
       <!--  
        <c:if test="${reportSize > maxSize}">
            <img src="<c:url value="/images/warning.png"/>"/> Max ${maxSize} results will be sent
        </c:if>
         --> 
        </strong></p>
    </div> 
    <c:if test="${reportSize > 0}">
        <div>
            <p><strong>Email to: <c:out value="${emailAddress}"/></strong></p>
        </div>
        
        <!-- Fetching limited records -->
        <form:form modelAttribute="reportCriteria" action="" id="reportCriteriaDetails">
	        <fieldset style="clear:both; padding-top: 0px\9; margin-bottom: 20px\9; width: 67%;">
	            <legend>Fetch Data</legend>
	        <div class="field">
                <div class="fieldLabel" style="width: 75px; padding-left:30px;">
	               <label>Start Index</label>
                </div>
                <div class="fieldValue">
	               <form:input path="startIndex" id="startIndex" value="1" style="width: 75px;"/>
                </div>
	            <div class="fieldLabel" style="width: 75px; padding-left:30px;">
	                <label for="maxResults">Fetch Limit</label>
	            </div>
	            <div class="fieldValue">
	                <form:select id="maxResults" path="maxResults" cssStyle="width:auto" style="width: 75px;" >
	                    <form:option value="100">100</form:option>
	                    <form:option value="1000">1000</form:option>
	                    <form:option value="5000">5000</form:option>
	                    <form:option value="10000">10000</form:option>                    
	                </form:select>                
	            </div>
	            <spring:message code="info.report.startIndex.and.fetchLimit" var="startIndexAndmaxResultsHelp"/>
                <img id="startIndexAndmaxResultsHelp" class="infoIcon" src="<c:url value="/images/information.png" />" title="${startIndexAndmaxResultsHelp}"/>
                <script type="text/javascript">
                    $(function() {
                        $("#startIndexAndmaxResultsHelp").tipTip({edgeOffset: 10});
                    });
                </script>
	        </div>
	        </fieldset>
        </form:form>
    </c:if>
</fieldset>
<c:if test="${reportSize > 0}">
<div id="buttons">    
    <p>                 
        <button type="button" id="send"><spring:message code="button.send" /></button>
    </p>
</div>
<script type="text/javascript">
        
    $('#send').click(function() {
    	
    	var $startIndex=$('#reportCriteriaDetails [name=startIndex]').val();
        var reportSize= ${reportSize};
         if(isNaN($startIndex)){
            alert('Please enter numbers only.');
        }else if($startIndex < 1){
            alert('Start index must be greater than 0.');
        } else if($startIndex > reportSize){
            alert('Start index must be less than or equal to report size.');
        } else{  
            $('#eventId').val('send');      
            $('#reportCriteria [name=startIndex]').val(parseInt($('#reportCriteriaDetails [name=startIndex]').val()));
            $('#reportCriteria [name=maxResults]').val($('#reportCriteriaDetails [name=maxResults]').val());
            $('#reportCriteria').submit();
        }
    });

</script>
</c:if>
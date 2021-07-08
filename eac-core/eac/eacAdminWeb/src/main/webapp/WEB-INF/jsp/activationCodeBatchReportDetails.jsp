<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<fieldset>
    <legend><spring:message code="title.results" /></legend>
    <div>
        <p><strong><c:out value="${reportSize}"/> results found
        <c:if test="${reportSize > maxSize}">
            <img src="<c:url value="/images/warning.png"/>"/> Max ${maxSize} results will be sent
        </c:if>
        </strong></p>
    </div> 
    <c:if test="${reportSize > 0}">
    <div>
        <p><strong>Email to: <c:out value="${emailAddress}"/></strong></p>
    </div> 
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
        $('#eventId').val('send');
        $('#reportCriteria').submit();
    });

</script>
</c:if>
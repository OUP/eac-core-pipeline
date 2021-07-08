<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<c:url value="/mvc/support/cacheEviction.htm" var="formUrl" />
<div id="cacheEvictionTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.cacheEviction"/></h1>
	</div>
	
	<c:if test="${not empty statusMessageKey}">
		<div class="success">
		<c:out value="${statusMessageKey}"/>
			<%-- <spring:message code="${statusMessageKey}"/>
			<c:if test="${adminEmail ne null}">
				<c:out value="${adminEmail}"/>
			</c:if> --%>
		</div>
	</c:if>
	
	<spring:hasBindErrors name="cacheEvictionBean">
		<div class="error">
			 <spring:bind path="cacheEvictionBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind> 
		</div>
	</spring:hasBindErrors>
	<form:form modelAttribute="cacheEvictionBean" method="POST">
		<fieldset>
			<legend><spring:message code="label.cacheEviction"/></legend>
			<div class="field">
				<div class="fieldLabel">
					<label><spring:message code="label.pattern" /></label>
				</div>
				<div class="fieldValue">
					<form:input id="pattern" path="pattern"/>
				</div>
			</div>
		</fieldset>
		<div id="buttons">
	    	<p>   
	    		<button type="button" id="reset"><spring:message code="button.reset"/></button>
	    		<button type="submit" id="save"><spring:message code="button.evict" /></button>
	        </p>
	    </div>
	</form:form>
</div>
<script type="text/javascript">
	$('#reset').click(function() {
		$('#pattern').val('') ;
	});
</script>
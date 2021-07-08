<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<spring:message code="label.pleaseSelect" var="pleaseSelect"/>
<div id="manageQuestionTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.manageQuestions"/></h1>
	</div>

	<c:if test="${not empty param['statusMessageKey']}">
		 <div class="success">
			<spring:message code="${param['statusMessageKey']}"/>
		</div>
	</c:if>
	
	<spring:hasBindErrors name="questionBean">
		<div class="error">
			<spring:bind path="questionBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>	
	
	<tags:breadcrumb/>
	
	<fieldset style="padding-top: 0px\9">
		<legend><spring:message code="label.questions"/></legend>
		<div class="field">
			<div class="fieldLabel">
				<label><spring:message code="label.question"/></label>
			</div>
			<div class="fieldValue">
				<select id="selectedQuestion" name="selectedQuestion">
					<c:if test="${empty questionBean.selectedQuestion}"><option value="pleaseSelect"><spring:message code="label.pleaseSelect"/></option></c:if>
					<c:forEach var="question" items="${questionBean.questions}">
						<c:set var="selected" value=""/>
						<c:if test="${question.id == questionBean.selectedQuestion.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<spring:message code="${question.elementText}" var="title" text=""/>
						<option value="${question.id}" title="${title}" ${selected}><c:out value="${question.displayName}"/></option>
					</c:forEach>
					<option value="new"<c:if test="${questionBean.newQuestion}"> selected</c:if>><spring:message code="label.newEntry" htmlEscape="true"/></option>
				</select>
			</div>
		</div>
	</fieldset>
	
	<div id="manageQuestionFormTile">
		<c:if test="${not empty questionBean.selectedQuestion || questionBean.newQuestion}">
			<tiles:insertAttribute name="manageQuestionFormTile"/>
		</c:if>
	</div>
		
	<script type="text/javascript">
		$(function() {
			var $selectedQuestion = $('#selectedQuestion');
			
			$selectedQuestion.change(function() {
				$(this).find('option').each(function() {
					if ($(this).val() == 'pleaseSelect') {
						$(this).remove();
					}
				});				
				$.ajax({
					type: 'GET',
					url: '<c:url value="/mvc/question/manage.htm"/>',
					data: 'id=' + $(this).val()
				}).done(function(html) {
					$('#manageQuestionFormTile').empty().append(html);
				});
				$('.success').remove();
				$('.error').remove();
			}).keypress(function() {
				$(this).trigger('change');
			});
		});
	</script>
</div>
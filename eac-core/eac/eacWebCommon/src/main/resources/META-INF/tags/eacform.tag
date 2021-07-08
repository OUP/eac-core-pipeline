<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="eac-common" uri="http://www.oup.com/eac/common-tags" %>
<%@ attribute name="regDto" required="true" type="com.oup.eac.dto.RegistrationDto"%>
<%@ attribute name="hideHelp" required="false" %>
<c:forEach items="${regDto.components}" var="component" varStatus="componentStatus">
	
		<c:if test="${not empty component.labelKey}">
		<h2><spring:message code="${component.labelKey}" text="?${component.labelKey}?"/></h2>
		</c:if>
		<c:forEach items="${component.fields}" var="field" varStatus="elementStatus">
	<div class="form_row">
			<c:choose>
				<c:when test="${field.element.tag.tagType.name eq 'URLLINK' || field.element.tag.tagType.name eq 'LABEL'}">
					<c:choose>
						<c:when test="${field.element.tag.tagType.name eq 'URLLINK'}">
								<a <c:if test="${field.element.tag.newWindow}">onclick="window.open(this.href); return false;" </c:if>href="<c:out value="${field.element.tag.url}"/>"><spring:message code="${field.element.question.elementText}" text="?${field.element.question.elementText}?"/></a>
						</c:when>
						<c:when test="${field.element.tag.tagType.name eq 'LABEL'}">
							<p>
								<spring:message code="${field.element.question.elementText}" text="?${field.element.question.elementText}?"/>
							</p>
						</c:when>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${field.element.tag.tagType.name eq 'RADIO'}">
							<p>
								<spring:message code="${field.element.question.elementText}" text="?${field.element.question.elementText}?"/>&#160;<c:if test="${field.required}"><span class="form_required">*</span></c:if>
								</p>
						</c:when>
						<c:when test="${field.element.tag.tagType.name eq 'CHECKBOX'}">
							<label class="form_label form_label--checkbox" for="answers_${field.id}">
								<form:checkbox id="answers_${field.id}" path="answers['${field.element.question.id}']" value="true" cssClass="form_checkbox" title="${elementTitle}" disabled="${regDto.readOnly}" onclick="javascript:removeCheckBoxText(this); return true;"/>
								<spring:message code="${field.element.question.elementText}" text="?${field.element.question.elementText}?"/>&#160;<c:if test="${field.required}"><span class="form_required">*</span></c:if>
							</label>
							<form:errors path="answers['${field.element.question.id}']" cssClass="error"></form:errors>
						</c:when>
						
						<c:otherwise>
							<label class="form_label" for="answers_${field.id}">
								<spring:message code="${field.element.question.elementText}" text="?${field.element.question.elementText}?"/>&#160;<c:if test="${field.required}"><span class="form_required">*</span></c:if>
							</label>							
						</c:otherwise>
					</c:choose>		
					<spring:message var="elementTitle" code="${field.element.titleText}" text="?${field.element.titleText}?" />		
					<c:choose>
						<c:when test="${field.element.tag.tagType.name eq 'TEXTFIELD'}">
								<form:input id="answers_${field.id}" path="answers['${field.element.question.id}']" cssClass="form_input" title="${elementTitle}" maxlength="255" disabled="${regDto.readOnly}" onkeyup="removeText(this)"/>
								<form:errors path="answers['${field.element.question.id}']" cssClass="error"></form:errors>
						</c:when>
						<c:when test="${field.element.tag.tagType.name eq 'HIDDENFIELD'}">
								<form:hidden id="answers_${field.id}" path="answers['${field.element.question.id}']"/>
								<form:errors path="answers['${field.element.question.id}']" cssClass="error"></form:errors>
						</c:when>
						<c:when test="${field.element.tag.tagType.name eq 'PASSWORDFIELD'}">
								<form:password id="answers_${field.id}" path="answers['${field.element.question.id}']" cssClass="form_input" title="${elementTitle}" disabled="${regDto.readOnly}" onkeyup="removeText(this)"/>
								<form:errors path="answers['${field.element.question.id}']" cssClass="error"></form:errors>
						</c:when>
						<c:when test="${field.element.tag.tagType.name eq 'SELECT'}">
								<form:select id="answers_${field.id}" path="answers['${field.element.question.id}']" cssClass="form_input" title="${elementTitle}" disabled="${regDto.readOnly}" onchange="removeText(this)">
									<c:if test="${field.element.tag.emptyOption}"><form:option value="" label="Please Select..."/></c:if>
									<c:forEach items="${field.element.tag.options}" var="option" varStatus="optionStatus">
										<form:option value="${option.value}">
										
										    <spring:message code="${option.label}" text="?${option.label}?"/>
										</form:option> 
									</c:forEach>
								</form:select>
								<form:errors path="answers['${field.element.question.id}']" cssClass="error"></form:errors>
						</c:when>
						<c:when test="${field.element.tag.tagType.name eq 'MULTISELECT'}">
						<form:errors path="answers['${field.element.question.id}']" cssClass="error"></form:errors>
							<div class="form_checkboxWrap_tmp">
								<eac-common:checkboxes items="${field.element.tag.options}" path="answers['${field.element.question.id}']" itemLabel="label" itemValue="value" disabled="${regDto.readOnly}" onclick="javascript:removeCheckBoxTextMulti(this); return true;"/>
							</div>
						</c:when>
						<c:when test="${field.element.tag.tagType.name eq 'RADIO'}">
						<form:errors path="answers['${field.element.question.id}']" cssClass="error"></form:errors>
									<c:forEach items="${field.element.tag.options}" var="option" varStatus="optionStatus">
											<label class="form_label form_label--radio" for="answers_${optionStatus.index}_${field.id}" ><spring:message code="${option.label}" text="?${option.label}?"/>
											<form:radiobutton id="answers_${optionStatus.index}_${field.id}" path="answers['${field.element.question.id}']" value="${option.value}" cssClass="form_radio" title="${elementTitle}" disabled="${regDto.readOnly}"/>
											</label>
									</c:forEach>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</div>
		</c:forEach>
</c:forEach>
<script>

$(document).ready(function() {
	$('.form_checkboxWrap_tmp label').wrap('<div class="form_checkboxWrap"/>');
	$('.form_checkboxWrap_tmp input').each(function() { $(this).appendTo($(this).prev()); })
});
</script>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="changePasswordTile">
	<c:choose>
		<c:when test="${not empty invalidToken}">
			<div class="error">
				<spring:message code="error.invalidChangePasswordToken"/><br/>
				<c:url value="/mvc/password/reset.htm" var="resetUrl"/>
				<a href="${resetUrl}"><spring:message code="button.resetPassword"/></a>
			</div>
		</c:when>
		<c:otherwise>
			<c:set var="loggedIn" value="${pageContext.request.userPrincipal != null}"/>
			<c:if test="${loggedIn}">
				<div id="heading" class="ui-corner-top">
					<h1><spring:message code="title.changePassword"/></h1>
				</div>			
			</c:if>
			<form:form modelAttribute="resetAdminPasswordBean">
				<spring:hasBindErrors name="resetAdminPasswordBean">
					<div class="error" 
					<c:if test="${loggedIn}">style="width: auto"</c:if>
					>
					</div>
						<spring:bind path="resetAdminPasswordBean.*">
							<c:forEach items="${status.errorMessages}" var="error">
								<span><c:out value="${error}"/></span><br>
							</c:forEach>
						</spring:bind>
					</div>
				</spring:hasBindErrors>	
				<fieldset>
					<legend><spring:message code="title.changePassword"/></legend>
					<spring:message code="info.passwordRequirement"/>
					<div class="field" style="margin-top: 20px">
						<div class="fieldLabel">
							<label><spring:message code="label.newPassword"/></label>
						</div>
						<div class="fieldValue">
							<form:password id="password" path="password" maxlength="15"/>
						</div>
					</div>
					<div class="field">
						<div class="fieldLabel">
							<label><spring:message code="label.passwordAgain"/></label>
						</div>
						<div class="fieldValue">
							<form:password path="confirmPassword" maxlength="15"/>
						</div>
					</div>
					<div id="buttons" style="margin-top: 10px; margin-right: 8px">
						<button id="submit" type="submit" class="button"><spring:message code="button.changePassword"/></button>
					</div>
				</fieldset>
				<c:if test="${loggedIn}">
					<input type="hidden" name="loggedIn" value="1"/>
				</c:if>
			</form:form>
			<script type="text/javascript">
				$(function() {
					$('#password').focus();
				});
			</script>			
		</c:otherwise>
	</c:choose>
</div>
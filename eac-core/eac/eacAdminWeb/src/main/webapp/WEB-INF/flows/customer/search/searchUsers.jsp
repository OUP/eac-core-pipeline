<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div>
	<h1><spring:message code="title" /></h1>
	<p>Some help text here</p>
	
	<c:if test="${!empty users}">
		<table class="summary">
			<thead>
				<tr>
					<th>ID</th>
					<th>First Name</th>
					<th>Family Name</th>
					<th>Email Address</th>
					<th>Username</th>
					<th>Locked?</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${empty users}">
					<tr>
						<td colspan="7">No users found</td>
					</tr>
				</c:if>
				<c:forEach var="user" items="${users}">
					<tr>
						<td><c:out value="${user.id}"/></td>
						<td><c:out value="${user.firstName}"/></td>
						<td><c:out value="${user.familyName}"/></td>
						<td><c:out value="${user.emailAddress}"/></td>
						<td><c:out value="${user.username}"/></td>
						<td><c:out value="${user.locked}"/></td>				
						<td>
							<spring:url var="editUserUrl" value="/user/create/{id}">
								<spring:param name="id" value="${user.id}"/>
							</spring:url>
							<form:form action="${editUserUrl}" method="edit">
								<button type="submit">Edit</button>
							</form:form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
</div>


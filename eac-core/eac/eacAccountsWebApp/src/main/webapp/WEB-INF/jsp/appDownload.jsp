<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Download app</title>
</head>
<body>
	<div class="row">
		<div class="col small-12">
		<%@ include file="/WEB-INF/jsp/info.jsp" %>
			<main class="box box--noPadding">
				<div class="stepHeading stepHeading--complete"><span class="stepHeading_number">1</span> Log in or register</div>
				<div class="stepHeading stepHeading--complete"><span class="stepHeading_number">2</span> Activate content</div>
				<div class="stepHeading stepHeading--active"><span class="stepHeading_number">3</span> Download app</div>
				<div class="box_inner">
					<h1>Download app</h1>
					<p><strong>${productName}</strong> has been added to your account and is now available.</p>
					<p>To view your content, download the app from the App Store and sign into your account.</p>
					<p>
						<c:forEach items="${appIDs}" var="mapItem">
							<c:if test="${mapItem.key == 'appleappid'}">
								<a class="storeBadge storeBadge--apple" href="${mapItem.value}">Download on the App Store</a>
							</c:if>
							<c:if test="${mapItem.key == 'googleappid'}">
								<a class="storeBadge storeBadge--google" href="${mapItem.value}">Get it on Google play</a>
							</c:if>
						</c:forEach>
					</p>
					<p><a href="<c:url value="/activationCode.htm"/>">&larr; Redeem another access code</a></p>
				</div>
			</main>
		</div>
	</div>
</body>
</html>